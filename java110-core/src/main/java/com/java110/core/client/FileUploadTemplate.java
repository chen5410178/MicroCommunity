package com.java110.core.client;

import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class FileUploadTemplate {

    private static final String ROOT_PATH = "hc/";


    @Autowired
    private S3UploadTemplate s3UploadTemplate;

    @Autowired
    private CosUploadTemplate cosUploadTemplate;


    public String saveFile(InputStream inputStream,String fileName){

        String newfileName = ROOT_PATH+ fileName;

        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, S3Util.S3_SWITCH);

        if (S3Util.S3_SWITCH_S3.equals(ossSwitch)) {
            newfileName = s3UploadTemplate.upload(inputStream, newfileName);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            newfileName = cosUploadTemplate.upload(inputStream, newfileName);
        } else {
            newfileName = s3UploadTemplate.upload(inputStream, newfileName);
        }
        return newfileName;
    }

    public InputStream downloadFile(String fileName){

        InputStream inputStream = null;

        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, S3Util.S3_SWITCH);

        if (S3Util.S3_SWITCH_S3.equals(ossSwitch)) {
            inputStream = s3UploadTemplate.download(fileName);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            inputStream = cosUploadTemplate.download(fileName);
        } else {
            inputStream = s3UploadTemplate.download(fileName);
        }

        return inputStream;
    }
}
