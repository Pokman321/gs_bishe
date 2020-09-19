package com.de.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author gs
 * @date 2020/7/14 - 2:48
 */
public class VideoListVO {
    private int videoId;

    private String userName;

    private String videoName;

    private String videoCoverImage;

    private int cameraCategoryId;

    private String cameraCategoryIcon;

    private String cameraCategoryName;

    private String userAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date videoTime;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

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

    public String getVideoCoverImage() {
        return videoCoverImage;
    }

    public void setVideoCoverImage(String videoCoverImage) {
        this.videoCoverImage = videoCoverImage;
    }

    public int getCameraCategoryId() {
        return cameraCategoryId;
    }

    public void setCameraCategoryId(int cameraCategoryId) {
        this.cameraCategoryId = cameraCategoryId;
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

    public Date getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(Date videoTime) {
        this.videoTime = videoTime;
    }
}
