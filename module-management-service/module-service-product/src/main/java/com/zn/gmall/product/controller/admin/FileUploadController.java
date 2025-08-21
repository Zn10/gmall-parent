package com.zn.gmall.product.controller.admin;

import com.zn.gmall.common.config.minio.MinioProperties;
import com.zn.gmall.common.result.Result;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


/**
 * SPU图片上传管理控制
 */
@Api(tags = "图片上传管理控制")
@Slf4j
@RestController
@RequestMapping("admin/product")
@SuppressWarnings("all")
public class FileUploadController {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @ApiOperation("文件上传控制器")
    @RequestMapping("fileUpload")
    public Result<String> fileUpload(@RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return Result.<String>fail().message("请不要上传空的文件！");
        }

        String url;
        String bucketName = minioProperties.getBucketName();

        // 检查并创建存储桶
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
            log.info("成功创建存储桶: {}", bucketName);
        }

        // 生成唯一文件名
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString();

        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

        // 构建访问URL
        url = minioProperties.getEndpointUrl() + "/" + bucketName + "/" + fileName;
        log.info("文件访问地址: {}", url);
        return Result.ok(url);
    }
}