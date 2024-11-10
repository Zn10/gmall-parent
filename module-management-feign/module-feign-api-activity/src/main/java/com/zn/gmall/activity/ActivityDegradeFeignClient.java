package com.zn.gmall.activity;

import com.zn.gmall.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Package: com.zn.gmall.activity
 * Description:
 * Created on 2024-11-10 02:22
 *
 * @author zhaonian
 */
@Component
public class ActivityDegradeFeignClient implements ActivityFeignClient {


    @Override
    public Result<Map<String, Object>> trade() {
        return Result.fail();
    }

    @Override
    public Result findAll() {
        return Result.fail();
    }

    @Override
    public Result getSeckillGoods(Long skuId) {
        return Result.fail();
    }
}

