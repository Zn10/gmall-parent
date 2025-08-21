package com.zn.gmall.common.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    @Bean
    public MinioClient minioClient(@Qualifier("minio-com.zn.gmall.common.config.minio.MinioProperties") MinioProperties properties) throws Exception {
        return MinioClient.builder()
                .endpoint(properties.getEndpointUrl())
                .credentials(properties.getAccessKey(), properties.getSecreKey())
                .build();
    }
}
