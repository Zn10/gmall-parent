package com.zn.gmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zn.gmall.common.cache.GmallCache;
import com.zn.gmall.common.constant.RedisConst;
import com.zn.gmall.model.product.*;
import com.zn.gmall.product.mapper.*;
import com.zn.gmall.product.service.api.ManageService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("ManageService")
public class ManageServiceImpl implements ManageService {
    @Resource
    private BaseCategory1Mapper baseCategory1Mapper;

    @Resource
    private BaseCategory2Mapper baseCategory2Mapper;

    @Resource
    private BaseCategory3Mapper baseCategory3Mapper;

    @Resource
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Resource
    private BaseAttrValueMapper baseAttrValueMapper;

    @Resource
    private SpuInfoMapper spuInfoMapper;

    @Resource
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Resource
    private SpuImageMapper spuImageMapper;

    @Resource
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Resource
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Resource
    private SpuPosterMapper spuPosterMapper;

    @Resource
    private SkuInfoMapper skuInfoMapper;

    @Resource
    private SkuImageMapper skuImageMapper;

    @Resource
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Resource
    private SkuAttrValueMapper skuAttrValueMapper;

    @Resource
    private BaseCategoryViewMapper baseCategoryViewMapper;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, SkuInfo> redisTemplate;

    @Resource
    private BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public BaseTrademark getTrademarkByTmId(Long tmId) {
        return baseTrademarkMapper.selectById(tmId);
    }


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
            // 将二级数据放入一级里面
            category1.put("categoryChild", category2Child);
            list.add(category1);
        }
        return list;
    }

    private SkuInfo getSkuInfoFromDatabase(Long skuId) {
        // 查询 SKU 基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);

        // 查询 SKU 图片信息
        List<SkuImage> skuImageList =
                skuImageMapper.selectList(
                        new QueryWrapper<SkuImage>()
                                .eq("sku_id", skuId));
        // ※由于存在缓存穿透问题，所以一定要判空操作
        if (skuInfo != null) {
            // 属性装配
            skuInfo.setSkuImageList(skuImageList);
        }

        return skuInfo;
    }

    @Override
    public List<BaseAttrInfo> getAttrList(Long skuId) {

        return baseAttrInfoMapper.selectBaseAttrInfoListBySkuId(skuId);
    }


    @Override
    public List<SpuPoster> findSpuPosterBySpuId(Long spuId) {
        QueryWrapper<SpuPoster> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("spu_id", spuId);
        return spuPosterMapper.selectList(spuInfoQueryWrapper);
    }

    @Override
    @GmallCache(prefix = "saleAttrValuesBySpu:")
    public Map getSkuValueIdsMap(Long spuId) {
        Map<Object, Object> map = new HashMap<>();
        // key = 125|123 ,value = 37
        List<Map> mapList = skuSaleAttrValueMapper.selectSaleAttrValuesBySpu(spuId);
        if (mapList != null && mapList.size() > 0) {
            // 循环遍历
            for (Map skuMap : mapList) {
                // key = 125|123 ,value = 37
                map.put(skuMap.get("value_ids"), skuMap.get("sku_id"));
            }
        }
        return map;
    }

    @Override
    @GmallCache(prefix = "spuSaleAttrListCheckBySku:")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuId, spuId);
    }

    /**
     * 获取sku价格
     *
     * @param skuId
     * @return BigDecimal
     */
    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        RLock lock = redissonClient.getLock(skuId + ":lock");
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  解锁！
            lock.unlock();
        }
        //  返回价格
        return price;

    }


    @Override
    @GmallCache(prefix = "categoryViewByCategory3Id:")
    public BaseCategoryView getCategoryViewByCategory3Id(Long category3Id) {
        return baseCategoryViewMapper.selectById(category3Id);
    }

    @Override
    @GmallCache(prefix = RedisConst.SKUKEY_PREFIX)
    public SkuInfo getSkuInfo(Long skuId) {

        // 大前提：Redis 服务器宕机几率极低，不予考虑
        // 因为生产环境下 Redis 服务器都会搭建集群，可用性极高。
        // 如果我们考虑 Redis 服务器宕机这个因素，那所有实例都有可能宕机，
        // 代码就没法写了

        // 1、声明 SkuInfo 变量，用于接收查询结果
        SkuInfo skuInfo;

        // 2、查询缓存
        // [1]创建 SkuInfo 业务数据 Key 字符串
        String skuKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKUKEY_SUFFIX;

        // [2]正式读取缓存
        skuInfo = redisTemplate.opsForValue().get(skuKey);

        // 3、判断缓存是否命中
        if (skuInfo != null) {

            // 4、如果命中成功，则返回当前查询结果，逻辑结束
            return skuInfo;
        }

        // 5、如果命中失败，则申请分布式锁
        // [1]创建分布式锁的 Key 字符串
        // 注意：并非所有 SKU 共用一个锁，而是每个 skuId 一个锁
        String lockKey = RedisConst.SKUKEY_PREFIX + skuId + RedisConst.SKULOCK_SUFFIX;

        // [2]通过 Redisson 客户端获取锁对象
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // [3]尝试加锁
            // waitTime：尝试加锁的等待时间
            // leaseTime：加成功的锁的过期时间
            boolean tryLockResult =
                    lock.tryLock(
                            RedisConst.SKULOCK_EXPIRE_PX1,
                            RedisConst.SKULOCK_EXPIRE_PX2,
                            TimeUnit.SECONDS);

            // 6、如果申请分布式锁失败，则线程 sleep 1 秒，
            // 然后递归调用当前方法重试
            if (!tryLockResult) {
                TimeUnit.SECONDS.sleep(1);
                return getSkuInfo(skuId);
            }

            // 7、如果申请分布式锁成功，则查询数据库
            skuInfo = getSkuInfoFromDatabase(skuId);

            // 8、声明变量用于接收缓存时间
            // 创建随机的分钟数，附加到正式数据的缓存时间里面，避免缓存雪崩
            int seconds = (int) (Math.random() * 10) * 60;
            long cacheValueTimeout = RedisConst.SKUKEY_TIMEOUT + seconds;

            // 9、如果数据不存在则创建空对象
            if (skuInfo == null) {
                // 应对缓存穿透问题，设置较短过期时间
                cacheValueTimeout = RedisConst.SKUKEY_TEMPORARY_TIMEOUT;

                // 应对缓存穿透问题，创建存入缓存的空对象
                skuInfo = new SkuInfo();
            }

            // 10、不管是数据库查出来的对象还是空对象都执行存入缓存
            redisTemplate
                    .opsForValue()
                    .set(
                            skuKey,
                            skuInfo,
                            cacheValueTimeout,
                            TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            // 11、解开分布式锁
            lock.unlock();
        }
        // 12、如果前面没有返回 skuInfo 对象，则这里返回
        return skuInfo;
    }

    @Override
    public IPage<SkuInfo> getPage(Page<SkuInfo> pageParam) {
        QueryWrapper<SkuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        return skuInfoMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onSale(Long skuId) {
        // 更改销售状态
        SkuInfo skuInfoUp = new SkuInfo();
        skuInfoUp.setId(skuId);
        skuInfoUp.setIsSale(1);
        skuInfoMapper.updateById(skuInfoUp);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSale(Long skuId) {
        // 更改销售状态
        SkuInfo skuInfoUp = new SkuInfo();
        skuInfoUp.setId(skuId);
        skuInfoUp.setIsSale(0);
        skuInfoMapper.updateById(skuInfoUp);
    }

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
        if (skuImageList != null && skuImageList.size() > 0) {
            // 循环遍历
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            }
        }
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        // 调用判断集合方法
        if (!CollectionUtils.isEmpty(skuSaleAttrValueList)) {
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            }
        }
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    @Override
    public List<SpuImage> getSpuImageList(Long spuId) {
        QueryWrapper<SpuImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", spuId);
        return spuImageMapper.selectList(queryWrapper);
    }

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
        spuInfoMapper.insert(spuInfo);
        //  获取到spuImage 集合数据
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        //  判断不为空
        if (!CollectionUtils.isEmpty(spuImageList)) {
            //  循环遍历
            for (SpuImage spuImage : spuImageList) {
                //  需要将spuId 赋值
                spuImage.setSpuId(spuInfo.getId());
                //  保存spuImge
                spuImageMapper.insert(spuImage);
            }
        }
        //  获取销售属性集合
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        //  判断
        if (!CollectionUtils.isEmpty(spuSaleAttrList)) {
            //  循环遍历
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                //  需要将spuId 赋值
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);
                //  再此获取销售属性值集合
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                //  判断
                if (!CollectionUtils.isEmpty(spuSaleAttrValueList)) {
                    //  循环遍历
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        //   需要将spuId， sale_attr_name 赋值
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }
            }
        }
        //  获取到posterList 集合数据
        List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
        //  判断不为空
        if (!CollectionUtils.isEmpty(spuPosterList)) {
            for (SpuPoster spuPoster : spuPosterList) {
                //  需要将spuId 赋值
                spuPoster.setSpuId(spuInfo.getId());
                //  保存spuPoster
                spuPosterMapper.insert(spuPoster);
            }
        }
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return baseSaleAttrMapper.selectList(null);
    }

    @Override
    public IPage<SpuInfo> getSpuInfoPage(Page<SpuInfo> pageParam, SpuInfo spuInfo) {
        QueryWrapper<SpuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id", spuInfo.getCategory3Id());
        queryWrapper.orderByDesc("id");
        return spuInfoMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public BaseAttrInfo getAttrInfo(Long attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectById(attrId);
        // 查询到最新的平台属性值集合数据放入平台属性中！
        baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        return baseAttrInfo;
    }

    /**
     * 根据属性id获取属性值
     *
     * @param attrId
     * @return List<BaseAttrValue>
     */
    private List<BaseAttrValue> getAttrValueList(Long attrId) {
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id", attrId);
        return baseAttrValueMapper.selectList(queryWrapper);
    }

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
        // 删除条件：baseAttrValue.attrId = baseAttrInfo.id
        QueryWrapper<BaseAttrValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_id", baseAttrInfo.getId());
        baseAttrValueMapper.delete(queryWrapper);
        // 获取页面传递过来的所有平台属性值数据
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if (attrValueList != null && attrValueList.size() > 0) {
            // 循环遍历
            for (BaseAttrValue baseAttrValue : attrValueList) {
                // 获取平台属性Id 给attrId
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseCategory1> getCategory1() {
        return baseCategory1Mapper.selectList(null);
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        QueryWrapper<BaseCategory2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category1_id", category1Id);
        return baseCategory2Mapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        // select * from baseCategory3 where Category2Id = ?
        QueryWrapper<BaseCategory3> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category2_id", category2Id);
        return baseCategory3Mapper.selectList(queryWrapper);
    }

    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long category1Id, Long category2Id, Long category3Id) {
        return baseAttrInfoMapper.selectBaseAttrInfoList(category1Id, category2Id, category3Id);
    }
}

