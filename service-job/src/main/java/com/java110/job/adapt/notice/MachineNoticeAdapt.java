package com.java110.job.adapt.notice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.factory.WechatFactory;
import com.java110.core.log.LoggerFactory;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.notice.NoticeDto;
import com.java110.dto.notice.NoticeStaffDto;
import com.java110.dto.notice.NoticeWechatDto;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.wechat.*;
import com.java110.dto.user.StaffAppAuthDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.system.Business;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.java110.intf.community.INoticeInnerServiceSMO;
import com.java110.intf.community.INoticeV1InnerServiceSMO;
import com.java110.intf.store.ISmallWeChatInnerServiceSMO;
import com.java110.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.java110.intf.user.*;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.msgNotify.IMsgNotify;
import com.java110.job.msgNotify.MsgNotifyFactory;
import com.java110.po.notice.NoticePo;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.cache.UrlCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 发布公告推送通知
 *
 * @author fqz
 * @date 2022-10-15
 */
@Component(value = "machineNoticeAdapt")
public class MachineNoticeAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(MachineNoticeAdapt.class);

    @Autowired
    private INoticeV1InnerServiceSMO noticeV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;


    @Autowired
    private IWechatSubscribeV1InnerServiceSMO wechatSubscribeV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;


    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessNotices = new JSONArray();
        System.out.println("收到日志：>>>>>>>>>>>>>" + data.toJSONString());
        if (data.containsKey(NoticePo.class.getSimpleName())) {
            Object bObj = data.get(NoticePo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessNotices.add(bObj);
            } else if (bObj instanceof List) {
                businessNotices = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessNotices = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessNotices.add(data);
            }
        }
        for (int noticeIndex = 0; noticeIndex < businessNotices.size(); noticeIndex++) {
            JSONObject businessNotice = businessNotices.getJSONObject(noticeIndex);
            doDealNotice(businesses, businessNotice);
        }
    }

    private void doDealNotice(List<Business> businesses, JSONObject businessNotice) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setNoticeId(businessNotice.getString("noticeId"));
        List<NoticeDto> noticeDtos = noticeV1InnerServiceSMOImpl.queryNotices(noticeDto);
        Assert.listOnlyOne(noticeDtos, "查询公告错误！");

        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(businessNotice.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "查询小区错误！");
        updateNoticeState(noticeDtos.get(0), NoticeDto.STATE_DOING);
        if (NoticeDto.NOTICE_TYPE_OWNER.equals(businessNotice.getString("noticeTypeCd"))) {
            noticeOwnerWechat(noticeDtos.get(0), communityDtos.get(0));
        } else {
            noticeStaffWechat(noticeDtos.get(0), communityDtos.get(0));
        }
        updateNoticeState(noticeDto, NoticeDto.STATE_FINISH);


    }


    /**
     * 给业主发送消息
     *
     * @param noticeDto
     * @param communityDto
     */
    private void noticeOwnerWechat(NoticeDto noticeDto, CommunityDto communityDto) {

        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        smallWeChatDto.setWechatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (ListUtil.isNull(smallWeChatDtos)) {
            return;
        }

        WechatSubscribeDto wechatSubscribeDto = new WechatSubscribeDto();
        wechatSubscribeDto.setAppId(smallWeChatDtos.get(0).getAppId());
        int count = wechatSubscribeV1InnerServiceSMOImpl.queryWechatSubscribesCount(wechatSubscribeDto);
        int maxCount = 200;
        double maxPage = Math.ceil(count / maxCount) + 1;
        for (int page = 0; page < maxPage; page++) {
            wechatSubscribeDto.setPage(page + 1);
            wechatSubscribeDto.setRow(maxCount);
            wechatSubscribeDto.setAppId(smallWeChatDtos.get(0).getAppId());
            List<WechatSubscribeDto> wechatSubscribeDtos = wechatSubscribeV1InnerServiceSMOImpl.queryWechatSubscribes(wechatSubscribeDto);
            doSendNoticeOwnerWechat(wechatSubscribeDtos, noticeDto, communityDto, smallWeChatDtos.get(0));
        }


    }

    /**
     * 给关注业主发送消息
     *
     * @param wechatSubscribeDtos
     * @param noticeDto
     * @param communityDto
     * @param smallWeChatDto
     */
    private void doSendNoticeOwnerWechat(List<WechatSubscribeDto> wechatSubscribeDtos, NoticeDto noticeDto, CommunityDto communityDto, SmallWeChatDto smallWeChatDto) {
        IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
        String wechatUrl = UrlCache.getOwnerUrl()+"/#/pages/notice/detail/detail?noticeId="+noticeDto.getNoticeId()+"&communityId="+communityDto.getCommunityId();
        if (!StringUtil.isEmpty(wechatUrl) && wechatUrl.contains("?")) {
            wechatUrl += ("&wAppId=" + smallWeChatDto.getAppId());
        } else {
            wechatUrl += ("?wAppId=" + smallWeChatDto.getAppId());
        }
        for (WechatSubscribeDto wechatSubscribeDto : wechatSubscribeDtos) {
            try {
                msgNotify.sendWechatMsg(
                        new NoticeWechatDto(communityDto.getCommunityId(),
                                wechatSubscribeDto.getOpenId(),
                                noticeDto.getTitle(),
                                "系统管理员", wechatUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给员工发送消息
     *
     * @param noticeDto
     * @param communityDto
     */
    private void noticeStaffWechat(NoticeDto noticeDto, CommunityDto communityDto) {
        IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(communityDto.getCommunityId());
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);
        if (ListUtil.isNull(communityMemberDtos)) {
            return;
        }
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setStoreId(communityMemberDtos.get(0).getMemberId());
        staffAppAuthDto.setAppType("WECHAT");
        List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);

        if (ListUtil.isNull(staffAppAuthDtos)) {
            return;
        }
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");

        for (StaffAppAuthDto wechatSubscribeDto : staffAppAuthDtos) {
            try {
                msgNotify.sendWechatMsg(
                        new NoticeWechatDto(communityDto.getCommunityId(),
                                wechatSubscribeDto.getOpenId(),
                                noticeDto.getTitle(),
                                "系统管理员", wechatUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void updateNoticeState(NoticeDto noticeDto, String state) {

        NoticePo noticePo = new NoticePo();
        noticePo.setNoticeId(noticeDto.getNoticeId());
        noticePo.setState(state);
        noticeV1InnerServiceSMOImpl.updateNotice(noticePo);
    }
}
