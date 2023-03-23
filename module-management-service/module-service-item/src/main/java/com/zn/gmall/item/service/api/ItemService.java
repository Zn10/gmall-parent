package com.zn.gmall.item.service.api;

import java.util.Map;

public interface ItemService {
    /**
     * 获取sku详情信息
     *
     * @param skuId 商品SKUID
     */
    Map<String, Object> getBySkuId(Long skuId);

}
