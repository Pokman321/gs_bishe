package com.de.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author gs
 * @date 2020/6/30 - 0:20
 */
@Configuration
@ImportResource(locations = {"classpath:bean.xml"})
//@ImportResource(locations = {"bean.xml"})
public class Httpconfiguration {

//    @Bean
//    @Scope("scopeName")
//    public CloseableHttpClient httpClient(){
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        return httpClientBuilder.build();
//    }


}
