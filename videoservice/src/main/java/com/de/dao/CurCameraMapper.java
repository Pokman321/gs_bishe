package com.de.dao;

import com.de.entity.CurImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author gs
 * @date 2020/6/9 - 11:44
 */
//TODO:添加相机的过程中动态创建一张该相机的表，用于存取该相机下的行人信息
@Repository
public interface CurCameraMapper {

    int existTable(String tableName);

    void dropTable(@Param("tableName") String tableName);

    int createNewTable(@Param("tableName") String tableName,@Param("camera_id") int camera_id);

    int addImage(@Param("tableName") String tableName,@Param("curImage") CurImage curImage);
//    int addImage(String tableName, CurImage curImage);

    CurImage selectByPrimaryKey(@Param("tableName") String tableName,@Param("id") long id);

    List<CurImage> selectByTime(@Param("tableName") String tableName, @Param("start_time") long start_time, @Param("end_time") long end_time);

    List<CurImage> selectAllImage(@Param("tableName") String tableName);

    List<CurImage> selectImageHasPerson(@Param("tableName") String tableName);

//    Boolean deleteBatch(@Param("tableName") String tableName,@Param("id") List<Long> id);
    Boolean deleteBatch(Map map);


}
