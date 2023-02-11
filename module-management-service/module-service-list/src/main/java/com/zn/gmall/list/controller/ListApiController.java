package com.zn.gmall.list.controller;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.list.service.api.SearchService;
import com.zn.gmall.model.list.SearchParam;
import com.zn.gmall.model.list.SearchResponseVo;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:46
 */
@RestController
@RequestMapping("/api/list")
public class ListApiController {

    @Resource
    private SearchService searchService;

    /**
     * 搜索商品
     *
     * @param searchParam
     * @throws IOException
     */
    @PostMapping("/do/search")
    public Result list(@RequestBody SearchParam searchParam) throws Throwable {
        SearchResponseVo response = searchService.search(searchParam);
        return Result.ok(response);
    }

    @GetMapping("/inner/incr/goods/hot/score/{skuId}")
    public Result<Void> incrGoodsHotScore(@PathVariable("skuId") Long skuId) {

        searchService.incrHotScore(skuId);

        return Result.ok();
    }

    @GetMapping("/remove/goods/from/elastic/search/{skuId}")
    public Result<Void> removeGoodsFromElasticSearch(@PathVariable("skuId") Long skuId) {
        searchService.removeGoodsFromElasticSearch(skuId);
        return Result.ok();
    }

    @GetMapping("/inner/import/sku/to/elastic/search/{skuId}")
    public Result<Void> importSkuToElasticSearch(@PathVariable("skuId") Long skuId) {
        searchService.importGoodsToElasticSearch(skuId);
        return Result.ok();
    }

}
