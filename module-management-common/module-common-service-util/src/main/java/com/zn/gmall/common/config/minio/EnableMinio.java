package com.zn.gmall.common.config.minio;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MinioProperties.class})
public @interface EnableMinio {
}

