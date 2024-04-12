package com.zn.gmall.list.service.api;

import com.zn.gmall.model.list.SearchParam;
import com.zn.gmall.model.list.vo.SearchResponseVo;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:35
 */
public interface SearchService {

    /**
     * 将商品数据导入 ElasticSearch
     *
     * @param skuId
     */
    void importGoodsToElasticSearch(Long skuId);

    /**
     * 将商品数据从 ElasticSearch 移除
     *
     * @param skuId
     */
    void removeGoodsFromElasticSearch(Long skuId);

    /**
     * 累加商品热度信息
     * 在 Redis 中的操作：每次都 +1
     * 在 ElasticSearch 中的操作：隔 10 次执行一次更新
     *
     * @param skuId
     */
    void incrHotScore(Long skuId);

    /**
     * Service 方法内部调用，为了方便 junit 测试生成接口方法
     *
     * @param searchParam
     * @return SearchRequest
     */
    SearchRequest buildQueryDsl(SearchParam searchParam);

    /**
     * Service 方法内部调用，为了方便 junit 测试生成接口方法
     *
     * @param searchResponse ElasticSearch执行 DSL 语句后返回的响应结果
     *                       因为它返回的是一个复杂的大 JSON，无法直接返回给前端用于显示页面
     *                       所以需要当前这个方法来解析
     * @return SearchResponseVo
     */
    SearchResponseVo parseSearchResult(SearchResponse searchResponse);

    /**
     * 执行搜索逻辑
     *
     * @param searchParam 前端页面封装的搜索参数
     * @return SearchResponseVo 封装的用于页面显示的实体类对象
     */
    SearchResponseVo search(SearchParam searchParam) throws Throwable;
}
