package com.java110.fee.cmd.feeType;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.dict.DictDto;
import com.java110.dto.feeType.FeeTypeDto;
import com.java110.intf.dev.IDictV1InnerServiceSMO;
import com.java110.intf.fee.IFeeTypeV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.ListUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@Java110Cmd(serviceCode = "feeType.queryFeeTypeTree")
public class QueryFeeTypeTreeCmd extends Cmd {

    @Autowired
    private IFeeTypeV1InnerServiceSMO feeTypeV1InnerServiceSMOImpl;

    @Autowired
    private IDictV1InnerServiceSMO dictV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区编号");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        DictDto dictDto = new DictDto();
        dictDto.setTableName("pay_fee_config");
        dictDto.setTableColumns("fee_type_cd");
        List<DictDto> dictDtos = dictV1InnerServiceSMOImpl.queryDicts(dictDto);

        JSONArray feeTypeCds = new JSONArray();
        if (ListUtil.isNull(dictDtos)) {
            context.setResponseEntity(ResultVo.createResponseEntity(feeTypeCds));
            return;
        }
        JSONObject feeTypeInfo = null;

        feeTypeInfo = new JSONObject();
        feeTypeInfo.put("id", "c_0");
        feeTypeInfo.put("feeTypeCd", "");
        feeTypeInfo.put("text", "全部");
        feeTypeInfo.put("icon", "/img/org.png");
        feeTypeInfo.put("children", new JSONArray());
        feeTypeCds.add(feeTypeInfo);

        for (DictDto tmpDictDto : dictDtos) {
            feeTypeInfo = new JSONObject();
            feeTypeInfo.put("id", "c_" + tmpDictDto.getStatusCd());
            feeTypeInfo.put("feeTypeCd", tmpDictDto.getStatusCd());
            feeTypeInfo.put("text", tmpDictDto.getName());
            feeTypeInfo.put("icon", "/img/org.png");
            feeTypeInfo.put("children", new JSONArray());
            feeTypeCds.add(feeTypeInfo);
        }
        FeeTypeDto feeTypeDto = new FeeTypeDto();
        feeTypeDto.setCommunityId(reqJson.getString("communityId"));
        List<FeeTypeDto> feeTypeDtos = feeTypeV1InnerServiceSMOImpl.queryFeeTypes(feeTypeDto);


        JSONObject feeType = null;
        for (int cIndex = 0; cIndex < feeTypeCds.size(); cIndex++) {
            feeType = feeTypeCds.getJSONObject(cIndex);
            // find floor data in unitDtos
            findFeeType(feeType, feeTypeDtos);
        }
        context.setResponseEntity(ResultVo.createResponseEntity(feeTypeCds));

    }

    private void findFeeType(JSONObject feeTypeCdInfo, List<FeeTypeDto> feeTypeDtos) {

        JSONArray feeTypes = feeTypeCdInfo.getJSONArray("children");
        JSONObject feeType = null;
        for (FeeTypeDto tmpFeeTypeDto : feeTypeDtos) {
            if(!tmpFeeTypeDto.getFeeTypeCd().equals(feeTypeCdInfo.getString("feeTypeCd"))){
                continue;
            }
            feeType = new JSONObject();
            feeType.put("id", "f_" + tmpFeeTypeDto.getFtId());
            feeType.put("ftId", tmpFeeTypeDto.getFtId());
            feeType.put("feeTypeCd", tmpFeeTypeDto.getFeeTypeCd());
            feeType.put("text", tmpFeeTypeDto.getFtName());
            feeType.put("icon", "/img/floor.png");
            feeType.put("children", new JSONArray());
            feeTypes.add(feeType);
        }
    }
}
