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
 * @date 2020/8/4 - 3:55
 */
@Component
//@RabbitListener(queues = "gs.algorithm")
public class GpuUserReceiver implements ChannelAwareMessageListener {

    @Resource
    @Lazy
    private RabbitTemplate rabbitTemplate;

    public int cnt;

    @Override
//    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        try {

            cnt++;
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
//                    false,true);

            String msg = new String(message.getBody());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = msg.split("!")[1];

            long time1 = Long.parseLong(time);
            Date date = new Date(time1);
            String res = simpleDateFormat.format(date);
            System.out.println("GpuUserReceiver>>>>>>>接收到消息:"+res);
            try {
                Thread.sleep(10000);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        false);
                rabbitTemplate.convertAndSend("replyMessage",this.getClass().getName()+System.currentTimeMillis());
//                rabbitTemplate.convertAndSend("replyMessage",this.getClass().getName()+System.currentTimeMillis());
                System.out.println("GpuUserReceiver>>>>>>消息已消费");
//                cnt--;
                System.out.println("当前GPU中仍有"+cnt+"条消息已经被消费");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false,true);
                System.out.println("GpuUserReceiver>>>>>>拒绝消息，要求Mq重新派发");
                throw e;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
