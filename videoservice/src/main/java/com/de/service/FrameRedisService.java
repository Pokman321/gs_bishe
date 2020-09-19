package com.de.service;

import org.bytedeco.javacv.FFmpegFrameGrabber;

/**
 * @author gs
 * @date 2020/6/30 - 22:20
 */
public interface FrameRedisService {
    FFmpegFrameGrabber createGrabber(String path);
    String startCameraPush(FFmpegFrameGrabber grabber, String cameraId, String pathUrl);
}
