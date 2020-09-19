package com.de.mysearch.config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis相关配置
 * Created by macro on 2019/4/8.
 */
@Configuration
@MapperScan({"com.de.mysearch.mapper", "com.de.mysearch.dao"})
public class MyBatisConfig {
}
