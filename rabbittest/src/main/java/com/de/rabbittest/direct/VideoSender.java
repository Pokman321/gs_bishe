package com.de.rabbittest.direct;

import com.de.util.MyResult;
import com.de.util.ResultGenerator;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
//import com.rabbitmq.client.MessageProperties;
/**
 * @author gs
 * @date 2020/8/4 - 4:10
 */
@Component
//public class VideoSender implements ChannelAwareMessageListener {
public class VideoSender{

    @Autowired
    @Lazy
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue replyQueue;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitAdmin rabbitAdmin;


    public Map<String,Integer> getQueueCount() throws IOException, TimeoutException {


        Map<String,Integer> resultmap = new HashMap<>();
        Connection connection = connectionFactory.createConnection();
        // 创建通道
        Channel channel = connection.createChannel(false);
        // 设置消息交换机
//        channel.exchangeDeclare("amp.topic", "direct", true, false, null);
        AMQP.Queue.DeclareOk declareOk_gpu = channel.queueDeclarePassive("gs.algorithm");
        AMQP.Queue.DeclareOk declareOk_cpu = channel.queueDeclarePassive("gs.service");
        //获取队列中的消息个数
        int gpuCount = declareOk_gpu.getMessageCount();
        int cpuCount = declareOk_cpu.getMessageCount();
        resultmap.put("algorithm", gpuCount);
        resultmap.put("service", cpuCount);

        channel.close();
        connection.close();
        return resultmap;
    }


//    private MyResult result;

    public MyResult send(String msg) throws InterruptedException, IOException {
        Connection connection = connectionFactory.createConnection();
        // 创建通道
        Channel channel = connection.createChannel(false);

        String sendMsg = msg +"---"+ System.currentTimeMillis();;
        System.out.println("Sender : " + sendMsg);

        MessageProperties properties = new MessageProperties();
//        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//        properties.

        this.rabbitTemplate.send("gs.service",new Message(msg.getBytes(),properties));
        this.rabbitTemplate.send("gs.algorithm",new Message(msg.getBytes(),properties));

        while(true){

            Message getResponse = rabbitTemplate.receive("replyMessage");



//                    channel.basicGet(queueName, true);

            if(null!=getResponse){
                MyResult result = ResultGenerator.genSuccessResult();
                String a = new String(getResponse.getBody(), StandardCharsets.UTF_8);
                result.setData(a);
                System.out.println(replyQueue.isAutoDelete());
                channel.basicAck(getResponse.getMessageProperties().getDeliveryTag(),false);

                return result;

            }
            else{
                Thread.sleep(1000);
            }


        }

        //        this.rabbitTemplate.convertAndSend("gs.service", sendMsg);
//        Message message = new Message(sendMsg.getBytes(),null);
//        this.rabbitTemplate.send("gs.algorithm",message);
//        this.rabbitTemplate.convertAndSend("gs.algorithm", sendMsg);

//        while(result!=null){
//            break;
////            return result;
//        }
//
//
//        return result;


//        MyResult result = ResultGenerator.genSuccessResult();
//        result.setData(msg);
//        return result;
    }

//    public MyResult saveResult(String msg){
//        result = ResultGenerator.genSuccessResult();
//        result.setData(msg);
//        return result;
//    }


//    @Override
//    public void onMessage(Message message, Channel channel) throws Exception {
//        try {
//            String msg = new String(message.getBody());
//            if (StringUtils.equals(msg,null)){
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
//                        false,true);
//                System.out.println("生产者拒收"+msg+"处理完的消息");
//            }
//            else{
//                saveResult(msg);
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
//                        false);
//                System.out.println("生产者收到"+msg+"处理完的消息");
//            }
////            saveResult(msg);
////
//////            System.out.println("GpuUserReceiver>>>>>>>接收到消息:"+msg);
////            try {
//////                Thread.sleep(10000);
////                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
////                        false);
////
////                System.out.println("生产者收到"+msg+"处理完的消息");
////            } catch (Exception e) {
////                System.out.println(e.getMessage());
////                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
////                        false,true);
////                System.out.println("生产者拒收"+msg+"处理完的消息");
////                throw e;
////            }
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }







//    public void send(String msg){
//        String sendMsg = msg +"---"+ System.currentTimeMillis();;
//        System.out.println("Sender : " + sendMsg);
//        this.rabbitTemplate.convertAndSend("gs.service", sendMsg);
//        this.rabbitTemplate.convertAndSend("gs.algorithm", sendMsg);
//
//    }

}
