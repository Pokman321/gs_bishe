package com.de.controller.admin;

import com.de.entity.AdminUser;
import com.de.service.*;
import com.de.util.MD5Util;
import com.de.util.OtherUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

/**
 * @author gs
 * @date 2020/6/4 - 20:43
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private CameraService cameraService;

    @Resource
    private VideoService videoService;

    @Resource
    private PersonService personService;

    @Resource
    private UploadFileSevice uploadFileSevice;

    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", categoryService.getTotalCategories());
        request.setAttribute("cameraCount",cameraService.getTotalCameras() );
        request.setAttribute("linkCount", 3);
        request.setAttribute("videoCount", videoService.getTotalVideos());
        request.setAttribute("personCount", personService.getPersonCount());
        return "admin/index";
    }

    @GetMapping(value = "/register")
    private String register(){
        System.out.println("进来了");
        return "admin/register";
    }


    @PostMapping(value = "/register")
    private String register(@RequestParam("userName") String userName,
                            @RequestParam("password") String password,
                            @RequestParam("nickName") String nickName,
                            @RequestParam("userAvatar") String userAvatar,

                            @RequestParam("email") String email,
                            @RequestParam("verifyCode") String verifyCode, HttpSession session) throws InterruptedException, IOException {
        if(StringUtils.isEmpty(userName)){
            session.setAttribute("errorMsg","用户名不能为空");
            return "admin/register";
        }
        if (org.springframework.util.StringUtils.isEmpty(userName) || org.springframework.util.StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/register";
        }


        if(StringUtils.isEmpty(nickName) ){
            session.setAttribute("errorMsg","昵称不能为空");
            return "admin/register";
        }

        if(StringUtils.isEmpty(email) ){
            session.setAttribute("errorMsg","邮箱不能为空");
            return "admin/register";
        }

        String kaptchaCode = session.getAttribute("verifyCode")+"";

        if(StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)){
            session.setAttribute("errorMsg","验证码错误");
            return "admin/register";
        }

        AdminUser adminUser = new AdminUser();

        adminUser.setLoginUserName(userName);
        adminUser.setLoginPassword(MD5Util.MD5Encode(password,"UTF-8"));
        adminUser.setNickName(nickName);
        adminUser.setEmail(email);



        if(userAvatar!=null){
            adminUser.setUserAvatar(userAvatar);
        }
        else {
            adminUser.setUserAvatar(OtherUtils.default_avatar);
        }


        if(adminUserService.register(adminUser)==0){
            session.setAttribute("successMsg","注册成功");

//            System.out.println("登录成功");
            Thread.sleep(2000);
            return "redirect:/admin/login";
        }
        else if(adminUserService.register(adminUser)==1){
            session.setAttribute("errorMsg","用户已注册");
            return "admin/register";
        }
        else if(adminUserService.register(adminUser)==2){
            session.setAttribute("errorMsg","昵称被占用");
            return "admin/register";
        }
        else if(adminUserService.register(adminUser)==3){
            session.setAttribute("errorMsg","注册失败，请重试");
            return "admin/register";
        }
        return "admin/register";
    }


    @PostMapping(value = "/login")
    private String login(@RequestParam("userName") String userName,
                         @RequestParam("password") String password,
                         @RequestParam(value = "isManager",defaultValue = "false") String isManager,
                         @RequestParam("verifyCode") String verifyCode,
                         HttpSession session){
        if(StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg","验证码不能为空");
            return "admin/login";
        }
        if (org.springframework.util.StringUtils.isEmpty(userName) || org.springframework.util.StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }

        String kaptchaCode = session.getAttribute("verifyCode")+"";

        if(StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)){
            session.setAttribute("errorMsg","验证码错误");
            return "admin/login";
        }



        AdminUser adminUser = adminUserService.login(userName,password,isManager);
        System.out.println("没有问题");
        System.out.println(isManager);
        if(adminUser!=null){
            session.setAttribute("loginUser",adminUser.getNickName());
            session.setAttribute("loginUserId",adminUser.getAdminUserId());
//            CurrentUser currentUser = new CurrentUser();
//            currentUser.setPersonId(adminUser.getAdminUserId());
//            currentUser.setPersonName(adminUser.getNickName());

            if (isManager.equals("true")){

                session.setAttribute("isManager","true");
                return "redirect:/admin/index";
            }
            System.out.println("登录成功");
            return "redirect:/myvideo/index";
        }
        else{
            session.setAttribute("errorMsg","登录失败");
            return "admin/login";
        }

    }

}
