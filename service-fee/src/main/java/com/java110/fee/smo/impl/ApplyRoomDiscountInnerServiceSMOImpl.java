package com.java110.fee.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.PageDto;
import com.java110.dto.room.ApplyRoomDiscountDto;
import com.java110.fee.dao.IApplyRoomDiscountServiceDao;
import com.java110.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.java110.po.room.ApplyRoomDiscountPo;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 房屋折扣申请内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ApplyRoomDiscountInnerServiceSMOImpl extends BaseServiceSMO implements IApplyRoomDiscountInnerServiceSMO {

    @Autowired
    private IApplyRoomDiscountServiceDao applyRoomDiscountServiceDaoImpl;

    //子表 apply_room_discount_fee 主键前缀
    private static final String CODE_PREFIX_ARD_FEE_ID = "15";


    @Override
    public int saveApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        int saveFlag = 1;
        applyRoomDiscountServiceDaoImpl.saveApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountPo));
        return saveFlag;
    }

    @Override
    public int saveApplyRoomDiscountFees(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        if (applyRoomDiscountPo.getFeeIds() == null || applyRoomDiscountPo.getFeeIds().size() < 1) {
            return 0;
        }
        List<String> feeIds = applyRoomDiscountPo.getFeeIds();
        List<String> feeNames = applyRoomDiscountPo.getFeeNames();
        for (int feeIndex = 0; feeIndex < feeIds.size(); feeIndex++) {
            Map<String, Object> info = new HashMap<>();
            info.put("ardFeeId", GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ARD_FEE_ID));
            info.put("ardId", applyRoomDiscountPo.getArdId());
            info.put("feeId", feeIds.get(feeIndex));
            if (feeNames != null && feeNames.size() > feeIndex) {
                info.put("feeName", feeNames.get(feeIndex));
            } else {
                info.put("feeName", "");
            }
            info.put("communityId", applyRoomDiscountPo.getCommunityId());
            info.put("createTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            info.put("statusCd", "0");
            applyRoomDiscountServiceDaoImpl.saveApplyRoomDiscountFeeInfo(info);
        }
        return feeIds.size();
    }

    @Override
    public int deleteApplyRoomDiscountFees(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        Map<String, Object> info = new HashMap<>();
        info.put("ardId", applyRoomDiscountPo.getArdId());
        applyRoomDiscountServiceDaoImpl.deleteApplyRoomDiscountFeeByArdId(info);
        return 1;
    }

    @Override
    public int updateApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        int saveFlag = 1;
        applyRoomDiscountServiceDaoImpl.updateApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountPo));
        return saveFlag;
    }

    @Override
    public int deleteApplyRoomDiscount(@RequestBody ApplyRoomDiscountPo applyRoomDiscountPo) {
        int saveFlag = 1;
        applyRoomDiscountPo.setStatusCd("1");
        applyRoomDiscountServiceDaoImpl.updateApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountPo));
        deleteApplyRoomDiscountFees(applyRoomDiscountPo);
        return saveFlag;
    }

    @Override
    public List<ApplyRoomDiscountDto> queryApplyRoomDiscounts(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto) {

        //校验是否传了 分页信息

        int page = applyRoomDiscountDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            applyRoomDiscountDto.setPage((page - 1) * applyRoomDiscountDto.getRow());
        }

        List<Map> infos = applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountInfo(BeanConvertUtil.beanCovertMap(applyRoomDiscountDto));
        freshFeeIds(infos);
        List<ApplyRoomDiscountDto> applyRoomDiscounts = BeanConvertUtil.covertBeanList(infos, ApplyRoomDiscountDto.class);

        return applyRoomDiscounts;
    }

    @Override
    public List<ApplyRoomDiscountDto> queryFirstApplyRoomDiscounts(ApplyRoomDiscountDto applyRoomDiscountDto) {
        List<Map> infos = applyRoomDiscountServiceDaoImpl.queryFirstApplyRoomDiscounts(BeanConvertUtil.beanCovertMap(applyRoomDiscountDto));
        freshFeeIds(infos);
        List<ApplyRoomDiscountDto> applyRoomDiscounts = BeanConvertUtil.covertBeanList(infos, ApplyRoomDiscountDto.class);
        return applyRoomDiscounts;
    }

    /**
     * GROUP_CONCAT 返回的 feeIds/feeNames 为逗号分隔字符串，转换为 List
     *
     * @param infos
     */
    private void freshFeeIds(List<Map> infos) {
        if (infos == null || infos.size() < 1) {
            return;
        }
        for (Map info : infos) {
            if (info.get("feeIds") != null && !StringUtil.isEmpty(info.get("feeIds").toString())) {
                info.put("feeIds", new ArrayList<>(java.util.Arrays.asList(info.get("feeIds").toString().split(","))));
            } else {
                info.put("feeIds", new ArrayList<>());
            }
            if (info.get("feeNames") != null && !StringUtil.isEmpty(info.get("feeNames").toString())) {
                info.put("feeNames", new ArrayList<>(java.util.Arrays.asList(info.get("feeNames").toString().split(","))));
            } else {
                info.put("feeNames", new ArrayList<>());
            }
        }
    }


    @Override
    public int queryApplyRoomDiscountsCount(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto) {
        return applyRoomDiscountServiceDaoImpl.queryApplyRoomDiscountsCount(BeanConvertUtil.beanCovertMap(applyRoomDiscountDto));
    }

    public IApplyRoomDiscountServiceDao getApplyRoomDiscountServiceDaoImpl() {
        return applyRoomDiscountServiceDaoImpl;
    }

    public void setApplyRoomDiscountServiceDaoImpl(IApplyRoomDiscountServiceDao applyRoomDiscountServiceDaoImpl) {
        this.applyRoomDiscountServiceDaoImpl = applyRoomDiscountServiceDaoImpl;
    }
}
