package com.de.controller.Video;

import com.de.entity.UpdateVideo;
import com.de.rabbittest.direct.RabbitVideoService;
import com.de.rabbittest.entity.VideoNotice;
import com.de.service.*;
import com.de.util.MyCameraUtils;
import com.de.util.MyResult;
import com.de.util.OtherUtils;
import com.de.util.ResultGenerator;
import com.de.vo.VideoDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gs
 * @date 2020/6/20 - 3:53
 */
@Controller
@Slf4j

@RequestMapping("/myvideoedit")
public class UploadVideoController {


    @Value("${rtsp.mot_url}")
    private String mot_url;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private UploadFileSevice uploadFileSevice;

    @Autowired
    private DoMOTService doMOTService;

    @Autowired
    private FrameRedisService frameRedisService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private RabbitVideoService rabbitVideoService;

//    @Autowired
//    private VideoSender videoSender;


//    @Autowired
//    private VideoFramService videoFramService;

//    @RequestMapping("/upload")
//    public String jstest(){
//        return "video/amaze/upload";
//    }

    @RequestMapping("/update/{videoId}")
    public String updatevideo(@PathVariable("videoId") int videoId, HttpServletRequest request){
        VideoDetailVO videoDetailVO = videoService.getVideoDetail(videoId);
        if(videoDetailVO!=null){
            request.setAttribute("videoDetailVO",videoDetailVO);
        }
//        request.setAttribute();
        return "video/amaze/reedit";
    }


    @RequestMapping("/upload")
    public String upload(){
        return "video/amaze/upload";
    }


    @PostMapping("/upload/file")
    @ResponseBody
    public MyResult fileupload(HttpServletRequest httpServletRequest, HttpSession session, @RequestParam("file") MultipartFile file) throws URISyntaxException {


        System.out.println("进来了");
        String newfileName=uploadFileSevice.fileupload(file);



        if(newfileName.equals("error")){
            return ResultGenerator.genFailResult("文件上传失败");
        }
        else{
            System.out.println(newfileName+"文件上传成功");
            MyResult resultSuccess = ResultGenerator.genSuccessResult("文件上传成功");

//            Map<String,String> resultdata = new HashMap<>();
            Map<String,String> resultdata = new HashMap<>();
            resultdata.put("videoPath",MyCameraUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newfileName);
//            resultdata.put("videoTime",Long.toString(System.currentTimeMillis()));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

//            long lt1 = Long.parseLong("1594201667844");
//            long lt2 = Long.parseLong("1594201882137");
//
//            Date date1 = new Date(lt1);
//            Date date2 = new Date(lt2);
//            String res1 = simpleDateFormat.format(date1);
//            String res2 = simpleDateFormat.format(date2);


            resultdata.put("videoTime",simpleDateFormat.format(new Date(System.currentTimeMillis())));

//            resultSuccess.setData(MyCameraUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newfileName);
//            session.setAttribute("videopath",path);
            resultSuccess.setData(resultdata);
            return resultSuccess;
//            return ResultGenerator.genSuccessResult("文件上传成功");
        }



//        FFmpegFrameGrabber grabber = videoFramService.createGrabber(path);
//
//        if (grabber != null) {
//            log.info("创建grabber成功");
//        } else {
//            return ResultGenerator.genFailResult("创建grabber失败");
//        }
//
//        doMOTService.doPostJson()
//
//        List<String> time_pic = videoFramService.startVideoPush(grabber,path);

//        JSONObject
//
//        doMOTService.doPostJson();

    }


    @RequestMapping("/upload/ajaxtest")
    @ResponseBody
    public MyResult ajaxtest(@Param("cameraId") String cameraId, HttpSession session) throws InterruptedException {
        System.out.println("进来了ajax");
        System.out.println("取到了camera"+cameraId);
        Thread.sleep(8000);
        session.setAttribute("ajaxtest","yyy,i love you");
        return ResultGenerator.genSuccessResult("ajax测试成功");

    }

    @RequestMapping("/upload/ajaxtest2")
    @ResponseBody
    public MyResult ajaxtest2(UpdateVideo video) throws InterruptedException {

        System.out.println(video);
//        System.out.println(video.get);
        return ResultGenerator.genSuccessResult("ajax测试成功");
    }


    @RequestMapping("/upload/ajaxresult")
    @ResponseBody
    public MyResult ajaxresult(HttpSession session) throws InterruptedException {
        System.out.println("进来了result");
        Map<String,String> a = new HashMap<>();
//        String a = (String) session.getAttribute("ajaxtest");
//         session.setAttribute("ajaxtest",1);
//        System.out.println("取到了session"+a);
        a.put("videopath","aaaaaaaa");
        a.put("videotime","bbbbbbbb");
//        String a = "1111111";
        MyResult resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(a);

        return resultSuccess;

    }

    @PostMapping("/domot")
    @ResponseBody
//    public MyResult domot() throws InterruptedException, IOException {
    public MyResult domot(@RequestParam("videoPath") String video_path,
                                                  @RequestParam("userId") Integer userId,
                                                  @RequestParam("videoId") Integer videoId,
                                                  @RequestParam("VideoTime") String videoTime)
            throws InterruptedException, IOException, ParseException, TimeoutException {
//        MyResult result = videoSender.send("domot now!"+System.currentTimeMillis());

        VideoNotice videoNotice = new VideoNotice();
        videoNotice.setUserId(userId);
        videoNotice.setVideoId(videoId);
        videoNotice.setVideoPath(video_path);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date videoTime1 = simpleDateFormat.parse(videoTime);

        videoNotice.setVideoTime(videoTime1);

        MyResult result = rabbitVideoService.send(videoNotice);
        if(result.getResultCode()==200){
            result.setData(rabbitVideoService.getQueueCount());
        }

        return result;
    }

    @RequestMapping("/upload/mot")
    @ResponseBody
    public MyResult domot(@RequestParam("cameraId") String cameraId, HttpSession session){
        String path = (String)session.getAttribute("videopath");

        JSONObject dd = new JSONObject();
        dd.put("cameraList","list"+cameraId);
        dd.put("cameraHash","hash"+cameraId);
//        dd.put("money","122244");

        JSONObject jsonResult = doMOTService.post(mot_url,dd);
        if(jsonResult==null){
            return ResultGenerator.genFailResult("服务器连接失败");
        }

        FFmpegFrameGrabber grabber = frameRedisService.createGrabber(path);

        String videopath = frameRedisService.startCameraPush(grabber,cameraId, path);


        if(videopath.length()==0){
            System.out.println("转换错误");
        }
//        frameRedisService.startCameraPush(grabber,cameraId,path);

        session.setAttribute("video_path"+cameraId,videopath);

        MyResult resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(videopath);

        return resultSuccess;

    }

//    @RequestMapping("/upload/videomot")
//    @ResponseBody
//    public MyResult dovideomot(@Param("videoPath") String videoPath,HttpSession session) throws IOException, TimeoutException, InterruptedException {
//
//        Map<String, Integer> queueCount = videoSender.getQueueCount();
//        MyResult result;
//        if(queueCount.get("algorithm")>10 || queueCount.get("service")>10){
//
//            result = ResultGenerator.genFailResult("当前排队人数较多，请稍后，或选择离线");
//            result.setData(queueCount);
//            return result;
//        }
//        else if(queueCount.get("algorithm")<5 ){
//
//            videoSender.send(videoPath);
//
//            result = ResultGenerator.genSuccessResult("已为您添加进队列，但当前有一些人,请耐心等待，或稍后在");
//        }
//        else{
//
//            videoSender.send(videoPath);
//
//            result = ResultGenerator.genSuccessResult();
//
//        }
//        return result;
//    }



    @RequestMapping("/upload/result")
    public MyResult getResult(@Param("cameraId") String cameraId,HttpSession session){
        String video_path = (String) session.getAttribute("video_path"+cameraId);
        MyResult resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(video_path);
//        session.removeAttribute("video_path"+cameraId);
        return resultSuccess;
    }

    @RequestMapping("/getfirstimage")
    public MyResult getFirstImage(@Param("videoId") int videoId){
        MyResult resultSuccess = ResultGenerator.genSuccessResult();
        resultSuccess.setData(videoService.getVideoDetail(videoId).getVideoCoverImage());
        return resultSuccess;
    }

//    @RequestMapping("/upload/save")
//    public MyResult save(UpdateVideo updateVideo){
//        CurrentUser currentUser = new CurrentUser();
//        updateVideo.setUserName(currentUser.getPersonName());
//        String s = videoService.saveVideo(updateVideo);
//        if("success".equals(s)){
//            MyResult resultSuccess = ResultGenerator.genSuccessResult();
//            return resultSuccess;
//        }
//        MyResult resultFail = ResultGenerator.genFailResult("保存错误");
//        return resultFail;
//    }

//    @PostMapping("/videos/domotandsave")
//    @ResponseBody
//    public MyResult


    @PostMapping("/videos/onlyvideo")
    @ResponseBody
    public MyResult videosave(@RequestParam("videoName") String videoName,
                                 @RequestParam(name = "videoCoverImage") String videoCoverImage,
                                 @RequestParam("cameraCategoryId") Integer cameraCategoryId,
                                 @RequestParam("videoPath") String videoPath,
                                 @RequestParam("videoTime") String videoTime,
                                 @RequestParam("hasResult") Byte hasResult,
                                 @RequestParam("isShow") Byte isShow,
                                 HttpSession session) throws ParseException {
        if (StringUtils.isEmpty(videoName)) {
            return ResultGenerator.genFailResult("请输入视频标题");
        }
        if (videoName.trim().length() > 50) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(videoPath)) {
            return ResultGenerator.genFailResult("请先上传视频");
        }



        if (StringUtils.isEmpty(videoCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }



        UpdateVideo video = new UpdateVideo();
        video.setVideoName(videoName);
        video.setVideoPath(videoPath);

        video.setVideoTime(OtherUtils.dataTrans(videoTime));

        video.setCameraCategoryId(cameraCategoryId);
        video.setVideoCoverImage(videoCoverImage);


        video.setHasResult((byte) 0);
        video.setIsShow(isShow);

        video.setUserName((String) session.getAttribute("loginUser"));

        String saveVideoResult = videoService.saveVideo(video);
        if ("success".equals(saveVideoResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveVideoResult);
        }
    }

    //离线上传，上传后进行运算并保存，加入到消息队列中
    @PostMapping("/videos/domotandsave")
    @ResponseBody
    public MyResult domotandsave(@RequestParam("videoName") String videoName,
                         @RequestParam(name = "videoCoverImage") String videoCoverImage,
                         @RequestParam("cameraCategoryId") Integer cameraCategoryId,
                         @RequestParam("videoPath") String videoPath,
                         @RequestParam("videoTime") String videoTime,
                         @RequestParam("hasResult") Byte hasResult,
                         @RequestParam("isShow") Byte isShow,
                         HttpSession session) throws ParseException {
        if (StringUtils.isEmpty(videoName)) {
            return ResultGenerator.genFailResult("请输入视频标题");
        }
        if (videoName.trim().length() > 50) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(videoPath)) {
            return ResultGenerator.genFailResult("请先上传视频");
        }



        if (StringUtils.isEmpty(videoCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }



        UpdateVideo video = new UpdateVideo();
        video.setVideoName(videoName);
        video.setVideoPath(videoPath);

        video.setVideoTime(OtherUtils.dataTrans(videoTime));

        video.setCameraCategoryId(cameraCategoryId);
        video.setVideoCoverImage(videoCoverImage);


        video.setHasResult(hasResult);
        video.setIsShow(isShow);

        video.setUserName((String) session.getAttribute("loginUser"));

        String saveVideoResult = videoService.motAndSave(video);
        if ("success".equals(saveVideoResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveVideoResult);
        }
    }


    @PostMapping("/videos/save")
    @ResponseBody
    public MyResult save(@RequestParam("videoName") String videoName,
                       @RequestParam(name = "videoCoverImage") String videoCoverImage,
                       @RequestParam("cameraCategoryId") Integer cameraCategoryId,
                       @RequestParam("videoPath") String videoPath,
                       @RequestParam("videoTime") String videoTime,
                       @RequestParam("resultPath") String resultPath,
                       @RequestParam("resultTime") String resultTime,
                       @RequestParam("hasResult") Byte hasResult,
                       @RequestParam("isShow") Byte isShow,
                         HttpSession session) throws ParseException {
        if (StringUtils.isEmpty(videoName)) {
            return ResultGenerator.genFailResult("请输入视频标题");
        }
        if (videoName.trim().length() > 50) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(videoPath)) {
            return ResultGenerator.genFailResult("请先上传视频");
        }

        if (StringUtils.isEmpty(resultPath)) {
            return ResultGenerator.genFailResult("请先上传跟踪结果");
        }

        if (StringUtils.isEmpty(videoCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        


        UpdateVideo video = new UpdateVideo();
        video.setVideoName(videoName);
        video.setVideoPath(videoPath);

        video.setVideoTime(OtherUtils.dataTrans(videoTime));

        video.setCameraCategoryId(cameraCategoryId);
        video.setVideoCoverImage(videoCoverImage);

        video.setResultPath(resultPath);
        video.setResultTime(OtherUtils.dataTrans(resultTime));

        video.setHasResult(hasResult);
        video.setIsShow(isShow);

        video.setUserName((String) session.getAttribute("loginUser"));

        String saveVideoResult = videoService.saveVideo(video);
        if ("success".equals(saveVideoResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveVideoResult);
        }
    }


    @PostMapping("/videos/update")
    @ResponseBody
    public MyResult update(@RequestParam("videoName") String videoName,
                         @RequestParam(name = "videoCoverImage") String videoCoverImage,
                         @RequestParam("cameraCategoryId") Integer cameraCategoryId,
                         @RequestParam("videoPath") String videoPath,
                         @RequestParam("videoTime") String videoTime,
                         @RequestParam("resultPath") String resultPath,
                         @RequestParam("resultTime") String resultTime,
                         @RequestParam("hasResult") Byte hasResult,
                         @RequestParam("isShow") Byte isShow) throws ParseException {
        if (StringUtils.isEmpty(videoName)) {
            return ResultGenerator.genFailResult("请输入视频标题");
        }
        if (videoName.trim().length() > 50) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(videoPath)) {
            return ResultGenerator.genFailResult("请先上传视频");
        }

        if (StringUtils.isEmpty(resultPath)) {
            return ResultGenerator.genFailResult("请先上传跟踪结果");
        }

        if (StringUtils.isEmpty(videoCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }



        UpdateVideo video = new UpdateVideo();
        video.setVideoName(videoName);
        video.setVideoPath(videoPath);

        video.setVideoTime(OtherUtils.dataTrans(videoTime));

        video.setCameraCategoryId(cameraCategoryId);
        video.setVideoCoverImage(videoCoverImage);

        video.setResultPath(resultPath);
        video.setResultTime(OtherUtils.dataTrans(resultTime));

        video.setHasResult(hasResult);
        video.setIsShow(isShow);


        String updateVideoResult = videoService.updateVideo(video);
        if ("success".equals(updateVideoResult)) {
            return ResultGenerator.genSuccessResult("更新成功");
        } else {
            return ResultGenerator.genFailResult(updateVideoResult);
        }
    }



}
