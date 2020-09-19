package com.de.controller.Video;

import com.de.service.CameraService;
import com.de.service.CategoryService;
import com.de.service.ConfigService;
import com.de.service.LinkService;
import com.de.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author gs
 * @date 2020/6/10 - 22:07
 */
@Controller
@Slf4j
@RequestMapping("/mycamera")
public class MyCameraController {

    public static String theme = "amaze";

    @Resource
    private CameraService cameraService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private LinkService linkService;

    @Resource
    private ConfigService configService;

    @GetMapping({"/","/index","index.html"})
    public String index(HttpServletRequest request){
        return this.page(request,1);
    }

    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum){
        PageResult cameraPageResult = cameraService.getCamerasForIndexPage(pageNum);
        if (cameraPageResult==null){
            return "error/error_404";
        }

        request.setAttribute("cameraPageResult",cameraPageResult);
        request.setAttribute("allCategory",cameraService.getCameraById(1));
        request.setAttribute("pageName", "首页");
        request.setAttribute("configuration",1);

        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/index";

    }


    public String category(HttpServletRequest request,@PathVariable("categoryName") String categoryName)
    {
        return category(request,categoryName,1);
    }

    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer page) {
//        PageResult cameraPageResult = cameraService.getCamerasPageByPosition(categoryName,page);
        return null;
    }
}
