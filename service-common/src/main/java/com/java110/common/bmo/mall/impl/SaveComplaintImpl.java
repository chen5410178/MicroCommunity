package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.community.CommunityMemberDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.dto.complaint.ComplaintEventDto;
import com.java110.dto.complaint.ComplaintTypeDto;
import com.java110.dto.data.DatabusDataDto;
import com.java110.dto.file.FileDto;
import com.java110.dto.room.RoomDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.intf.common.IFileRelInnerServiceSMO;
import com.java110.intf.community.ICommunityInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.job.IDataBusInnerServiceSMO;
import com.java110.intf.store.IComplaintEventV1InnerServiceSMO;
import com.java110.intf.store.IComplaintTypeV1InnerServiceSMO;
import com.java110.intf.store.IComplaintV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.po.complaint.ComplaintEventPo;
import com.java110.po.complaint.ComplaintPo;
import com.java110.po.file.FileRelPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveComplaintImpl")
public class SaveComplaintImpl implements IMallCommonApiBmo {

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    /** 投诉类型服务接口 */
    @Autowired
    private IComplaintTypeV1InnerServiceSMO complaintTypeV1InnerServiceSMOImpl;

    /** 房屋信息服务接口 */
    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    /** 小区信息服务接口 */
    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    /** 投诉事件服务接口 */
    @Autowired
    private IComplaintEventV1InnerServiceSMO complaintEventV1InnerServiceSMOImpl;

    /** 用户信息服务接口 */
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    /** 文件服务接口 */
    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    /** 文件关联服务接口 */
    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /** 数据总线服务接口 */
    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        // 验证必填字段
        Assert.hasKeyAndValue(reqJson, "complaintName", "请求报文中未包含complaintName");
        Assert.hasKeyAndValue(reqJson, "typeCd", "请求报文中未包含typeCd");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");

        // 验证投诉类型是否存在
        ComplaintTypeDto complaintTypeDto = new ComplaintTypeDto();
        complaintTypeDto.setTypeCd(reqJson.getString("typeCd"));
        complaintTypeDto.setCommunityId(reqJson.getString("communityId"));
        List<ComplaintTypeDto> complaintTypeDtos = complaintTypeV1InnerServiceSMOImpl.queryComplaintTypes(complaintTypeDto);

        Assert.listOnlyOne(complaintTypeDtos, "投诉类型不存在");

        // 验证房屋信息是否存在
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋不存在");

        // 构建房屋完整名称：楼层-单元-房间号
        reqJson.put("roomName", roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());

        // 处理业主信息，设置默认值
        String ownerId = roomDtos.get(0).getOwnerId();
        if (StringUtil.isEmpty(ownerId)) {
            ownerId = "-1"; // 默认业主ID
        }
        String ownerName = roomDtos.get(0).getOwnerName();
        if (StringUtil.isEmpty(ownerName)) {
            ownerName = "未知"; // 默认业主姓名
        }
        reqJson.put("ownerId", ownerId);
        reqJson.put("ownerName", ownerName);
    }

    @Override
    @Java110Transactional
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        // 获取当前用户ID

        // 验证用户信息
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("mallTel"));
        userDto.setLevelCd(UserDto.LEVEL_CD_USER);

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        // 验证小区是否已入驻物业
        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(reqJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "小区未入驻");

        // 保存投诉主信息
        ComplaintPo complaintPo = BeanConvertUtil.covertBean(reqJson, ComplaintPo.class);
        complaintPo.setComplaintId(GenerateCodeFactory.getGeneratorId("11"));
        complaintPo.setState(ComplaintDto.STATE_WAIT); // 设置初始状态为等待处理
        complaintPo.setStoreId(communityMemberDtos.get(0).getMemberId()); // 设置物业ID
        complaintPo.setStartUserId(userDtos.get(0).getUserId()); // 设置发起用户ID
        int flag = complaintV1InnerServiceSMOImpl.saveComplaint(complaintPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        // 保存投诉事件记录
        ComplaintEventPo complaintEventPo = new ComplaintEventPo();
        complaintEventPo.setRemark("用户提交投诉");
        complaintEventPo.setComplaintId(complaintPo.getComplaintId());
        complaintEventPo.setCommunityId(complaintPo.getCommunityId());
        complaintEventPo.setEventType(ComplaintEventDto.EVENT_TYPE_SUBMIT); // 事件类型：提交
        complaintEventPo.setCreateUserId(userDtos.get(0).getUserId());
        complaintEventPo.setCreateUserName(userDtos.get(0).getName());
        complaintEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        flag = complaintEventV1InnerServiceSMOImpl.saveComplaintEvent(complaintEventPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        // 保存投诉相关图片
        savePhone(reqJson, complaintPo);

        // 发送投诉通知给处理人员
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_SEND_COMPLAINT_NOTIFY_STAFF,BeanConvertUtil.beanCovertJson(complaintPo)));

        // 设置成功响应
        context.setResponseEntity(ResultVo.success());
    }

    /**
     * 保存投诉相关图片
     *
     * 处理投诉相关的图片附件，包括图片数据存储和文件关联关系建立
     *
     * @param reqJson 请求JSON数据，包含图片信息
     * @param complaintPo 投诉实体对象
     */
    private void savePhone(JSONObject reqJson, ComplaintPo complaintPo) {
        // 检查是否存在图片数据
        if (!reqJson.containsKey("photos")) {
            return;
        }

        JSONArray photos = reqJson.getJSONArray("photos");
        if (ListUtil.isNull(photos)) {
            return;
        }

        // 遍历处理所有图片
        for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
            String _photo = photos.getString(photoIndex);

            // 如果图片数据长度超过512，说明是base64编码的图片数据，需要保存到文件系统
            if (_photo.length() > 512) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(_photo); // 图片base64数据
                fileDto.setSuffix("jpeg"); // 文件后缀
                fileDto.setCommunityId(reqJson.getString("communityId"));
                _photo = fileInnerServiceSMOImpl.saveFile(fileDto); // 保存文件并返回文件路径
            }

            // 建立文件与投诉的关联关系
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            businessUnit.put("relTypeCd", "13000"); // 关联类型编码
            businessUnit.put("saveWay", "table"); // 存储方式
            businessUnit.put("objId", complaintPo.getComplaintId()); // 关联对象ID
            businessUnit.put("fileRealName", _photo); // 文件真实名称
            businessUnit.put("fileSaveName", _photo); // 文件保存名称
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo); // 保存文件关联关系
        }
    }
}
