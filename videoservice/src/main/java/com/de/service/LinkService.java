package com.de.service;

import com.de.entity.CameraLink;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author gs
 * @date 2020/6/15 - 14:39
 */
public interface LinkService {
    /**
     * 查询友链的分页数据
     *
     * @param pageUtil
     * @return
     */
    PageResult getCameraLinkPage(PageQueryUtil pageUtil);

    int getTotalLinks();

    Boolean saveLink(CameraLink link);

    CameraLink selectById(Integer id);

    Boolean updateLink(CameraLink tempLink);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回友链页面所需的所有数据
     *
     * @return
     */
    Map<Byte, List<CameraLink>> getLinksForLinkPage();
}
