package com.zn.gmall.activity.service.api;

import com.zn.gmall.model.activity.SeckillGoods;

import java.util.List;

/**
 * Package: com.zn.gmall.activity.service.api
 * Description:
 * Created on 2024-11-10 02:20
 *
 * @author zhaonian
 */
public interface SeckillGoodsService {
    /**
     * 返回全部列表
     * @return
     */
    List<SeckillGoods> findAll();

    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    SeckillGoods getSeckillGoods(Long id);

}
