package com.de.rabbittest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author gs
 * @date 2020/8/11 - 20:18
 */
public class VideoNotice {

    private Integer id;

    private String topicCode;

    private String title;

    private Integer userId;

    public Integer videoId;

    private String videoName;

    private String videoPath;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date videoTime;

    private Byte isSend;

    private Byte isComplete;

    private Byte isDelete;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date noticeTime;


    public Byte getIsSend() {
        return isSend;
    }

    public void setIsSend(Byte isSend) {
        this.isSend = isSend;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", VideoNotice.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("topicCode='" + topicCode + "'")
                .add("title='" + title + "'")
                .add("userId=" + userId)
                .add("videoId=" + videoId)
                .add("videoName='" + videoName + "'")
                .add("videoPath='" + videoPath + "'")
                .add("videoTime=" + videoTime)
                .add("isSend=" + isSend)
                .add("isComplete=" + isComplete)
                .add("isDelete=" + isDelete)
                .add("noticeTime=" + noticeTime)
                .toString();
    }
}
