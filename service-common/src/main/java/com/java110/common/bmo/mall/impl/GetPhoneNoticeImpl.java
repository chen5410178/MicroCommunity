package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.dto.notice.NoticeDto;
import com.java110.intf.community.*;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getPhoneNoticeImpl")
public class GetPhoneNoticeImpl implements IMallCommonApiBmo {

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
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 调用父类方法验证分页信息
        Assert.jsonObjectHaveKey(reqJson, "page", "请求中未包含page信息");
        Assert.jsonObjectHaveKey(reqJson, "row", "请求中未包含row信息");
        // 验证请求中必须包含小区ID
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
// 将请求JSON转换为NoticeDto对象
        NoticeDto noticeDto = BeanConvertUtil.covertBean(reqJson, NoticeDto.class);
        // 设置查询类型为业主公告类型
        noticeDto.setNoticeTypeCd(NoticeDto.NOTICE_TYPE_OWNER);
        // 查询符合条件的公告总数
        int count = noticeInnerServiceSMOImpl.queryNoticesCount(noticeDto);

        List<NoticeDto> notices = null;

        // 根据查询结果数量处理公告列表
        if (count > 0) {
            // 查询公告列表并转换为前端VO对象
            notices = noticeInnerServiceSMOImpl.queryNotices(noticeDto);
        } else {
            // 如果没有数据，返回空列表
            notices = new ArrayList<>();
        }

        // 构建HTTP响应实体
        // 构建HTTP响应实体，将API响应对象转换为JSON字符串
        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, notices);
        // 设置响应实体到数据流上下文
        context.setResponseEntity(responseEntity);
    }
}
