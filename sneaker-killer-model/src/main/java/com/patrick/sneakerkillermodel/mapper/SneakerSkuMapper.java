package com.patrick.sneakerkillermodel.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SneakerSkuMapper {
    /**
     * 返回值表示匹配的条数 TODO 如何表示实际被修改的条数？
     * @param itemId
     * @param size
     * @return
     */
    int decreaseStock(@Param("itemId") Integer itemId, @Param("size") String size);
}
