package com.zn.gmall.item.client;

import com.zn.gmall.common.result.Result;
import org.springframework.stereotype.Component;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/14:23
 */
@Component
public class ItemDegradeFeignClient implements ItemFeignClient {


    @Override
    public Result getItem(Long skuId) {
        return Result.fail();
    }
}

