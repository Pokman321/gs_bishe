package com.de.service.impl;

/**
 * @author gs
 * @date 2020/7/5 - 12:32
 */

import com.alibaba.fastjson.JSON;
import com.de.dao.CurCameraMapper;
import com.de.dao.PersonMapper;
import com.de.dao.VideoMapper;
import com.de.entity.CurImage;
import com.de.entity.Person;
import com.de.entity.UpdateVideo;
import com.de.service.FrameRedisService;
import com.de.util.FilesToMov;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jim2mov.core.MovieSaveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


//import org.opencv.core.Mat;
//import com.alibaba.fastjson.JSONObject;

/**
 * @author gs
 * @date 2020/6/23 - 13:22
 */
@Slf4j
@Service
//@RabbitListener(queues = "mot.direct.A")
public class FrameRedisServiceImpl implements FrameRedisService {

    @Autowired
    private static CurCameraMapper curCameraMapper;

    @Autowired
    private static PersonMapper personMapper;

    @Autowired
    private static VideoMapper videoMapper;


    private static boolean flag=true;

    private static final int THREAD_COUNTS =
            Runtime.getRuntime().availableProcessors();
    //任务队列
    private static BlockingQueue<Runnable> taskQueue
            = new ArrayBlockingQueue<>(10);
    //线程池，固定大小，有界队列
    private static ExecutorService taskExecutor =
            new ThreadPoolExecutor(THREAD_COUNTS, THREAD_COUNTS, 60,
                    TimeUnit.SECONDS, taskQueue);

    @Value("${rtsp.mot_url}")
    private static String mot_url;

//    private Jedis jedis = new Jedis(mot_url,6379);

    @Value("${rtsp.transport.type}")
    private String rtspTransportType;

    @Value("$save_dir")
    private static String save_dir;

    @Value("$video_dir")
    private String video_dir;


    private static boolean isStart = false;


    public static int frameRate = 24;

    public static int frameWidth = 480;

    public static int frameHeight = 270;

    public static int frameNum = 0;

    final static Base64.Decoder decoder = Base64.getDecoder();
    final static Base64.Encoder encoder = Base64.getEncoder();

//    public static ConcurrentLinkedDeque<String> time_list = new ConcurrentLinkedDeque<>();

    public static LinkedBlockingDeque<String> time_list = new LinkedBlockingDeque<>();

    private static CountDownLatch latch = new CountDownLatch(2);

    private static JedisPool jPool = new JedisPool(mot_url,6379);

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    private static UpdateVideo updateVideo = new UpdateVideo();

    public static class RedisSave implements Runnable{


        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
//        List<String> list = new ArrayList<>();

        FFmpegFrameGrabber grabber;
        String cameraId;
        String pathUrl;

        public RedisSave(FFmpegFrameGrabber grabber, String cameraId, String pathUrl){
            super();
            this.grabber = grabber;
            this.cameraId = cameraId;
            this.pathUrl = pathUrl;
        }

        @Override
        public void run() {
            Jedis jedis_save = jPool.getResource();
            jedis_save.auth("123456");
            while (true)  {

                try {
                    if (grabber != null && !isStart) {
                        grabber.setFormat("mp4");
                        grabber.start();
                        isStart = true;
                        log.info("启动grabber成功");
                    }
                    if (grabber != null) {
                        Frame frame = grabber.grabImage();
//                        if (frameNum==0){
//
//                        }
                        frameNum++;
                        if (null == frame) {
//                            continue;
                            flag=false;
                            break;
                        }
                        BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
                        byte[] bytes1 = bufImg2Bytes(bufferedImage, "jpg");
                        String name = timestamp();

                        String encodeText = encoder.encodeToString(bytes1);


                        jedis_save.hset("hash" + cameraId, name, encodeText);
                        jedis_save.lpush("list" + cameraId, name);
                        time_list.put(name);

                    }
                } catch (FrameGrabber.Exception | RuntimeException | InterruptedException e) {
                    log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
                    log.error("exception : ", e);
                    try {
                        grabber.stop();
                        jedis_save.close();
                    } catch (FrameGrabber.Exception ex) {
                        log.error("grabber stop exception: ", ex);
                    } finally {
                        grabber = null;
                        isStart = false;
                    }
                }finally {
                    jedis_save.close();
                }
            }
            System.out.println("存图片结束");
        }


    }



    public static class RedisGet implements Runnable{

//        @Resource(name = "jedis_get")
//        private Jedis jedis_get;
        private String cameraId;

        public RedisGet(String cameraId){
            this.cameraId = cameraId;
        }

        @SneakyThrows
        @Override
        public void run() {

            String pic_result;
            String mot_result;
            String cur_name;

            Jedis jedis_get = jPool.getResource();
            jedis_get.auth("123456");

            while(flag || !time_list.isEmpty()){

//                cur_name = time_list.takeFirst();
                cur_name = jedis_get.blpop(0,"finished"+cameraId).get(1);
                mot_result = jedis_get.hget("result" + cameraId, cur_name);
                pic_result = jedis_get.hget("resultpic"+cameraId, cur_name);
                jedis_get.hdel("hash"+cameraId, cur_name);
//                jedis_get.blpop(0,"list"+cameraId);
                jedis_get.hdel("result"+cameraId, cur_name);
                jedis_get.hdel("resultpic"+cameraId,cur_name);
//                RedisGet.class.notifyAll();

                String save_path = save_dir+"/"+cur_name+".jpg";
                if (atomicInteger.incrementAndGet()==1){

                    updateVideo.setVideoCoverImage(save_path);
                    new URI(save_path);
                    videoMapper.insertSelective(updateVideo);
                }
                int person_num = 0;
//            JSONObject resultMap = JSONObject.parseObject(mot_result);
                if(!cameraId.equals("0")){
                    Map maps = (Map) JSON.parse(mot_result);

                    for (Object map : maps.entrySet()){
                        person_num++;
                        Person person = new Person();
                        person.setPersonId(Integer.parseInt ((String) ((Map.Entry)map).getKey()));
                        person.setPosition((String) ((Map.Entry)map).getKey());
                        person.setTime(Long.parseLong(cur_name));
                        person.setCameraId(Integer.parseInt(cameraId));
                        person.setImageUrl(save_path);
                        personMapper.savePerson(person);
                    }

                    byte[] pic_byte = decoder.decode(pic_result);
                    byte2image(pic_byte,save_path);
                    CurImage curImage = new CurImage();
                    curImage.setCameraId(Integer.parseInt(cameraId));
                    curImage.setPersonNum(person_num);
                    curImage.setImageUrl(save_path);
                    curImage.setImageTime(Long.parseLong(cur_name));

                    curCameraMapper.addImage("camera"+cameraId,curImage);

                }



            }

            System.out.println(Thread.currentThread()+"存结果结束");



        }
    }


    public FFmpegFrameGrabber createGrabber(String path) {
        // 获取视频源
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(path);
            frameWidth =  grabber.getImageWidth();
            frameRate = (int) grabber.getFrameRate();
            frameHeight = grabber.getImageHeight();


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

//    @RabbitHandler
    public String startCameraPush(FFmpegFrameGrabber grabber, String cameraId, String pathUrl) {

        long start = System.currentTimeMillis();

        RedisSave redisSave = new RedisSave(grabber, cameraId, pathUrl);
        new Thread(redisSave).start();
        RedisGet redisGet = new RedisGet(cameraId);
        for(int i=0;i<8;i++){
            taskExecutor.execute(redisGet);
        }
//        while(flag && time_list.isEmpty())
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("一共解析出"+frameNum+"张图片，共用时"+(System.currentTimeMillis()-start));
        System.out.println("开始生成视频");
        FilesToMov filesToMov = new FilesToMov(time_list,save_dir,video_dir);

        String video_result="";

        try {
            video_result = filesToMov.Trans2Mov();
//            if(video_result.equals("fail")){
//                return "fail";
//            }
        } catch (MovieSaveException e) {
            System.out.println("转换视频失败");
            e.printStackTrace();
        }

        return video_result;
    }


    public static byte[] bufImg2Bytes(BufferedImage original,String format){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(original, format, bStream);
        } catch (IOException e) {
            throw new RuntimeException("bugImg读取失败:"+e.getMessage(),e);
        }
        return bStream.toByteArray();
    }

    private static String timestamp(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date now = new Date();
        return timeFormat.format(now);
    }

    private static void byte2image(byte[] data,String path){
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

}

