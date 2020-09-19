package com.de.rtsp;
//import org.bytedeco.javacv.*;
//import org.bytedeco.javacv.Frame;
import com.de.config.Constants;
import com.de.util.MyCameraUtils;
import org.bytedeco.javacpp.opencv_core;
import com.de.entity.Image;
import com.de.service.WebSocketServer;
import com.de.util.MyResult;
import com.de.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

/**
 *  
 *  * @projectName videoservice
 *  * @title     MediaUtils   
 *  * @package    com.de.rtsp  
 *  * @description   获取rtsp流，解析为视频帧，websocket传递到前台显示 
 *  * @author IT_CREAT     
 *  * @date  2020 2020/4/12 0012 下午 18:24  
 *  * @version V1.0.0 
 *  
 */
@Slf4j
@Component
@EnableAsync
public class MediaTransfer {

    @Value("${rtsp.url}")
    private String rtspUrl;

    @Value("${rtsp.transport.type}")
    private String rtspTransportType;

    private static FFmpegFrameGrabber grabber;

    private static OpenCVFrameGrabber local_grabber;

    private static boolean isStart = false;

    /**
     * 视频帧率
     */
    public static int frameRate = 24;
    /**
     * 视频宽度
     */
    public static int frameWidth = 480;
    /**
     * 视频高度
     */
    public static int frameHeight = 270;

//    public void byte2image(byte[] data,String path){
//        if(data.length<3||path.equals("")) return;
//        try{
//            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
//            imageOutput.write(data, 0, data.length);
//            imageOutput.close();
//            System.out.println("Make Picture success,Please find image in " + path);
//        } catch(Exception ex) {
//            System.out.println("Exception: " + ex);
//            ex.printStackTrace();
//        }
//    }
//    public static BufferedImage mat2BufImg (opencv_core.Mat matrix) {
//        opencv_core.Mat tempMat=matrix;
////        cvtColor(matrix,tempMat,COLOR_BGRA2RGBA);//先要转bgra->rgba
//        OpenCVFrameConverter.ToMat openCVConverter = new OpenCVFrameConverter.ToMat();
//        Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
//        return java2DConverter.convert(openCVConverter.convert(tempMat));
//    }
//
//    public static byte[] bufImg2Bytes(BufferedImage original){
//        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(original, "png", bStream);
//        } catch (IOException e) {
//            throw new RuntimeException("bugImg读取失败:"+e.getMessage(),e);
//        }
//        return bStream.toByteArray();
//    }
//
//
//    public MyResult localTest() throws FrameGrabber.Exception, InterruptedException {
//        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//        System.out.println("111111111111111111111");
//        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
//        grabber.start();   //开始获取摄像头数据
//        System.out.println(grabber);
//        CanvasFrame canvas = new CanvasFrame("摄像头");//新建一个窗口
//        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        canvas.setAlwaysOnTop(false);
////        int ex=0;
//        while (true) {
//            if (!canvas.isDisplayable()) {//窗口是否关闭
//                grabber.stop();//停止抓取
//                System.exit(-1);//退出
//            }
//
//            Frame frame = grabber.grab();
//
//            canvas.showImage(grabber.grabFrame());//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
//            org.bytedeco.javacpp.opencv_core.Mat mat_new = converter.convertToMat(grabber.grabFrame());
//
//            BufferedImage bufferedImage = mat2BufImg(mat_new);
//            //将mat转为Byte[]
//            byte[] bytes1 = bufImg2Bytes(bufferedImage);
//
//            //byte[]转为图片
////            byte2image(bytes1,"E:\\Liao_java\\new_camera_test\\image_res\\7.jpg");
//
//            final Base64.Decoder decoder = Base64.getDecoder();
//            final Base64.Encoder encoder = Base64.getEncoder();
//
//            //将byte[]转为base64的字符串
//            String encodedText = encoder.encodeToString(bytes1);
//
//            //将base64解码为byte[]
//            byte[] new_byte = decoder.decode(encodedText);
//
//            //byte[]转为图片
//            byte2image(new_byte,"E:\\Liao_java\\new_camera_test\\lib\\7.jpg");
//
////
//            Thread.sleep(100);//50毫秒刷新一次图像
//        }
//
//    }

    public void byte2image(byte[] data,String path){
        if(data.length<3||path.equals("")) return;
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }

    public MyResult localTest(HttpServletRequest httpServletRequest) throws FrameGrabber.Exception {
        System.out.println();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        System.out.println("================未阻塞===============");
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();

        try {
            grabber.setFormat("mp4");
            grabber.setOption("stimoout", "5*1000*1000");
            grabber.start();
            Frame frame = grabber.grabFrame();
            System.out.println("输出了结果");
            int cnt_frame = 0;
            while (null == frame) {
                frame = grabber.grabFrame();
                cnt_frame++;
                if(cnt_frame==3){
                    return ResultGenerator.genFailResult("无法获取到相机下的图像");
                }
            }
            BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
            byte[] bytes = imageToBytes(bufferedImage, "jpg");

            MyResult result = ResultGenerator.genSuccessResult();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Random r = new Random();
            StringBuilder tempName = new StringBuilder();
            tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(".jpg");
            String newFileName = tempName.toString();
            assert bytes != null;
            byte2image(bytes,Constants.FILE_UPLOAD_DIC+newFileName);

            result.setData(MyCameraUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);

            grabber.stop();
            return result;
            //使用websocket发送视频帧数据
//            WebSocketServer.sendAllByObject(new Image(bytes));
        } catch (FrameGrabber.Exception | RuntimeException | URISyntaxException e) {
            log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
            log.error("exception : " , e);

            try {
                grabber.stop();
                return ResultGenerator.genFailResult("无法连接成功");
            } catch (FrameGrabber.Exception ex) {
                log.error("grabber stop exception: ", ex);
            } finally {
//                grabber.stop();
                grabber = null;
                isStart = false;

            }
        } finally {
            grabber.stop();
        }

        return ResultGenerator.genFailResult("其他未名错误");

    }

    public MyResult cameraTest(String cameraUrl){
        FFmpegFrameGrabber grabber = createGrabber(cameraUrl);
        int cnt=0;
        while (grabber == null) {
            if(cnt==3){
                return ResultGenerator.genFailResult("无法创建grabber");
            }
            log.info("重试连接rstp："+cameraUrl+",开始创建grabber");
            grabber = createGrabber(rtspUrl);
            cnt++;
        }


        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();


        try {
//            if (!isStart) {
//                grabber.setFormat("mp4");
//                grabber.start();
//                isStart = true;
//                log.info("启动grabber成功");
//            }
            grabber.setFormat("mp4");
            grabber.setOption("stimoout", "5*1000*1000");
            grabber.start();
            Frame frame = grabber.grabImage();
            System.out.println("输出了结果");
            int cnt_frame = 0;
            while (null == frame) {
                frame = grabber.grabImage();
                cnt_frame++;
                if(cnt_frame==3){
                    return ResultGenerator.genFailResult("无法获取到相机下的图像");
                }
            }
            BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
            byte[] bytes = imageToBytes(bufferedImage, "jpg");

            MyResult result = ResultGenerator.genSuccessResult();
            result.setData(bytes);

            return result;
            //使用websocket发送视频帧数据
//            WebSocketServer.sendAllByObject(new Image(bytes));
        } catch (FrameGrabber.Exception | RuntimeException e) {
            log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
            log.error("exception : " , e);

            try {
                grabber.stop();
            } catch (FrameGrabber.Exception ex) {
                log.error("grabber stop exception: ", ex);
            } finally {
                grabber = null;
                isStart = false;

            }
        }

        return ResultGenerator.genFailResult("其他未名错误");
    }


    /**
     * 开启获取rtsp流，通过websocket传输数据
     */
    @Async
    public void live() {
        log.info("连接rtsp："+rtspUrl+",开始创建grabber");
        grabber = createGrabber(rtspUrl);
        if (grabber != null) {
            log.info("创建grabber成功");
        } else {
            log.info("创建grabber失败");
        }
        startCameraPush();
    }

//    public OpenCVFrameGrabber createGrabber(String rtsp) {
//        // 获取视频源
//        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
//        grabber.setOption("rtsp_transport", rtspTransportType);
//        //设置帧率
//        grabber.setFrameRate(frameRate);
//        //设置获取的视频宽度
//        grabber.setImageWidth(frameWidth);
//        //设置获取的视频高度
//        grabber.setImageHeight(frameHeight);
//        //设置视频bit率
////            grabber.setFrameRate(24);
//        grabber.setVideoBitrate(2000000);
////            grabber.setVideoBitrate(10);
//        return grabber;
//    }

    /**
     * 构造视频抓取器
     *
     * @param rtsp 拉流地址
     * @return
     */
    public FFmpegFrameGrabber createGrabber(String rtsp) {
        // 获取视频源
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtsp);
            grabber.setOption("rtsp_transport", rtspTransportType);
            //设置帧率
            grabber.setFrameRate(frameRate);
            //设置获取的视频宽度
            grabber.setImageWidth(frameWidth);
            //设置获取的视频高度
            grabber.setImageHeight(frameHeight);
            //设置视频bit率
//            grabber.setFrameRate(24);
            grabber.setVideoBitrate(2000000);
//            grabber.setVideoBitrate(10);
            return grabber;
        } catch (FrameGrabber.Exception e) {
            log.error("创建解析rtsp FFmpegFrameGrabber 失败");
            log.error("create rtsp FFmpegFrameGrabber exception: ", e);
            return null;
        }
    }

    /**
     * 推送图片（摄像机直播）
     */
    public void startCameraPush() {
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        while (true) {
            if (grabber == null) {
                log.info("重试连接rtsp："+rtspUrl+",开始创建grabber");
                grabber = createGrabber(rtspUrl);
                log.info("创建grabber成功");
            }
            try {
                if (grabber != null && !isStart) {
                    grabber.setFormat("mp4");
                    grabber.start();
                    isStart = true;
                    log.info("启动grabber成功");
                }
                if (grabber != null) {
                    Frame frame = grabber.grabImage();
                    if (null == frame) {
                        continue;
                    }
                    BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
                    byte[] bytes = imageToBytes(bufferedImage, "jpg");
                    //使用websocket发送视频帧数据
                    WebSocketServer.sendAllByObject(new Image(bytes));
                }
            } catch (FrameGrabber.Exception | RuntimeException e) {
                log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
                log.error("exception : " , e);
                if (grabber != null) {
                    try {
                        grabber.stop();
                    } catch (FrameGrabber.Exception ex) {
                        log.error("grabber stop exception: ", ex);
                    } finally {
                        grabber = null;
                        isStart = false;
                    }
                }
            }
        }
    }

    /**
     * 图片转字节数组
     *
     * @param bImage 图片数据
     * @param format 格式
     * @return 图片字节码
     */
    private byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            log.error("bufferImage 转 byte 数组异常");
            log.error("bufferImage transfer byte[] exception: ", e);
            return null;
        }
        return out.toByteArray();
    }

}
