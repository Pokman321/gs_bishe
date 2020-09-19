package com.de.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author gs
 * @date 2020/6/12 - 22:58
 */
public class CameraCategory {
    private Integer categoryId;

    private String categoryName;

    private String categoryIcon;

    private Integer hasCameraNum;

    private Integer hasVideoNum;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public Integer getHasVideoNum() {
        return hasVideoNum;
    }

    public void setHasVideoNum(Integer hasVideoNum) {
        this.hasVideoNum = hasVideoNum;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon == null ? null : categoryIcon.trim();
    }

    public Integer getHasCameraNum() {
        return hasCameraNum;
    }

    public void setHasCameraNum(Integer hasCameraNum) {
        this.hasCameraNum = hasCameraNum;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", CameraCategory.class.getSimpleName() + "[", "]")
                .add("categoryId=" + categoryId)
                .add("categoryName='" + categoryName + "'")
                .add("categoryIcon='" + categoryIcon + "'")
                .add("hasCameraNum=" + hasCameraNum)
                .add("hasVideoNum=" + hasVideoNum)
                .add("isDeleted=" + isDeleted)
                .add("createTime=" + createTime)
                .toString();
    }
}
