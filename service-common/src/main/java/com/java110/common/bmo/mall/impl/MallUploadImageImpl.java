package com.java110.common.bmo.mall.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.bmo.mall.IMallCommonApiBmo;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("mallUploadImageImpl")
public class MallUploadImageImpl implements IMallCommonApiBmo {

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Override
    public void validate(ICmdDataFlowContext context, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson,"uploadFile","未包含图片内容");
    }

    @Override
    public void doCmd(ICmdDataFlowContext context, JSONObject reqJson) {
        JSONObject paramIn = new JSONObject();
        paramIn.put("context", reqJson.getString("uploadFile"));
        paramIn.put("fileName", "upload.jpg");

        FileDto fileDto = BeanConvertUtil.covertBean(paramIn, FileDto.class);

        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
//        String file = paramIn.getString("fileName");
//        if (file.contains("mp4") || file.contains("MP4") || file.contains("AVI") || file.contains("avi")
//                || file.contains("WMV") || file.contains("wmv")) {
//            fileDto.setFlag("");
//        } else {
//            fileDto.setFlag("0");
//        }

        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);

        JSONObject outParam = new JSONObject();
        outParam.put("fileId", fileName);
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        outParam.put("url", imgUrl + fileName);



        ResponseEntity<String> responseEntity =  ResultVo.createResponseEntity(outParam);
        context.setResponseEntity(responseEntity);
    }
}
