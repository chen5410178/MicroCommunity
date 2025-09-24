package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IPayFeeAuditInnerServiceSMO
 * @Description 缴费审核接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/initializePayFeeApi")
public interface IInitializePayFeeInnerServiceSMO {

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deletePayFee", method = RequestMethod.POST)
    int deletePayFee(@RequestBody Map communityId);

    /**
     * 按费用项删除缴费记录
     * @param configIds
     */
    @RequestMapping(value = "/deleteConfigsFeeDetail", method = RequestMethod.POST)
    int deleteConfigsFeeDetail(@RequestBody List<String> configIds);

    @RequestMapping(value = "/deleteConfigsFeeReceipt", method = RequestMethod.POST)
    int deleteConfigsFeeReceipt(@RequestBody List<String> configIds);

    @RequestMapping(value = "/deleteConfigsMeterWater", method = RequestMethod.POST)
    int deleteConfigsMeterWater(@RequestBody List<String> configIds);

    @RequestMapping(value = "/deleteConfigsFeeDetailMonth", method = RequestMethod.POST)
    int deleteConfigsFeeDetailMonth(@RequestBody List<String> configIds);

    @RequestMapping(value = "/deleteConfigsFee", method = RequestMethod.POST)
    int deleteConfigsFee(@RequestBody List<String> configIds);

    @RequestMapping(value = "/deleteConfigsFeeRules", method = RequestMethod.POST)
    int deleteConfigsFeeRules(@RequestBody List<String> configIds);

    @RequestMapping(value = "/deleteAssetImportLog", method = RequestMethod.POST)
    int deleteAssetImportLog(@RequestBody String communityId);

    @RequestMapping(value = "/deleteAssetImportLogDetail", method = RequestMethod.POST)
    int deleteAssetImportLogDetail(@RequestBody String communityId);

    @RequestMapping(value = "/deleteImportFee", method = RequestMethod.POST)
    int deleteImportFee(@RequestBody String communityId);

    @RequestMapping(value = "/deleteImportFeeDetail", method = RequestMethod.POST)
    int deleteImportFeeDetail(@RequestBody String communityId);

    @RequestMapping(value = "/deletePayFeeOrder", method = RequestMethod.POST)
    int deletePayFeeOrder(@RequestBody String communityId);
}
