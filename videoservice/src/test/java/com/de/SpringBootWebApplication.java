//package com.de;
//
///**
// * @author gs
// * @date 2020/8/13 - 15:48
// */
//import java.util.Arrays;
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.ServletComponentScan;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
////import org.springframework.boot.web.support.SpringBootServletInitializer;
//import org.springframework.context.ApplicationContext;
//
//@ServletComponentScan
//@MapperScan("com.de.dao")
////@ComponentScan(basePackages = "")
////@SpringBootApplication(exclude = DataSourceAutoConfigration.class)
//@SpringBootApplication(scanBasePackages = "com.de")
//public class SpringBootWebApplication extends SpringBootServletInitializer implements CommandLineRunner {
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(SpringBootWebApplication.class);
//    }
//
//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(SpringBootWebApplication.class, args);
//    }
//
//    @Autowired
//    private ApplicationContext appContext;
//
//    @Override
//    public void run(String... args) throws Exception
//    {
//        String[] beans = appContext.getBeanDefinitionNames();
//        Arrays.sort(beans);
//        for (String bean : beans)
//        {
//            System.out.println(bean + " of Type :: " + appContext.getBean(bean).getClass());
//        }
//    }
//}
