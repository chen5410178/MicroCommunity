package com.java110.job.adapt.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.system.Business;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.WechatSubscribeDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.user.IWechatSubscribeV1InnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.po.wechat.WechatSubscribePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component(value = "synchronizeWechatSubscribeAdapt")
public class SynchronizeWechatSubscribeAdapt extends DatabusAdaptImpl {
    private static Logger logger = LoggerFactory.getLogger(SynchronizeWechatSubscribeAdapt.class);

    @Autowired
    private IWechatSubscribeV1InnerServiceSMO wechatSubscribeV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    private static final String GET_USER = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";

    private static final String GET_BATCH_USER = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        String communityId = data.getString("communityId");
        if (StringUtil.isEmpty(communityId)) {
            throw new CmdException("未包含小区");
        }

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(communityId);
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            throw new CmdException("未配置公众号");
        }

        String accessToken = WechatFactory.getAccessToken(smallWeChatDtos.get(0).getAppId(), smallWeChatDtos.get(0).getAppSecret());

        WechatSubscribePo tmpWechatSubscribePo = new WechatSubscribePo();
        tmpWechatSubscribePo.setAppId(smallWeChatDtos.get(0).getAppId());
        wechatSubscribeV1InnerServiceSMOImpl.deleteWechatSubscribe(tmpWechatSubscribePo);
        getAllOpenId(accessToken, "", smallWeChatDtos.get(0));


    }

    private void getAllOpenId(String accessToken, String nextOpenid, SmallWeChatDto weChatDto) {
        String url = GET_USER.replace("ACCESS_TOKEN", accessToken);
        if (!StringUtil.isEmpty(nextOpenid)) {
            url += ("&next_openid=" + nextOpenid);
        }
        ResponseEntity<String> paramOut = outRestTemplate.getForEntity(url, String.class);
        logger.info("获取用户返回:{}", paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(paramOut.getBody());
        }
        JSONObject paramOutObj = JSONObject.parseObject(paramOut.getBody());
        if (paramOutObj.containsKey("errcode")) {
            throw new IllegalArgumentException(paramOut.getBody());
        }
        if (!paramOutObj.containsKey("data")) {
            return;
        }
        JSONObject dataObj = paramOutObj.getJSONObject("data");
        JSONArray openids = dataObj.getJSONArray("openid");
        nextOpenid = paramOutObj.getString("next_openid");
        List<WechatSubscribePo> wechatSubscribePos = new ArrayList<>();
        JSONArray user_list = new JSONArray();
        JSONObject user = null;
        for (int openIndex = 0; openIndex < openids.size(); openIndex++) {
            String openId = openids.getString(openIndex);

            user = new JSONObject();
            user.put("openid", openId);
            user.put("lang", "zh_CN");
            user_list.add(user);

            WechatSubscribePo wechatSubscribePo = new WechatSubscribePo();
            wechatSubscribePo.setAppId(weChatDto.getAppId());
            wechatSubscribePo.setOpenId(openId);
            wechatSubscribePo.setSubId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            wechatSubscribePo.setOpenType(WechatSubscribeDto.OPEN_TYPE_WECHAT);
            wechatSubscribePos.add(wechatSubscribePo);
            if (user_list.size() >= 100) {
                getUserUnionId(user_list, wechatSubscribePos, accessToken);
                user_list = new JSONArray();
            }
        }

        if (!user_list.isEmpty()) {
            getUserUnionId(user_list, wechatSubscribePos, accessToken);
        }


        if (!ListUtil.isNull(wechatSubscribePos)) {
            wechatSubscribeV1InnerServiceSMOImpl.saveWechatSubscribes(wechatSubscribePos);
        }
        //（关注者列表已返回完时，返回next_openid为空）
        if (!StringUtil.isEmpty(nextOpenid)) {
            getAllOpenId(accessToken, nextOpenid, weChatDto);
        }
    }

    private void getUserUnionId(JSONArray userList, List<WechatSubscribePo> wechatSubscribePos, String accessToken) {

        if (ListUtil.isNull(userList)) {
            return;
        }

        String url = GET_BATCH_USER.replace("ACCESS_TOKEN", accessToken);


        ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, userList.toJSONString(), String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(responseEntity.getBody());
        }
        JSONObject paramOutObj = JSONObject.parseObject(responseEntity.getBody());
        if (paramOutObj.containsKey("errcode")) {
            throw new IllegalArgumentException(paramOutObj.getString("errmsg"));
        }

        JSONArray user_info_list = paramOutObj.getJSONArray("user_info_list");
        if (ListUtil.isNull(user_info_list)) {
            return;
        }
        JSONObject user = null;
        for (int userIndex = 0; userIndex < user_info_list.size(); userIndex++) {
            user = user_info_list.getJSONObject(userIndex);

            for(WechatSubscribePo wechatSubscribePo : wechatSubscribePos){
                if(user.getString("openid").equals(wechatSubscribePo.getOpenId())){
                    wechatSubscribePo.setUnionId(user.getString("unionid"));
                }
            }
        }

    }


}
