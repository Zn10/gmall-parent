package com.zn.gmall.product.controller.admin;

import com.zn.gmall.common.result.Result;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * SPU图片上传管理控制
 */
@Api(tags ="图片上传管理控制")
@Slf4j
@RestController
@RequestMapping("admin/product")
@SuppressWarnings("all")
public class FileUploadController {

    //  获取文件上传对应的地址
    @Value("${minio.endpointUrl}")
    public String endpointUrl;

    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secreKey}")
    public String secreKey;

    @Value("${minio.bucketName}")
    public String bucketName;

    /**
     * 文件上传控制器
     *
     * @param file 文件信息
     * @return
     * @throws Exception
     */
    @ApiOperation("文件上传控制器")
    @RequestMapping("fileUpload")
    public Result<String> fileUpload(@RequestPart("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return Result.<String>fail().message("请不要上传空的文件！");
        }

        //  准备获取到上传的文件路径！
        String url;

        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        // MinioClient minioClient = new MinioClient("https://play.min.io", "Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(endpointUrl)
                        .credentials(accessKey, secreKey)
                        .build();
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (isExist) {
            log.error("文件桶已存在，桶名为:{}", bucketName);
        } else {
            // 创建一个名为gmall的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
        //  定义一个文件的名称 : 文件上传的时候，名称不能重复！
        String fileName = System.currentTimeMillis() + UUID.randomUUID().toString();
        // 使用putObject上传一个文件到存储桶中。
        //  minioClient.putObject("gmall","gmall.zip", "/home/user/Photos/gmall.zip");
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        url = endpointUrl + "/" + bucketName + "/" + fileName;

        log.info("图片地址url:\t{}", url);
        //  将文件上传之后的路径返回给页面！
        return Result.ok(url);
    }
}