package com.de.dao;

import com.de.entity.Camera;
import com.de.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gs
 * @date 2020/6/10 - 0:41
 */
@Repository
public interface CameraMapper {

    //删除
    int deleteByPrimaryKey(int cameraId);

    int deleteBatch(Integer[] ids);

    //增加

    int insertCameras(Camera camera);

    int insertSelective(Camera camera);

    //查询
    Camera selectByPrimaryKey(int cameraId);

    List<Camera> findAllCameras(PageQueryUtil pageUtil);

    List<Camera> findEnableCameras(PageQueryUtil pageUtil);

    List<Camera> findCameraList(PageQueryUtil pageUtil);

//    List<Camera> findCameraListByType(@Param("type") int type, @Param("limit") int limit);

    int getTotalCameras(PageQueryUtil pageQueryUtil);

    //修改
    int updateByPrimaryKeySelective(Camera record);

    int updateByPrimaryKey(Camera record);

    int updateCameraCategorys(@Param("categoryName") String categoryName, @Param("categoryId") Integer categoryId, @Param("ids")Integer[] ids);

    int updateCameraEnable(int cameraId);

}
