package com.zn.gmall.common.config.minio;

import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MinioClient.class)
@ConditionalOnProperty(prefix = "minio", name = "enable", havingValue = "true", matchIfMissing = false)
public class MinioAutoConfiguration {
    private final MinioProperties minioProperties;

    public MinioAutoConfiguration(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public MinioClient minioClient(MinioProperties properties) throws Exception {
        return MinioClient.builder()
            .endpoint(properties.getEndpointUrl())
            .credentials(properties.getAccessKey(), properties.getSecreKey())
            .build();
    }
}
