package com.zn.gmall.task.service.api;

import com.zn.gmall.model.user.UserInfo;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/14:08
 */
public interface UserService {
    /**
     * 执行具体的登录逻辑
     *
     * @param userInfo 仅封装了前端页面传入的用户名、密码
     * @return UserInfo 从数据库查询得到的完整对象
     */
    UserInfo login(UserInfo userInfo);
}
