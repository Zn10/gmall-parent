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
        Result<Map<String, Object>> result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result findAll() {
        Result result = Result.fail();
        result.message("服务降级了");
        return result;
    }

    @Override
    public Result getSeckillGoods(Long skuId) {
        Result result = Result.fail();
        result.message("服务降级了");
        return result;
    }
}

