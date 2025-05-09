package com.zn.gmall.user.service.api;

import com.zn.gmall.model.user.UserAddress;

import java.util.List;

@SuppressWarnings("all")
public interface UserAddressService {
    /**
     * 根据用户Id 查询用户的收货地址列表！
     * @param userId
     * @return
     */
    List<UserAddress> findUserAddressListByUserId(String userId);

}
