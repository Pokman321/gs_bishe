//package com.de.service.impl;
//
//import com.de.dao.PersonMapper;
//import com.de.entity.Person;
//import com.de.service.DoMOTService;
//import com.de.service.VideoFramService;
//import lombok.extern.slf4j.Slf4j;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.FrameGrabber;
//import org.bytedeco.javacv.Java2DFrameConverter;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author gs
// * @date 2020/6/20 - 9:36
// */
//@Slf4j
//@Service
//public class VideoFrameServiceImpl implements VideoFramService {
//
//    private static final int CORE_POOL_SIZE=5;
//    private static final int MAX_POOL_SIZE=10;
//    private static final int QUEUE_CAPACITY=100;
//    private static final Long KEEP_ALIVE_TIME=1L;
//
//
//    @Autowired
//    public DoMOTService doMOTService;
//
//    @Value("${rtsp.mot_url}")
//    private String mot_url;
//
//    public Jedis jedis = new Jedis("192.168.18.74",6379);
//
//    @Value("${rtsp.transport.type}")
//    private String rtspTransportType;
//
//    @Value("$save_dir")
//    private String save_dir;
//
//    private static boolean isStart = false;
//
//    private PersonMapper personMapper;
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
//    final Base64.Decoder decoder = Base64.getDecoder();
//    final Base64.Encoder encoder = Base64.getEncoder();
//
//    public List<String> time_lis = new ArrayList<>();
//
//    @Override
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
//    public static class SavePic implements Runnable{
//
//        @Override
//        public void run() {
//
//        }
//    }
//
//
//    @Override
//    public List<String> startVideoPush(FFmpegFrameGrabber grabber,String path) throws RuntimeException{
//
//        Person person = new Person();
//
//
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                CORE_POOL_SIZE,
//                MAX_POOL_SIZE,
//                KEEP_ALIVE_TIME,
//                TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
//                new ThreadPoolExecutor.CallerRunsPolicy()
//        );
//
//        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
//        Map<String,String> map = new HashMap<String,String>();
//        while (true) {
//
//            map.clear();
//
//            if (grabber == null) {
//                log.info("重试连接rtsp："+path+",开始创建grabber");
//                grabber = createGrabber(path);
//                log.info("创建grabber成功");
//            }
//            try {
//                if (grabber != null && !isStart) {
//                    grabber.setFormat("mp4");
//                    grabber.start();
//                    isStart = true;
//                    log.info("启动grabber成功");
//                }
//                if (grabber != null) {
//                    Frame frame = grabber.grabImage();
//                    if (null == frame) {
//                        break;
//                    }
//                    BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
//                    byte[] bytes = bufImg2Bytes(bufferedImage, "jpg");
//
//                    String encodedText = encoder.encodeToString(bytes);
//
//                    String name = timestamp();
//
//
//
//                    time_lis.add(name);
//
//                    redis_save(name,encodedText,jedis);
////                    map.put(name,encodedText);
//
//                    map.put("time",name);
//                    map.put("camera","0");
//                    map.put("show","1");
//
//                    JSONObject json = new JSONObject(map);
//
////                    String jsonObject = JSON.toJSONString(map);
//
//                    JSONObject jsonResult =  doMOTService.post(mot_url,json);
//
//
//
//                    Iterator<String> it = json.keys();
//
//                    while(it.hasNext()) {
//                        // 获得key
//                        String key = it.next();
//                        JSONObject value = (JSONObject) (json.get(key));
//
//                        String[] position = value.getString("position").split("#");
//                        String personid = value.getString("personId");
//
//
//
//                        person.setPersonId(Integer.parseInt(personid));
//                        person.setCameraId(Integer.parseInt(value.getString("cameraId")));
//                        person.setImageUrl(save_dir+"/"+value.getString("cameraId")+"/"+name+".jpg");
//                        person.setTime(name);
//                        person.getPosition(value.getString("position").replace("#"," "));
//
//                        personMapper.insertSelective(person);
//
//
//
//                    }
//
//
//                        String position = jsonResult.getString("position");
//                    String[] position_lis = position.split("#");
//                    int id = Integer.parseInt(jsonResult.getString("id"));
//
//
//
//                    return time_lis;
//
//
//                }
//            } catch (FrameGrabber.Exception | RuntimeException e) {
//                log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
//                log.error("exception : " , e);
//                try {
//                    grabber.stop();
//                } catch (FrameGrabber.Exception ex) {
//                    log.error("grabber stop exception: ", ex);
//                } finally {
//                    grabber = null;
//                    isStart = false;
//                }
//            }
//        }
//        return null;
//
//
//    }
//
//    public void drawBboxSave(String[] position,Jedis jedis,String time){
//        String pic = jedis.hget("hash_table",time);
//
//
//    }
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
//    private String timestamp(){
//        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        Date now = new Date();
//        return timeFormat.format(now);
//    }
//
//    public Jedis redis_save(String img_name, String img, Jedis jedis){
//        jedis.rpush("key_list", img_name);
//        jedis.hset("hash_table",img_name,img);
//        jedis.close();
//        return jedis;
//
//    }
//
//
//}
