package com.de.rabbittest.dao;

import com.de.rabbittest.entity.VideoNotice;
import org.springframework.stereotype.Repository;

/**
 * @author gs
 * @date 2020/8/11 - 20:30
 */
@Repository
public interface VideoMessageMapper {

    int selectUserId(int userId);

    VideoNotice selectVideoId(int videoId);

    int insertMessage(VideoNotice videoNotice);

    int updateMessage(VideoNotice videoNotice);

    Boolean finishSend(VideoNotice videoNotice);

    int deleteByVideoId(int videoId);

    int deleteBatch(Integer[] ids);

}
