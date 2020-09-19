package com.de.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author gs
 * @date 2020/6/10 - 18:05
 */
public class Position {
    private int positionId;

    private String positionName;

    private String postionIcon;

    private int hasCameraNum;

    private Byte isDelated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPostionIcon() {
        return postionIcon;
    }

    public void setPostionIcon(String postionIcon) {
        this.postionIcon = postionIcon;
    }

    public int getHasCameraNum() {
        return hasCameraNum;
    }

    public void setHasCameraNum(int hasCameraNum) {
        this.hasCameraNum = hasCameraNum;
    }

    public Byte getIsDelated() {
        return isDelated;
    }

    public void setIsDelated(Byte isDelated) {
        this.isDelated = isDelated;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());

        sb.append(", positionId=").append(positionId);
        sb.append(", positionName='").append(positionName).append('\'');
        sb.append(", postionIcon='").append(postionIcon).append('\'');
        sb.append(", hasCameraNum=").append(hasCameraNum);
        sb.append(", isDelated=").append(isDelated);
        sb.append(", createTime=").append(createTime);
        sb.append(']');
        return sb.toString();
    }
}
