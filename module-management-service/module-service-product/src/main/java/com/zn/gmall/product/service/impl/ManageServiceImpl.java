package com.zn.gmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.cache.GmallCache;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.model.product.*;
import com.zn.gmall.mq.constant.MqConst;
import com.zn.gmall.mq.service.RabbitService;
import com.zn.gmall.product.mapper.*;
import com.zn.gmall.product.service.api.ManageService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品汇总实现
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class ManageServiceImpl implements ManageService {
    @Autowired
    private BaseCategory1Mapper baseCategory1Mapper;

    @Autowired
    private BaseCategory2Mapper baseCategory2Mapper;

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SpuPosterMapper spuPosterMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private BaseCategoryViewMapper baseCategoryViewMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitService rabbitService;

    /**
     * 获取全部分类信息
     *
     * @return List<JSONObject>
     */
    @Override
    @GmallCache(prefix = "category")
    public List<JSONObject> getBaseCategoryList() {
        // 声明几个json 集合
        ArrayList<JSONObject> list = new ArrayList<>();
        // 声明获取所有分类数据集合
        List<BaseCategoryView> baseCategoryViewList = baseCategoryViewMapper.selectList(null);
        // 循环上面的集合并安一级分类Id 进行分组
        Map<Long, List<BaseCategoryView>> category1Map = baseCategoryViewList.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        int index = 1;
        // 获取一级分类下所有数据
        for (Map.Entry<Long, List<BaseCategoryView>> entry1 : category1Map.entrySet()) {
            // 获取一级分类Id
            Long category1Id = entry1.getKey();
            // 获取一级分类下面的所有集合
            List<BaseCategoryView> category2List1 = entry1.getValue();
            //
            JSONObject category1 = new JSONObject();
            category1.put("index", index);
            category1.put("categoryId", category1Id);
            // 一级分类名称
            category1.put("categoryName", category2List1.get(0).getCategory1Name());
            // 变量迭代
            index++;
            // 循环获取二级分类数据
            Map<Long, List<BaseCategoryView>> category2Map = category2List1.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            // 声明二级分类对象集合
            List<JSONObject> category2Child = getJsonObjects(category2Map);
            // 将二级数据放入一级里面
            category1.put("categoryChild", category2Child);
            list.add(category1);
        }
        return list;
    }

    private static List<JSONObject> getJsonObjects(Map<Long, List<BaseCategoryView>> category2Map) {
        List<JSONObject> category2Child = new ArrayList<>();
        // 循环遍历
        for (Map.Entry<Long, List<BaseCategoryView>> entry2 : category2Map.entrySet()) {
            // 获取二级分类Id
            Long category2Id = entry2.getKey();
            // 获取二级分类下的所有集合
            List<BaseCategoryView> category3List = entry2.getValue();
            // 声明二级分类对象
            JSONObject category2 = new JSONObject();

            category2.put("categoryId", category2Id);
            category2.put("categoryName", category3List.get(0).getCategory2Name());
            // 添加到二级分类集合
            category2Child.add(category2);

            List<JSONObject> category3Child = new ArrayList<>();

            // 循环三级分类数据
            category3List.forEach(category3View -> {
                JSONObject category3 = new JSONObject();
                category3.put("categoryId", category3View.getCategory3Id());
                category3.put("categoryName", category3View.getCategory3Name());

                category3Child.add(category3);
            });

            // 将三级数据放入二级里面
            category2.put("categoryChild", category3Child);

        }
        return category2Child;
    }

    private SkuInfo getSkuInfoFromDatabase(Long skuId) {
        // 查询 SKU 基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo != null) {
            // 查询 SKU 图片信息
            List<SkuImage> skuImageList =
                    skuImageMapper.selectList(
                            new QueryWrapper<SkuImage>()
                                    .eq("sku_id", skuId));

            // 属性装配
            skuInfo.setSkuImageList(skuImageList);
        }
        return skuInfo;
    }

    /**
     * 通过skuId 集合来查询数据
     *
     * @param skuId 商品SKUid
     * @return List<BaseAttrInfo>
     */
    @Override
    public List<BaseAttrInfo> getAttrList(Long skuId) {
        return baseAttrInfoMapper.selectBaseAttrInfoListBySkuId(skuId);
    }

    /**
     * 根据spuid获取商品海报
     *
     * @param spuId 商品SPUid
     * @return List<SpuPoster>
     */
    @Override
    public List<SpuPoster> findSpuPosterBySpuId(Long spuId) {
        QueryWrapper<SpuPoster> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("spu_id", spuId);
        return spuPosterMapper.selectList(spuInfoQueryWrapper);
    }

    /**
     * 根据spuId 查询map 集合属性
     *
     * @param spuId 商品SPUid
     */
    @Override
    @GmallCache(prefix = "saleAttrValuesBySpu:")
    public Map<Object, Object> getSkuValueIdsMap(Long spuId) {
        Map<Object, Object> map = new HashMap<>();
        // key = 125|123 ,value = 37
        List<Map<Object, Object>> mapList = skuSaleAttrValueMapper.selectSaleAttrValuesBySpu(spuId);
        if (mapList != null && !mapList.isEmpty()) {
            // 循环遍历
            for (Map<Object, Object> skuMap : mapList) {
                // key = 125|123 ,value = 37
                map.put(skuMap.get("value_ids"), skuMap.get("sku_id"));
            }
        }
        return map;
    }

    /**
     * 根据spuId，skuId 查询销售属性集合
     *
     * @param skuId 商品SKUid
     * @param spuId 商品SPUid
     * @return List<SpuSaleAttr>
     */
    @Override
    @GmallCache(prefix = "spuSaleAttrListCheckBySku:")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    /**
     * 获取sku价格
     *
     * @param skuId 商品SKUid
     * @return BigDecimal
     */
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        RLock lock = redissonClient.getLock(skuId + RedisConst.SKULOCK_SUFFIX);
        //  上锁
        lock.lock();
        SkuInfo skuInfo;
        BigDecimal price = new BigDecimal(0);
        try {
            QueryWrapper<SkuInfo> skuInfoQueryWrapper = new QueryWrapper<>();
            skuInfoQueryWrapper.eq("id", skuId);
            skuInfoQueryWrapper.select("price");
            skuInfo = skuInfoMapper.selectOne(skuInfoQueryWrapper);
            if (skuInfo != null) {
                price = skuInfo.getPrice();
            }
            // 给默认值
            return new BigDecimal(0);
        } catch (Exception e) {
            log.error("exception message", e);
        } finally {
            //  解锁！
            lock.unlock();
        }
        //  返回价格
        return price;

    }

    /**
     * 通过三级分类id查询分类信息
     *
     * @param category3Id 三级分类id
     * @return BaseCategoryView
     */
    @Override
    @GmallCache(prefix = "categoryViewByCategory3Id:")
    public BaseCategoryView getCategoryViewByCategory3Id(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    /**
     * 根据skuId 查询skuInfo
     *
     * @param skuId 商品SKUID
     */
    @Override
    @GmallCache(prefix = RedisConst.SKUKEY_PREFIX)
    public SkuInfo getSkuInfo(Long skuId) {
        SkuInfo skuInfo = getSkuInfoFromDatabase(skuId);
        return skuInfo;
    }

    /**
     * SKU分页列表
     *
     * @param pageParam 页码参数
     * @return IPage<SkuInfo>
     */
    @Override
    public IPage<SkuInfo> getPage(Page<SkuInfo> pageParam) {
        QueryWrapper<SkuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return skuInfoMapper.selectPage(pageParam, queryWrapper);
    }

    /**
     * 商品上架
     *
     * @param skuId 商品SKUid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onSale(Long skuId) {
        // 更改销售状态
        SkuInfo skuInfoUp = new SkuInfo();
        skuInfoUp.setId(skuId);
        skuInfoUp.setIsSale(1);
        skuInfoMapper.updateById(skuInfoUp);
        //  通知mq商品上架
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_GOODS, MqConst.ROUTING_GOODS_UPPER, skuId);
    }

    /**
     * 商品下架
     *
     * @param skuId 商品SKUid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSale(Long skuId) {
        // 更改销售状态
        SkuInfo skuInfoUp = new SkuInfo();
        skuInfoUp.setId(skuId);
        skuInfoUp.setIsSale(0);
        skuInfoMapper.updateById(skuInfoUp);
        //  通知mq商品下架
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_GOODS, MqConst.ROUTING_GOODS_LOWER, skuId);
    }

    /**
     * 保存数据
     *
     * @param skuInfo SKU实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSkuInfo(SkuInfo skuInfo) {
    /*
        skuInfo 库存单元表 --- spuInfo！
        skuImage 库存单元图片表 --- spuImage!
        skuSaleAttrValue sku销售属性值表{sku与销售属性值的中间表} --- skuInfo ，spuSaleAttrValue
        skuAttrValue sku与平台属性值的中间表 --- skuInfo ，baseAttrValue
     */
        skuInfoMapper.insert(skuInfo);
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList != null && !skuImageList.isEmpty()) {
            // 循环遍历
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
            }
            skuImageMapper.batchInsert(skuImageList);
        }
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        // 调用判断集合方法
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)) {
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
            }
            skuSaleAttrValueMapper.batchInsert(skuSaleAttrValueList);
        }
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
            }
            skuAttrValueMapper.batchInsert(skuAttrValueList);
        }
    }

    /**
     * 根据spuId 查询销售属性集合
     *
     * @param spuId 商品SKUid
     * @return List<SpuSaleAttr>
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    /**
     * 根据spuId 查询spuImageList
     *
     * @param spuId 商品SPUid
     * @return List<SpuImage>
     */
    @Override
    public List<SpuImage> getSpuImageList(Long spuId) {
        QueryWrapper<SpuImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        return spuImageMapper.selectList(queryWrapper);
    }

    /**
     * 保存商品数据
     *
     * @param spuInfo 商品SPU实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuInfo spuInfo) {
    /*
        spuInfo;
        spuImage;
        spuSaleAttr;
        spuSaleAttrValue;
        spuPoster
     */
        if (spuInfo != null) {
            spuInfoMapper.insert(spuInfo);
            //  获取到spuImage 集合数据
            List<SpuImage> spuImageList = spuInfo.getSpuImageList();
            //  判断不为空
            if (!CollectionUtils.isEmpty(spuImageList)) {
                //  循环遍历
                for (SpuImage spuImage : spuImageList) {
                    //  需要将spuId 赋值
                    spuImage.setSpuId(spuInfo.getId());
                }
                //  批量保存SpuImage
                spuImageMapper.batchInsert(spuImageList);
            }
            //  获取销售属性集合
            List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
            List<SpuSaleAttrValue> spuSaleAttrValueList = null;
            //  判断
            if (!CollectionUtils.isEmpty(spuSaleAttrList)) {
                //  循环遍历
                for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                    //  需要将spuId 赋值
                    spuSaleAttr.setSpuId(spuInfo.getId());
                    // 设置销售属性名称
                    spuSaleAttr.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                    //  再此获取销售属性值集合
                    spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                    //  判断
                    if (!CollectionUtils.isEmpty(spuSaleAttrValueList)) {
                        //  循环遍历
                        for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                            //   需要将spuId， sale_attr_name 赋值
                            spuSaleAttrValue.setSpuId(spuInfo.getId());
                            spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                            spuSaleAttrValue.setSaleAttrValueName(spuSaleAttrValue.getSaleAttrValueName());
                            spuSaleAttrValue.setBaseSaleAttrId(spuSaleAttr.getId());
                        }
                    }
                }
                spuSaleAttrMapper.batchInsert(spuSaleAttrList);
            }
            //  获取到spuSaleAttrValueList 集合数据
            if (spuSaleAttrValueList != null) {
                spuSaleAttrValueMapper.batchInsert(spuSaleAttrValueList);
            }
            //  获取到posterList 集合数据
            List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
            //  判断不为空
            if (!CollectionUtils.isEmpty(spuPosterList)) {
                for (SpuPoster spuPoster : spuPosterList) {
                    //  需要将spuId 赋值
                    spuPoster.setSpuId(spuInfo.getId());
                    spuPoster.setImgName(spuPoster.getImgName());
                    spuPoster.setImgUrl(spuPoster.getImgUrl());
                }
                //  保存spuPoster
                spuPosterMapper.batchInsert(spuPosterList);
            }
        }
    }

    /**
     * 查询所有的销售属性数据
     *
     * @return List<BaseSaleAttr>
     */
    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    /**
     * spu分页查询
     *
     * @param pageParam 页码参数
     * @param spuInfo   商品列表参数
     * @return IPage<SpuInfo>
     */
    @Override
    public IPage<SpuInfo> getSpuInfoPage(Page<SpuInfo> pageParam, SpuInfo spuInfo) {
        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id", spuInfo.getCategory3Id());
        queryWrapper.orderByDesc("create_time");
        return spuInfoMapper.selectPage(pageParam, queryWrapper);
    }

    /**
     * 根据attrId 查询平台属性对象
     *
     * @param attrId 属性id
     * @return BaseAttrInfo
     */
    @Override
    public BaseAttrInfo getAttrInfo(Long attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        // 查询到最新的平台属性值集合数据放入平台属性中！
        // 判断平台属性不为空
        if (baseAttrInfo != null) {
            List<BaseAttrValue> attrValueList = getAttrValueList(attrId);
            // 判断平台属性值不为空
            if (!CollectionUtils.isEmpty(attrValueList)) {
                baseAttrInfo.setAttrValueList(attrValueList);
            }
        }
        return baseAttrInfo;
    }

    /**
     * 根据属性id获取属性值
     *
     * @param attrId 属性id
     * @return List<BaseAttrValue>
     */
    private List<BaseAttrValue> getAttrValueList(Long attrId) {
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id", attrId);
        return baseAttrValueMapper.selectList(queryWrapper);
    }

    /**
     * 保存平台属性方法
     *
     * @param baseAttrInfo 平台属性实例
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        // 什么情况下 是添加，什么情况下是更新，修改 根据baseAttrInfo 的Id
        // baseAttrInfo
        if (baseAttrInfo.getId() != null) {
            // 修改数据
            baseAttrInfoMapper.updateById(baseAttrInfo);
        } else {
            // 新增
            // baseAttrInfo 插入数据
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
        // baseAttrValue 平台属性值
        // 修改：通过先删除{baseAttrValue}，在新增的方式！
        // 删除条件：baseAttrValue.attrId = baseAttrInfo.id 逻辑删除
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id", baseAttrInfo.getId());
        baseAttrValueMapper.delete(queryWrapper);
        // 获取页面传递过来的所有平台属性值数据
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if (attrValueList != null && !attrValueList.isEmpty()) {
            // 循环遍历
            for (BaseAttrValue baseAttrValue : attrValueList) {
                // 获取平台属性Id 给attrId
                baseAttrValue.setAttrId(baseAttrInfo.getId());
            }
            // 批量新增
            baseAttrValueMapper.batchInsert(attrValueList);
        }
    }

    /**
     * 查询所有的一级分类信息
     *
     * @return List<BaseCategory1>
     */
    @Override
    public List<BaseCategory1> getCategory1() {
        return baseCategory1Mapper.selectList(null);
    }

    /**
     * 根据一级分类Id 查询二级分类数据
     *
     * @param category1Id 一级分类Id
     * @return List<BaseCategory2>
     */
    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        QueryWrapper<BaseCategory2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category1_id", category1Id);
        return baseCategory2Mapper.selectList(queryWrapper);
    }

    /**
     * 根据二级分类Id 查询三级分类数据
     *
     * @param category2Id 二级分类Id
     * @return List<BaseCategory3>
     */
    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        // select * from baseCategory3 where Category2Id = ?
        QueryWrapper<BaseCategory3> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category2_id", category2Id);
        return baseCategory3Mapper.selectList(queryWrapper);
    }

    /**
     * 根据分类Id 获取平台属性数据
     * 接口说明：
     * 1，平台属性可以挂在一级分类、二级分类和三级分类
     * 2，查询一级分类下面的平台属性，传：category1Id，0，0；   取出该分类的平台属性
     * 3，查询二级分类下面的平台属性，传：category1Id，category2Id，0；
     * 取出对应一级分类下面的平台属性与二级分类对应的平台属性
     * 4，查询三级分类下面的平台属性，传：category1Id，category2Id，category3Id；
     * 取出对应一级分类、二级分类与三级分类对应的平台属性
     *
     * @param category1Id 一级分类Id
     * @param category2Id 二级分类Id
     * @param category3Id 三级分类Id
     * @return List<BaseAttrInfo>
     */
    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        return baseAttrInfoMapper.selectBaseAttrInfoList(category1Id, category2Id, category3Id);
    }

    @Override
    public void remove(Long attrId) {
        // 删除平台属性
        baseAttrInfoMapper.deleteById(attrId);
        // 删除平台属性值
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id", attrId);
        baseAttrValueMapper.delete(queryWrapper);
    }
}

