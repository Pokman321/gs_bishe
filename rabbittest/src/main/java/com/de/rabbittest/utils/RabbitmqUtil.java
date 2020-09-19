//package com.de.rabbittest.utils;
//
///**
// * @author gs
// * @date 2020/8/7 - 18:23
// */
//
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.connection.Connection;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.util.Assert;
//
//import java.io.IOException;
//
///**
// * 监控rabbitmq的工具类
// */
//@Slf4j
//public class RabbitMqUtil{
//    private static RabbitTemplate amqpTemplate;
//
//    /**
//     * 初始化amqpTemplate
//     */
//    static {
//        amqpTemplate = SpringContextUtil.getBean(RabbitTemplate.class);
//        Assert.notNull(amqpTemplate,"获取不到amqpTemplate");
//    }
//
//    /**
//     * 查询队列中的消息数量
//     * @param exchange 交换机名字
//     * @param exchangeType 交换机类型 fanout 或 direct
//     * @param quene 队列名字
//     * @return
//     */
//    public static BaseResultDTO getMessageCount(String exchange, String exchangeType, String quene) {
//        Assert.hasText(exchange,"exchange不能为空");
//        Assert.hasText(quene,"队列名不能为空");
//        Assert.hasText(exchangeType,"exchangeType不能为空");
//        checkInitSuccess();
//
//
//        BaseResultDTO baseResultDTO = new BaseResultDTO();
//        ConnectionFactory connectionFactory = amqpTemplate.getConnectionFactory();
//        // 创建连接
//        Connection connection = connectionFactory.createConnection();
//        // 创建通道
//        Channel channel = connection.createChannel(false);
//        // 设置消息交换机
//        try {
//            channel.exchangeDeclare(exchange, exchangeType, true, false, null);
//            AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(quene);
//            //获取队列中的消息个数
//            Integer queueCount = declareOk.getMessageCount();
//            // 关闭通道和连接
//            channel.close();
//            connection.close();
//
//            baseResultDTO.setSuccess(true);
//            baseResultDTO.setMessage(queueCount.toString());
//        } catch (IOException e) {
//            LoggerFormatUtil.warn(e,log,"连接rabbitmq异常");
//        }
//
//
//        return baseResultDTO;
//    }
//
//
//    /**
//     * 校验amqpTemplate是否已经实例化
//     * @return
//     */
//    private static void checkInitSuccess(){
//        if (null == amqpTemplate){
//            amqpTemplate = SpringContextUtil.getBean(RabbitTemplate.class);
//        }
//        Assert.notNull(amqpTemplate, "amqpTemplate 实例化异常");
//    }
//}
//
//
