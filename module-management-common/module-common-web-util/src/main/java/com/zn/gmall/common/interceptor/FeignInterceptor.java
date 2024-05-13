package com.zn.gmall.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: gmall-parent
 * @description: 拦截器
 * @author: Mr.Zhao
 * @create: 2024-05-13 20:45
 **/
@Component
public class FeignInterceptor implements RequestInterceptor {

    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        requestTemplate.header("userTempId", request.getHeader("userTempId"));
        requestTemplate.header("userId", request.getHeader("userId"));
    }
}

