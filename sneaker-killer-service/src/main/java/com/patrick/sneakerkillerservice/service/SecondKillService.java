package com.patrick.sneakerkillerservice.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.patrick.sneakerkillermodel.entity.Order;
import com.patrick.sneakerkillermodel.entity.SecondKillItem;
import com.patrick.sneakerkillermodel.mapper.OrderMapper;
import com.patrick.sneakerkillermodel.mapper.SecondKillItemMapper;
import com.patrick.sneakerkillermodel.mapper.SneakerSkuMapper;
import com.patrick.sneakerkillermodel.mapper.UserMapper;
import com.patrick.sneakerkillerservice.config.PropertiesConfig;
import com.patrick.sneakerkillerservice.exception.AlreadyBoughtException;
import com.patrick.sneakerkillerservice.util.SnowflakeIdGenerator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
public class SecondKillService {
    private static final Logger logger = LoggerFactory.getLogger(SecondKillService.class);

    OrderMapper orderMapper;
    UserMapper userMapper;
    SecondKillItemMapper secondKillItemMapper;
    SneakerSkuMapper sneakerSkuMapper;
    SnowflakeIdGenerator snowflakeIdGenerator;
    RedissonClient redissonClient;
    PropertiesConfig propertiesConfig;
    SendMessageService sendMessageService;

    @Autowired
    public SecondKillService(OrderMapper orderMapper, UserMapper userMapper, SecondKillItemMapper secondKillItemMapper, SneakerSkuMapper sneakerSkuMapper, RedissonClient redissonClient, PropertiesConfig propertiesConfig, SendMessageService sendMessageService){
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
        this.secondKillItemMapper = secondKillItemMapper;
        this.sneakerSkuMapper = sneakerSkuMapper;
        this.redissonClient = redissonClient;
        this.propertiesConfig = propertiesConfig;
        this.sendMessageService = sendMessageService;
        this.snowflakeIdGenerator = new SnowflakeIdGenerator(this.propertiesConfig.getWorkerId(), this.propertiesConfig.getDatacenterId());
    }

    /**
     * 执行秒杀逻辑
     * @param itemId 商品id
     * @param size 鞋的尺码
     * @param userId 用户id
     */
    public void executeKill(Integer itemId, String size, Integer userId){
        // 这相当于对sneaker_sku这张表的一行上锁
        String lockKey = new StringBuffer().append(userId).append(itemId).append("-RedissonLock").toString();

        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 分布式锁
            boolean cache = lock.tryLock(30, 10, TimeUnit.SECONDS);

            if(cache){
                // 先查询该用户有没有购买过, 限制只能买一次
                Integer buyCount = orderMapper.countByUserIdAndItemId(itemId, userId);
                if(buyCount <=0 ){
                    // 获取商品信息
                    SecondKillItem secondKillItem = secondKillItemMapper.getByIdAndSize(itemId,size);
                    if(secondKillItem != null && secondKillItem.isCanBuy() == 1){
                        // 库存扣减1
                        int success = sneakerSkuMapper.decreaseStock(itemId, size);
                        logger.info("库存扣减成功 userId:{}, itemId: {}, size: {}", userId, itemId, size);
                        if(success > 0){
                            this.generateOrderAndNotify(userId, itemId, size, secondKillItem.getSecondKillPrice());
                        }
                    }
                } else {
                    logger.error("用户重复购买userId:{},itemId{}", userId, itemId);
                    throw new AlreadyBoughtException("已抢购成功,每位用户限购一件");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * 生成订单和异步邮件通知
     *
     * @param userId
     * @param itemId
     * @param shoeSize
     * @param secondKillPrice
     */
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
        logger.info("创建订单:{}", JSONObject.toJSONString(order, SerializerFeature.WriteMapNullValue));

        // 再检查一次
        if(orderMapper.countByUserIdAndItemId(itemId, userId)<=0){
            int number = orderMapper.add(order);
            logger.info("订单入库:orderID:{}",order.getId());
            if(number > 0){
                sendMessageService.sendMessageToDeadQueue(order.getId());
                // 发消息到邮件队列
                sendMessageService.sendMessageToMailMQ(userMapper.getMailById(userId));
            }
        }
    }
}
