package com.de.service.impl;

import com.de.dao.CameraCategoryMapper;
import com.de.dao.CameraMapper;
import com.de.entity.Camera;
import com.de.entity.CameraCategory;
import com.de.entity.Image;
import com.de.rtsp.MediaTransfer;
import com.de.service.CameraService;
import com.de.service.WebSocketServer;
import com.de.util.*;
import com.de.vo.CameraListVO;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gs
 * @date 2020/6/11 - 13:51
 */
@Service
@Slf4j
public class CameraServiceImpl implements CameraService {

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private CameraCategoryMapper categoryMapper;

    @Autowired
    private MediaTransfer mediaTransfer;


    @Override
    @Transactional
    public String saveCamera(Camera camera) {
        CameraCategory cameraCategory = categoryMapper.selectByPrimaryKey(camera.getCameraCategoryId());
        if(cameraCategory==null){
            camera.setCameraCategoryId(0);
            camera.setCameraCategoryName("未知分类");
        }else{
            camera.setCameraCategoryName(cameraCategory.getCategoryName());
            cameraCategory.setHasCameraNum(cameraCategory.getHasCameraNum()+1);

        }
        if (cameraMapper.insertSelective(camera)>0){
            categoryMapper.updateByPrimaryKeySelective(cameraCategory);
            return "success";

        }
        return "保存失败";

    }

    @Override
    public PageResult getCamerasPage(PageQueryUtil pageUtil) {
        List<Camera> cameraList = cameraMapper.findAllCameras(pageUtil);
        int total = cameraMapper.getTotalCameras(pageUtil);
        PageResult pageResult = new PageResult(cameraList,total,pageUtil.getLimit(),pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return cameraMapper.deleteBatch(ids)>0;
    }

    @Override
    public int getTotalCameras() {
        return cameraMapper.getTotalCameras(null);
    }

    @Override
    public Camera getCameraById(int cameraId) {
        return cameraMapper.selectByPrimaryKey(cameraId);
    }

    @Override
    @Transactional
    public String updateCamera(Camera camera) {
        Camera cameraForUpdate = cameraMapper.selectByPrimaryKey(camera.getCameraId());
        if (cameraForUpdate==null){
            return "数据不存在";
        }
        cameraForUpdate.setCameraName(camera.getCameraName());
        cameraForUpdate.setCameraUrl(camera.getCameraUrl());
        cameraForUpdate.setCameraCoverImage(camera.getCameraCoverImage());
        cameraForUpdate.setCameraEnable(camera.getCameraEnable());
        cameraForUpdate.setIsDeleted(camera.getIsDeleted());

        CameraCategory cameraCategory = categoryMapper.selectByPrimaryKey(camera.getCameraCategoryId());
        if (cameraCategory == null) {
            cameraForUpdate.setCameraCategoryId(0);
            cameraForUpdate.setCameraCategoryName("默认分类");
        } else {
            //设置相机分类名称
            cameraForUpdate.setCameraCategoryName(cameraCategory.getCategoryName());
            cameraForUpdate.setCameraCategoryId(cameraCategory.getCategoryId());
            //分类的排序值加1
            cameraCategory.setHasCameraNum(cameraCategory.getHasCameraNum()+1);
        }

        //修改camera信息->修改分类排序值->删除原关系数据->保存新的关系数据
        categoryMapper.updateByPrimaryKeySelective(cameraCategory);

        if (cameraMapper.updateByPrimaryKeySelective(cameraForUpdate) > 0) {
            return "success";
        }
        return "修改失败";


    }

    @Override
    public PageResult getCamerasForIndexPage(int page) {
        Map params = new HashMap();
        params.put("page",page);
        params.put("limit",8);
        params.put("cameraStatus",1);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        List<Camera> cameraList = cameraMapper.findAllCameras(pageUtil);
        List<CameraListVO> cameraListVOS = getCameraListVOsByCameras(cameraList);
        int total = cameraMapper.getTotalCameras(pageUtil);
        PageResult pageResult = new PageResult(cameraListVOS,total,pageUtil.getLimit(),pageUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult getCamerasPageByCategory(String categoryName, int page) {
        if (PatternUtil.validKeyword(categoryName)) {
            CameraCategory cameraCategory = categoryMapper.selectByCategoryName(categoryName);
            if ("默认分类".equals(categoryName) && cameraCategory == null) {
                cameraCategory = new CameraCategory();
                cameraCategory.setCategoryId(0);
            }
            if (cameraCategory != null && page > 0) {
                Map param = new HashMap();
                param.put("page", page);
                param.put("limit", 9);
                param.put("cameraCategoryId", cameraCategory.getCategoryId());
                param.put("cameraStatus", 1);//过滤发布状态下的数据
                PageQueryUtil pageUtil = new PageQueryUtil(param);
                List<Camera> cameraList = cameraMapper.findAllCameras(pageUtil);
                List<CameraListVO> cameraListVOS = getCameraListVOsByCameras(cameraList);
                int total = cameraMapper.getTotalCameras(pageUtil);
                PageResult pageResult = new PageResult(cameraListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
                return pageResult;
            }
        }
        return null;
    }

    @Override
    public PageResult getCamerasPageBySearch(String keyword, int page) {
        if (page>0 && PatternUtil.validKeyword(keyword)){
            Map param = new HashMap();
            param.put("page",page);
            param.put("limit",9);
            param.put("keyword",keyword);
            param.put("cameraStatus",1);
            PageQueryUtil pageUtil = new PageQueryUtil(param);
            List<Camera> cameraList = cameraMapper.findAllCameras(pageUtil);

            List<CameraListVO> cameraListVOS = getCameraListVOsByCameras(cameraList);
            int total = cameraMapper.getTotalCameras(pageUtil);
            PageResult pageResult = new PageResult(cameraListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
            return pageResult;
        }
        return null;
    }

//    @Override
//    public List<SimpleCameraListVO> getCameraListForIndexPage(int type) {
//        List<SimpleCameraListVO> simpleCameraListVOS = new ArrayList<>();
//        List<Camera> cameras = cameraMapper.findCameraListByType(type,9);
//        if(!CollectionUtils.isEmpty(cameras)){
//            for (Camera camera:cameras){
//                SimpleCameraListVO simpleCameraListVO = new SimpleCameraListVO();
//                BeanUtils.copyProperties(camera,simpleCameraListVO);
//                simpleCameraListVOS.add(simpleCameraListVO);
//            }
//        }
//        return simpleCameraListVOS;
//    }


    public List<CameraListVO> getCameraListVOsByCameras(List<Camera> cameraList) {
        List<CameraListVO> cameraListVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cameraList)) {
            List<Integer> categoryIds = cameraList.stream().map(Camera::getCameraCategoryId).collect(Collectors.toList());
            Map<Integer, String> cameraCategoryMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(categoryIds)) {
                List<CameraCategory> cameraCategories = categoryMapper.selectByCategoryIds(categoryIds);
                if (!CollectionUtils.isEmpty(cameraCategories)) {
                    cameraCategoryMap = cameraCategories.stream().collect(Collectors.toMap(CameraCategory::getCategoryId, CameraCategory::getCategoryIcon, (key1, key2) -> key2));
                }
            }
            for (Camera camera : cameraList) {
                CameraListVO cameraListVO = new CameraListVO();
                BeanUtils.copyProperties(camera, cameraListVO);
                if (cameraCategoryMap.containsKey(camera.getCameraCategoryId())) {
                    cameraListVO.setCameraCategoryIcon(cameraCategoryMap.get(camera.getCameraCategoryId()));
                } else {
                    cameraListVO.setCameraCategoryId(0);
                    cameraListVO.setCameraCategoryName("默认分类");
                    cameraListVO.setCameraCategoryIcon("/admin/dist/img/category/00.png");
                }
                cameraListVOS.add(cameraListVO);
            }
        }
        return cameraListVOS;
    }

    @Override
    public MyResult cameraConnect(String cameraUrl,HttpServletRequest httpServletRequest) throws FrameGrabber.Exception {

//        return mediaTransfer.cameraTest(cameraUrl);
        return mediaTransfer.localTest(httpServletRequest);

//        try {
//            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(cameraUrl);
//            if(grabber!=null){
//                return ResultGenerator.genSuccessResult();
//            }
//            else{
//                return ResultGenerator.genFailResult("相机路径无法连接");
//            }
//        } catch (FrameGrabber.Exception e) {
//            log.error("创建解析rtsp FFmpegFrameGrabber 失败");
//            log.error("create rtsp FFmpegFrameGrabber exception: ", e);
//            return ResultGenerator.genFailResult("相机路径无法连接");
//        }
    }



}
