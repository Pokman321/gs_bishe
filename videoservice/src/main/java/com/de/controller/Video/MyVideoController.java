package com.de.controller.Video;

import com.de.entity.CameraLink;
import com.de.service.CategoryService;
import com.de.service.ConfigService;
import com.de.service.LinkService;
import com.de.service.VideoService;
import com.de.util.MyResult;
import com.de.util.PageResult;
import com.de.util.ResultGenerator;
import com.de.vo.VideoDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author gs
 * @date 2020/7/14 - 0:44
 */
@Controller
@RequestMapping("/myvideo")
public class MyVideoController {

//    public static CurrentUser currentUser = new CurrentUser();

    public static String theme = "amaze";
    
    @Resource
    private VideoService videoService;
    
    @Resource
    private CategoryService categoryService;
    
    @Resource
    private ConfigService configService;

    @Resource
    private LinkService linkService;

    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request,HttpSession session) {
        return this.page(request, 1,session);
    }

    @GetMapping({"/ownvideo"})
    public String ownvideo(HttpServletRequest request,HttpSession session){
        return this.ownpage(request, 1,session);
    }

    @GetMapping({"/ownpage/{pageNum}"})
    public String ownpage(HttpServletRequest request, @PathVariable("pageNum") int pageNum, HttpSession session){
        PageResult videoPageResult = videoService.getOwnVideoForIndexPage(pageNum,(String)session.getAttribute("loginUser"));
        if (videoPageResult == null) {
            System.out.println("还没有结果");
            return "error/error_404";
        }
        request.setAttribute("videoPageResult", videoPageResult);
        request.setAttribute("newVideos", videoService.getVideoListForIndexPage(1));
        request.setAttribute("hotVideos", videoService.getVideoListForIndexPage(0));

        request.setAttribute("pageName", "首页");
//        request.setAttribute("configurations", configService.getAllConfigs());
        return "video/" + theme + "/index";
    }

    @PostMapping("/videos/delete")
    @ResponseBody
    public MyResult delete(@RequestBody Integer id) {
        if (id == null) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (videoService.deleteByPrimaryKey(id)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }


    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum, HttpSession session) {
//        PageResult videoPageResult = videoService.getVideosForIndexPage(pageNum,(String) session.getAttribute("UserName"));
        PageResult videoPageResult = videoService.getVideosForIndexPage(pageNum,(String) session.getAttribute("loginUser"));
        if (videoPageResult == null) {
            System.out.println("还没有结果");
            return "error/error_404";
        }
        request.setAttribute("videoPageResult", videoPageResult);
        request.setAttribute("newVideos", videoService.getVideoListForIndexPage(1));
        request.setAttribute("hotVideos", videoService.getVideoListForIndexPage(0));

        request.setAttribute("pageName", "首页");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "video/" + theme + "/index";
    }

    @GetMapping({"/categories"})
    public String categories(HttpServletRequest request) {

        request.setAttribute("categories", categoryService.getAllCategories());
        request.setAttribute("pageName", "分类页面");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "video/" + theme + "/category";
    }

    @GetMapping({"/video/{videoId}"})
    public String detail(HttpServletRequest request, @PathVariable("videoId") int videoId) {
        VideoDetailVO videoDetailVO = videoService.getVideoDetail(videoId);
        if (videoDetailVO != null) {
            request.setAttribute("videoDetailVO", videoDetailVO);
            request.setAttribute("pageName", "详情");
            request.setAttribute("configurations", configService.getAllConfigs());
            return "video/" + theme + "/detail";
        }else{
            return "error/error_400";
        }

    }


    @GetMapping({"/category/{categoryName}"})
    public String category(Model model, HttpServletRequest request, @PathVariable("categoryName") String categoryName) {
        return category(model,request, categoryName, 1);
    }

    @GetMapping({"/category/{categoryName}/{page}"})
    public String category(Model model, HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer page) {

        PageResult videoPageResult = videoService.getVideosPageByCategory(categoryName, page);
//        request.setAttribute("videoPageResult", videoPageResult);
//        request.setAttribute("pageName", "分类");
//        request.setAttribute("pageUrl", "category");
//        request.setAttribute("keyword", categoryName);
//        request.setAttribute("newVideos", videoService.getVideoListForIndexPage(1));
//        request.setAttribute("hotVideos", videoService.getVideoListForIndexPage(0));
//
//        request.setAttribute("configurations", configService.getAllConfigs());

        model.addAttribute("videoPageResult",videoPageResult);
        model.addAttribute("pageName","分类");
        model.addAttribute("pageUrl", "category");
        model.addAttribute("keyword", categoryName);
        model.addAttribute("newVideos", videoService.getVideoListForIndexPage(1));
        model.addAttribute("hotVideos", videoService.getVideoListForIndexPage(0));

        return "video/" + theme + "/list";
    }

    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return search(request, keyword, 1);
    }

    @GetMapping({"/search/{keyword}/{page}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page) {
        PageResult videoPageResult = videoService.getVideosPageBySearch(keyword, page);
        request.setAttribute("videoPageResult", videoPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        request.setAttribute("newVideos", videoService.getVideoListForIndexPage(1));
        request.setAttribute("hotVideos", videoService.getVideoListForIndexPage(0));

//        request.setAttribute("configurations", configService.getAllConfigs());
        return "video/" + theme + "/list";
    }

    @GetMapping({"/link"})
    public String link(HttpServletRequest request) {
        request.setAttribute("pageName", "友情链接");
        Map<Byte, List<CameraLink>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey((byte) 0)) {
                request.setAttribute("favoriteLinks", linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", linkMap.get((byte) 2));
            }
        }
//        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/" + theme + "/link";
    }

}
