package com.patrick.sneakerkillermodel.mapper;

import com.patrick.sneakerkillermodel.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {
    /**
     * 查询结果>1则表示该用户已经秒杀过该商品
     * @param itemId 商品id
     * @param userId 用户id
     * @return
     */
    Integer countByUserIdAndItemId(@Param("itemId") Integer itemId, @Param("userId") Integer userId);

    /**
     * 添加一条订单记录
     * @return
     */
    int add(Order order);
}
