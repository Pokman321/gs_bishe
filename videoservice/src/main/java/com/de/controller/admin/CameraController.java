package com.de.controller.admin;

import com.de.config.Constants;
import com.de.entity.Camera;
import com.de.service.CameraService;
import com.de.service.CategoryService;
import com.de.util.MyCameraUtils;
import com.de.util.MyResult;
import com.de.util.PageQueryUtil;
import com.de.util.ResultGenerator;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author gs
 * @date 2020/6/10 - 17:47
 */
@Controller
@RequestMapping("/admin")
public class CameraController {

    @Resource
    private CameraService cameraService;
    @Resource
    private CategoryService categoryService;

    @GetMapping("/cameras/list")
    @ResponseBody
    public MyResult list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(cameraService.getCamerasPage(pageUtil));
    }


    @GetMapping("/cameras")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "cameras");
        return "admin/camera";
    }

    @GetMapping("/cameras/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        request.setAttribute("categories", categoryService.getAllCategories());
        return "admin/edit";
    }

    @GetMapping("/cameras/edit/{cameraId}")
    public String edit(HttpServletRequest request, @PathVariable("cameraId") int cameraId) {
        request.setAttribute("path", "edit");
        Camera camera = cameraService.getCameraById(cameraId);
        if (camera == null) {
            return "error/error_400";
        }
        request.setAttribute("camera", camera);
        request.setAttribute("categories", categoryService.getAllCategories());
        return "admin/edit";
    }

    @PostMapping("/cameras/save")
    @ResponseBody
    public MyResult save(@RequestParam("cameraName") String cameraName,
                         @RequestParam(name = "cameraUrl", required = true) String cameraUrl,
                         @RequestParam("cameraCategoryId") Integer cameraCategoryId,

                         @RequestParam("cameraCoverImage") String cameraCoverImage,
                         @RequestParam("cameraEnable") Byte cameraEnable
                     ) {
        if (StringUtils.isEmpty(cameraName)) {
            return ResultGenerator.genFailResult("请输入相机名称");
        }
        if (cameraName.trim().length() > 20) {
            return ResultGenerator.genFailResult("名字过长");
        }

        if (cameraUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }

        if (StringUtils.isEmpty(cameraCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Camera camera = new Camera();
        camera.setCameraName(cameraName);
        camera.setCameraUrl(cameraUrl);
        camera.setCameraCategoryId(cameraCategoryId);

        camera.setCameraCoverImage(cameraCoverImage);
        camera.setCameraEnable(cameraEnable);

        String saveCameraResult = cameraService.saveCamera(camera);
        if ("success".equals(saveCameraResult)) {
            return ResultGenerator.genSuccessResult("添加成功");
        } else {
            return ResultGenerator.genFailResult(saveCameraResult);
        }
    }

    @PostMapping("/cameras/update")
    @ResponseBody
    public MyResult update(@RequestParam("cameraId") int cameraId,
                         @RequestParam("cameraName") String cameraName,
                         @RequestParam(name = "cameraUrl", required = true) String cameraUrl,
                         @RequestParam("cameraCategoryId") Integer cameraCategoryId,

                         @RequestParam("cameraCoverImage") String cameraCoverImage,
                         @RequestParam("cameraEnable") Byte cameraEnable) {
        if (StringUtils.isEmpty(cameraName)) {
            return ResultGenerator.genFailResult("请输入相机名称");
        }
        if (cameraName.trim().length() > 15) {
            return ResultGenerator.genFailResult("相机名称过长");
        }

        if (cameraUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }

        if (StringUtils.isEmpty(cameraCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        Camera camera = new Camera();
        camera.setCameraId(cameraId);
        camera.setCameraName(cameraName);
        camera.setCameraUrl(cameraUrl);
        camera.setCameraCategoryId(cameraCategoryId);

        camera.setCameraCoverImage(cameraCoverImage);
        camera.setCameraEnable(cameraEnable);

        String updateCameraResult = cameraService.updateCamera(camera);
        if ("success".equals(updateCameraResult)) {
            return ResultGenerator.genSuccessResult("修改成功");
        } else {
            return ResultGenerator.genFailResult(updateCameraResult);
        }
    }

    @PostMapping("/cameras/md/uploadfile")
    public void uploadFileByEditormd(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(name = "editormd-image-file", required = true)
                                             MultipartFile file) throws IOException, URISyntaxException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        String fileUrl = MyCameraUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + fileUrl + "\"}");
        } catch (UnsupportedEncodingException e) {
            response.getWriter().write("{\"success\":0}");
        } catch (IOException e) {
            response.getWriter().write("{\"success\":0}");
        }
    }

    @PostMapping("/cameras/delete")
    @ResponseBody
    public MyResult delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (cameraService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    @GetMapping("/cameras/connect")
    @ResponseBody
    public MyResult connect(@RequestParam("cameraUrl") String cameraUrl,HttpServletRequest httpServletRequest) throws FrameGrabber.Exception {
        return cameraService.cameraConnect(cameraUrl,httpServletRequest);
    }

    @PostMapping({"/upload/file"})
    @ResponseBody
    public MyResult upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            MyResult resultSuccess = ResultGenerator.genSuccessResult();
            resultSuccess.setData(MyCameraUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }
}