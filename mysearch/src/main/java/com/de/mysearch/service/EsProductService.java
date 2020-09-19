package com.de.mysearch.service;

import com.de.mysearch.domain.EsProduct;
import org.springframework.data.domain.Page;
//import com.github.pagehelper.Page;

//import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索管理Service
 * Created by macro on 2018/6/19.
 */
public interface EsProductService {
    /**
     * 从数据库中导入所有商品到ES
     */
    int importAll();

    /**
     * 根据id删除商品
     */
    void delete(Integer id);

    /**
     * 根据id创建商品
     */
    EsProduct create(Integer id);

    /**
     * 批量删除商品
     */
    void delete(List<Integer> ids);

    /**
     * 简单搜索，给用户用的
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 排序搜索，给用户用的
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 根据各种id搜索视频，内部调用
     */

//    Page<EsProduct> search(Integer videoId, String videoName, Integer cameraCategoryId, String cameraCategoryName, String userName);
    Page<EsProduct> search(Map<String,String> map,Integer pageNum,Integer pageSize);
    /**
     * 根据其实终止时间搜索，给用户用的
     */

    Page<EsProduct> search(String startDate, String endDate,Integer pageNum,Integer pageSize);

    /**
     * 根据用户点击视频id，进行相关视频推荐，内部调用
     */

    Page<EsProduct> recommend(Integer id, Integer pageNum, Integer pageSize);

    /**
     * 获取搜索词相关品牌、分类、属性
     */
//    EsProductRelatedInfo searchRelatedInfo(String keyword);
}
