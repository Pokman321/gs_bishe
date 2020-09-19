package com.de.mysearch.service.impl;

//import com.de.search.dao.EsProductDao;
import com.de.mysearch.dao.EsProductDao;
//import com.macro.mall.search.domain.EsProduct;
import com.de.mysearch.domain.EsProduct;
//import com.macro.mall.search.domain.EsProductRelatedInfo;
//import com.macro.mall.search.repository.EsProductRepository;
//import com.macro.mall.search.service.EsProductService;
import com.de.mysearch.repository.MyEsProductRepository;
import com.de.mysearch.repository.MyEsProductRepository;
import com.de.mysearch.service.EsProductService;
//import com.github.pagehelper.Page;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;


/**
 * 商品搜索管理Service实现类
 * Created by macro on 2018/6/19.
 */
@Service
public class EsProductServiceImpl implements EsProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsProductServiceImpl.class);
//    @Resource(name = "EsProductDao")
    @Autowired
    private EsProductDao productDao;
    @Autowired
    private MyEsProductRepository productRepository;


    @Override
    public int importAll() {
        List<EsProduct> esProductList = productDao.getAllEsProductList(null);
        System.out.println(esProductList);
        Iterable<EsProduct> esProductIterable = productRepository.saveAll(esProductList);
        Iterator<EsProduct> iterator = esProductIterable.iterator();
        int result = 0;
        while (iterator.hasNext()) {
            result++;
            iterator.next();
        }
        return result;
    }

    @Override
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public EsProduct create(Integer id) {
        EsProduct result = null;
        List<EsProduct> esProductList = productDao.getAllEsProductList(id);
//        List<EsProduct> esProductList = productDao.getAllEsProductList();
        if (esProductList.size() > 0) {
            EsProduct esProduct = esProductList.get(0);
            result = productRepository.save(esProduct);
        }
        return result;
    }

    @Override
    public void delete(List<Integer> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<EsProduct> esProductList = new ArrayList<>();
            for (Integer id : ids) {
                EsProduct esProduct = new EsProduct();
                esProduct.setVideoId(id);
                esProductList.add(esProduct);
            }
            productRepository.deleteAll(esProductList);
        }
    }

    @Override
    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return productRepository.findByVideoNameOrUserNameOrCameraCategoryName(keyword, keyword, keyword, pageable);
    }



    @Override
    public Page<EsProduct> search(Map<String,String> querymap,Integer pageNum, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //分页
        nativeSearchQueryBuilder.withPageable(pageable);

        for(Map.Entry<String,String> map:querymap.entrySet()){
            if ("videoId".equals(map.getKey()) && map.getValue()!=null){
                List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
                filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("videoId", Integer.valueOf( map.getValue())),
                        ScoreFunctionBuilders.weightFactorFunction(10)));
                break;
            }
            else if ("videoName".equals(map.getKey()) && map.getValue()!=null){
                List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
                filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("videoName", map.getValue()),
                        ScoreFunctionBuilders.weightFactorFunction(10)));
                break;
            }
            else if ("cameraCategoryId".equals(map.getKey()) && map.getValue()!=null){
                List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
                filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("cameraCategoryId", Integer.valueOf(map.getValue())),
                        ScoreFunctionBuilders.weightFactorFunction(10)));
                break;
            }
            else if ("cameraCategoryName".equals(map.getKey()) && map.getValue()!=null){
                List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
                filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("cameraCategoryName", map.getValue()),
                        ScoreFunctionBuilders.weightFactorFunction(10)));
                break;
            }
            else if ("userName".equals(map.getKey()) && map.getValue()!=null){
                List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
                filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("userName", map.getValue()),
                        ScoreFunctionBuilders.weightFactorFunction(10)));
                break;
            }
            else{
                nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
            }
        }
        nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));

        nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        LOGGER.info("DSL:{}", searchQuery.getQuery().toString());

        return productRepository.search(searchQuery);

    }

    @Override
    public Page<EsProduct> search(String startDate, String endDate,Integer pageNum,Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //分页
        nativeSearchQueryBuilder.withPageable(pageable);
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("videoTime").from(startDate).to(endDate);

        nativeSearchQueryBuilder.withQuery(rangeQueryBuilder);

        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("videoId").order(SortOrder.DESC));

        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        LOGGER.info("DSL:{}", searchQuery.getQuery().toString());
        return productRepository.search(searchQuery);


    }



    @Override
    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize, Integer sort) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //分页
        nativeSearchQueryBuilder.withPageable(pageable);
        //过滤
//        if (brandId != null || productCategoryId != null) {
//            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//            if (brandId != null) {
//                boolQueryBuilder.must(QueryBuilders.termQuery("brandId", brandId));
//            }
//            if (productCategoryId != null) {
//                boolQueryBuilder.must(QueryBuilders.termQuery("productCategoryId", productCategoryId));
//            }
//            nativeSearchQueryBuilder.withFilter(boolQueryBuilder);
//        }
        //搜索
        if (StringUtils.isEmpty(keyword)) {
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchAllQuery());
        } else {
            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("videoName", keyword),
                    ScoreFunctionBuilders.weightFactorFunction(10)));
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("userName", keyword),
                    ScoreFunctionBuilders.weightFactorFunction(5)));
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("cameraCategoryName", keyword),
                    ScoreFunctionBuilders.weightFactorFunction(2)));
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
            filterFunctionBuilders.toArray(builders);
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .setMinScore(2);
            nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
        }
        //排序
        if(sort==1){
            //按新品从新到旧
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("videoId").order(SortOrder.DESC));
        }
        else if(sort==2){
            //按新品从旧到新
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("videoId").order(SortOrder.ASC));
        }
        else if(sort==3){
            //按热度从高到低
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("videoViews").order(SortOrder.DESC));
        }else{
            //按相关度
            nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        }
        nativeSearchQueryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        LOGGER.info("DSL:{}", searchQuery.getQuery().toString());
//        return productRepository.search(searchQuery);
        return productRepository.search(searchQuery);
    }

    @Override
    public Page<EsProduct> recommend(Integer id, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        List<EsProduct> esProductList = productDao.getAllEsProductList(id);
//        List<EsProduct> esProductList = productDao.getAllEsProductList();
        if (esProductList.size() > 0) {
            EsProduct esProduct = esProductList.get(0);
            String userName = esProduct.getUserName();
            Integer productCameraCategoryId = esProduct.getCameraCategoryId();
            String videoName = esProduct.getVideoName();
//            String tags = esProduct.getTags();
            //根据视频标题、发布者、场景进行搜索
            List<FunctionScoreQueryBuilder.FilterFunctionBuilder> filterFunctionBuilders = new ArrayList<>();
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("userName", userName),
                    ScoreFunctionBuilders.weightFactorFunction(8)));
            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("videoName", videoName),
                    ScoreFunctionBuilders.weightFactorFunction(3)));

            filterFunctionBuilders.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("cameraCategoryId", productCameraCategoryId),
                    ScoreFunctionBuilders.weightFactorFunction(2)));
            FunctionScoreQueryBuilder.FilterFunctionBuilder[] builders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[filterFunctionBuilders.size()];
            filterFunctionBuilders.toArray(builders);
            FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(builders)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .setMinScore(2);
            //用于过滤掉相同的商品
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("videoId",id));
            //构建查询条件
            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
            builder.withQuery(functionScoreQueryBuilder);
            builder.withFilter(boolQueryBuilder);
            builder.withPageable(pageable);
            NativeSearchQuery searchQuery = builder.build();
            LOGGER.info("DSL:{}", searchQuery.getQuery().toString());
            return productRepository.search(searchQuery);
        }
        return new PageImpl<>(null);
    }


}
