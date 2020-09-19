package com.de.dao;

import com.de.entity.UpdateVideo;
import com.de.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gs
 * @date 2020/7/14 - 1:31
 */
@Repository
public interface VideoMapper {

    //删除
    int deleteByPrimaryKey(int videoId);

    int deleteBatch(Integer[] ids);

    //增加

//    int insertVideos(UpdateVideo updateVideo);

    int insertSelective(UpdateVideo updateVideo);

    int updateByPrimaryKeySelective(UpdateVideo updateVideo);

//    int updateByPrimaryKey(UpdateVideo updateVideo);

//    List<UpdateVideo> findAllVideos(PageQueryUtil pageUtil);


    List<UpdateVideo> findVideoList(PageQueryUtil pageUtil);

    List<UpdateVideo> findVideoListByKeyWord(PageQueryUtil pageUtil);

    List<UpdateVideo> findOwnVideoList(PageQueryUtil pageUtil);

    List<UpdateVideo> findVideoListByType(@Param("type") int type, @Param("limit") int limit);

    int getTotalVideos(PageQueryUtil pageUtil);

    int getUserTotalVideos(PageQueryUtil pageUtil);


    UpdateVideo selectByPrimaryKey(int videoId);


    int updateCameraCategorys(@Param("categoryName") String categoryName, @Param("categoryId") Integer categoryId, @Param("ids")Integer[] ids);



}
