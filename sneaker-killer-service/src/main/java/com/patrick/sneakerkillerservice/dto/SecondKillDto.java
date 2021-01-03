package com.patrick.sneakerkillerservice.dto;

import javax.validation.constraints.NotNull;

public class SecondKillDto {
    /**
     * 商品id
     */
    @NotNull(message = "item id can not be null")
    private Integer itemId;

    /**
     * 商品尺码信息
     */
    @NotNull(message = "shoe size can not be null")
    private String size;
    /**
     * 用户id,从token中获取，不需要校验
     */
    private Integer userId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
