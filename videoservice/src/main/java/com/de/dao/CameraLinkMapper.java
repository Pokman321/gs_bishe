package com.de.dao;

import com.de.entity.CameraLink;
import com.de.util.PageQueryUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gs
 * @date 2020/6/15 - 14:16
 */
@Repository
public interface CameraLinkMapper {
    int deleteByPrimaryKey(Integer linkId);

    int insert(CameraLink record);

    int insertSelective(CameraLink record);

    CameraLink selectByPrimaryKey(Integer linkId);

    int updateByPrimaryKeySelective(CameraLink record);

    int updateByPrimaryKey(CameraLink record);

    List<CameraLink> findLinkList(PageQueryUtil pageUtil);

    int getTotalLinks(PageQueryUtil pageUtil);

    int deleteBatch(Integer[] ids);
}
