package com.de.controller.Video;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gs
 * @date 2020/7/18 - 4:43
 */
@Controller
@RequestMapping("/jstest")
public class JsTest {

    @RequestMapping("/")
    public String jstest(){
        return "video/amaze/jstest";
    }

}
