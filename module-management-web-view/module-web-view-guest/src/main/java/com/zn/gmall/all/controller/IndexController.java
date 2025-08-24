package com.zn.gmall.all.controller;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.product.client.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Package: com.zn.gmall.all.controller
 * Description:
 * Created on 2024-11-08 19:52
 *
 * @author zhaonian
 */
@Controller
@Slf4j
public class IndexController {
    @Resource
    private ProductFeignClient productFeignClient;

    @RequestMapping("/")
    public String index(HttpServletRequest request){
        // 获取首页分类数据
        Result<List<JSONObject>> result = productFeignClient.getBaseCategoryList();
        log.info("首页分类数据:{}",result.getData());
        request.setAttribute("list",result.getData());
        return "index/index";
    }
}
