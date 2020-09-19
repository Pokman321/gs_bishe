package com.de.rabbittest.entity;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author gs
 * @date 2020/8/14 - 11:24
 */
public class VideoMqMessage {

    private Map<String,Object> header;

    private VideoMqMessage.Body body;

    public VideoMqMessage(Map<String,Object> header, VideoMqMessage.Body body){
        this.header = header;
        this.body = body;
    }

    public VideoMqMessage(){}

    public Map<String,Object> getHeader(){
        return this.header;
    }

    public VideoMqMessage.Body getBody(){
        return this.body;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VideoMqMessage.class.getSimpleName() + "[", "]")
                .add("header=" + header)
                .add("body=" + body)
                .toString();
    }

    public static class Body{
        @NotNull
        private String topicCode;
        @NotNull
        private String title;
        @NotNull
        private Date messageTime;

        public Body(){

        }

        public Body(String topicCode,String title){
            this.topicCode = topicCode;
            this.title = title;
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

        public Date getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(Date messageTime) {
            this.messageTime = messageTime;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Body.class.getSimpleName() + "[", "]")
                    .add("topicCode='" + topicCode + "'")
                    .add("title='" + title + "'")
                    .add("messageTime=" + messageTime)
                    .toString();
        }
    }

}
