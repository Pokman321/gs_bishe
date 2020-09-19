package com.de.service;

//import org.opencv.video.Video;

import com.de.entity.UpdateVideo;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;
import com.de.vo.SimpleVideoListVO;
import com.de.vo.VideoDetailVO;

import java.util.List;

/**
 * @author gs
 * @date 2020/7/14 - 1:31
 */
public interface VideoService {

    String saveVideo(UpdateVideo updateVideo);

    String motAndSave(UpdateVideo updateVideo);

    PageResult getVideosPage(PageQueryUtil pageQueryUtil);

    PageResult getVideosPageByKeyWord(PageQueryUtil pageQueryUtil);

    Boolean deleteBatch(Integer[] ids);

    Boolean deleteByPrimaryKey(Integer id);

    int getTotalVideos();

    UpdateVideo getVideoById(int videoId);

    String updateVideo(UpdateVideo updateVideo);


    PageResult getVideosForIndexPage(int page,String userName);


    List<SimpleVideoListVO> getVideoListForIndexPage(int type);
    /**
     * 视频详情
     *
     * @param videoId
     * @return
     */
    VideoDetailVO getVideoDetail(int videoId);




    /**
     * 根据分类获取视频列表
     *
     * @param categoryId
     * @param page
     * @return
     */
    PageResult getVideosPageByCategory(String categoryId, int page);

    /**
     * 根据搜索获取视频列表
     *
     * @param keyword
     * @param page
     * @return
     */
    PageResult getVideosPageBySearch(String keyword, int page);

    PageResult getOwnVideoForIndexPage(int page,String userName);


}
