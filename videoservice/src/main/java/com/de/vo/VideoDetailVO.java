package com.de.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author gs
 * @date 2020/7/14 - 2:53
 */
public class VideoDetailVO {

    private int videoId;

    private String userName;

    private String videoName;

    private String videoPath;

    private String resultPath;

    private Byte hasResult;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date videoTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date resultTime;

    private String cameraCategoryIcon;

    private String cameraCategoryName;

    private Byte isShow;

    private int videoViews;

    private String videoCoverImage;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public Byte getHasResult() {
        return hasResult;
    }

    public void setHasResult(Byte hasResult) {
        this.hasResult = hasResult;
    }

    public Date getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(Date videoTime) {
        this.videoTime = videoTime;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public String getCameraCategoryIcon() {
        return cameraCategoryIcon;
    }

    public void setCameraCategoryIcon(String cameraCategoryIcon) {
        this.cameraCategoryIcon = cameraCategoryIcon;
    }

    public String getCameraCategoryName() {
        return cameraCategoryName;
    }

    public void setCameraCategoryName(String cameraCategoryName) {
        this.cameraCategoryName = cameraCategoryName;
    }

    public Byte getIsShow() {
        return isShow;
    }

    public void setIsShow(Byte isShow) {
        this.isShow = isShow;
    }

    public int getVideoViews() {
        return videoViews;
    }

    public void setVideoViews(int videoViews) {
        this.videoViews = videoViews;
    }

    public String getVideoCoverImage() {
        return videoCoverImage;
    }

    public void setVideoCoverImage(String videoCoverImage) {
        this.videoCoverImage = videoCoverImage;
    }
}
