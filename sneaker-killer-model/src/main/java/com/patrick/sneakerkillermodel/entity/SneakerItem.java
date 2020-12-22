package com.patrick.sneakerkillermodel.entity;

import java.math.BigDecimal;

public class SneakerItem {
    private Integer id;
    private String name;
    /**
     * 商品图片文件名
     */
    private String image;
    private String detail;
    /**
     * 商品原价
     */
    private BigDecimal price;
    /**
     * 总库存
     */
    private Integer stock;

    /**
     * 所有尺码信息，以逗号分割，如35,36,37,38,39,40
     */
    private String size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
