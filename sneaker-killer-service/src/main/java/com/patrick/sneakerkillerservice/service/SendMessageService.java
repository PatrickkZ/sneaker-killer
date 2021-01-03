package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillerservice.dto.SecondKillDto;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendMessageService {

    @Value("${rabbit.second-kill.request.exchange}")
    private String secondKillExchange;

    @Value("${rabbit.second-kill.request.routing-key}")
    private String secondKillKey;

    @Value("${rabbitmq.order.exchange}")
    private String orderExchange;

    @Value("${rabbitmq.order.routing-key}")
    private String orderKey;

    @Value("${rabbit.mail.exchange}")
    private String sendMailExchange;

    @Value("${rabbit.mail.routing-key}")
    private String sendMailKey;

    @Value("${order.expire-time}")
    private String orderExpireTime;

    RabbitTemplate rabbitTemplate;

    @Autowired
    public SendMessageService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageToMQ(SecondKillDto secondKillDto){
        try {
            if(secondKillDto != null){
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(secondKillExchange);
                rabbitTemplate.setRoutingKey(secondKillKey);
                rabbitTemplate.convertAndSend(secondKillDto
                        , message -> {
                            MessageProperties mp=message.getMessageProperties();
                            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,SecondKillDto.class);
                            return message;
                        });
            }
        }catch (Exception e){
            // TODO 改log
            System.out.println("消息发送异常");
        }
    }

    public void sendMessageToMailMQ(String toMail){
        try {
            if(toMail != null){
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(sendMailExchange);
                rabbitTemplate.setRoutingKey(sendMailKey);
                rabbitTemplate.convertAndSend(toMail
                        , message -> {
                            MessageProperties mp=message.getMessageProperties();
                            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,String.class);
                            return message;
                        });
            }
        }catch (Exception e){
            // TODO 改log
            System.out.println("消息发送异常");
        }
    }

    // TODO setHeader?
    public void sendMessageToDeadQueue(Long orderId){
        try {
            if(orderId != null){
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange(orderExchange);
                rabbitTemplate.setRoutingKey(orderKey);
                rabbitTemplate.convertAndSend(orderId
                        , message -> {
                            MessageProperties mp=message.getMessageProperties();
                            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,SecondKillDto.class);
                            // TODO 设置过期时间 测试设置为30s
                            mp.setExpiration(orderExpireTime);
                            return message;
                        });
            }
        }catch (Exception e){
            System.out.println("消息发送异常");
        }
    }
}
