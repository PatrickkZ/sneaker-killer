package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillermodel.entity.Order;
import com.patrick.sneakerkillermodel.mapper.OrderMapper;
import com.patrick.sneakerkillerservice.dto.SecondKillDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiveMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveMessageService.class);

    SecondKillService secondKillService;
    SendMailService sendMailService;
    OrderMapper orderMapper;


    @Autowired
    public ReceiveMessageService(SecondKillService secondKillService, OrderMapper orderMapper, SendMailService sendMailService){
        this.secondKillService = secondKillService;
        this.orderMapper = orderMapper;
        this.sendMailService = sendMailService;
    }

    /**
     * 执行秒杀逻辑, RabbitListener修饰的方法不能有返回值, 必须返回void, 否则报错
     * @param dto
     * @return
     */
    @RabbitListener(queues = "${rabbit.second-kill.request.queue}", containerFactory = "multiListenerContainer")
    public void executeKill(SecondKillDto dto){
        Integer itemId = dto.getItemId();
        String size = dto.getSize();
        Integer userId = dto.getUserId();
        if(itemId != null && size != null && userId != null){
            secondKillService.executeKill(itemId, size, userId);
        }
    }

    /**
     * 死信队列中的消息，如果这时候订单还没支付，则修改订单状态为-1，表示超时未支付
     * @param orderId
     */
    @RabbitListener(queues = "${rabbitmq.order.dead.queue}", containerFactory = "multiListenerContainer")
    public void expireOrder(Long orderId){
        if(orderId != null){
            Order order = orderMapper.getById(orderId);
            if(order != null && order.getStatus() == 0){
                logger.info("订单超时未支付, 订单编号:{}", orderId);
                orderMapper.expireOrder(orderId);
            }
        }
    }

    /**
     * 向用户发邮件
     * @param toMail 用户邮箱
     */
    @RabbitListener(queues = "${rabbit.mail.queue}", containerFactory = "multiListenerContainer")
    public void sendMail(String toMail){
        logger.info("发送邮件至:{}",toMail);
        sendMailService.sendMail(toMail);
    }

}
