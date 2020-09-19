package com.de.config;

import com.de.mysearch.config.MyBatisConfig;
//import com.de.rabbittest.config.RabbitConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * @author gs
 * @date 2020/8/13 - 12:56
 */
@Configuration
@AutoConfigureBefore
//@EnableAutoConfiguration
@Import(value = {RabbitConfig.class,MyBatisConfig.class})
@PropertySource("classpath:application.properties")
//@Import(value = {MyBatisConfig.class})
//@Import(value = MyBatisConfig.class)
public class OtherConfig {
}
