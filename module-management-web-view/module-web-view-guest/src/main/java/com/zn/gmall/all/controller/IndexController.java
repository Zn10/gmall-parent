package com.zn.gmall.all.controller;

import com.alibaba.fastjson.JSONObject;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.product.client.ProductFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
public class IndexController {
    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping({"/","index.html"})
    public String index(HttpServletRequest request){
        // 获取首页分类数据
        Result<List<JSONObject>> result = productFeignClient.getBaseCategoryList();
        request.setAttribute("list",result.getData());
        return "index/index";
    }
}
