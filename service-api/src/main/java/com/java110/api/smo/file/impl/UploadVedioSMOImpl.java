package com.java110.api.smo.file.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.file.IUploadVedioSMO;
import com.java110.core.client.CosUploadTemplate;
import com.java110.core.client.S3UploadTemplate;
import com.java110.core.context.IPageData;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 添加小区服务实现类
 * add by wuxw 2019-06-30
 */
@Service("uploadVedioSMOImpl")
public class UploadVedioSMOImpl extends DefaultAbstractComponentSMO implements IUploadVedioSMO {

    private static final String ROOT_PATH = "hc/";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private S3UploadTemplate s3UploadTemplate;

    @Autowired
    private CosUploadTemplate cosUploadTemplate;

    @Override
    public ResponseEntity<Object> upload(IPageData pd, MultipartFile uploadFile) throws IOException {

        //JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        if (uploadFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("上传文件超过1024兆");
        }

        String fileName = "";
        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, S3Util.S3_SWITCH);
        if (S3Util.S3_SWITCH_S3.equals(ossSwitch)) {
            fileName = s3UploadTemplate.upload(uploadFile, ROOT_PATH);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            fileName = cosUploadTemplate.upload(uploadFile, ROOT_PATH);
        } else {
            fileName = s3UploadTemplate.upload(uploadFile, ROOT_PATH);
        }
        JSONObject outParam = new JSONObject();
        outParam.put("fileName", uploadFile.getOriginalFilename());
        outParam.put("realFileName", fileName);

        return new ResponseEntity<Object>(outParam.toJSONString(), HttpStatus.OK);

    }


    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
