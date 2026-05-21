package com.java110.utils.util;

import com.java110.utils.cache.MappingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class S3Util {

    private static final Logger logger = LoggerFactory.getLogger(S3Util.class);

    public static final String DOMAIN = "OSS";
    public static final String S3_SWITCH = "OSS_SWITCH";
    public static final String S3_SWITCH_S3 = "S3";
    public static final String S3_SWITCH_FTP = "FTP";
    public static final String ENDPOINT = "endpoint";
    public static final String ACCESS_KEY_ID = "accessKeyId";
    public static final String ACCESS_KEY_SECRET = "accessKeySecret";
    public static final String BUCKET_NAME = "bucketName";
    public static final String REGION = "region";

    public static S3Client getS3Client() {
        String endpoint = MappingCache.getValue(DOMAIN, ENDPOINT);
        String accessKeyId = MappingCache.getValue(DOMAIN, ACCESS_KEY_ID);
        String accessKeySecret = MappingCache.getValue(DOMAIN, ACCESS_KEY_SECRET);
        String region = MappingCache.getValue(DOMAIN, REGION);
        if (StringUtil.isEmpty(region)) {
            region = extractRegionFromEndpoint(endpoint);
            logger.info("S3 region未配置，从endpoint自动提取: {}", region);
        }
        logger.info("S3Client初始化: endpoint={}, region={}, bucket={}",
                endpoint, region, MappingCache.getValue(DOMAIN, BUCKET_NAME));
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint.indexOf("://") == -1 ? "https://" + endpoint : endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, accessKeySecret)
                ))
                .build();
        return s3Client;
    }

    private static String extractRegionFromEndpoint(String endpoint) {
        if (StringUtil.isEmpty(endpoint)) {
            return "us-east-1";
        }
        String host = endpoint;
        if (host.contains("://")) {
            host = host.substring(host.indexOf("://") + 3);
        }
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("oss-([^.]+)\\.").matcher(host);
        if (m.find()) {
            return m.group(1);
        }
        java.util.regex.Matcher m2 = java.util.regex.Pattern.compile("s3[.-]([^.]+)\\.").matcher(host);
        if (m2.find()) {
            return m2.group(1);
        }
        return "us-east-1";
    }

    public static void uploadByInputStream(S3Client s3Client, InputStream inputStream,
                                           String objectName) {
        String bucketName = MappingCache.getValue(DOMAIN, BUCKET_NAME);
        uploadByInputStream(s3Client, inputStream, bucketName, objectName);
    }

    public static void uploadByInputStream(S3Client s3Client, InputStream inputStream, String bucketName,
                                           String objectName) {
        try {
            byte[] bytes = readInputStream(inputStream);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build();
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            if (s3Client != null) {
                s3Client.close();
            }
        }
    }

    private static byte[] readInputStream(InputStream inputStream) throws Exception {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int n;
        while ((n = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, n);
        }
        return buffer.toByteArray();
    }

    public static void uploadByFile(S3Client s3Client, File file, String bucketName, String objectName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build();
            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s3Client != null) {
                s3Client.close();
            }
        }
    }

    public static void deleteFile(S3Client s3Client, String bucketName, String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    public static InputStream getInputStreamByS3(S3Client s3Client, String key) {
        String bucketName = MappingCache.getValue(DOMAIN, BUCKET_NAME);
        return getInputStreamByS3(s3Client, bucketName, key);
    }

    public static InputStream getInputStreamByS3(S3Client s3Client, String bucketName, String key) {
        InputStream content = null;
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            content = s3Client.getObject(getObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public static List<String> queryAllObject(S3Client s3Client, String bucketName) {
        List<String> results = new ArrayList<String>();
        try {
            ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);
            for (S3Object s3Object : listObjectsResponse.contents()) {
                results.add(" - " + s3Object.key() + "  " + "(size = " + s3Object.size() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s3Client != null) {
                s3Client.close();
            }
        }
        return results;
    }
}
