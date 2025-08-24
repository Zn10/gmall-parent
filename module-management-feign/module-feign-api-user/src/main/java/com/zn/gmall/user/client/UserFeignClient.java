package com.zn.gmall.user.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(contextId = "UserFeignClient", value = "service-user", fallback = UserDegradeFeignClient.class)
public interface UserFeignClient {

    @RequestMapping("/api/user/inner/findUserAddressListByUserId/{userId}")
    Result<List<UserAddress>> findUserAddressListByUserId(@PathVariable(value = "userId") String userId);
}

