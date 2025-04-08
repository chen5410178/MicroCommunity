package com.java110.api.smo.wechatGateway.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.properties.WechatAuthProperties;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.wechatGateway.IWechatGatewaySMO;
import com.java110.core.context.IPageData;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.wechat.SmallWeChatDto;
import com.java110.dto.wechat.SmallWechatAttrDto;
import com.java110.dto.wechat.WechatSubscribeDto;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.user.IWechatSubscribeV1InnerServiceSMO;
import com.java110.po.wechat.WechatSubscribePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.constant.ServiceCodeSmallWechatAttrConstant;
import com.java110.utils.constant.WechatConstant;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * wx登录
 */
@Service("wechatGatewaySMOImpl")
public class WechatGatewaySMOImpl extends DefaultAbstractComponentSMO implements IWechatGatewaySMO {

    private final static Logger logger = LoggerFactory.getLogger(WechatGatewaySMOImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate outRestTemplate;

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    public static final String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    @Autowired
    private IWechatSubscribeV1InnerServiceSMO wechatSubscribeV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> gateway(String fromUserName,
                                          String toUserName,
                                          String keyword,
                                          String msgType,
                                          String event,
                                          String eventKey,
                                          String appId) {
        String responseStr = null;
        if (WechatConstant.MSG_TYPE_TEXT.equals(msgType)) {
            responseStr = textResponseHandler(fromUserName, toUserName, keyword);
        } else if (WechatConstant.MSG_TYPE_EVENT.equals(msgType)) {
            responseStr = eventResponseHandler(fromUserName, toUserName, keyword, event, eventKey);
        } else {
            responseStr = eventResponseHandler(fromUserName, toUserName, keyword, event, eventKey);
        }
        //保存到关注表
        if(!"subscribe".equals(event)){
            return new ResponseEntity<>(responseStr, HttpStatus.OK);
        }

        WechatSubscribeDto wechatSubscribeDto = new WechatSubscribeDto();
        wechatSubscribeDto.setOpenId(fromUserName);
        wechatSubscribeDto.setOpenId(appId);
        List<WechatSubscribeDto> wechatSubscribeDtos = wechatSubscribeV1InnerServiceSMOImpl.queryWechatSubscribes(wechatSubscribeDto);
        if (!ListUtil.isNull(wechatSubscribeDtos)) {
            return new ResponseEntity<>(responseStr, HttpStatus.OK);
        }

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setAppId(appId);
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            throw new CmdException("未配置公众号");
        }

        String accessToken = WechatFactory.getAccessToken(smallWeChatDtos.get(0).getAppId(), smallWeChatDtos.get(0).getAppSecret());

        String url = GET_USER_INFO.replace("ACCESS_TOKEN", accessToken).replace("OPENID",fromUserName);
        ResponseEntity<String> paramOut = outRestTemplate.getForEntity(url, String.class);
        logger.info("获取用户返回:{}", paramOut);
        if (paramOut.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(paramOut.getBody());
        }
        JSONObject paramOutObj = JSONObject.parseObject(paramOut.getBody());
        if (paramOutObj.containsKey("errcode")) {
            throw new IllegalArgumentException(paramOut.getBody());
        }

        WechatSubscribePo wechatSubscribePo = new WechatSubscribePo();
        wechatSubscribePo.setAppId(appId);
        wechatSubscribePo.setOpenId(fromUserName);
        wechatSubscribePo.setUnionId(paramOutObj.getString("unionid"));
        wechatSubscribePo.setSubId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        wechatSubscribePo.setOpenType(WechatSubscribeDto.OPEN_TYPE_WECHAT);
        wechatSubscribeV1InnerServiceSMOImpl.saveWechatSubscribe(wechatSubscribePo);

        return new ResponseEntity<>(responseStr, HttpStatus.OK);
    }


    @Override
    public SmallWeChatDto getSmallWechat(IPageData pd, SmallWeChatDto smallWeChatDto) {

        //List<SmallWeChatDto> smallWeChatDtos = super.getForApis(pd, smallWeChatDto, ServiceCodeSmallWeChatConstant.LIST_SMALL_WE_CHATS, SmallWeChatDto.class);
        ResponseEntity<String> responseEntity = this.callCenterService(restTemplate, pd, "",
                "smallWeChat.listSmallWeChats?appId="
                        + smallWeChatDto.getAppId() + "&page=1&row=1", HttpMethod.GET);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        JSONObject smallWechatObj = JSONObject.parseObject(responseEntity.getBody().toString());
        JSONArray smallWeChats = smallWechatObj.getJSONArray("smallWeChats");


        if (smallWeChats == null || smallWeChats.size() < 1) {
            return null;
        }
        smallWeChatDto = BeanConvertUtil.covertBean(smallWeChats.get(0), SmallWeChatDto.class);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(smallWeChatDto.getObjId());
        smallWechatAttrDto.setWechatId(smallWeChatDto.getWeChatId());
        List<SmallWechatAttrDto> smallWechatAttrs = super.getForApis(pd, smallWechatAttrDto, ServiceCodeSmallWechatAttrConstant.LIST_SMALLWECHATATTRS, SmallWechatAttrDto.class);

        smallWeChatDto.setSmallWechatAttrs(smallWechatAttrs);
        return smallWeChatDto;
    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 文字处理
     *
     * @param fromUserName
     * @param toUserName
     * @param keyword
     * @return
     */
    private String textResponseHandler(String fromUserName, String toUserName,
                                       String keyword) {
        if (StringUtil.isEmpty(keyword)) {
            return WechatFactory
                    .formatText(toUserName, fromUserName, "未包含任何信息");
        } else {
            String responseStr = keyWordHandler(fromUserName, toUserName,
                    keyword);
            return WechatFactory
                    .formatText(toUserName, fromUserName, responseStr);
        }
    }

    /**
     * 事件处理
     *
     * @param fromUserName
     * @param toUserName
     * @param keyword
     * @return
     */
    private String keyWordHandler(String fromUserName, String toUserName,
                                  String keyword) {
        // TODO Auto-generated method stub
        String domain = WechatConstant.TOKEN;
        //String url = domain + "/IMSS/indexPage.do";
        String remark = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN, WechatConstant.WELCOME);

        if (StringUtil.isEmpty(remark)) {
            remark = WechatConstant.DEFAULT_WELCOME;
        }

        return remark;
    }

    /**
     * 事件处理
     *
     * @param fromUserName
     * @param toUserName
     * @param event
     * @param eventKey
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked"})
    public String eventResponseHandler(String fromUserName, String toUserName, String keyWords, String event,
                                       String eventKey) {
        String resultStr = "";
        //
        if (event.equals("subscribe")) {
            resultStr = MappingCache.getRemark(WechatConstant.WECHAT_DOMAIN, WechatConstant.WELCOME);
        } else if (event.equals("unsubscribe")) {

        } else if (event.equalsIgnoreCase("CLICK")) {
            resultStr = textResponseHandler(fromUserName, toUserName,
                    eventKey);
        } else {

        }
        if (StringUtil.isEmpty(resultStr)) {
            resultStr = WechatConstant.DEFAULT_WELCOME;
        }
        return WechatFactory.formatText(toUserName, fromUserName, resultStr);
    }

    /**
     * 判断是否绑定业主
     *
     * @param openId
     * @return
     */
    private boolean judgeBindOwner(IPageData pd, String openId) {
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setOpenId(openId);
        List<OwnerAppUserDto> ownerAppUserDtos = super.getForApis(pd, ownerAppUserDto, ServiceCodeConstant.LIST_APPUSERBINDINGOWNERS, OwnerAppUserDto.class);

        if (ownerAppUserDtos == null || ownerAppUserDtos.size() != 1) {
            return false;
        }
        return true;
    }


    /**
     * 校验是否在关注表里
     *
     * @param appId
     * @param fromUserName
     */
    private void saveWechatSubscribe(String appId, String fromUserName) {

    }

}
