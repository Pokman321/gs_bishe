package com.de.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ?
 * ?* @projectName videoservice
 * ?* @title ? ? IndexController ??
 * ?* @package ? ?com.de.controller ?
 * ?* @description ?Ê×Ò³
 * ?* @author IT_CREAT ? ??
 * ?* @date ?2020 2020/4/11 0011 ÉÏÎç 0:53 ?
 * ?* @version V1.0.0?
 * ?
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String indexView(){
        return "index";
    }

}
