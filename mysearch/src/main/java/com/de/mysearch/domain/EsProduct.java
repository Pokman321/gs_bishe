package com.de.mysearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.StringJoiner;

/**
 * @author gs
 * @date 2020/8/6 - 12:19
 */
@Document(indexName = "gs_new",type = "product",shards = 1,replicas = 0)
public class EsProduct {

    @Id
    private int videoId;

    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String userName;

    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String videoName;

    private String videoPath;

    private String resultPath;

    private Byte hasResult;

//    @Field(type = FieldType.Date)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @Field( type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @Field( type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||yyyy-MM")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date videoTime;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    @Field( type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    @Field( type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||yyyy-MM")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    private Date resultTime;

    private Byte isShow;

    private int cameraCategoryId;

    @Field(type = FieldType.Keyword)
    private String cameraCategoryName;

    private int videoViews;

    private String videoCoverImage;

//    @Field(type = FieldType.)
//    private String[] tags;

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

    public Byte getIsShow() {
        return isShow;
    }

    public void setIsShow(Byte isShow) {
        this.isShow = isShow;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", EsProduct.class.getSimpleName() + "[", "]")
                .add("videoId=" + videoId)
                .add("userName='" + userName + "'")
                .add("videoName='" + videoName + "'")
                .add("videoPath='" + videoPath + "'")
                .add("resultPath='" + resultPath + "'")
                .add("hasResult=" + hasResult)
                .add("videoTime=" + videoTime)
                .add("resultTime=" + resultTime)
                .add("isShow=" + isShow)
                .add("cameraCategoryId=" + cameraCategoryId)
                .add("cameraCategoryName='" + cameraCategoryName + "'")
                .add("videoViews=" + videoViews)
                .add("videoCoverImage='" + videoCoverImage + "'")
                .toString();
    }
}
