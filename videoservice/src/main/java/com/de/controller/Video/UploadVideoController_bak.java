package com.de.controller.Video;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * @author gs
 * @date 2020/6/16 - 15:07
 */
@Controller
public class UploadVideoController_bak {
//    @RequestMapping("/fileupload")
    public String fileuoload2(HttpServletRequest request, MultipartFile upload, ModelMap modelMap) throws Exception {
        System.out.println("springmvc文件上传...");

        // 使用fileupload组件完成文件上传
        // 上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads/");
        System.out.println("路径在:"+path);
        // 判断，该路径是否存在
        File file = new File(path);
        if(!file.exists()){
            // 创建该文件夹
            file.mkdirs();
        }

        // 说明上传文件项
        // 获取上传文件的名称
        String filename = upload.getOriginalFilename();
        // 把文件的名称设置唯一值，uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid+"_"+filename;
        // 完成文件上传
        upload.transferTo(new File(path,filename));

        modelMap.addAttribute("filename",filename);
        modelMap.addAttribute("path",path);
        return "ok.html";
    }
}
