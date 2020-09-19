package com.de.controller.admin;

import com.de.service.CategoryService;
import com.de.service.VideoService;
import com.de.util.MyResult;
import com.de.util.PageQueryUtil;
import com.de.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gs
 * @date 2020/7/29 - 20:24
 * 管理者只允许删用户视频，不允许篡改视频
 */
@RequestMapping("/admin")
@Controller
public class VideoController {

    @Resource
    private VideoService videoService;
    @Resource
    private CategoryService categoryService;

    @GetMapping("/videos/list")
    @ResponseBody
    public MyResult list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(videoService.getVideosPage(pageUtil));
    }

    @GetMapping("/videos/list/{keyWord}")
    @ResponseBody
    public MyResult list_key(HttpServletRequest request, @PathVariable("keyWord") String keyWord){
        return list_key(request,keyWord,1);
    }

    @GetMapping("/video/list/{keyWord}/{page}")
    @ResponseBody
    public MyResult list_key(HttpServletRequest request,@PathVariable("keyWord") String keyWord,@PathVariable("page") Integer page){
        Map<String,Object> params = new HashMap<>();
        params.put("limit",10);
        params.put("page",page);
        params.put("keyWord",keyWord);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(videoService.getVideosPageByKeyWord(pageUtil));
    }


    @GetMapping("/videos")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "videos");
        return "admin/video";
    }



    @PostMapping("/videos/delete")
    @ResponseBody
    public MyResult delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (videoService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    
}
