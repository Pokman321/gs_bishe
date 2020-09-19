package com.de.service;

import org.bytedeco.javacv.FFmpegFrameGrabber;

/**
 * @author gs
 * @date 2020/6/20 - 4:18
 */
public interface VideoFramService {

    public FFmpegFrameGrabber createGrabber(String path);

    public String startVideoPush(FFmpegFrameGrabber grabber, String path);

}
