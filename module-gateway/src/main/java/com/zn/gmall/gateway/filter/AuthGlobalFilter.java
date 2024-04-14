package com.zn.gmall.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.common.result.ResultCodeEnum;
import com.zn.gmall.common.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: 赵念
 * @create-date: 2023/2/11/14:14
 */
@Component
public class AuthGlobalFilter implements GlobalFilter {

    @Resource
    private RedisTemplate redisTemplate;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${authUrls.url}")
    private String authUrls;

    /**
     * 声明常量，专门用于表示用户不存在的情况
     */
    public static final String USER_ID_NOT_EXISTS = "userIdNotExists";

    public static final String TOKEN_HAS_BEEN_STOLEN = "tokenHasBeanStolen";

    public static final String USER_TEMP_ID_NOT_EXISTS = "userTempIdNotExists";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 一、准备工作
        // 通过 exchange 对象获取请求对象
        ServerHttpRequest request = exchange.getRequest();

        // 获取用户的请求地址
        URI uri = request.getURI();
        String path = uri.getPath();

        // 二、保护 inner 地址的内部服务
        // 1、声明 pattern 模式，用来匹配 inner 类型的地址
        String innerPattern = "/**/inner/**";

        // 2、使用指定的模式来和用户请求地址进行匹配
        if (antPathMatcher.match(innerPattern, path)) {

            // 3、如果能够匹配上，则返回禁止用户访问的响应
            ServerHttpResponse response = exchange.getResponse();

            Mono<Void> mono = out(response, ResultCodeEnum.PERMISSION);

            return mono;
        }

        // 三、登录认证
        // 1、token 盗用的情况返回响应
        String userId = getUserId(request);

        if (TOKEN_HAS_BEEN_STOLEN.equals(userId)) {
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.PERMISSION);
        }

        // 2、给异步请求返回未登录提示
        // [1]声明需要登录才可以访问的异步请求地址模式
        String authPattern = "/**/auth/**";

        // [2]检测当前请求路径是否匹配上述模式
        if (antPathMatcher.match(authPattern, path)) {
            // [3]处理 userId 不存在的情况
            if (USER_ID_NOT_EXISTS.equals(userId)) {
                return out(exchange.getResponse(), ResultCodeEnum.LOGIN_AUTH);
            }
        }

        // 3、给同步请求返回未登录提示
        // [1]针对 authUrls 进行拆分
        String[] split = authUrls.split(",");
        for (String authUrl : split) {

            // path.indexOf(authUrl) 返回 -1 说明 path 中没有 authUrl
            // 返回的不是 -1，说明有 authUrl，也就是匹配
            // 也就是说当前地址的请求应该登录后再访问
            if (path.contains(authUrl) && USER_ID_NOT_EXISTS.equals(userId)) {

                // 由于是同步请求，所以需要重定向到登录页面
                ServerHttpResponse response = exchange.getResponse();

                // 设置响应状态码为：302
                response.setStatusCode(HttpStatus.SEE_OTHER);

                // 创建 location 字符串作为重定向的目标地址（携带来源地址，便于登录后前往）
                String location = "http://passport.gmall.com/login.html?originUrl=" + request.getURI();

                // 设置响应消息头
                response.getHeaders().set(HttpHeaders.LOCATION, location);

                // 返回响应
                return response.setComplete();
            }
        }

        // 四、将 userId 和 userTempId 传递到后续微服务
        // 1、尝试读取 userTempId
        String userTempId = getUserTempId(request);

        // 2、声明变量，默认值是默认的请求对象（没有设置请求消息头）
        ServerHttpRequest requestObjHasBeanSetHeader = request;

        // 3、设置 userId 对应的请求消息头
        if (isUserIdExists(userId)) {
            requestObjHasBeanSetHeader =
                    request.mutate().header("userId", userId).build();
        }

        // 4、设置 userTempId 对应的请求消息头
        if (isUserTempIdExists(userTempId)) {
            requestObjHasBeanSetHeader =
                    request.mutate().header("userTempId", userTempId).build();
        }

        // 5、通过放行请求，把已经设置了请求消息头的请求对象传给后续微服务
        return chain
                .filter(
                        exchange
                                .mutate()
                                .request(requestObjHasBeanSetHeader)
                                .build()
                );
    }

    private boolean isUserTempIdExists(String userTempId) {
        return !StringUtils.isEmpty(userTempId) &&
                !USER_TEMP_ID_NOT_EXISTS.equals(userTempId);
    }

    private boolean isUserIdExists(String userId) {
        return !StringUtils.isEmpty(userId) &&
                !USER_ID_NOT_EXISTS.equals(userId) &&
                !TOKEN_HAS_BEEN_STOLEN.equals(userId);
    }

    /**
     * 根据响应对象和自定义的响应码生成响应结果
     *
     * @param response 响应对象
     * @param resultCodeEnum 自定义响应码
     * @return
     */
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        // 返回用户没有权限登录的状态码
        Result<Object> result = Result.build(null, resultCodeEnum);

        // 生成响应体的字节数组
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);

        // 生成响应数据缓冲区
        DataBuffer wrap = response.bufferFactory().wrap(bits);

        // 设置响应消息头
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        // 输入到页面
        return response.writeWith(Mono.just(wrap));
    }

    /**
     * 获取当前用户的 id
     * 流程：
     * 1、从请求对象中根据 header 查找 token
     * 2、如果没有从 header 中找到 token，再根据 Cookie 查找 token
     * 3、如果上面两个地方都没有找到，说明用户没有登录
     * 4、如果从上面任何一个地方读取到了 token，那么就根据 token 访问 Redis
     * 5、从 Redis 中读取用户 id
     *
     * @param request 请求参数
     * @return 返回token
     */
    private String getUserId(ServerHttpRequest request) {

        // 声明一个变量用于接收 token 值
        String token = null;

        // 尝试从 header 中读取 token
        List<String> tokenList = request.getHeaders().get("token");

        // 对 tokenList 做判空操作
        if (!CollectionUtils.isEmpty(tokenList)) {

            // token 这个响应消息头肯定只有一个值，所以取下标 0 即可
            token = tokenList.get(0);
        } else {

            // 尝试到 Cookie 中获取 token
            // MultiValueMap 类型允许一个 key 对应一个 List 集合（多个值）
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();

            // 又因为我们的 Cookie 中，一个 key 肯定只对应一个值，所以调用 getFirst() 方法，
            // 从 value 的 List 集合中取第一个值（唯一值）即可
            HttpCookie tokenCookie = cookies.getFirst("token");

            // 对 tokenCookie 进行判空操作
            if (tokenCookie != null) {
                // 从 tokenCookie 中取值
                token = tokenCookie.getValue();
            }

        }

        // 判断 token 是否被赋值了
        if (!StringUtils.isEmpty(token)) {
            // 拼接 token 在 Redis 中存储时使用的 key
            String userKey = "user:login:" + token;

            // 根据这个 key 读取 Redis 缓存数据
            String userDataJSON
                    = (String) redisTemplate.opsForValue().get(userKey);

            if (!StringUtils.isEmpty(userDataJSON)) {
                // 解析 JSON 字符串
                JSONObject userJSONObj = JSON.parseObject(userDataJSON);
                String userId = userJSONObj.getString("userId");
                String redisIp = userJSONObj.getString("ip");

                // 获取用户请求中解析得到的 IP，进行 token 防盗用的检查
                String requestIp = IpUtil.getGatwayIpAddress(request);

                if (requestIp != null && requestIp.equals(redisIp)) {
                    // 二者相等，说明 Redis 中存储的 IP 和用户请求中提供的一致
                    // IP 地址验证通过，说明 userId 可信，直接返回即可
                    return userId;
                } else {
                    // 二者不相等，说明发生了 token 盗用的情况
                    return TOKEN_HAS_BEEN_STOLEN;
                }
            }
        }

        // 返回特定值表示 id 不存在
        return USER_ID_NOT_EXISTS;
    }

    /**
     * 读取用户的临时 id。
     *
     * @param request 请求参数
     * @return userTempId
     */
    private String getUserTempId(ServerHttpRequest request) {

        // 1、声明变量，存放 userTempId
        String userTempId = USER_TEMP_ID_NOT_EXISTS;

        // 2、读取 header 部分
        List<String> headerList = request.getHeaders().get("userTempId");

        if (!CollectionUtils.isEmpty(headerList)) {
            userTempId = headerList.get(0);
        } else {

            // 3、读取 Cookie 部分
            HttpCookie cookie = request.getCookies().getFirst("userTempId");

            // 4、从 Cookie 中获取数据
            if (cookie != null) {
                userTempId = cookie.getValue();
            }

        }

        // 5、返回 userTempId，如果从请求中成功获取到则默认值被覆盖，否则返回默认值
        return userTempId;
    }
}
