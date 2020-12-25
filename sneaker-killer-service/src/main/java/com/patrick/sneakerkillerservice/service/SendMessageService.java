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
import org.springframework.stereotype.Service;

@Service
public class SendMessageService {

    RabbitTemplate rabbitTemplate;

    @Autowired
    public SendMessageService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessageToMQ(SecondKillDto secondKillDto){
        try {
            if(secondKillDto != null){
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                rabbitTemplate.setExchange("SecondKillExchange");
                rabbitTemplate.setRoutingKey("second-kill");
                rabbitTemplate.convertAndSend("SecondKillExchange", "second-kill", secondKillDto
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
}
