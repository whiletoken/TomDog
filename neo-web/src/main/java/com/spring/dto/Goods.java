package com.spring.dto;

import java.io.Serializable;

/**
 * 商品类
 *
 * @author william
 **/

public class Goods implements Serializable {

    private Long goodsId;

    private String goodsName;

    private Long goodsType;

    public Goods(Long goodsId, String goodsName, Long goodsType) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
    }

    public Goods() {
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Long goodsType) {
        this.goodsType = goodsType;
    }
}
