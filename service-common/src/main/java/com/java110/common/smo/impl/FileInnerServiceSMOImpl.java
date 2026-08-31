package com.java110.common.smo.impl;

import com.java110.common.dao.IFileServiceDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.client.CosUploadTemplate;
import com.java110.core.client.S3UploadTemplate;
import com.java110.dto.file.FileDto;
import com.java110.intf.common.IFileInnerServiceSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.util.COSUtil;
import com.java110.utils.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FileInnerServiceSMOImpl extends BaseServiceSMO implements IFileInnerServiceSMO {

    private static final String ROOT_PATH = "hc/";

    @Autowired
    private IFileServiceDao fileServiceDaoImpl;

    @Autowired
    private S3UploadTemplate s3UploadTemplate;

    @Autowired
    private CosUploadTemplate cosUploadTemplate;

    @Override
    public String saveFile(@RequestBody FileDto fileDto) {

        //int saveFileFlag = fileServiceDaoImpl.saveFile(BeanConvertUtil.beanCovertMap(fileDto));
        String fileName = "";
        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, S3Util.S3_SWITCH);

        if (S3Util.S3_SWITCH_S3.equals(ossSwitch)) {
            fileName = s3UploadTemplate.upload(fileDto.getContext(), ROOT_PATH);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            fileName = cosUploadTemplate.upload(fileDto.getContext(), ROOT_PATH);
        } else {
            fileName = s3UploadTemplate.upload(fileDto.getContext(), ROOT_PATH);
        }
        return fileName;
    }

    @Override
    public List<FileDto> queryFiles(@RequestBody FileDto fileDto) {
        //return BeanConvertUtil.covertBeanList(fileServiceDaoImpl.getFiles(BeanConvertUtil.beanCovertMap(fileDto)), FileDto.class);
        List<FileDto> fileDtos = new ArrayList<>();
        String fileName = fileDto.getFileSaveName();
        String objectPath = "hc/";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (fileName.contains("/")) {
            objectPath += fileName.substring(0, fileName.lastIndexOf("/") + 1);
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
        }
        String context = "";
        String ossSwitch = MappingCache.getValue(MappingConstant.FILE_DOMAIN, S3Util.S3_SWITCH);
        if (S3Util.S3_SWITCH_S3.equals(ossSwitch)) {
            context = s3UploadTemplate.download(objectPath, fileName);
        } else if (COSUtil.COS_SWITCH_COS.equals(ossSwitch)) {
            context = cosUploadTemplate.download(objectPath, fileName);
        } else {
            context = s3UploadTemplate.download(objectPath, fileName);
        }

        fileDto.setContext(context);
        fileDto.setSuffix(suffix);
        fileDtos.add(fileDto);
        return fileDtos;
    }

    public IFileServiceDao getFileServiceDaoImpl() {
        return fileServiceDaoImpl;
    }

    public void setFileServiceDaoImpl(IFileServiceDao fileServiceDaoImpl) {
        this.fileServiceDaoImpl = fileServiceDaoImpl;
    }
}
