package com.de.service.impl;

import com.de.dao.CameraLinkMapper;
import com.de.entity.CameraLink;
import com.de.service.LinkService;
import com.de.util.PageQueryUtil;
import com.de.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gs
 * @date 2020/6/15 - 14:48
 */
@Service
public class LinkServiceImpl implements LinkService {
    @Autowired
    private CameraLinkMapper cameraLinkMapper;

    @Override
    public PageResult getCameraLinkPage(PageQueryUtil pageUtil) {
        List<CameraLink> links = cameraLinkMapper.findLinkList(pageUtil);
        int total = cameraLinkMapper.getTotalLinks(pageUtil);
        PageResult pageResult = new PageResult(links, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public int getTotalLinks() {
        return cameraLinkMapper.getTotalLinks(null);
    }

    @Override
    public Boolean saveLink(CameraLink link) {
        return cameraLinkMapper.insertSelective(link) > 0;
    }

    @Override
    public CameraLink selectById(Integer id) {
        return cameraLinkMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateLink(CameraLink tempLink) {
        return cameraLinkMapper.updateByPrimaryKeySelective(tempLink) > 0;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return cameraLinkMapper.deleteBatch(ids) > 0;
    }

    @Override
    public Map<Byte, List<CameraLink>> getLinksForLinkPage() {
        //获取所有链接数据
        List<CameraLink> links = cameraLinkMapper.findLinkList(null);
        if (!CollectionUtils.isEmpty(links)) {
            //根据type进行分组
            Map<Byte, List<CameraLink>> linksMap = links.stream().collect(Collectors.groupingBy(CameraLink::getLinkType));
            return linksMap;
        }
        return null;
    }
}
