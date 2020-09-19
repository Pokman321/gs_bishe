package com.de.rabbittest.direct;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gs
 * @date 2020/8/4 - 3:51
 * 只有消息者确认，如果一台计算机无法处理消息，可以要求mq发给其他任意消费者，
 * 添加消费者回应生产者模式，构建一个消费者到生产者回复的信道
 */
@Component
//@RabbitListener(queues = "gs.service")
public class CpuUserReceiver implements ChannelAwareMessageListener {

//    @Autowired
    @Resource
    @Lazy
    private RabbitTemplate rabbitTemplate;

    @Override
//    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        try {

//            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
//                    false,true);


            String msg = new String(message.getBody());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = msg.split("!")[1];

            long time1 = Long.parseLong(time);
            Date date = new Date(time1);
            String res = simpleDateFormat.format(date);
            System.out.println("CpuUserReceiver>>>>>>>接收到消息:"+res);


//            System.out.println("CpuUserReceiver>>>>>>>接收到消息:"+msg);
            try {
                Thread.sleep(5000);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        false);
                rabbitTemplate.convertAndSend("replyMessage",this.getClass().getName()+System.currentTimeMillis());
//                rabbitTemplate.
                System.out.println("CpuUserReceiver>>>>>>消息已消费");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false,true);
                System.out.println("CpuUserReceiver>>>>>>拒绝消息，要求Mq重新派发");
                throw e;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }




}
