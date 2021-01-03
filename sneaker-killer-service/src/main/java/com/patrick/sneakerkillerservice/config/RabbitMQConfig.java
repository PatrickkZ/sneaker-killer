package com.patrick.sneakerkillerservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.order.queue}")
    private String orderQueue;

    @Value("${rabbitmq.order.exchange}")
    private String orderExchange;

    @Value("${rabbitmq.order.routing-key}")
    private String orderKey;

    @Value("${rabbitmq.order.dead.queue}")
    private String orderDeadQueue;

    @Value("${rabbitmq.order.dead.exchange}")
    private String orderDeadExchange;

    @Value("${rabbitmq.order.dead.routing-key}")
    private String orderDeadKey;

    @Value("${rabbit.second-kill.request.queue}")
    private String secondKillQueue;

    @Value("${rabbit.second-kill.request.exchange}")
    private String secondKillExchange;

    @Value("${rabbit.second-kill.request.routing-key}")
    private String secondKillKey;

    @Value("${rabbit.mail.queue}")
    private String sendMailQueue;

    @Value("${rabbit.mail.exchange}")
    private String sendMailExchange;

    @Value("${rabbit.mail.routing-key}")
    private String sendMailKey;


    CachingConnectionFactory connectionFactory;
    SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    @Autowired
    public RabbitMQConfig(CachingConnectionFactory connectionFactory, SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer){
        this.connectionFactory = connectionFactory;
        this.factoryConfigurer = factoryConfigurer;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("消息发送成功");
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("消失丢失");
            }
        });
        return rabbitTemplate;
    }

    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        // TODO 不配这两个不会显示消息发送成功还是失败
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消费模式-NONE
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(20);
        factory.setMaxConcurrentConsumers(30);
        factory.setPrefetchCount(15);
        return factory;
    }

    /**
     * 这里开始是死信队列
     * @return
     */
    @Bean
    public Queue orderQueue(){
        Map<String, Object> argsMap= new HashMap<>();
        argsMap.put("x-dead-letter-exchange", orderDeadExchange);
        argsMap.put("x-dead-letter-routing-key", orderDeadKey);
        return new Queue(orderQueue, true, false, false, argsMap);
    }

    /**
     * 基本交换机
     * @return
     */
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(orderExchange, true, false);
    }

    /**
     * 基本交换机+基本路由 to 死信队列
     * @return
     */
    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(orderKey);
    }

    /**
     * 这里开始是真正处理消息的队列
     * @return
     */
    @Bean
    public Queue orderDeadQueue(){
        return new Queue(orderDeadQueue, true);
    }

    @Bean
    public DirectExchange orderDeadExchange(){
        return new DirectExchange(orderDeadExchange, true, false);
    }

    @Bean
    public Binding deadBinding(){
        return BindingBuilder.bind(orderDeadQueue()).to(orderDeadExchange()).with(orderDeadKey);
    }

    /**
     * 这里开始是接口限流的消息队列
     * @return
     */
    @Bean
    public Queue SecondKillQueue() {
        return new Queue(secondKillQueue,true);
    }

    @Bean
    DirectExchange SecondKillExchange() {
        return new DirectExchange(secondKillExchange,true,false);
    }

    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(SecondKillQueue()).to(SecondKillExchange()).with(secondKillKey);
    }

    /**
     * 这里开始是发邮件的队列
     */
    @Bean
    public Queue sendMailQueue() {
        return new Queue(sendMailQueue, true);
    }

    @Bean
    public DirectExchange sendMailExchange(){
        return new DirectExchange(sendMailExchange, true, false);
    }

    @Bean
    public Binding mailBinding(){
        return BindingBuilder.bind(sendMailQueue()).to(sendMailExchange()).with(sendMailKey);
    }
}
