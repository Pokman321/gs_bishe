package com.de.rabbittest.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gs
 * @date 2020/8/12 - 11:31
 * 工厂类，将实体类构造为Message，该类具有普适性
 */
public class MessageBuilder {

    private Map<String,Object> header;
    private VideoMqMessage.Body body;

    private MessageBuilder(){
        this.body = new VideoMqMessage.Body();
        this.header = new HashMap<>();
    }


    private MessageBuilder(String topic,String title){
//        VideoMqMessage videoMqMessage = new VideoMqMessage();

//        VideoMqMessage videoMqMessage = new VideoMqMessage();

        this.body = new VideoMqMessage.Body(topic,title);
        this.header = new HashMap<>();

    }


    public static MessageBuilder newMessage(String topic,String title){
        return new MessageBuilder(topic,title);
    }

    public static MessageBuilder newMessage(){
        return new MessageBuilder();
    }

    public MessageBuilder userId(int userId){
        this.header.put("userId",userId);
        return this;
    }

    public MessageBuilder videoId(int videoId){
        this.header.put("videoId",videoId);
        return this;
    }

    public MessageBuilder videoPath(String videoPath){
        this.header.put("videoPath",videoPath);
        return this;
    }

    public MessageBuilder title(String title){
        this.body.setTitle(title);
        return this;
    }

    public MessageBuilder topicCode(String topicCode){
        this.body.setTopicCode(topicCode);
        return this;
    }

    public MessageBuilder messageTime(Date messageTime){
        this.body.setMessageTime(messageTime);
        return this;
    }

    public VideoMqMessage builder(){
        if(this.header.get("userId")!=null && ((String) this.header.get("userId")).trim().length()!=0){
            if(this.header.get("videoId")!=null && ((String) this.header.get("videoId")).trim().length()!=0){
                if(this.header.get("videoPath")!=null && ((String) this.header.get("videoPath")).trim().length()!=0){
                    if(this.body.getTitle()!=null && this.body.getTitle().trim().length()!=0){
                        if(this.body.getTopicCode()!=null && this.body.getTopicCode().trim().length()!=0){
                            VideoMqMessage videoMqMessage = new VideoMqMessage(this.header,this.body);
                            return videoMqMessage;
                        }
                        else{
                            throw new RuntimeException("topicCode is required");
                        }
                    }
                    else{
                        throw new RuntimeException("title is required");
                    }
                }
                else{
                    throw new RuntimeException("videoPath is required");
                }
            }
            else{
                throw new RuntimeException("videoId is required");
            }
        }
        else{
            throw new RuntimeException("userId is required");
        }

    }
}
