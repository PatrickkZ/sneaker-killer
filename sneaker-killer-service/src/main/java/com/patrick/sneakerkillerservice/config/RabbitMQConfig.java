package com.patrick.sneakerkillerservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

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

    // 队列 起名：SecondKillQueue
    @Bean
    public Queue SecondKillQueue() {
        return new Queue("SecondKillQueue",true);
    }

    // Direct交换机 起名：SecondKillExchange
    @Bean
    DirectExchange SecondKillExchange() {
        return new DirectExchange("SecondKillExchange",true,false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：second-kill
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(SecondKillQueue()).to(SecondKillExchange()).with("second-kill");
    }
}
