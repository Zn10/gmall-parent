package com.zn.gmall.list.controller.api;

import com.zn.gmall.common.result.Result;
import com.zn.gmall.list.service.api.SearchService;
import com.zn.gmall.model.list.SearchParam;
import com.zn.gmall.model.list.vo.SearchResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:46
 */
@RestController
@RequestMapping("/api/list")
@Slf4j
@SuppressWarnings("all")
public class ListApiController {

    @Autowired
    private SearchService searchService;

    /**
     * 上架商品
     *
     * @param skuId
     * @return
     */
    @GetMapping("inner/upperGoods/{skuId}")
    public Result<Void> upperGoods(@PathVariable("skuId") Long skuId) {
        log.info("商品上架:{}", skuId);
        if (skuId == null) {
            return Result.<Void>fail().message("skuid为空");
        }
        searchService.upperGoods(skuId);
        return Result.ok();
    }

    /**
     * 下架商品
     *
     * @param skuId
     * @return
     */
    @GetMapping("inner/lowerGoods/{skuId}")
    public Result<Void> lowerGoods(@PathVariable("skuId") Long skuId) {
        log.info("商品下架:{}", skuId);
        if (skuId == null) {
            return Result.<Void>fail().message("skuid为空");
        }
        searchService.lowerGoods(skuId);
        return Result.ok();
    }


    /**
     * 搜索商品
     *
     * @param searchParam
     * @throws IOException
     */
    @PostMapping("/do/search")
    public Result<SearchResponseVo> list(@RequestBody SearchParam searchParam) throws Throwable {
        log.info("搜索参数:{}", searchParam);
        SearchResponseVo response = searchService.search(searchParam);
        return Result.ok(response);
    }

    @GetMapping("/inner/incr/goods/hot/score/{skuId}")
    public Result<Void> incrGoodsHotScore(@PathVariable("skuId") Long skuId) {
        log.info("商品热度评分增加:{}", skuId);
        if (skuId == null) {
            return Result.<Void>fail().message("skuid为空");
        }
        searchService.incrHotScore(skuId);
        return Result.ok();
    }

    @GetMapping("/remove/goods/from/elastic/search/{skuId}")
    public Result<Void> removeGoodsFromElasticSearch(@PathVariable("skuId") Long skuId) {
        log.info("商品从es中删除:{}", skuId);
        if (skuId == null) {
            return Result.<Void>fail().message("skuid为空");
        }
        searchService.removeGoodsFromElasticSearch(skuId);
        return Result.ok();
    }

    @GetMapping("/inner/import/sku/to/elastic/search/{skuId}")
    public Result<Void> importSkuToElasticSearch(@PathVariable("skuId") Long skuId) {
        log.info("商品导入es:{}", skuId);
        if (skuId == null) {
            return Result.<Void>fail().message("skuid为空");
        }
        searchService.importGoodsToElasticSearch(skuId);
        return Result.ok();
    }

}
