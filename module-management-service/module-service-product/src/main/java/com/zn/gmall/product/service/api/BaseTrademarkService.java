package com.zn.gmall.product.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zn.gmall.model.product.BaseTrademark;

public interface BaseTrademarkService extends IService<BaseTrademark> {

    /**
     * Banner分页列表
     *
     * @param pageParam
     * @return IPage<BaseTrademark>
     */
    IPage<BaseTrademark> getPage(Page<BaseTrademark> pageParam);

}

