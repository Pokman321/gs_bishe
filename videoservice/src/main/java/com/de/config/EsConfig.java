package com.de.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author gs
 * @date 2020/8/13 - 16:20
 */
@EnableElasticsearchRepositories(basePackages = "com.de.mysearch.repository")
@Configuration
public class EsConfig {
}
