package com.zn.gmall.user.controller.api;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.user.UserAddress;
import com.zn.gmall.user.service.api.UserAddressService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
@SuppressWarnings("all")
public class UserApiController {

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 获取用户地址
     *
     * @param userId
     * @return
     */
    @ApiOperation("根据用户id，获取用户地址")
    @GetMapping("inner/findUserAddressListByUserId/{userId}")
    public Result<List<UserAddress>> findUserAddressListByUserId(@PathVariable("userId") String userId) {
        log.info("获取用户地址，用户id：{}", userId);
        if (userId == null) {
            return Result.<List<UserAddress>>fail().message("用户id不能为空");
        }
        List<UserAddress> userAddressList = userAddressService.findUserAddressListByUserId(userId);
        return Result.ok(userAddressList);
    }

}
