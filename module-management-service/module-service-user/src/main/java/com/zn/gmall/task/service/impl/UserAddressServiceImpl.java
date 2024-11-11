package com.zn.gmall.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zn.gmall.model.user.UserAddress;
import com.zn.gmall.task.mapper.UserAddressMapper;
import com.zn.gmall.task.service.api.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
@Slf4j
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> findUserAddressListByUserId(String userId) {
        // 操作哪个数据库表，则就使用哪个表对应的mapper！
        // new Example() ; 你操作的哪个表，则对应的传入表的实体类！
        // select * from userAddress where userId = ？;
        QueryWrapper<UserAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<UserAddress> userAddressList = userAddressMapper.selectList(queryWrapper);
        return userAddressList;
    }


}
