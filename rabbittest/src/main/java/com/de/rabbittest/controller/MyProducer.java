package com.de.rabbittest.controller;

import com.de.rabbittest.direct.TestService;
import com.de.rabbittest.direct.VideoSender;
import com.de.rabbittest.direct.RabbitVideoService;
import com.de.rabbittest.entity.VideoNotice;
//import com.de.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gs
 * @date 2020/8/3 - 20:10
 */
@Controller
@RequestMapping("/admin")
public class MyProducer {

    @Autowired
    private VideoSender videoSender;

    @Autowired
    private RabbitVideoService rabbitVideoService;

    @Autowired
    private TestService testService;

//    @Autowired
//    private AdminUserService adminUserService;

    @GetMapping("/")
    @ResponseBody
    public Map<String, Integer> getMessageNum() throws IOException, TimeoutException {
        return rabbitVideoService.getQueueCount();
    }

    @GetMapping("/getmessage")
    @ResponseBody
    public Map<String,Integer> getMessage() throws IOException {
        return rabbitVideoService.getMessageCount();

    }


    @PostMapping("/domot")
    @ResponseBody
//    public MyResult domot() throws InterruptedException, IOException {
    public void domot(@RequestParam("videoPath") String video_path,
                      @RequestParam("userId") Integer userId,
                      @RequestParam("videoId") Integer videoId,
                      @RequestParam("VideoTime") String videoTime)
            throws InterruptedException, IOException, ParseException {
//        MyResult result = videoSender.send("domot now!"+System.currentTimeMillis());

        VideoNotice videoNotice = new VideoNotice();
        videoNotice.setUserId(userId);
        videoNotice.setVideoId(videoId);
        videoNotice.setVideoPath(video_path);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date videoTime1 = simpleDateFormat.parse(videoTime);

        videoNotice.setVideoTime(videoTime1);




        rabbitVideoService.send(videoNotice);
//        return result;
    }

    @PostMapping("/domotoffline")
    @ResponseBody
//    public MyResult domot() throws InterruptedException, IOException {
    public void domotoffline(@RequestParam("videoPath") String video_path,
                      @RequestParam("userId") Integer userId,
                      @RequestParam("videoId") Integer videoId,
                      @RequestParam("VideoTime") String videoTime)
            throws InterruptedException, IOException, ParseException {
//        MyResult result = videoSender.send("domot now!"+System.currentTimeMillis());

        VideoNotice videoNotice = new VideoNotice();
        videoNotice.setUserId(userId);
        videoNotice.setVideoId(videoId);
        videoNotice.setVideoPath(video_path);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date videoTime1 = simpleDateFormat.parse(videoTime);

        videoNotice.setVideoTime(videoTime1);



        rabbitVideoService.send(videoNotice);
//        return result;
    }


    @GetMapping("/test")
    @ResponseBody
    public int test() throws InterruptedException, UnsupportedEncodingException {
        return testService.increase();
    }

}