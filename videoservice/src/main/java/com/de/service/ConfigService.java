package com.de.service;

import java.util.Map;

/**
 * @author gs
 * @date 2020/6/15 - 14:21
 */
public interface ConfigService {


    int updateConfig(String configName, String configValue);

    /**
     * 获取所有的配置项
     *
     * @return
     */
    Map<String, String> getAllConfigs();
}