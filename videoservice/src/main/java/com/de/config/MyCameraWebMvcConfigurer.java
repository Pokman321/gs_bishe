package com.de.config;

import com.de.interceptor.AdminLoginInterceptor;
import com.de.interceptor.VideoCameraInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author gs
 * @date 2020/7/12 - 2:03
 */
@Configuration
public class MyCameraWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Autowired
    private VideoCameraInterceptor videoCameraInterceptor;


//    public void addInterceptors(InterceptorRegistry registry) {
//        // 添加一个拦截器，拦截以/admin为前缀的url路径
//        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login").excludePathPatterns("/admin/dist/**").excludePathPatterns("/admin/plugins/**").excludePathPatterns("/admin/register").excludePathPatterns("/admin/upload/file");
//        registry.addInterceptor(videoCameraInterceptor).addPathPatterns("/myvideo/**").excludePathPatterns("/admin/login").excludePathPatterns("/admin/dist/**").excludePathPatterns("/admin/plugins/**");
//        registry.addInterceptor(videoCameraInterceptor).addPathPatterns("/mycamera/**").excludePathPatterns("/admin/login").excludePathPatterns("/admin/dist/**").excludePathPatterns("/admin/plugins/**");
//        registry.addInterceptor(videoCameraInterceptor).addPathPatterns("/myvideoedit/**").excludePathPatterns("/admin/login").excludePathPatterns("/admin/dist/**").excludePathPatterns("/admin/plugins/**");
//    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }

}
