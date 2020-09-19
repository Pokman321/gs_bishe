package com.de.service;


import com.de.entity.Camera;
import com.de.util.MyResult;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;
import org.bytedeco.javacv.FrameGrabber;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gs
 * @date 2020/6/10 - 15:04
 */
public interface CameraService {
    String saveCamera(Camera camera);

    PageResult getCamerasPage(PageQueryUtil pageUtil);

    Boolean deleteBatch(Integer[] ids);

    int getTotalCameras();

    Camera getCameraById(int cameraId);

    String updateCamera(Camera camera);

    PageResult getCamerasForIndexPage(int page);

    PageResult getCamerasPageByCategory(String categoryName,int page);

    PageResult getCamerasPageBySearch(String keyword,int page);

    MyResult cameraConnect(String cameraUrl, HttpServletRequest httpServletRequest) throws FrameGrabber.Exception;

//    List<SimpleCameraListVO> getCameraListForIndexPage(int type);
//    List<CameraListVO> getCameraListVOsByCameras(List<Camera> cameraList);

}
