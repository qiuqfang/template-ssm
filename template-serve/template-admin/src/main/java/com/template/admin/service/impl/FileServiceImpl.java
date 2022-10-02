package com.template.admin.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.template.admin.controller.FileController;
import com.template.admin.service.FileService;
import io.minio.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
@Service
public class FileServiceImpl implements FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Value("${minio.bucketName}")
    private String minioBucketName;
    @Value("${minio.endpoint}")
    private String minioEndpoint;
    @Value("${minio.accessKey}")
    private String minioAccessKey;
    @Value("${minio.secretKey}")
    private String minioSecretKey;

    @Override
    public Map<String, Object> minioUpload(MultipartFile file, HttpServletRequest request) {
        try {
            //创建一个MinIO的Java客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioEndpoint)
                    .credentials(minioAccessKey, minioSecretKey)
                    .build();
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioBucketName)
                    .build());
            if (isExist) {
                LOGGER.info("存储桶已经存在！");
            } else {
                //创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioBucketName)
                        .build());
            }
            String fileName = renameFileName(file);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String folderName = sdf.format(new Date());
            // 设置存储对象名称
            String objectName = folderName + "/" + fileName;
            // 使用putObject上传一个文件到存储桶中
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioBucketName)
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), 0)
                    .build());
            LOGGER.info("文件上传成功!");
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(minioBucketName)
                    .object(objectName)
                    // 通过什么方式访问上传的图片
                    .method(Method.GET)
                    // 设置过期时间
                    .expiry(7, TimeUnit.DAYS)
                    .build());
            Map<String, Object> map = new HashMap<>();
            map.put("folderName", folderName);
            map.put("name", fileName);
            map.put("url", presignedObjectUrl);
            return map;
        } catch (Exception e) {
            LOGGER.info("上传文件发生错误: {}！", e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean minioDelete(String folderName, String name) {
        try {
            //创建一个MinIO的Java客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioEndpoint)
                    .credentials(minioAccessKey, minioSecretKey)
                    .build();
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioBucketName)
                    .object(folderName + "/" + name)
                    .build());
            return Boolean.TRUE;
        } catch (Exception e) {
            LOGGER.info("删除文件发生错误: {}！", e.getMessage());
        }
        return Boolean.FALSE;
    }

    @Override
    public void minioDownload(String folderName, String name, HttpServletResponse response) {
        try {
            // 设置直接下载
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            //创建一个MinIO的Java客户端
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioEndpoint)
                    .credentials(minioAccessKey, minioSecretKey)
                    .build();
            ObjectStat objectStat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(minioBucketName)
                    .object(folderName + "/" + name)
                    .build());
            // 设置内容类型
            response.setContentType(objectStat.contentType());
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioBucketName)
                    .object(folderName + "/" + name)
                    .build());
            // 将输入流复制到输出流
            IoUtil.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            LOGGER.info("下载文件发生错误: {}！", e.getMessage());
        }
    }

    private String renameFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileExtensionName = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用hutool工具生成唯一id重命名文件
        String fileName = IdUtil.fastSimpleUUID() + fileExtensionName;
        return fileName;
    }
}
