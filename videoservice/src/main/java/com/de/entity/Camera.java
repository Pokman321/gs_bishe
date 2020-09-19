package com.de.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author gs
 * @date 2020/6/10 - 17:48
 */
//`camera_id`,`camera_name`,`camera_url`,`camera_cover_image`,
// `camera_position_id`,`camera_position_name`,`camera_enable`,
// `is_deleted`,`create_time`,`update_time`
public class Camera {
    private int cameraId;

    private String cameraName;

    private String cameraUrl;

    private String cameraCoverImage;

    private int cameraCategoryId;

    private String cameraCategoryName;

    private int cameraViews;


    private Byte cameraEnable;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private Date updateTime;



    public int getCameraViews() {
        return cameraViews;
    }

    public void setCameraViews(int cameraViews) {
        this.cameraViews = cameraViews;
    }

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

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Camera{");
        sb.append(getClass().getSimpleName());
        sb.append("Hash = ").append(hashCode());
        sb.append(", cameraId=").append(cameraId);
        sb.append(", cameraName='").append(cameraName).append('\'');
        sb.append(", cameraUrl='").append(cameraUrl).append('\'');
        sb.append(", cameraCoverImage='").append(cameraCoverImage).append('\'');
        sb.append(", cameraCategoryId=").append(cameraCategoryId);
        sb.append(", cameraCategoryName='").append(cameraCategoryName).append('\'');

        sb.append(", cameraEnable=").append(cameraEnable);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", cameraViews=").append(cameraViews);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }


}
