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
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        Map info = BeanConvertUtil.beanCovertMap(applyRoomDiscountDto);

        //1. feeId 过滤：先查子表反查 ardIds，避免 SQL EXISTS（only_full_group_by 兼容）
        if (!StringUtil.isEmpty(applyRoomDiscountDto.getFeeId())) {
            List<Map> feeInfos = applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountFees(info);
            List<String> ardIds = getDistinctArdIds(feeInfos);
            if (ListUtil.isNull(ardIds)) {
                return new ArrayList<>();
            }
            info.put("ardIds", ardIds);
        }

        //2. 查主表（无 GROUP BY / 无子表联表 / 无 EXISTS）
        List<Map> infos = applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountInfo(info);

        //3. 按 ardIds 查子表并组装 feeIds/feeNames
        if (infos != null && infos.size() > 0) {
            Map feeInfoMap = new HashMap();
            feeInfoMap.put("ardIds", getArdIdsFromInfos(infos));
            List<Map> feeInfos = applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountFees(feeInfoMap);
            freshFeeIds(infos, feeInfos);
        }

        List<ApplyRoomDiscountDto> applyRoomDiscounts = BeanConvertUtil.covertBeanList(infos, ApplyRoomDiscountDto.class);

        return applyRoomDiscounts;
    }

    @Override
    public List<ApplyRoomDiscountDto> queryFirstApplyRoomDiscounts(ApplyRoomDiscountDto applyRoomDiscountDto) {
        Map info = BeanConvertUtil.beanCovertMap(applyRoomDiscountDto);

        //1. feeId 过滤：先查子表反查 ardIds，避免 SQL EXISTS（only_full_group_by 兼容）
        if (!StringUtil.isEmpty(applyRoomDiscountDto.getFeeId())) {
            List<Map> feeInfos = applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountFees(info);
            List<String> ardIds = getDistinctArdIds(feeInfos);
            if (ListUtil.isNull(ardIds)) {
                return new ArrayList<>();
            }
            info.put("ardIds", ardIds);
        }

        //2. 查主表（无 GROUP BY / 无子表联表 / 无 EXISTS）
        List<Map> infos = applyRoomDiscountServiceDaoImpl.queryFirstApplyRoomDiscounts(info);

        //3. 按 ardIds 查子表并组装 feeIds/feeNames
        if (infos != null && infos.size() > 0) {
            Map feeInfoMap = new HashMap();
            feeInfoMap.put("ardIds", getArdIdsFromInfos(infos));
            List<Map> feeInfos = applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountFees(feeInfoMap);
            freshFeeIds(infos, feeInfos);
        }

        List<ApplyRoomDiscountDto> applyRoomDiscounts = BeanConvertUtil.covertBeanList(infos, ApplyRoomDiscountDto.class);
        return applyRoomDiscounts;
    }

    /**
     * 从子表查询结果中提取去重后的 ardId 集合（过滤 null/空，保证顺序稳定）
     *
     * @param feeInfos 子表查询结果
     * @return ardId 集合
     */
    private List<String> getDistinctArdIds(List<Map> feeInfos) {
        Set<String> ardIds = new LinkedHashSet<>();
        if (feeInfos == null || feeInfos.size() < 1) {
            return new ArrayList<>(ardIds);
        }
        for (Map feeInfo : feeInfos) {
            Object ardId = feeInfo.get("ardId");
            if (ardId != null && !StringUtil.isEmpty(ardId.toString())) {
                ardIds.add(ardId.toString());
            }
        }
        return new ArrayList<>(ardIds);
    }

    /**
     * 从主表查询结果中提取去重后的 ardId 集合
     *
     * @param infos 主表查询结果
     * @return ardId 集合
     */
    private List<String> getArdIdsFromInfos(List<Map> infos) {
        Set<String> ardIds = new LinkedHashSet<>();
        if (infos == null || infos.size() < 1) {
            return new ArrayList<>(ardIds);
        }
        for (Map info : infos) {
            Object ardId = info.get("ardId");
            if (ardId != null && !StringUtil.isEmpty(ardId.toString())) {
                ardIds.add(ardId.toString());
            }
        }
        return new ArrayList<>(ardIds);
    }

    /**
     * 按 ardId 分组组装 feeIds/feeNames（List<String>），替代原 GROUP_CONCAT 逗号拆分逻辑
     *
     * @param infos    主表查询结果
     * @param feeInfos 子表查询结果
     */
    private void freshFeeIds(List<Map> infos, List<Map> feeInfos) {
        if (infos == null || infos.size() < 1) {
            return;
        }
        Map<String, List<String>> ardFeeIdMap = new HashMap<>();
        Map<String, List<String>> ardFeeNameMap = new HashMap<>();
        if (feeInfos != null) {
            for (Map feeInfo : feeInfos) {
                String ardId = feeInfo.get("ardId") == null ? "" : feeInfo.get("ardId").toString();
                List<String> feeIds = ardFeeIdMap.get(ardId);
                if (feeIds == null) {
                    feeIds = new ArrayList<>();
                    ardFeeIdMap.put(ardId, feeIds);
                }
                feeIds.add(feeInfo.get("feeId") == null ? "" : feeInfo.get("feeId").toString());
                List<String> feeNames = ardFeeNameMap.get(ardId);
                if (feeNames == null) {
                    feeNames = new ArrayList<>();
                    ardFeeNameMap.put(ardId, feeNames);
                }
                feeNames.add(feeInfo.get("feeName") == null ? "" : feeInfo.get("feeName").toString());
            }
        }
        for (Map info : infos) {
            String ardId = info.get("ardId") == null ? "" : info.get("ardId").toString();
            info.put("feeIds", ardFeeIdMap.getOrDefault(ardId, new ArrayList<>()));
            info.put("feeNames", ardFeeNameMap.getOrDefault(ardId, new ArrayList<>()));
        }
    }


    @Override
    public int queryApplyRoomDiscountsCount(@RequestBody ApplyRoomDiscountDto applyRoomDiscountDto) {
        Map info = BeanConvertUtil.beanCovertMap(applyRoomDiscountDto);

        //feeId 过滤：先查子表反查 ardIds，避免 SQL EXISTS（only_full_group_by 兼容）
        if (!StringUtil.isEmpty(applyRoomDiscountDto.getFeeId())) {
            List<Map> feeInfos = applyRoomDiscountServiceDaoImpl.getApplyRoomDiscountFees(info);
            List<String> ardIds = getDistinctArdIds(feeInfos);
            if (ListUtil.isNull(ardIds)) {
                return 0;
            }
            info.put("ardIds", ardIds);
        }

        return applyRoomDiscountServiceDaoImpl.queryApplyRoomDiscountsCount(info);
    }

    public IApplyRoomDiscountServiceDao getApplyRoomDiscountServiceDaoImpl() {
        return applyRoomDiscountServiceDaoImpl;
    }

    public void setApplyRoomDiscountServiceDaoImpl(IApplyRoomDiscountServiceDao applyRoomDiscountServiceDaoImpl) {
        this.applyRoomDiscountServiceDaoImpl = applyRoomDiscountServiceDaoImpl;
    }
}
