package com.de.mysearch.repository;

import com.de.mysearch.domain.EsProduct;
//import com.macro.mall.search.domain.EsProduct;

//import com.github.pagehelper.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 商品ES操作类
 * Created by macro on 2018/6/19.
 */

public interface MyEsProductRepository extends ElasticsearchRepository<EsProduct, Integer> {
    /**
     * 搜索查询
     *
     * @param videoName             视频名称
     * @param userName              作者名字
     * @param cameraCategoryName    地点名称
     * @param page                  分页信息
     * @return
     */
    Page<EsProduct> findByVideoNameOrUserNameOrCameraCategoryName(String videoName, String userName, String cameraCategoryName, Pageable page);;

}
