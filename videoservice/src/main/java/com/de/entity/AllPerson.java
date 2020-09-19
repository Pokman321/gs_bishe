package com.de.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author gs
 * @date 2020/6/10 - 18:15
 */
public class AllPerson {
    private int personId;

    private String personName;

    private String personImage;

    private Byte isDelated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonImage() {
        return personImage;
    }

    public void setPersonImage(String personImage) {
        this.personImage = personImage;
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
        sb.append(", personId=").append(personId);
        sb.append(", personName='").append(personName).append('\'');
        sb.append(", personImage='").append(personImage).append('\'');
        sb.append(", isDelated=").append(isDelated);
        sb.append(", createTime=").append(createTime);
        sb.append(']');
        return sb.toString();
    }
}
