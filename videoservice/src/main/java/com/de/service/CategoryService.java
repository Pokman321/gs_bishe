package com.de.service;

import com.de.entity.CameraCategory;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;

import java.util.List;

/**
 * @author gs
 * @date 2020/6/12 - 22:54
 */

public interface CategoryService {

    PageResult getCameraCategoryPage(PageQueryUtil pageUtil);

    int getTotalCategories();

    Boolean saveCategory(String categoryName,String categoryIcon);

    Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    Boolean deleteBatch(Integer[] ids);

    List<CameraCategory> getAllCategories();

}
