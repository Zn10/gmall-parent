package com.zn.gmall.all.controller;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.item.client.ItemFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class ItemController {

    @Autowired
    ItemFeignClient itemFeignClient;

    /**
     * sku详情页面
     *
     * @param skuId
     * @param model
     * @return
     */
    @RequestMapping("/{skuId}.html")
    public String getItem(@PathVariable Long skuId, Model model) {
        // 通过skuId 查询skuInfo
        Result<Map<String, Object>> result = itemFeignClient.getItem(skuId);

        // 整个 Map 数据整体全部存入 Model，Map 的 Key 就是请求域中数据的属性名
        model.addAllAttributes(result.getData());
        return "item/index";
    }

}
