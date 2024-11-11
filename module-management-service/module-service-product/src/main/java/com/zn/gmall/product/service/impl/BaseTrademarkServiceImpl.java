package com.zn.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zn.gmall.model.product.BaseTrademark;
import com.zn.gmall.product.mapper.BaseTrademarkMapper;
import com.zn.gmall.product.service.api.BaseTrademarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("all")
@Service
@Slf4j
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark> implements BaseTrademarkService {

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    /**
     * Banner分页列表
     *
     * @param pageParam 页码参数
     * @return IPage<BaseTrademark>
     */
    @Override
    public IPage<BaseTrademark> getPage(Page<BaseTrademark> pageParam) {
        QueryWrapper<BaseTrademark> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");
        return baseTrademarkMapper.selectPage(pageParam, queryWrapper);
    }
}

