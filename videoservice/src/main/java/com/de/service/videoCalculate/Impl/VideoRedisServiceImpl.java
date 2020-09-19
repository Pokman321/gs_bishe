package com.de.service.videoCalculate.Impl;

import com.alibaba.fastjson.JSON;
import com.de.dao.CurCameraMapper;
import com.de.dao.PersonMapper;
import com.de.dao.VideoMapper;
import com.de.entity.CurImage;
import com.de.entity.Person;
import com.de.entity.UpdateVideo;
import com.de.rabbittest.entity.VideoMqMessage;
import com.de.rabbittest.utils.GsonUtil;
import com.de.service.DoMOTService;
import com.de.service.videoCalculate.VideoRedisService;
import com.de.util.FilesToMov;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jim2mov.core.MovieSaveException;
import org.json.JSONObject;
import org.springframework.amqp.core.Message;
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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gs
 * @date 2020/8/4 - 18:15
 */
@Service
@Slf4j
public class VideoRedisServiceImpl implements VideoRedisService {

    @Autowired
    private CurCameraMapper curCameraMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private VideoMapper videoMapper;


    private boolean flag=true;

    private static final int THREAD_COUNTS =
            Runtime.getRuntime().availableProcessors();
    //任务队列
    private BlockingQueue<Runnable> taskQueue
            = new ArrayBlockingQueue<>(10);
    //线程池，固定大小，有界队列
    private ExecutorService taskExecutor =
            new ThreadPoolExecutor(THREAD_COUNTS, THREAD_COUNTS, 60,
                    TimeUnit.SECONDS, taskQueue);

    @Value("${rtsp.mot_url}")
    private static String mot_url;

    @Autowired
    private DoMOTService doMOTService;

//    private Jedis jedis = new Jedis(mot_url,6379);

    @Value("${rtsp.transport.type}")
    private String rtspTransportType;

    @Value("$save_dir")
    private static String save_dir;

    @Value("$video_dir")
    private String video_dir;


    private boolean isStart = false;


    public static int frameRate = 24;

    public static int frameWidth = 480;

    public static int frameHeight = 270;

    public int frameNum = 0;

    final static Base64.Decoder decoder = Base64.getDecoder();
    final static Base64.Encoder encoder = Base64.getEncoder();

//    public static ConcurrentLinkedDeque<String> time_list = new ConcurrentLinkedDeque<>();

    public LinkedBlockingDeque<String> time_list = new LinkedBlockingDeque<>();

    private CountDownLatch latch = new CountDownLatch(2);

    private JedisPool jPool = new JedisPool(mot_url,6379);

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private UpdateVideo updateVideo = new UpdateVideo();

    public class RedisSave implements Runnable{


        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
//        List<String> list = new ArrayList<>();

        FFmpegFrameGrabber grabber;
        String userId;
        String pathUrl;


        //用于解析mp4视频，将结果存进去
        public RedisSave(FFmpegFrameGrabber grabber, String userId, String pathUrl){
            super();
            this.grabber = grabber;
            this.userId = userId;
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


                        jedis_save.hset("hash" + userId, name, encodeText);
                        jedis_save.lpush("list" + userId, name);
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



    public class RedisGet implements Runnable{

        //        @Resource(name = "jedis_get")
//        private Jedis jedis_get;
        private String userId;

        public RedisGet(String userId){
            this.userId = userId;
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
                cur_name = jedis_get.blpop(0,"finished"+userId).get(1);
                mot_result = jedis_get.hget("result" + userId, cur_name);
                pic_result = jedis_get.hget("resultpic"+userId, cur_name);
                jedis_get.hdel("hash"+userId, cur_name);
//                jedis_get.blpop(0,"list"+userId);
                jedis_get.hdel("result"+userId, cur_name);
                jedis_get.hdel("resultpic"+userId,cur_name);
//                RedisGet.class.notifyAll();

                String save_path = save_dir+"/"+cur_name+".jpg";
                if (atomicInteger.incrementAndGet()==1){

                    updateVideo.setVideoCoverImage(save_path);
                    new URI(save_path);
                    videoMapper.insertSelective(updateVideo);
                }
                int person_num = 0;
//            JSONObject resultMap = JSONObject.parseObject(mot_result);
                if(!userId.equals("0")){
                    Map maps = (Map) JSON.parse(mot_result);

                    for (Object map : maps.entrySet()){
                        person_num++;
                        Person person = new Person();
                        person.setPersonId(Integer.parseInt ((String) ((Map.Entry)map).getKey()));
                        person.setPosition((String) ((Map.Entry)map).getKey());
                        person.setTime(Long.parseLong(cur_name));
                        person.setCameraId(Integer.parseInt(userId));
                        person.setImageUrl(save_path);
                        personMapper.savePerson(person);
                    }

                    byte[] pic_byte = decoder.decode(pic_result);
                    byte2image(pic_byte,save_path);
//                    CurImage curImage = new CurImage();
//                    curImage.setCameraId(Integer.parseInt(userId));
//                    curImage.setPersonNum(person_num);
//                    curImage.setImageUrl(save_path);
//                    curImage.setImageTime(Long.parseLong(cur_name));
//
//                    curCameraMapper.addImage("camera"+userId,curImage);

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
    public String startCameraPush(FFmpegFrameGrabber grabber,String userId,String videoPath) {

        long start = System.currentTimeMillis();

        VideoRedisServiceImpl.RedisSave redisSave = new VideoRedisServiceImpl.RedisSave(grabber, userId, videoPath);
        new Thread(redisSave).start();
        VideoRedisServiceImpl.RedisGet redisGet = new VideoRedisServiceImpl.RedisGet(userId);
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

    protected VideoMqMessage convertBodyToBean(String body){
        return GsonUtil.jsonToObject(body,VideoMqMessage.class);
    }


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String msg = new String(message.getBody(),StandardCharsets.UTF_8);

        log.info("CPU接收到新视频请求消息: "+msg);

        if(StringUtils.isEmpty(msg)){
            if(StringUtils.isNotBlank(msg)){
                VideoMqMessage videoMqMessage = this.convertBodyToBean(msg);
                //取出消息中的关键信息
                int userId =(Integer)videoMqMessage.getHeader().get("userId");
                int videoId = (Integer)videoMqMessage.getHeader().get("videoId");
                String videoPath = (String)videoMqMessage.getHeader().get("videoPath");
                Date messageTime = (Date)videoMqMessage.getBody().getMessageTime();
                //订阅不同的topic，消费者可以根据消息body中的topicCode对不同业务加以区分
                if(videoMqMessage.getBody().getTopicCode().equals("domot")){

                    org.json.JSONObject json = new org.json.JSONObject();
                    json.put("videoId",videoId);


                    JSONObject jsonResult = doMOTService.post(mot_url,json);
                    //向算法端发送http请求，进一步算法端准备好，并对消息进行同步，避免算法端处理的视频与业务层出现错乱
                    JSONObject post = doMOTService.post(mot_url, json);
//                    if(post.get()){
//
//                    }


                    FFmpegFrameGrabber grabber = createGrabber(videoPath);

                    String videoResult = startCameraPush(grabber, Integer.toString(userId), videoPath);
                }
            }
        }

//        String videoPathId = new String(message.getBody(), StandardCharsets.UTF_8);
//        String videoPath = videoPathId.split("#")[0];
//        String userId = videoPathId.split("#")[1];
        
        

    }

    
}
