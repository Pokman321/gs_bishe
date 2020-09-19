package com.de.mysearch.dao;

import com.de.mysearch.domain.EsProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gs
 * @date 2020/8/6 - 12:22
 */
@Repository
public interface EsProductDao {
    /**
     * 获取指定ID的搜索商品
     */
    List<EsProduct> getAllEsProductList(@Param("videoId") Integer id);
//    List<EsProduct> getAllEsProductList(Long id);
}