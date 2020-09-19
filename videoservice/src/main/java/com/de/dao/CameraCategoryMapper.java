package com.de.dao;

import com.de.entity.CameraCategory;
import com.de.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gs
 * @date 2020/6/12 - 23:33
 */
@Repository
public interface CameraCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(CameraCategory record);

    int insertSelective(CameraCategory record);

    CameraCategory selectByPrimaryKey(Integer categoryId);

    CameraCategory selectByCategoryName(String categoryName);

    int updateByPrimaryKeySelective(CameraCategory record);

    int updateByPrimaryKey(CameraCategory record);

    List<CameraCategory> findCategoryList(PageQueryUtil pageUtil);

    List<CameraCategory> selectByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

    int getTotalCategories(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);
}
