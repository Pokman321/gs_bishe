//package com.de.config;
//
///**
// * @author gs
// * @date 2020/8/19 - 16:16
// */
//
//
//import com.de.rabbittest.direct.CpuUserReceiver;
//import com.de.rabbittest.direct.GpuUserReceiver;
//import com.de.rabbittest.direct.VideoSender;
//import com.de.rabbittest.direct.RabbitVideoService;
//import com.de.service.videoCalculate.VideoRedisService;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
////import sun.jvm.hotspot.utilities.MessageQueue;
//
///**
// * @author gs
// * @date 2020/8/3 - 19:48
// */
//@Configuration
//@PropertySource("classpath:application.properties")
//public class RabbitConfig {
//
////    @Value("${spring.rabbitmq.host}")
////    private String addresses;
////
////    @Value("${spring.rabbitmq.port}")
////    private String port;
////
////    @Value("${spring.rabbitmq.username}")
////    private String username;
////
////    @Value("${spring.rabbitmq.password}")
////    private String password;
////
////    @Value("${spring.rabbitmq.virtual-host}")
////    private String virtualHost;
////
////    @Value("${spring.rabbitmq.publisher-confirms}")
////    private boolean publisherConfirms;
//
//
//    @Value("127.0.0.1")
//    private String addresses;
//
//    @Value("5672")
//    private String port;
//
//    @Value("guest")
//    private String username;
//
//    @Value("guest")
//    private String password;
//
//    @Value("/")
//    private String virtualHost;
//
//    @Value("true")
//    private boolean publisherConfirms;
//
//
////    @Autowired
////    private CpuUserReceiver cpuUserReceiver;
//
//    @Autowired
//    private VideoRedisService videoRedisService;
//
//
////    @Autowired
////    private VideoRedisService videoRedisService;
//
////    @Autowired
////    private GpuUserReceiver gpuUserReceiver;
//
//    @Autowired
//    private VideoSender videoSender;
//
//    @Autowired
//    private RabbitVideoService rabbitVideoService;
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setAddresses(addresses+":"+port);
//        connectionFactory.setUsername(username);
//        connectionFactory.setPassword(password);
//        connectionFactory.setVirtualHost(virtualHost);
//        /** 如果要进行消息回调，则这里必须要设置为true */
//        //发送者确认
//        connectionFactory.setPublisherConfirms(publisherConfirms);
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
//        return new RabbitAdmin(connectionFactory);
//    }
//
//    @Bean
//    public RabbitTemplate newRabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        //失败确认
//        template.setMandatory(true);
//        template.setConfirmCallback(confirmCallback());
//        template.setReturnCallback(returnCallback());
//        return template;
//    }
//
//    //===============使用了RabbitMQ系统缺省的交换器==========
//    //绑定键即为队列名称
//    @Bean
//    public Queue cpuQueue() {
////        return new Queue("gs.service",true);
//        return new Queue("gs.service");
//    }
//
//    @Bean
//    public Queue gpuQueue() {
////        return new Queue("gs.algorithm",true);
//        return new Queue("gs.algorithm");
//    }
//
//    @Bean
//    public Queue replyQueue() {
//        return new Queue("replyMessage");
//    }
//
//
//
//    @Bean
//    public DirectExchange exchange(){
////        return new DirectExchange("direct");
////        return new DirectExchange("direct",true,false);
//        return new DirectExchange("direct");
//    }
//
//
//
//    @Bean
//    public Binding bindingCpuExchange() {
//        return BindingBuilder
//                .bind(cpuQueue())
//                .to(exchange())
//                .with("gs.service");
//    }
//
//    @Bean
//    public Binding bindingGpuExchange(){
//        return BindingBuilder
//                .bind(gpuQueue()).to(exchange()).with("gs.algorithm");
//    }
//
//
//    @Bean
//    public Binding bindingReplyExchange(){
//        return BindingBuilder.bind(replyQueue()).to(exchange()).with("replyMessage");
//    }
//
//
//    //===============两个消费者确认==========
//    @Bean
//    public SimpleMessageListenerContainer cpuMessageContainer() {
//        SimpleMessageListenerContainer container
//                = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueues(cpuQueue());
////        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(videoRedisService);
////        container.setMessageListener();
//        return container;
//    }
//
//
////    @Bean
////    public SimpleMessageListenerContainer gpuMessageContainer() {
////        SimpleMessageListenerContainer container
////                = new SimpleMessageListenerContainer(connectionFactory());
////        container.setQueues(gpuQueue());
////        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
////        container.setMessageListener(gpuUserReceiver);
////        return container;
////    }
//
//    //
//    @Bean
//    public SimpleMessageListenerContainer replyMessageContainer(){
//        SimpleMessageListenerContainer container
//                = new SimpleMessageListenerContainer(connectionFactory());
//        container.setQueues(replyQueue());
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        container.setMessageListener(rabbitVideoService);
//        return container;
//    }
//
//
//
//    //发送者配置
//    //===============生产者发送确认==========
//    @Bean
//    public RabbitTemplate.ConfirmCallback confirmCallback(){
//
//        return new RabbitTemplate.ConfirmCallback(){
//
//            @Override
//            public void confirm(CorrelationData correlationData,
//                                boolean ack, String cause) {
//                if (ack) {
//
////                    System.out.println(correlationData+"发送者确认发送给mq成功");
//                } else {
//                    //处理失败的消息
//                    System.out.println("发送者发送给mq失败,考虑重发:"+cause);
//                }
//            }
//        };
//    }
//    //发送者配置
//    @Bean
//    public RabbitTemplate.ReturnCallback returnCallback(){
//        return new RabbitTemplate.ReturnCallback(){
//
//            @Override
//            public void returnedMessage(Message message,
//                                        int replyCode,
//                                        String replyText,
//                                        String exchange,
//                                        String routingKey) {
//                System.out.println("无法路由的消息，需要考虑另外处理。");
//                System.out.println("Returned replyText："+replyText);
//                System.out.println("Returned exchange："+exchange);
//                System.out.println("Returned routingKey："+routingKey);
//                String msgJson  = new String(message.getBody());
//                System.out.println("Returned Message："+msgJson);
//            }
//        };
//    }
//
//
//}
