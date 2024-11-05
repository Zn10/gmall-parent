package com.zn.gmall.item.client;

import com.zn.gmall.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/14:23
 */
@Component
public class ItemDegradeFeignClient implements ItemFeignClient {


    @Override
    public Result<Map<String, Object>> getItem(Long skuId) {
        Result<Map<String, Object>> result = Result.fail();
        result.message("服务降级");
        return result;
    }
}

