package com.de.rabbittest.direct;

import com.de.rabbittest.dao.VideoMessageMapper;
import com.de.rabbittest.dao.VideoNoticeMapper;
import com.de.rabbittest.entity.VideoMqMessage;
import com.de.rabbittest.entity.VideoNotice;
import com.de.rabbittest.entity.MessageBuilder;
import com.de.rabbittest.utils.GsonUtil;
import com.de.util.MyResult;
import com.de.util.ResultGenerator;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import jdk.internal.jline.internal.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author gs
 * @date 2020/8/7 - 15:09
 */
@Service
@Slf4j
public class RabbitVideoService implements ChannelAwareMessageListener {

    @Autowired
    @Lazy
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue replyQueue;
    
    @Autowired
    private VideoMessageMapper videoMessageMapper;

    @Autowired
    private VideoNoticeMapper videoNoticeMapper;


//    @Autowired
//    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    //获取消息队列中的消息数量，判断是否适合在线等待
//    public Map<String,Integer> getMessageCount() throws IOException {
    public MyResult getMessageCount() throws IOException {
//    public void getMessageCount() throws IOException {
        String cmd = "cmd /c rabbitmqctl list_queues";
//        String cmd = "python E:\\leetcode\\Ali_test\\1.py";
//                System.out.println(cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        // 取得命令结果的输出流
        InputStream fis = p.getInputStream();
        // 用一个读输出流类去读
        InputStreamReader isr = new InputStreamReader(fis);
        // 用缓冲器读行
        BufferedReader br = new BufferedReader(isr);
        String line;
        int cpu_message = 0;
        int gpu_message = 0;

        while((line = br.readLine()) != null){
//            System.out.println(line);
            if(line.contains("gs.service")){
                int cnt=0;
//                String[] s_cpu = line.split(" ");
//                System.out.println(s_cpu[s_cpu.length-1]);

                int i = line.length()-1;

                while(line.charAt(i)-'0'>=0 && line.charAt(i)-'0'<=9){
                    cpu_message+=Math.pow(10,cnt)*(line.charAt(i)-'0')+cpu_message;
                    i--;
                    cnt++;
                }
//                cpu_message =  line.charAt(line.length()-1);
//                cpu_message =Integer.parseInt(s_cpu[s_cpu.length-1]);
            }
            else if(line.contains("gs.algorithm")){
                int j = line.length()-1;

                int cnt2=0;

                while(line.charAt(j)-'0'>=0 && line.charAt(j)-'0'<=9){
                    gpu_message+=Math.pow(10,cnt2)*(line.charAt(j)-'0')+gpu_message;
                    j--;
                    cnt2++;
                }
            }

        }
//        Map<String,Integer> message_map = new HashMap<>();
//        message_map.put("service",cpu_message);
//        message_map.put("algorithm",gpu_message);

        MyResult result = ResultGenerator.genSuccessResult();

        result.setData(gpu_message);
        return result;

//        System.out.println(cpu_message+"  "+gpu_message);
//        float res_score = Float.parseFloat(line);
    }


    public Map<String,Integer> getQueueCount() throws IOException, TimeoutException {


        System.out.println("发送获取消息数量请求");
        Map<String,Integer> resultmap = new HashMap<>();
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        // 创建通道
        Channel channel = connection.createChannel(false);
        channel.exchangeDeclare("direct","direct",true,false,null);
        // 设置消息交换机
//        channel.dele
//        channel.exchangeDeclare("direct", "direct", true, false, null);
        AMQP.Queue.DeclareOk declareOk_gpu = channel.queueDeclarePassive("gs.algorithm");
        AMQP.Queue.DeclareOk declareOk_cpu = channel.queueDeclarePassive("gs.service");
        AMQP.Queue.DeclareOk declareOk_reply = channel.queueDeclarePassive("replyMessage");


        //获取队列中的消息个数
        int gpuCount = declareOk_gpu.getMessageCount();
        int cpuCount = declareOk_cpu.getMessageCount();
        int replyCount = declareOk_reply.getMessageCount();
        resultmap.put("algorithm", gpuCount);
        resultmap.put("service", cpuCount);
        resultmap.put("reply", replyCount);

        System.out.println(resultmap);

        channel.close();
        connection.close();
        return resultmap;
    }

    public Boolean checkNotice(VideoNotice videoNotice){
        if(videoNotice.getUserId()==null){
            Log.error("-------------------- checkNotice error : userId is null");
            return Boolean.FALSE;
        }
        if(videoNotice.getVideoId()==null){
            Log.error("-------------------- checkNotice error : videoId is null");
            return Boolean.FALSE;
        }
//        if(videoNotice.getVideoName()==null){
//            Log.error("-------------------- checkNotice error : videoName is null");
//            return Boolean.FALSE;
//        }
        if(videoNotice.getVideoTime()==null){
            Log.error("-------------------- checkNotice error : videoTime is null");
            return Boolean.FALSE;
        }
        if(videoNotice.getVideoPath()==null){
            Log.error("-------------------- checkNotice error : videoPath is null");
            return Boolean.FALSE;
        }

        //当消息时间未设置时，在此处设置为当前时间
        if(videoNotice.getNoticeTime()==null){
            videoNotice.setNoticeTime(new Date(System.currentTimeMillis()));
        }

        return Boolean.TRUE;
    }


    //
    public MyResult send(VideoNotice videoNotice) throws InterruptedException, UnsupportedEncodingException {

        //消息检查
        if(this.checkNotice(videoNotice)){
            int userId = videoNotice.getUserId();

            //检查消息数据库中是否有该用户未处理完的视频，如果有，不予受理
            if (videoNoticeMapper.selectUserId(userId)!=null){
                return ResultGenerator.genFailResult("您有尚未处理完的视频，请等待后再上传新的");
            }

            //将视频保存到消息数据库中，以便后续查询和故障恢复
            int i = videoNoticeMapper.insert(videoNotice);
            //当消息保存数据库成功，构造message
            if(i>0){
                MessageBuilder messageBuilder = MessageBuilder.newMessage(videoNotice.getTopicCode(),videoNotice.getTitle());
                messageBuilder.userId(videoNotice.getUserId())
                        .videoId(videoNotice.getVideoId())
                        .videoPath(videoNotice.getVideoPath())
                        .messageTime(videoNotice.getNoticeTime());
                VideoMqMessage videoMqMessage = messageBuilder.builder();
                //将消息转化为jsonstr的格式
                String msg = GsonUtil.objectToJsonStr(videoMqMessage);
                MessageProperties properties = new MessageProperties();
                try{
                    this.rabbitTemplate.send("gs.service",new Message(msg.getBytes(),properties));
                    this.rabbitTemplate.send("gs.algorithm",new Message(msg.getBytes(),properties));
                    videoNotice.setIsSend((byte) 1);
                    videoMessageMapper.finishSend(videoNotice);
                    return ResultGenerator.genSuccessResult("已成功将视频添加到消息队列中");
                } catch (Exception e){
                    log.error("--------------------- saveAndSend error !!! ", e);
                    return ResultGenerator.genFailResult("发送失败");
                }
            }

        }
        return null;

    }

    //等待CPU处理后返回的消息(按照"#用户id#视频id#结果路径#结果时间"构成)
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msg = new String(message.getBody());

            if (StringUtils.equals(msg,null)){
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                        false,true);
                System.out.println("生产者拒收"+msg+"处理完的消息");
            }
            else{
                System.out.println("接受到的视频路径为"+msg);
                String userId = msg.split("#")[0];
                String videoId = msg.split("#")[1];
                String resultPath = msg.split("#")[2];
                String resultTime = msg.split("#")[3];

                videoMessageMapper.selectUserId(Integer.parseInt(userId));

                WebSocketRabbit.sendInfo(msg,userId);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                        false);
//                System.out.println("生产者收到"+msg+"处理完的消息");
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
