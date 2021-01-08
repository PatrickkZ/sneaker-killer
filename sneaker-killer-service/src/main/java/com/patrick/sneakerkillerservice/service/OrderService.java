package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillermodel.entity.Order;
import com.patrick.sneakerkillermodel.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderMapper orderMapper){
        this.orderMapper = orderMapper;
    }

    public List<Order> getByUserId(Integer userId){
        return orderMapper.getByUserId(userId);
    }

    public boolean payOrder(Long orderId){
        Order order = orderMapper.getById(orderId);
        if(order.getStatus() != 0){
            return false;
        }
        orderMapper.payOrder(orderId);
        logger.info("订单支付成功,订单编号: {}", orderId);
        return true;
    }
}
