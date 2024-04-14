package com.zn.gmall.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.common.util.IpUtil;
import com.zn.gmall.model.user.UserInfo;
import com.zn.gmall.user.service.api.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/14:07
 */
@Api(tags = "认证中心")
@RestController
@RequestMapping("/api/user/passport")
public class PassportApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/logout")
    public Result<Void> logout(@RequestHeader("token") String token) {

        // 用户是否登录，以 Redis 中保存的 token 为准
        // 所以执行退出登录，就删除 Redis 的 token
        String tokenKey = RedisConst.USER_LOGIN_KEY_PREFIX + token;
        redisTemplate.delete(tokenKey);

        return Result.ok();
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(
            @RequestBody UserInfo userInfo, HttpServletRequest request) {

        // 1、调用 Service 方法验证用户名密码
        UserInfo userInfoLogin = userService.login(userInfo);

        // 2、如果用户名密码正确
        if (userInfoLogin != null) {
            // 3、生成 token 值
            String token =
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .toUpperCase();

            // 4、获取用户昵称数据
            String nickName = userInfoLogin.getNickName();

            // 5、把 token 和用户昵称数据封装到 Map 中
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("token", token);
            resultMap.put("nickName", nickName);

            // 6、把 token 值存入 Redis
            // [1]设定 token 信息存储使用的 key
            String userKey = RedisConst.USER_LOGIN_KEY_PREFIX + token;

            // [2]设定 token 信息存储使用的 value
            // value 中包含 userId 和用户IP地址
            JSONObject userJson = new JSONObject();
            userJson.put("userId", userInfoLogin.getId());
            userJson.put("ip", IpUtil.getIpAddress(request));
            String userValue = userJson.toJSONString();

            // [3]存入 Redis
            redisTemplate
                    .opsForValue()
                    .set(userKey, userValue, RedisConst.USERKEY_TIMEOUT, TimeUnit.SECONDS);

            return Result.ok(resultMap);
        } else {
            // 7、如果用户名密码错误则返回失败信息
            return Result.<Map<String, String>>fail().message("用户名或密码不正确");
        }
    }

}