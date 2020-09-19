package com.de.service.impl;

import com.de.dao.AdminUserMapper;
import com.de.dao.CameraCategoryMapper;
import com.de.dao.VideoMapper;
import com.de.entity.AdminUser;
import com.de.entity.CameraCategory;
import com.de.entity.UpdateVideo;
import com.de.service.DoMOTService;
import com.de.service.VideoService;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;
import com.de.util.PatternUtil;
import com.de.vo.SimpleVideoListVO;
import com.de.vo.VideoDetailVO;
import com.de.vo.VideoListVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gs
 * @date 2020/7/14 - 1:31
 */
@Service
@EnableAsync
public class VideoServiceImpl implements VideoService {

    private static final int PAGE_LIMIT = 10;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CameraCategoryMapper cameraCategoryMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private DoMOTService doMOTService;

    @Override
    @Transactional
    public String saveVideo(UpdateVideo updateVideo) {
        CameraCategory cameraCategory = cameraCategoryMapper.selectByPrimaryKey(updateVideo.getCameraCategoryId());
        if(cameraCategory==null){
            updateVideo.setCameraCategoryId(0);
            updateVideo.setCameraCategoryName("默认分类");
        }else{
            updateVideo.setCameraCategoryName(cameraCategory.getCategoryName());
            cameraCategory.setHasVideoNum(cameraCategory.getHasVideoNum()+1);
        }
        //保存文章


        if (videoMapper.insertSelective(updateVideo)>0){
            cameraCategoryMapper.updateByPrimaryKeySelective(cameraCategory);
            return "success";

        }
        return "保存失败";


    }

    @Override
    @Async
    public String motAndSave(UpdateVideo updateVideo) {
        String videoPath = updateVideo.getVideoPath();

        return null;
    }

    @Override
    public PageResult getVideosPage(PageQueryUtil pageQueryUtil) {
        List<UpdateVideo> videoList = videoMapper.findVideoList(pageQueryUtil);
        int total = videoMapper.getTotalVideos(pageQueryUtil);
        PageResult pageResult = new PageResult(videoList, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult getVideosPageByKeyWord(PageQueryUtil pageQueryUtil) {
        List<UpdateVideo> videoList = videoMapper.findVideoListByKeyWord(pageQueryUtil);
        int num = videoList.size();
        PageResult pageResult = new PageResult(videoList,num,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return videoMapper.deleteBatch(ids)>0;

    }

    @Override
    public Boolean deleteByPrimaryKey(Integer id) {
        return videoMapper.deleteByPrimaryKey(id)>0;
    }

    @Override
    public int getTotalVideos() {
        return videoMapper.getTotalVideos(null);
//        return 0;
    }

    @Override
    public UpdateVideo getVideoById(int videoId) {
        return videoMapper.selectByPrimaryKey(videoId);
    }

    @Override
    public String updateVideo(UpdateVideo updateVideo) {
        UpdateVideo videoForUpdate = videoMapper.selectByPrimaryKey(updateVideo.getVideoId());

        if (videoForUpdate == null) {
            return "数据不存在";
        }
        videoForUpdate.setVideoName(updateVideo.getVideoName());
        videoForUpdate.setVideoPath(updateVideo.getVideoPath());
        videoForUpdate.setVideoTime(updateVideo.getVideoTime());
        videoForUpdate.setVideoCoverImage(updateVideo.getVideoCoverImage());
        videoForUpdate.setResultPath(updateVideo.getResultPath());
        videoForUpdate.setResultTime(updateVideo.getResultTime());
        videoForUpdate.setHasResult(updateVideo.getHasResult());
        videoForUpdate.setIsShow(updateVideo.getIsShow());
        CameraCategory cameraCategory = cameraCategoryMapper.selectByPrimaryKey(updateVideo.getCameraCategoryId());
        if (cameraCategory == null) {
            videoForUpdate.setCameraCategoryId(0);
            videoForUpdate.setCameraCategoryName("默认分类");
        } else {
            //设置博客分类名称
            videoForUpdate.setCameraCategoryName(cameraCategory.getCategoryName());
            videoForUpdate.setCameraCategoryId(cameraCategory.getCategoryId());
            //分类的排序值加1
            cameraCategory.setHasVideoNum(cameraCategory.getHasVideoNum() + 1);
        }
        cameraCategoryMapper.updateByPrimaryKeySelective(cameraCategory);

        if (videoMapper.updateByPrimaryKeySelective(videoForUpdate) > 0) {
            return "success";
        }
        return "修改失败";
    }

    public PageResult getOwnVideoForIndexPage(int page,String userName){
        Map params = new HashMap();
        params.put("page",page);
        params.put("userName",userName);
        params.put("limit",PAGE_LIMIT);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<UpdateVideo> videoList = videoMapper.findOwnVideoList(pageUtil);
        List<VideoListVO> videoListVOS = getVideoListVOsByVideos(videoList);
        int total = videoMapper.getTotalVideos(pageUtil);
        PageResult pageResult = new PageResult(videoListVOS,total,pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }


    @Override
    public PageResult getVideosForIndexPage(int page,String userName) {
        Map<String,Object> params1 = new HashMap<>();
        Map<String,Object> params2 = new HashMap<>();
        params1.put("page",page);
        params1.put("limit",PAGE_LIMIT);
        params1.put("isShow",1);
        params1.put("userName","***");
        params2.put("page",page);
        params2.put("limit",PAGE_LIMIT);
        params2.put("userName","123");
        PageQueryUtil pageUtil1 = new PageQueryUtil(params1);
        PageQueryUtil pageUtil2 = new PageQueryUtil(params2);

        List<UpdateVideo> videoList1 = videoMapper.findVideoList(pageUtil1);
        List<UpdateVideo> videoList2 = videoMapper.findVideoList(pageUtil2);

        videoList1.addAll(videoList2);
        List<VideoListVO> videoListVOS = getVideoListVOsByVideos(videoList1);
        int total = videoMapper.getTotalVideos(pageUtil1)+videoMapper.getTotalVideos(pageUtil2);
        PageResult pageResult = new PageResult(videoListVOS,total,pageUtil1.getLimit(), pageUtil1.getPage());
        return pageResult;
    }

    private List<VideoListVO> getVideoListVOsByVideos(List<UpdateVideo> videoList){
        List<VideoListVO> videoListVOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(videoList)){
            List<Integer> categoryIds = videoList.stream().map(UpdateVideo::getCameraCategoryId).collect(Collectors.toList());
            Map<Integer,String> videoCategoryMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(categoryIds)){
                List<CameraCategory> cameraCategories = cameraCategoryMapper.selectByCategoryIds(categoryIds);
                if (!CollectionUtils.isEmpty(cameraCategories)) {
                    videoCategoryMap = cameraCategories.stream().collect(Collectors.toMap(CameraCategory::getCategoryId, CameraCategory::getCategoryIcon, (key1, key2) -> key2));
                }
            }
            for(UpdateVideo updateVideo:videoList){
                VideoListVO videoListVO = new VideoListVO();
                BeanUtils.copyProperties(updateVideo,videoListVO);
                if (videoCategoryMap.containsKey(updateVideo.getCameraCategoryId())) {
                    videoListVO.setCameraCategoryIcon(videoCategoryMap.get(updateVideo.getCameraCategoryId()));
                } else {
                    videoListVO.setCameraCategoryId(0);
                    videoListVO.setCameraCategoryName("默认分类");
                    videoListVO.setCameraCategoryIcon("/admin/dist/img/category/00.png");
                }


                AdminUser adminUser = adminUserMapper.selectByName(updateVideo.getUserName());
                if (adminUser==null){
                    System.out.println("存在非法用户侵入,用户姓名为"+updateVideo.getUserName());
                }
                else{
                    videoListVO.setUserAvatar(adminUser.getUserAvatar());
                }

                videoListVOS.add(videoListVO);
            }
        }
        return videoListVOS;
    }


    @Override
    public List<SimpleVideoListVO> getVideoListForIndexPage(int type) {

        List<SimpleVideoListVO> simpleVideoListVOS = new ArrayList<>();
        List<UpdateVideo> updateVideos = videoMapper.findVideoListByType(type,9);
        if(!CollectionUtils.isEmpty(updateVideos)){
            for(UpdateVideo video:updateVideos){
                SimpleVideoListVO simpleVideoListVO = new SimpleVideoListVO();
                BeanUtils.copyProperties(video,simpleVideoListVO);
                simpleVideoListVOS.add(simpleVideoListVO);
            }
        }

        return simpleVideoListVOS;
    }

    @Override
    public VideoDetailVO getVideoDetail(int videoId) {
        UpdateVideo updateVideo = videoMapper.selectByPrimaryKey(videoId);
        return getVideoDetailVO(updateVideo);

    }

    @Override
    public PageResult getVideosPageByCategory(String categoryName, int page) {
        if (PatternUtil.validKeyword(categoryName)) {
            CameraCategory cameraCategory = cameraCategoryMapper.selectByCategoryName(categoryName);
            if ("默认分类".equals(categoryName) && cameraCategory == null) {
                cameraCategory = new CameraCategory();
                cameraCategory.setCategoryId(0);
            }
            if (cameraCategory != null && page > 0) {
                Map param = new HashMap();
                param.put("page", page);
                param.put("limit", 9);
                param.put("cameraCategoryId", cameraCategory.getCategoryId());
//                param.put("blogStatus", 1);//过滤发布状态下的数据
                PageQueryUtil pageUtil = new PageQueryUtil(param);
                List<UpdateVideo> videoList = videoMapper.findVideoList(pageUtil);
                List<VideoListVO> videoListVOS = getVideoListVOsByVideos(videoList);
                int total = videoMapper.getTotalVideos(pageUtil);
                PageResult pageResult = new PageResult(videoListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
                return pageResult;
            }
        }
        return null;
    }

    @Override
    public PageResult getVideosPageBySearch(String keyword, int page) {
        if (page > 0 && PatternUtil.validKeyword(keyword)) {
            Map param = new HashMap();
            param.put("page", page);
            param.put("limit", 9);
            param.put("keyword", keyword);
//            param.put("blogStatus", 1);//过滤发布状态下的数据
            PageQueryUtil pageUtil = new PageQueryUtil(param);
            List<UpdateVideo> videoList = videoMapper.findVideoList(pageUtil);
            List<VideoListVO> videoListVOS = getVideoListVOsByVideos(videoList);
            int total = videoMapper.getTotalVideos(pageUtil);
            PageResult pageResult = new PageResult(videoListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }

    private VideoDetailVO getVideoDetailVO(UpdateVideo updateVideo) {
        if (updateVideo != null) {
            //增加浏览量
            updateVideo.setVideoViews(updateVideo.getVideoViews() + 1);
            videoMapper.updateByPrimaryKeySelective(updateVideo);
            VideoDetailVO videoDetailVO = new VideoDetailVO();
            BeanUtils.copyProperties(updateVideo, videoDetailVO);
//            videoDetailVO.setVideoContent(MarkDownUtil.mdToHtml(videoDetailVO.getVideoContent()));
            CameraCategory videoCategory = cameraCategoryMapper.selectByPrimaryKey(updateVideo.getCameraCategoryId());
            if (videoCategory == null) {
                videoCategory = new CameraCategory();
                videoCategory.setCategoryId(0);
                videoCategory.setCategoryName("默认分类");
                videoCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            //分类信息
            videoDetailVO.setCameraCategoryIcon(videoCategory.getCategoryIcon());
//            if (!StringUtils.isEmpty(video.getVideoTags())) {
//                //标签设置
//                List<String> tags = Arrays.asList(video.getVideoTags().split(","));
//                videoDetailVO.setVideoTags(tags);
//            }
            //设置评论数
//            Map params = new HashMap();
//            params.put("videoId", video.getVideoId());
//            params.put("commentStatus", 1);//过滤审核通过的数据
//            videoDetailVO.setCommentCount(videoCommentMapper.getTotalVideoComments(params));
            return videoDetailVO;
        }
        return null;
    }
}
