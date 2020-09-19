package com.de.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gs
 * @date 2020/6/14 - 21:56
 */
public class CameraListVO implements Serializable {

    private int cameraId;

    private String cameraName;

    private String cameraUrl;

    private String cameraCoverImage;

    private int cameraCategoryId;

    private String cameraCategoryIcon;

    private String cameraCategoryName;

    private Byte cameraEnable;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public String getCameraCoverImage() {
        return cameraCoverImage;
    }

    public void setCameraCoverImage(String cameraCoverImage) {
        this.cameraCoverImage = cameraCoverImage;
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

    public Byte getCameraEnable() {
        return cameraEnable;
    }

    public void setCameraEnable(Byte cameraEnable) {
        this.cameraEnable = cameraEnable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
