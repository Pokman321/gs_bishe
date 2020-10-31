package com.de.rabbittest.entity;

import java.io.Serializable;
import java.util.Date;

/**
@author gs
@date 2020/10/31 - 15:33
*/

/**
 * video_notice
 */
public class VideoNotice implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * topicCode
     */
    private String topicCode;

    /**
     * title
     */
    private String title;

    /**
     * userId
     */
    private Integer userId;

    /**
     * videoId
     */
    private Integer videoId;

    /**
     * videoName
     */
    private String videoName;

    /**
     * videoPath
     */
    private String videoPath;

    /**
     * videoTime
     */
    private Date videoTime;

    /**
     * isSend
     */
    private Byte isSend;

    /**
     * isComplete
     */
    private Byte isComplete;

    /**
     * isDelete
     */
    private Byte isDelete;

    /**
     * noticeTime
     */
    private Date noticeTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Date getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(Date videoTime) {
        this.videoTime = videoTime;
    }

    public Byte getIsSend() {
        return isSend;
    }

    public void setIsSend(Byte isSend) {
        this.isSend = isSend;
    }

    public Byte getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Byte isComplete) {
        this.isComplete = isComplete;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }
}