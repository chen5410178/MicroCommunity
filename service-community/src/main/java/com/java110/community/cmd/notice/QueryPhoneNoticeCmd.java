package com.java110.community.cmd.notice;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.doc.annotation.*;
import com.java110.dto.community.CommunityDto;
import com.java110.dto.floor.FloorDto;
import com.java110.dto.notice.NoticeDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.unit.FloorAndUnitDto;
import com.java110.intf.community.*;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.api.notice.ApiNoticeDataVo;
import com.java110.vo.api.notice.ApiNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 查询用户端公告 功能
 * 请求地址为/app/login.pcUserLogin
 */
@Java110Cmd(serviceCode = "notice.queryPhoneNotice")
public class QueryPhoneNoticeCmd extends Cmd {

    @Autowired
    private INoticeInnerServiceSMO noticeInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IFloorInnerServiceSMO floorInnerServiceSMOImpl;

    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        NoticeDto noticeDto = BeanConvertUtil.covertBean(reqJson, NoticeDto.class);

        // 多类型同时查询
        noticeDto.setNoticeTypeCd(NoticeDto.NOTICE_TYPE_OWNER);
        int count = noticeInnerServiceSMOImpl.queryNoticesCount(noticeDto);

        List<ApiNoticeDataVo> notices = null;

        if (count > 0) {
            notices = BeanConvertUtil.covertBeanList(noticeInnerServiceSMOImpl.queryNotices(noticeDto), ApiNoticeDataVo.class);
        } else {
            notices = new ArrayList<>();
        }

        ApiNoticeVo apiNoticeVo = new ApiNoticeVo();

        apiNoticeVo.setTotal(count);
        apiNoticeVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiNoticeVo.setNotices(notices);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiNoticeVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
