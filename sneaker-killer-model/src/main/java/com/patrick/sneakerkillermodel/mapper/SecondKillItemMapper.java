package com.patrick.sneakerkillermodel.mapper;

import com.patrick.sneakerkillermodel.entity.SecondKillItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondKillItemMapper {
    List<SecondKillItem> listAllItemCanSecondSkill();

    /**
     * 根据商品id和尺码查询商品信息,其中canBuy属性会根据时间和库存进行判断
     * @param id
     * @param size
     * @return
     */
    SecondKillItem getByIdAndSize(@Param("id") Integer id, @Param("size") String size);
}
