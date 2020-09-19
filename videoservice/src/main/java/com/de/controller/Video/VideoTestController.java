package com.de.controller.Video;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author gs
 * @date 2020/7/10 - 20:17
 */
//@RequestMapping("/videotest")
@Controller
public class VideoTestController {

    @RequestMapping("/cal")
//    public void cal( HttpSession session, HttpServletResponse response) throws IOException {
    public void cal(@RequestParam("video_path") String video_path, HttpSession session, HttpServletResponse response) throws IOException {
        System.out.println("进来了");
//        System.out.println(video_path);
        session.setAttribute("video_path",video_path);
//        response.sendRedirect("video/result");
//        return "video/video";
    }


    @RequestMapping("/result")
    public String getResult(){
        return "video/video";
    }
}
