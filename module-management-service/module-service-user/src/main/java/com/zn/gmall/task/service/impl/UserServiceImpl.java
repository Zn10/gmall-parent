package com.zn.gmall.task.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zn.gmall.model.user.UserInfo;
import com.zn.gmall.task.mapper.UserInfoMapper;
import com.zn.gmall.task.service.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/14:10
 */
@Service
@SuppressWarnings("all")
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo login(UserInfo userInfo) {

        // 1、密码加密
        // [1]获取明文密码
        String passwd = userInfo.getPasswd();

        // [2]执行加密操作
        passwd = DigestUtils.md5DigestAsHex(passwd.getBytes());

        // 2、执行查询
        // [1]封装查询条件
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>()
                .eq("login_name", userInfo.getLoginName())
                .eq("passwd", passwd);

        // [2]执行查询并返回结果
        UserInfo info = userInfoMapper.selectOne(queryWrapper);
        if (info != null) {
            return info;
        }
        return null;
    }
}