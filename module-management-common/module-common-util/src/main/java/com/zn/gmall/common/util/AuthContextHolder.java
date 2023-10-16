package com.zn.gmall.common.util;


import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 获取登录用户信息类
 * </p>
 */
public class AuthContextHolder {

    /**
     * 获取当前登录用户id
     *
     * @param request 请求
     */
    public static String getUserId(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        return StringUtils.isEmpty(userId) ? "" : userId;
    }

    /**
     * 获取当前未登录临时用户id
     *
     * @param request 请求
     */
    public static String getUserTempId(HttpServletRequest request) {
        String userTempId = request.getHeader("userTempId");
        return StringUtils.isEmpty(userTempId) ? "" : userTempId;
    }
}
