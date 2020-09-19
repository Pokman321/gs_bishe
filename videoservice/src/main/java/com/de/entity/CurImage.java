package com.de.entity;

import java.util.StringJoiner;

/**
 * @author gs
 * @date 2020/6/11 - 18:07
 */
public class CurImage {

    private int id;

    private int cameraId;

    private long imageTime;

    private String imageUrl;

    private int personNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public long getImageTime() {
        return imageTime;
    }

    public void setImageTime(long imageTime) {
        this.imageTime = imageTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurImage.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cameraId=" + cameraId)
                .add("imageTime=" + imageTime)
                .add("imageUrl='" + imageUrl + "'")
                .add("personNum=" + personNum)
                .toString();
    }
}
