package com.patrick.sneakerkillermodel.entity;

/**
 * 商品sku,用于表示商品的不同属性
 * 如运动鞋有不同的尺码
 */
public class SneakerSku {
    private Integer id;
    /**
     * 商品id
     */
    private Integer itemId;
    /**
     * 商品的尺码
     */
    private String size;
    /**
     * 某个尺码的商品的库存
     */
    private Integer stock;
}
