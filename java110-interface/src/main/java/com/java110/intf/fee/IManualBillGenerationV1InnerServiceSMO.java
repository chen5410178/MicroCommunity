package com.java110.intf.fee;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.fee.ManualBillGenerationDetailDto;
import com.java110.dto.fee.ManualBillGenerationDto;
import com.java110.po.fee.ManualBillGenerationDetailPo;
import com.java110.po.fee.ManualBillGenerationPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/manualBillGenerationApi")
public interface IManualBillGenerationV1InnerServiceSMO {

    @RequestMapping(value = "/saveManualBillGeneration", method = RequestMethod.POST)
    int saveManualBillGeneration(@RequestBody ManualBillGenerationPo manualBillGenerationPo);

    @RequestMapping(value = "/updateManualBillGeneration", method = RequestMethod.POST)
    int updateManualBillGeneration(@RequestBody ManualBillGenerationPo manualBillGenerationPo);

    @RequestMapping(value = "/deleteManualBillGeneration", method = RequestMethod.POST)
    int deleteManualBillGeneration(@RequestBody ManualBillGenerationPo manualBillGenerationPo);

    @RequestMapping(value = "/saveManualBillGenerationDetail", method = RequestMethod.POST)
    int saveManualBillGenerationDetail(@RequestBody ManualBillGenerationDetailPo manualBillGenerationDetailPo);

    @RequestMapping(value = "/saveManualBillGenerationDetails", method = RequestMethod.POST)
    int saveManualBillGenerationDetails(@RequestBody List<ManualBillGenerationDetailPo> manualBillGenerationDetailPos);

    @RequestMapping(value = "/updateManualBillGenerationDetail", method = RequestMethod.POST)
    int updateManualBillGenerationDetail(@RequestBody ManualBillGenerationDetailPo manualBillGenerationDetailPo);

    @RequestMapping(value = "/queryManualBillGenerations", method = RequestMethod.POST)
    List<ManualBillGenerationDto> queryManualBillGenerations(@RequestBody ManualBillGenerationDto manualBillGenerationDto);

    @RequestMapping(value = "/queryManualBillGenerationsCount", method = RequestMethod.POST)
    int queryManualBillGenerationsCount(@RequestBody ManualBillGenerationDto manualBillGenerationDto);

    @RequestMapping(value = "/queryManualBillGenerationDetails", method = RequestMethod.POST)
    List<ManualBillGenerationDetailDto> queryManualBillGenerationDetails(@RequestBody ManualBillGenerationDetailDto manualBillGenerationDetailDto);

    @RequestMapping(value = "/queryManualBillGenerationDetailsCount", method = RequestMethod.POST)
    int queryManualBillGenerationDetailsCount(@RequestBody ManualBillGenerationDetailDto manualBillGenerationDetailDto);
}
