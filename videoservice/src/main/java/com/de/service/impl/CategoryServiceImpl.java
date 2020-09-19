package com.de.service.impl;

import com.de.dao.CameraCategoryMapper;
import com.de.dao.CameraMapper;
import com.de.entity.CameraCategory;
import com.de.service.CategoryService;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gs
 * @date 2020/6/12 - 23:16
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CameraCategoryMapper cameraCategoryMapper;

    @Autowired
    private CameraMapper cameraMapper;


    @Override
    public PageResult getCameraCategoryPage(PageQueryUtil pageUtil) {
        List<CameraCategory> categoryList = cameraCategoryMapper.findCategoryList(pageUtil);
        int total = cameraCategoryMapper.getTotalCategories(pageUtil);
        PageResult pageResult = new PageResult(categoryList,total,pageUtil.getLimit(),pageUtil.getPage());
        return pageResult;
    }

    @Override
    public int getTotalCategories() {
        return cameraCategoryMapper.getTotalCategories(null);
    }

    @Override
    public Boolean saveCategory(String categoryName, String categoryIcon) {
        CameraCategory temp = cameraCategoryMapper.selectByCategoryName(categoryName);
        if(temp==null){
            CameraCategory cameraCategory = new CameraCategory();
            cameraCategory.setCategoryName(categoryName);
            cameraCategory.setCategoryIcon(categoryIcon);
            return cameraCategoryMapper.insertSelective(cameraCategory)>0;

        }
        return false;
    }

    @Override
    @Transactional
    public Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        CameraCategory cameraCategory = cameraCategoryMapper.selectByPrimaryKey(categoryId);
        if(cameraCategory!=null){
            cameraCategory.setCategoryName(categoryName);
            cameraCategory.setCategoryIcon(categoryIcon);

            cameraMapper.updateCameraCategorys(categoryName,cameraCategory.getCategoryId(),new Integer[]{categoryId});
            return cameraCategoryMapper.updateByPrimaryKeySelective(cameraCategory)>0;
        }
        return false;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length<1){
            return false;
        }
        cameraMapper.updateCameraCategorys("未分类地点",0,ids);

        return cameraCategoryMapper.deleteBatch(ids)>0;
    }

    @Override
    public List<CameraCategory> getAllCategories() {
        return cameraCategoryMapper.findCategoryList(null);
    }
}
