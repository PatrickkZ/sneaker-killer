package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillermodel.entity.SecondKillItem;
import com.patrick.sneakerkillermodel.mapper.OrderMapper;
import com.patrick.sneakerkillermodel.mapper.SecondKillItemMapper;
import com.patrick.sneakerkillermodel.mapper.SneakerSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecondKillService {

    OrderMapper orderMapper;
    SecondKillItemMapper secondKillItemMapper;
    SneakerSkuMapper sneakerSkuMapper;

    @Autowired
    public SecondKillService(OrderMapper orderMapper, SecondKillItemMapper secondKillItemMapper, SneakerSkuMapper sneakerSkuMapper){
        this.orderMapper = orderMapper;
        this.secondKillItemMapper = secondKillItemMapper;
        this.sneakerSkuMapper = sneakerSkuMapper;
    }

    public boolean executeKill(Integer itemId, String size, Integer userId){
        // 先查询该用户有没有购买过, 限制只能买一次
        Integer buyCount = orderMapper.countByUserIdAndItemId(itemId, userId);
        if(buyCount <=0 ){
            // 获取商品信息
            SecondKillItem secondKillItem = secondKillItemMapper.getByIdAndSize(itemId,size);
            if(secondKillItem != null && secondKillItem.isCanBuy() == 1){
                // 库存扣减1
                int success = sneakerSkuMapper.decreaseStock(itemId, size);
                if(success > 0){
                    // TODO 生成订单以及扣减成功异步通知
                    return true;
                }
            }
        }
        // TODO buyCount>0提示用户已经购买过
        return false;
    }

    // TODO 生成订单和异步通知方法
}
