package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillermodel.entity.Order;
import com.patrick.sneakerkillermodel.entity.SecondKillItem;
import com.patrick.sneakerkillermodel.mapper.OrderMapper;
import com.patrick.sneakerkillermodel.mapper.SecondKillItemMapper;
import com.patrick.sneakerkillermodel.mapper.SneakerSkuMapper;
import com.patrick.sneakerkillerservice.config.PropertiesConfig;
import com.patrick.sneakerkillerservice.exception.AlreadyBoughtException;
import com.patrick.sneakerkillerservice.util.SnowflakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SecondKillService {
    OrderMapper orderMapper;
    SecondKillItemMapper secondKillItemMapper;
    SneakerSkuMapper sneakerSkuMapper;
    SnowflakeIdGenerator snowflakeIdGenerator;
    PropertiesConfig propertiesConfig = new PropertiesConfig();


    @Autowired
    public SecondKillService(OrderMapper orderMapper, SecondKillItemMapper secondKillItemMapper, SneakerSkuMapper sneakerSkuMapper){
        this.orderMapper = orderMapper;
        this.secondKillItemMapper = secondKillItemMapper;
        this.sneakerSkuMapper = sneakerSkuMapper;
        this.snowflakeIdGenerator = new SnowflakeIdGenerator(propertiesConfig.getWorkerId(), propertiesConfig.getDatacenterId());
    }

    public boolean executeKill(Integer itemId, String size, Integer userId) throws AlreadyBoughtException {
        // 先查询该用户有没有购买过, 限制只能买一次
        Integer buyCount = orderMapper.countByUserIdAndItemId(itemId, userId);
        if(buyCount <=0 ){
            // 获取商品信息
            SecondKillItem secondKillItem = secondKillItemMapper.getByIdAndSize(itemId,size);
            if(secondKillItem != null && secondKillItem.isCanBuy() == 1){
                // 库存扣减1
                int success = sneakerSkuMapper.decreaseStock(itemId, size);
                if(success > 0){
                    this.generateOrderAndNotify(userId, itemId, size, secondKillItem.getSecondKillPrice());
                    return true;
                }
            }
        } else {
            throw new AlreadyBoughtException("已经抢购成功,每位用户限购一件");
        }
        return false;
    }

    // TODO 生成订单和异步通知方法
    private void generateOrderAndNotify(Integer userId, Integer itemId, String shoeSize, BigDecimal secondKillPrice){
        Order order = new Order();
        // 初始化订单
        order.setId(snowflakeIdGenerator.nextId());
        order.setUserId(userId);
        order.setItemId(itemId);
        order.setShoeSize(shoeSize);
        // 默认只能购买一件
        order.setItemCount(1);
        order.setItemPrice(secondKillPrice);
        // 刚创建订单, 默认未支付状态
        order.setStatus(0);

        // 再检查一次
        if(orderMapper.countByUserIdAndItemId(itemId, userId)<=0){
            int number = orderMapper.add(order);
            if(number > 0){
                // TODO 发邮件通知
            }
        }
    }
}
