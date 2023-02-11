package com.zn.gmall.list.repository;

import com.zn.gmall.model.list.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author: 赵念
 * @create-date: 2023/2/10/19:34
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
