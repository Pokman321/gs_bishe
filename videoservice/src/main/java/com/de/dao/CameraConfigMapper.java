package com.de.dao;

import com.de.entity.CameraConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gs
 * @date 2020/6/15 - 14:19
 */
@Repository
public interface CameraConfigMapper {
    List<CameraConfig> selectAll();

    CameraConfig selectByPrimaryKey(String configName);

    int updateByPrimaryKeySelective(CameraConfig record);
}
