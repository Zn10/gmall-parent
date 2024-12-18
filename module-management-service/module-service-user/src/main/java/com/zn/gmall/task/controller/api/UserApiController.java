package com.zn.gmall.task.controller.api;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.model.user.UserAddress;
import com.zn.gmall.task.service.api.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
@SuppressWarnings("all")
public class UserApiController {

    @Resource
    private UserAddressService userAddressService;

    /**
     * 获取用户地址
     * @param userId
     * @return
     */
    @GetMapping("inner/findUserAddressListByUserId/{userId}")
    public Result<List<UserAddress>> findUserAddressListByUserId(@PathVariable("userId") String userId){
        log.info("获取用户地址，用户id：{}",userId);
        if (userId == null){
            return Result.<List<UserAddress>>fail().message("用户id不能为空");
        }
        List<UserAddress> userAddressList = userAddressService.findUserAddressListByUserId(userId);
        return Result.ok(userAddressList);
    }

}
