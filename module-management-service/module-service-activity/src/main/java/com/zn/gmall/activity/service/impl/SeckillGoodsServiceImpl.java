package com.zn.gmall.activity.service.impl;

import com.zn.gmall.activity.service.api.SeckillGoodsService;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.model.activity.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Package: com.zn.gmall.activity.service.impl
 * Description:
 * Created on 2024-11-10 02:20
 *
 * @author zhaonian
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 查询全部
     */
    @Override
    public List<SeckillGoods> findAll() {
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).values();
        return seckillGoodsList;
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public SeckillGoods getSeckillGoods(Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).get(id.toString());
    }
}

