package com.de.rabbittest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.de.rabbittest.dao")
public class RabbittestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbittestApplication.class, args);
    }

}
