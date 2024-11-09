package com.zn.gmall.task.client;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.user.UserAddress;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDegradeFeignClient implements UserFeignClient {


    @Override
    public Result<List<UserAddress>> findUserAddressListByUserId(String userId) {
        Result<List<UserAddress>> result = Result.fail();
        result.message("服务降级");
        return result;
    }
}

