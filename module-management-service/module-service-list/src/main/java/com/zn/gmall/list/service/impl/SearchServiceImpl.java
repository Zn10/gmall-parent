package com.zn.gmall.list.service.impl;

import com.alibaba.fastjson.JSON;
import com.zn.gmall.common.result.Result;
import com.zn.gmall.list.repository.GoodsRepository;
import com.zn.gmall.list.service.api.SearchService;
import com.zn.gmall.model.list.Goods;
import com.zn.gmall.model.list.SearchAttr;
import com.zn.gmall.model.list.SearchParam;
import com.zn.gmall.model.list.vo.SearchResponseAttrVo;
import com.zn.gmall.model.list.vo.SearchResponseTmVo;
import com.zn.gmall.model.list.vo.SearchResponseVo;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.client.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:35
 */
@SuppressWarnings("all")
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestHighLevelClient highLevelClient;

    /**
     * 上架商品列表
     *
     * @param skuId 商品skuid
     */
    @Override
    public void upperGoods(Long skuId) {
        Goods goods = new Goods();
        //查询sku对应的平台属性
        Result<List<BaseAttrInfo>> baseAttrInfoResult = productFeignClient.getAttrList(skuId);
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoResult.getData();
        if (baseAttrInfoList != null) {
            List<SearchAttr> searchAttrList = baseAttrInfoList.stream().map(baseAttrInfo -> {
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrId(baseAttrInfo.getId());
                searchAttr.setAttrName(baseAttrInfo.getAttrName());
                //一个sku只对应一个属性值
                List<BaseAttrValue> baseAttrValueList = baseAttrInfo.getAttrValueList();
                searchAttr.setAttrValue(baseAttrValueList.get(0).getValueName());
                return searchAttr;
            }).collect(Collectors.toList());
            goods.setAttrs(searchAttrList);
        }
        //查询sku信息
        Result<SkuInfo> skuInfoResult = productFeignClient.getSkuInfo(skuId);
        SkuInfo skuInfo = skuInfoResult.getData();
        // 查询品牌
        Result<BaseTrademark> baseTrademarkResult = productFeignClient.getTrademarkById(skuInfo.getTmId());
            BaseTrademark baseTrademark = baseTrademarkResult.getData();
            if (baseTrademark != null) {
                goods.setTmId(skuInfo.getTmId());
                goods.setTmName(baseTrademark.getTmName());
                goods.setTmLogoUrl(baseTrademark.getLogoUrl());
            }
        // 查询分类
        Result<BaseCategoryView> baseCategoryViewResult = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
        BaseCategoryView baseCategoryView = baseCategoryViewResult.getData();
        if (baseCategoryView != null) {
            goods.setCategory1Id(baseCategoryView.getCategory1Id());
            goods.setCategory1Name(baseCategoryView.getCategory1Name());
            goods.setCategory2Id(baseCategoryView.getCategory2Id());
            goods.setCategory2Name(baseCategoryView.getCategory2Name());
            goods.setCategory3Id(baseCategoryView.getCategory3Id());
            goods.setCategory3Name(baseCategoryView.getCategory3Name());
        }

        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setId(skuInfo.getId());
        goods.setTitle(skuInfo.getSkuName());
        goods.setCreateTime(new Date());

        this.goodsRepository.save(goods);
    }

    /**
     * 下架商品列表
     *
     * @param skuId 商品skuid
     */
    @Override
    public void lowerGoods(Long skuId) {
        this.goodsRepository.deleteById(skuId);
    }

    @Override
    public void importGoodsToElasticSearch(Long skuId) {
        // 1、查询 SKU 信息
        Result<SkuInfo> skuInfoResult = productFeignClient.getSkuInfo(skuId);
        SkuInfo skuInfo = skuInfoResult.getData();

        // 2、查询 SKU 相关的分类信息
        Long category3Id = skuInfo.getCategory3Id();
        Result<BaseCategoryView> categoryViewResult = productFeignClient.getCategoryView(category3Id);
        BaseCategoryView categoryView = categoryViewResult.getData();

        // 3、查询 SKU 相关的品牌信息
        Long tmId = skuInfo.getTmId();
        Result<BaseTrademark> trademarkResult = productFeignClient.getTrademarkById(tmId);
        BaseTrademark trademark = trademarkResult.getData();

        // 4、查询 SKU 相关的平台属性信息
        Result<List<BaseAttrInfo>> baseAttrInfoResult = productFeignClient.getBaseAttrInfoBySkuId(skuId);
        List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoResult.getData();

        // 5、组装 Goods 对象
        Goods goods = new Goods();

        // [1]填充 SKU 信息
        goods.setId(skuId);
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());

        // [2]填充分类信息
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());

        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());

        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());

        // [3]填充品牌信息
        goods.setTmId(tmId);
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());

        // [4]填充平台属性信息
        List<SearchAttr> attrs = new ArrayList<>();
        for (BaseAttrInfo baseAttrInfo : baseAttrInfoList) {

            // 循环体每执行一次，就创建一个 searchAttr 对象
            SearchAttr searchAttr = new SearchAttr();

            // 填充 searchAttr 对象
            searchAttr.setAttrId(baseAttrInfo.getId());
            searchAttr.setAttrName(baseAttrInfo.getAttrName());

            // ※获取当前 SKU 对应的平台属性值
            // 因为一个 SKU 对应的平台属性值一定是一个单个值
            // 所以从平台属性值集合取下标 0 即可
            String valueName = baseAttrInfo.getAttrValueList().get(0).getValueName();
            searchAttr.setAttrValue(valueName);

            // searchAttr 对象填充完成存入 attrs 集合
            attrs.add(searchAttr);
        }

        // 填充 attrs 集合完成，存入 goods 对象
        goods.setAttrs(attrs);

        // 6、把 Goods 对象存入 ElasticSearch
        goodsRepository.save(goods);

    }

    @Override
    public void removeGoodsFromElasticSearch(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public void incrHotScore(Long skuId) {

        // 1、操作 Redis
        // [1]声明在 Redis 中存储热度值数据的 key
        String hotScoreKey = "hot:score";

        // [2]拼接 zset 集合中当前 skuId 对应的成员字符串
        String zSetMember = "skuId:" + skuId;

        // [3]基于上面两个设定，对 Redis 中对应热度值数据进行累加
        ZSetOperations<String, String> operator = redisTemplate.opsForZSet();
        Double plusResult = operator.incrementScore(hotScoreKey, zSetMember, 1);

        // 2、操作 ElasticSearch
        // [1]判断累加的结果是否是 100 的整数倍（节约 ElasticSearch 性能）
        if (plusResult % 100 == 0) {

            // [2]从 ElasticSearch 中查询对应数据
            Optional<Goods> optional = goodsRepository.findById(skuId);

            // [3]获取 Goods 对象
            Goods goods = optional.get();

            // [4]设置最新的热度值
            goods.setHotScore(plusResult.longValue());

            // [5]存回 ElasticSearch
            goodsRepository.save(goods);

        }

    }

    /**
     * 专门用于 DSL 语句的构造
     *
     * @param searchParam 封装前端页面传过来的搜索参数。其中每个参数都有可能为空，所以每个参数使用时都要做判空操作
     * @return
     */
    @Override
    public SearchRequest buildQueryDsl(SearchParam searchParam) {

        // 第一步：创建 DSL 查询器对象（对应 DSL 语句中除第一行之外的整个{}）
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 第二步：封装查询条件（对应 DSL 语句中的 query 部分）
        // 1、创建 BoolQueryBuilder 对象（对应 query 中 bool 部分）
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // 2、封装关键词信息（对应 bool 查询中的 must 部分）
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            // 相当于在 bool 查询中创建 must 结构
            boolQueryBuilder.must(

                    // 相当于在 must 结构创建 match 查询
                    QueryBuilders

                            // 表示在 title 字段查询关键词（用户在搜索框输入的）
                            // Operator.AND 含义：当前关键词是“华为手机”时，搜索结果必须同时包含“华为”和“手机”才匹配
                            // Operator.OR 含义：当前关键词是“华为手机”时，搜索结果只要包含“华为”和“手机”中的任何一个即可匹配
                            .matchQuery("title", searchParam.getKeyword()).operator(Operator.OR));
        }

        // 3、封装过滤信息（对应 bool 查询中的 filter 部分）
        // ●一级分类
        if (!StringUtils.isEmpty(searchParam.getCategory1Id())) {
            // 相当于在 bool 查询中创建 filter 结构
            boolQueryBuilder.filter(
                    // 相当于在 filter 结构创建 term 查询
                    QueryBuilders
                            // 表示在 category1Id 字段查询一级分类（在分类菜单中点击的）
                            .termQuery("category1Id", searchParam.getCategory1Id()));
        }
        // ●二级分类
        if (!StringUtils.isEmpty(searchParam.getCategory2Id())) {
            // 相当于在 bool 查询中创建 filter 结构
            boolQueryBuilder.filter(
                    // 相当于在 filter 结构创建 term 查询
                    QueryBuilders
                            // 表示在 category2Id 字段查询而级分类（在分类菜单中点击的）
                            .termQuery("category2Id", searchParam.getCategory2Id()));
        }
        // ●三级分类
        if (!StringUtils.isEmpty(searchParam.getCategory3Id())) {
            // 相当于在 bool 查询中创建 filter 结构
            boolQueryBuilder.filter(
                    // 相当于在 filter 结构创建 term 查询
                    QueryBuilders
                            // 表示在 category3Id 字段查询而级分类（在分类菜单中点击的）
                            .termQuery("category3Id", searchParam.getCategory3Id()));
        }

        // ●平台属性值
        // 认识 SearchParam 中平台属性的封装格式
        // 一个字符串：[23:4G:运行内存] 格式就是 [平台属性 id:平台属性值:平台属性名]
        // 多个字符串组成数组，对应 SearchParam 中的 props 属性
        // 前端发送请求时这些参数会附加到 URL 地址后面：xxx&props=23:4G:运行内存&props=24:128G:机身存储
        String[] props = searchParam.getProps();

        // 遍历参数数组
        for (int i = 0; props != null && i < props.length; i++) {

            // 从参数数组中取出一个参数字符串
            String attrString = props[i];

            // 解析参数字符串
            if (!StringUtils.isEmpty(attrString)) {
                String[] split = attrString.split(":");

                if (split.length == 3) {

                    // 从参数字符串中解析出平台属性 id
                    String attrId = split[0];

                    // 从参数字符串中解析出平台属性值
                    String attrValue = split[1];

                    // 对应 DSL 中的结构：filter -> nested -> query -> bool
                    BoolQueryBuilder attrBoolQuery = QueryBuilders.boolQuery();

                    // 对应 DSL 中的结构：filter -> nested -> query -> bool -> must
                    attrBoolQuery.must(
                            // 对应 DSL 中的结构：filter -> nested -> query -> bool -> must -> term
                            QueryBuilders.termQuery("attrs.attrId", attrId));

                    attrBoolQuery.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));

                    // 对应 DSL 中的结构：filter -> nested
                    NestedQueryBuilder attrNestedQuery = QueryBuilders.nestedQuery("attrs", attrBoolQuery, ScoreMode.None);

                    // 对应 DSL 中的结构：最外层 query -> bool -> filter
                    boolQueryBuilder.filter(attrNestedQuery);
                }
            }
        }

        // ●品牌信息
        // 在 SearchParam 中，品牌信息格式：品牌 id:品牌名称
        // 2:华为
        String trademark = searchParam.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            String[] split = trademark.split(":");
            if (split.length == 2) {
                String tmId = split[0];
                // 对应 DSL 中的结构：最外层 query -> bool -> filter
                boolQueryBuilder.filter(
                        // 对应 DSL 中的结构：最外层 query -> bool -> filter -> term
                        QueryBuilders.termQuery("tmId", tmId));
            }
        }

        // 把最外层的 query 组装到最外层的大括号
        searchSourceBuilder.query(boolQueryBuilder);

        // 第三步：封装排序方式（对应 DSL 语句中的 sort 部分）
        // 排序规则的格式：[排序字段:排序方向]例如：[order=1:desc&order=2:asc]
        // 解析排序字符串
        String order = searchParam.getOrder();
        if (!StringUtils.isEmpty(order)) {
            String[] split = order.split(":");
            String orderFieldNumber = split[0];
            String orderDirection = split[1];
            String orderFieldName = null;

            switch (orderFieldNumber) {
                case "1":
                    orderFieldName = "hotScore";
                    break;
                case "2":
                    orderFieldName = "price";
                    break;
            }

            assert orderFieldName != null;
            searchSourceBuilder.sort(orderFieldName, "ASC".equalsIgnoreCase(orderDirection) ? SortOrder.ASC : SortOrder.DESC);
        }

        // 第四步：封装分页数据（对应 DSL 语句中的 from、size 部分）
        Integer pageNo = searchParam.getPageNo();
        Integer pageSize = searchParam.getPageSize();
        int from = (pageNo - 1) * pageSize;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(pageSize);

        // 第五步：封装高亮信息（对应 DSL 语句中的 highlight 部分）
        // 1、创建 HighlightBuilder 对象
        HighlightBuilder highlighter = new HighlightBuilder();

        // 2、指定做高亮效果的字段
        highlighter.field("title");

        // 3、指定高亮效果的开始标签
        highlighter.preTags("<span style='color:red'>");

        // 4、指定高亮效果的结束标签
        highlighter.postTags("</span>");

        // 5、组装
        searchSourceBuilder.highlighter(highlighter);

        // 第六步：封装聚合信息（对应 DSL 语句中的 aggs 部分）
        // 1、品牌聚合（普通类型数据）
        TermsAggregationBuilder tmAgg = AggregationBuilders.terms("tmIdAgg").field("tmId").subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName")).subAggregation(AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl"));
        searchSourceBuilder.aggregation(tmAgg);

        // 2、平台属性聚合（nested类型数据）
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs").subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName")).subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue")));
        searchSourceBuilder.aggregation(attrAgg);

        // 第七步：指定查询结果中我们需要哪些字段
        searchSourceBuilder.fetchSource(new String[]{"id", "title", "defaultImg", "price"}, null);

        // ※打印 DSL 语句
        String DSL = searchSourceBuilder.toString();
        System.out.println("DSL = " + DSL);

        // 第八步：封装 SearchRequest 对象
        // [1]创建 SearchRequest 并指定 index
        SearchRequest searchRequest = new SearchRequest("goods");

        // [2]指定 type
        searchRequest.types("info");

        // [3]组装查询器对象
        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    @Override
    public SearchResponseVo parseSearchResult(SearchResponse searchResponse) {

        SearchResponseVo searchResponseVo = new SearchResponseVo();

        // 第一步：封装商品数据
        // 1、从 searchResponse 对象中获取商品数据
        // 解释：商品数据其实就是查询结果部分，在响应 JSON 中对应 hits 部分
        // hits 有内外两层，先获取外层的，再获取内层的
        SearchHits outerHits = searchResponse.getHits();

        // 获取内层 hits，这里就包含了我们要的商品数据
        SearchHit[] searchHitArr = outerHits.getHits();

        for (SearchHit searchHit : searchHitArr) {
            // 2、解析常规数据
            // 获取每一个 SearchHit 对象，每一个 SearchHit 对象都对应一个 Goods 对象
            // 从 SearchHit 对象中获取查询结果数据
            String sourceJSONString = searchHit.getSourceAsString();

            // 从 JSON 字符串解析得到 Goods 对象
            Goods goods = JSON.parseObject(sourceJSONString, Goods.class);

            // 3、解析高亮信息
            // 获取高亮信息数据
            Map<String, HighlightField> highlightMap = searchHit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightMap)) {

                // 声明高亮字段名称
                String fieldName = "title";

                // 根据高亮字段名称获取对应的高亮信息
                HighlightField highlightField = highlightMap.get(fieldName);

                // 进一步获取高亮具体信息
                if (highlightField != null) {
                    Text titleText = highlightField.getFragments()[0];

                    // 转化为字符串类型
                    String highlightTitle = titleText.toString();

                    // 设置到 Goods 对象中
                    goods.setTitle(highlightTitle);
                }

            }

            // 将 Goods 对象存入 SearchResponseVO 对象中的 List<Goods>
            searchResponseVo.getGoodsList().add(goods);
        }

        // 第二步：封装品牌数据（从搜索结果的聚合部分得到）
        // 1、获取搜索结果中整个的聚合部分
        Aggregations aggregations = searchResponse.getAggregations();

        // 2、将总体聚合结果转换为 Map 类型
        Map<String, Aggregation> aggregationMap = aggregations.asMap();

        // 3、从总聚合 Map 中获取品牌聚合信息
        // Aggregation 类型不能调用获取桶的方法，需要转换类型
        // ParsedXxxTerms 类型中间 Xxx 部分和聚合字段类型一致
        // tmIdAgg 是根据 tmId 聚合的，tmId 是 Long 类型，所以使用 ParsedLongTerms
        ParsedLongTerms tmIdAgg = (ParsedLongTerms) aggregationMap.get("tmIdAgg");

        List<? extends Terms.Bucket> tmIdAggBuckets = tmIdAgg.getBuckets();

        // 4、遍历 tmIdAggBuckets
        for (Terms.Bucket tmIdAggBucket : tmIdAggBuckets) {

            // 5、通过每一个 bucket 的 Key 的值就能获取到聚合字段的值，也就是 tmId
            String tmIdString = tmIdAggBucket.getKeyAsString();

            // 6、将 tmId 值从字符串转换为 Long 类型
            Long tmId = Long.parseLong(tmIdString);

            // 7、将 tmId 值保存到 SearchResponseTmVo 对象中
            SearchResponseTmVo searchResponseTmVo = new SearchResponseTmVo();
            searchResponseTmVo.setTmId(tmId);

            // 8、获取 tmName 值
            ParsedStringTerms tmNameAgg = (ParsedStringTerms) tmIdAggBucket.getAggregations().getAsMap().get("tmNameAgg");

            // 由于在 tmId 分组的基础上，tmName 肯定是唯一的，所以这里取下标 0
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            searchResponseTmVo.setTmName(tmName);

            // 9、获取 tmLogoUrl 值
            ParsedStringTerms tmLogoUrlAgg = (ParsedStringTerms) tmIdAggBucket.getAggregations().getAsMap().get("tmLogoUrlAgg");

            String tmLogoUrl = tmLogoUrlAgg.getBuckets().get(0).getKeyAsString();
            searchResponseTmVo.setTmLogoUrl(tmLogoUrl);

            // 10、存入 searchResponseVo 对象中的集合
            searchResponseVo.getTrademarkList().add(searchResponseTmVo);
        }

        // 第三步：封装平台属性数据
        // 1、先获取 nested 类型的聚合
        ParsedNested nestedAttrAgg = (ParsedNested) aggregationMap.get("attrAgg");

        // 2、再从 nested 类型的聚合中获取子聚合
        Map<String, Aggregation> attrAggMap = nestedAttrAgg.getAggregations().getAsMap();

        // 3、从子聚合的 Map 中获取平台属性 id 的聚合
        ParsedLongTerms attrIdAgg = (ParsedLongTerms) attrAggMap.get("attrIdAgg");

        // 4、从平台属性 id 聚合中获取桶的集合
        List<? extends Terms.Bucket> buckets = attrIdAgg.getBuckets();

        // 5、遍历平台属性 id 聚合的桶集合
        for (Terms.Bucket bucket : buckets) {

            // 6、获取平台属性 id 值
            String attrIdString = bucket.getKeyAsString();

            // 7、把平台属性 id 值转换为 Long 类型
            Long attrId = Long.parseLong(attrIdString);

            // 8、保存平台属性 id
            SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();
            searchResponseAttrVo.setAttrId(attrId);

            // 9、获取下一级聚合的总 Map
            Map<String, Aggregation> attrSubAggMap = bucket.getAggregations().getAsMap();

            // 10、获取平台属性名
            ParsedStringTerms attrNameAgg = (ParsedStringTerms) attrSubAggMap.get("attrNameAgg");
            // 说明：因为在基于平台属性 id 分组的基础上，平台属性名肯定是唯一的
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();

            // 11、保存平台属性名
            searchResponseAttrVo.setAttrName(attrName);

            // 12、获取平台属性值对应的聚合对象
            ParsedStringTerms attrValueAgg = (ParsedStringTerms) attrSubAggMap.get("attrValueAgg");

            // 13、获取平台属性值对应的桶集合
            List<? extends Terms.Bucket> attrValueAggBuckets = attrValueAgg.getBuckets();

            // 14、遍历平台属性值对应的桶集合
            for (Terms.Bucket attrValueAggBucket : attrValueAggBuckets) {
                String attrValue = attrValueAggBucket.getKeyAsString();
                searchResponseAttrVo.getAttrValueList().add(attrValue);
            }

            // 15、将 searchResponseAttrVo 对象存入 searchResponseVo 的对应集合
            searchResponseVo.getAttrsList().add(searchResponseAttrVo);
        }

        // 第四步：设置总记录数
        searchResponseVo.setTotal(outerHits.getTotalHits().value);
        return searchResponseVo;
    }

    @Override
    public SearchResponseVo search(SearchParam searchParam) throws IOException {

        // 1、基于 SearchParam 构造 DSL 语句
        SearchRequest searchRequest = buildQueryDsl(searchParam);

        // 2、基于封装 DSL 语句的 SearchRequest 对象执行搜索
        SearchResponse searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 3、对 ElasticSearch 返回的响应结果进行解析
        SearchResponseVo searchResponseVo = parseSearchResult(searchResponse);

        // 4、针对 SearchResponseVo 对象设置分页参数
        searchResponseVo.setPageNo(searchParam.getPageNo());
        searchResponseVo.setPageSize(searchParam.getPageSize());

        // ※计算总记录数
        // [1]获取总记录数
        Long totalRecordNumber = searchResponseVo.getTotal();

        // [2]计算总页数
        // 传统的计算方式：(totalRecordNumber % pageSize == 0 ? 0 : 1) + totalRecordNumber/pageSize
        // 改进的计算方式：(totalRecordNumber + pageSize - 1)/pageSize
        Long totalPages = (totalRecordNumber + searchParam.getPageSize() - 1) / searchParam.getPageSize();

        // [3]设置总页数
        searchResponseVo.setTotalPages(totalPages);

        return searchResponseVo;
    }
}
