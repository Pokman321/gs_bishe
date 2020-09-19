//package com.de.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.de.service.FrameRedisService;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.FrameGrabber;
//import org.bytedeco.javacv.Java2DFrameConverter;
//import org.bytedeco.opencv.opencv_core.IplImage;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfByte;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//
//import javax.annotation.Resource;
//import javax.imageio.ImageIO;
//import javax.imageio.stream.FileImageOutputStream;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Base64;
//import java.util.Date;
//import java.util.concurrent.ConcurrentLinkedDeque;
//import java.util.concurrent.LinkedBlockingDeque;
//
////import org.opencv.core.Mat;
////import com.alibaba.fastjson.JSONObject;
//
///**
// * @author gs
// * @date 2020/6/23 - 13:22
// */
//@Slf4j
//@Service
//public class FrameRedisServiceImpl_bak implements FrameRedisService {
//
//
//    @Value("${rtsp.mot_url}")
//    private String mot_url;
//
////    private Jedis jedis = new Jedis(mot_url,6379);
//
//    @Value("${rtsp.transport.type}")
//    private String rtspTransportType;
//
//    @Value("$save_dir")
//    private String save_dir;
//
//    private static boolean isStart = false;
//
////    private PersonMapper personMapper;
//
//    /**
//     * 视频帧率
//     */
//    public static int frameRate = 24;
//    /**
//     * 视频宽度
//     */
//    public static int frameWidth = 480;
//    /**
//     * 视频高度
//     */
//    public static int frameHeight = 270;
//
//    final static Base64.Decoder decoder = Base64.getDecoder();
//    final static Base64.Encoder encoder = Base64.getEncoder();
//
////    public static ConcurrentLinkedDeque<String> time_list = new ConcurrentLinkedDeque<>();
//
//    public static LinkedBlockingDeque<String> time_list = new LinkedBlockingDeque<>();
//
//    public static class RedisSave implements Runnable{
//
//        @Resource(name = "jedis_save")
//        Jedis jedis_save;
//
//        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
////        List<String> list = new ArrayList<>();
//
//        FFmpegFrameGrabber grabber;
//        String cameraId;
//        String pathUrl;
//
//
//
//        public RedisSave(FFmpegFrameGrabber grabber, String cameraId, String pathUrl){
//            super();
//            this.grabber = grabber;
//            this.cameraId = cameraId;
//            this.pathUrl = pathUrl;
//        }
//
//        @Override
//        public void run() {
//            while (true)  {
////                if (grabber == null) {
////                    log.info("重试连接rtsp：" + pathUrl + ",开始创建grabber");
////                    grabber = createGrabber(pathUrl);
////                    log.info("创建grabber成功");
////                }
//                try {
//                    if (grabber != null && !isStart) {
//                        grabber.setFormat("mp4");
//                        grabber.start();
//                        isStart = true;
//                        log.info("启动grabber成功");
//                    }
//                    if (grabber != null) {
//                        Frame frame = grabber.grabImage();
//                        if (null == frame) {
//                            continue;
//                        }
//                        BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
//                        byte[] bytes1 = bufImg2Bytes(bufferedImage, "jpg");
//                        String name = timestamp();
//
//                        String encodeText = encoder.encodeToString(bytes1);
//
//                        jedis_save.hset("hash" + cameraId, name, encodeText);
//                        jedis_save.lpush("list" + cameraId, name);
//                        synchronized (RedisSave.class){
//                            time_list.put(name);
//                        }
//
////                        list.add(name);
//
//                    }
//                } catch (FrameGrabber.Exception | RuntimeException | InterruptedException e) {
//                    log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
//                    log.error("exception : ", e);
//                    try {
//                        grabber.stop();
//                        jedis_save.close();
//
//                    } catch (FrameGrabber.Exception ex) {
//                        log.error("grabber stop exception: ", ex);
//                    } finally {
//                        grabber = null;
//                        isStart = false;
//                    }
//                }
//            }
//        }
//
//    }
//
//    public static class DrawandSave implements Runnable{
//
//        @Override
//        public void run() {
//
//        }
//    }
//
//
//    public static class RedisGet implements Runnable{
//
//        @Resource(name = "jedis_get")
//        private Jedis jedis_get;
//        private String cameraId;
////        private Base64.Decoder decoder = Base64.getDecoder();
//
//        public RedisGet(String cameraId){
//            this.cameraId = cameraId;
//        }
////        private final RedisGet redisGet = new RedisGet();
//        @SneakyThrows
//        @Override
//        public void run() {
//
////            List<String> result_list = jedis_get.brpop(0,"result0");
////
////
////
////            synchronized (RedisGet.class){
////                String result =  jedis_get.hget("result"+cameraId,time_list.getFirst());
////                while(result==null){
////                    RedisGet.class.wait();
////                }
////                time_list.
////            }
//
//            String pic;
//            String mot_result;
//            String cur_name;
//            synchronized (RedisGet.class){
//                time_list.takeFirst()
//                while(time_list.isEmpty() || jedis_get.hget("result"+cameraId,time_list.getFirst())==null){
//
//                    RedisGet.class.wait();
//                }
//                cur_name = time_list.getFirst();
//                mot_result = jedis_get.hget("result" + cameraId, cur_name);
//                pic = jedis_get.hget("hash"+cameraId, cur_name);
//                jedis_get.hdel("hash"+cameraId, cur_name);
//                jedis_get.blpop(0,"list"+cameraId);
//                jedis_get.hdel("result"+cameraId, cur_name);
//                RedisGet.class.notifyAll();
//            }
//
//            JSONObject resultMap = JSONObject.parseObject(mot_result);
//
//
////            for(Object entry:resultMap.entrySet())
//
//            byte[] pic_byte = decoder.decode(pic);
//            Mat mat = Imgcodecs.imdecode(new MatOfByte(pic_byte), Imgcodecs.IMREAD_ANYCOLOR);
//            IplImage matImage = new IplImage(mat);
//
//            new Thread(new DrawandSave()).start();
//
//            byte2image();
//
//
//
//        }
//    }
//
//
//    public FFmpegFrameGrabber createGrabber(String path) {
//        // 获取视频源
//        try {
//            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(path);
//            frameWidth =  grabber.getImageWidth();
//            frameRate = (int) grabber.getFrameRate();
//            frameHeight = grabber.getImageHeight();
//
//
//            grabber.setOption("rtsp_transport", rtspTransportType);
//            //设置帧率
//            grabber.setFrameRate(frameRate);
//            //设置获取的视频宽度
//            grabber.setImageWidth(frameWidth);
//            //设置获取的视频高度
//            grabber.setImageHeight(frameHeight);
//            //设置视频bit率
////            grabber.setFrameRate(24);
//            grabber.setVideoBitrate(2000000);
////            grabber.setVideoBitrate(10);
//            return grabber;
//        } catch (FrameGrabber.Exception e) {
//            log.error("创建解析rtsp FFmpegFrameGrabber 失败");
//            log.error("create rtsp FFmpegFrameGrabber exception: ", e);
//            return null;
//        }
//    }
//
//    public String startCameraPush(FFmpegFrameGrabber grabber, String cameraId, String pathUrl) {
//
//        RedisSave redisSave = new RedisSave(grabber, cameraId, pathUrl);
//        new Thread(redisSave).start();
//
//    }
//
//
//
//
//    public static byte[] bufImg2Bytes(BufferedImage original,String format){
//        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(original, format, bStream);
//        } catch (IOException e) {
//            throw new RuntimeException("bugImg读取失败:"+e.getMessage(),e);
//        }
//        return bStream.toByteArray();
//    }
//
//    private static String timestamp(){
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        Date now = new Date();
//        return timeFormat.format(now);
//    }
//
//    private static void byte2image(byte[] data,String path){
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
//
//}
