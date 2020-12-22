package com.patrick.sneakerkillermodel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class SecondKillItem {
    private Integer id;
    /**
     * 参与秒杀的商品id
     */
    private Integer itemId;
    /**
     * 秒杀价
     */
    private BigDecimal secondKillPrice;
    /**
     * 秒杀库存
     */
    private Integer secondKillStock;
    /**
     * 秒杀开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    /**
     * 秒杀结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 是否能够购买 0表示不能 1表示能
     * 需要满足条件 a:在活动时间内 b:库存大于0
     */
    private Integer canBuy;

    private SneakerItem sneakerItem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getSecondKillPrice() {
        return secondKillPrice;
    }

    public void setSecondKillPrice(BigDecimal secondKillPrice) {
        this.secondKillPrice = secondKillPrice;
    }

    public Integer getSecondKillStock() {
        return secondKillStock;
    }

    public void setSecondKillStock(Integer secondKillStock) {
        this.secondKillStock = secondKillStock;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public SneakerItem getSneakerItem() {
        return sneakerItem;
    }

    public void setSneakerItem(SneakerItem sneakerItem) {
        this.sneakerItem = sneakerItem;
    }

    public Integer isCanBuy() {
        return canBuy;
    }

    public void setCanBuy(Integer canBuy) {
        this.canBuy = canBuy;
    }
}
