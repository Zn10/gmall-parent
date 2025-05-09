package com.zn.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zn.gmall.model.product.BaseCategoryTrademark;
import com.zn.gmall.model.product.BaseTrademark;
import com.zn.gmall.model.product.vo.CategoryTrademarkVo;
import com.zn.gmall.product.mapper.BaseCategoryTrademarkMapper;
import com.zn.gmall.product.mapper.BaseTrademarkMapper;
import com.zn.gmall.product.service.api.BaseCategoryTrademarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类品牌实现
 */
@SuppressWarnings("all")
@Slf4j
@Service
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper, BaseCategoryTrademark> implements BaseCategoryTrademarkService {

    @Resource
    private BaseTrademarkMapper baseTrademarkMapper;

    @Resource
    private BaseCategoryTrademarkMapper baseCategoryTrademarkMapper;

    /**
     * 根据三级分类获取品牌
     *
     * @param category3Id 三级分类id
     * @return List<BaseTrademark>
     */
    @Override
    public List<BaseTrademark> findTrademarkList(Long category3Id) {
        //  根据分类Id 获取到品牌Id 集合数据
        //  select * from base_category_trademark where category3_id = ?;
        QueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkQueryWrapper = new QueryWrapper<>();
        baseCategoryTrademarkQueryWrapper.eq("category3_id", category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(baseCategoryTrademarkQueryWrapper);

        //  判断baseCategoryTrademarkList 这个集合
        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            //  需要获取到这个集合中的品牌Id 集合数据
            List<Long> tradeMarkIdList = baseCategoryTrademarkList.stream().map(BaseCategoryTrademark::getTrademarkId).collect(Collectors.toList());
            //  正常查询数据的话... 需要根据品牌Id 来获取集合数据！
            return baseTrademarkMapper.selectBatchIds(tradeMarkIdList);
        }
        //  如果集合为空，则默认返回空
        return null;
    }

    /**
     * 删除关联
     *
     * @param category3Id 三级分类id
     * @param trademarkId 关联id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long category3Id, Long trademarkId) {
        //  逻辑删除： 本质更新操作 is_deleted
        //  更新： update base_category_trademark set is_deleted = 1 where category3_id=? and trademark_id=?;
        QueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkQueryWrapper = new QueryWrapper<>();
        baseCategoryTrademarkQueryWrapper.eq("category3_id", category3Id);
        baseCategoryTrademarkQueryWrapper.eq("trademark_id", trademarkId);
        baseCategoryTrademarkMapper.delete(baseCategoryTrademarkQueryWrapper);

    }

    /**
     * 获取当前未被三级分类关联的所有品牌
     *
     * @param category3Id 三级分类id
     * @return List<BaseTrademark>
     */
    @Override
    public List<BaseTrademark> findCurrentTrademarkList(Long category3Id) {
        //  哪些是关联的品牌Id
        QueryWrapper<BaseCategoryTrademark> baseCategoryTrademarkQueryWrapper = new QueryWrapper<>();
        baseCategoryTrademarkQueryWrapper.eq("category3_id", category3Id);
        List<BaseCategoryTrademark> baseCategoryTrademarkList = baseCategoryTrademarkMapper.selectList(baseCategoryTrademarkQueryWrapper);

        //  判断
        if (!CollectionUtils.isEmpty(baseCategoryTrademarkList)) {
            //  找到关联的品牌Id 集合数据 {1,3}
            List<Long> tradeMarkIdList = baseCategoryTrademarkList.stream().map(BaseCategoryTrademark::getTrademarkId).collect(Collectors.toList());
            //  在所有的品牌Id 中将这些有关联的品牌Id 给过滤掉就可以！
            //  select * from base_trademark; 外面 baseTrademarkMapper.selectList(null) {1,2,3,5}
            //  返回数据
            return baseTrademarkMapper.selectList(null).stream().filter(baseTrademark -> !tradeMarkIdList.contains(baseTrademark.getId())).collect(Collectors.toList());
        }
        //  如果说这个三级分类Id 下 没有任何品牌！ 则获取到所有的品牌数据！
        return baseTrademarkMapper.selectList(null);
    }

    /**
     * 保存分类与品牌关联
     *
     * @param categoryTrademarkVo 封装分类与品牌VO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CategoryTrademarkVo categoryTrademarkVo) {
        /*
        private Long category3Id;
        private List<Long> trademarkIdList;

        category3Id 61 tmId 2;
        category3Id 61 tmId 5;
         */
        //  获取到品牌Id 集合数据
        List<Long> trademarkIdList = categoryTrademarkVo.getTrademarkIdList();

        //  判断
        if (!CollectionUtils.isEmpty(trademarkIdList)) {
            //  做映射关系
            List<BaseCategoryTrademark> baseCategoryTrademarkList = trademarkIdList.stream().map((trademarkId) -> {
                //  创建一个分类Id 与品牌的关联的对象
                BaseCategoryTrademark baseCategoryTrademark = new BaseCategoryTrademark();
                baseCategoryTrademark.setCategory3Id(categoryTrademarkVo.getCategory3Id());
                baseCategoryTrademark.setTrademarkId(trademarkId);
                //  返回数据
                return baseCategoryTrademark;
            }).collect(Collectors.toList());

            //  集中保存到数据库    baseCategoryTrademarkList
            this.saveBatch(baseCategoryTrademarkList);
        }
    }
}

