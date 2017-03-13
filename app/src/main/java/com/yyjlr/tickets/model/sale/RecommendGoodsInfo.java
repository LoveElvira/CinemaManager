package com.yyjlr.tickets.model.sale;

import java.io.Serializable;

/**
 * Created by Richie on 2016/8/23.
 */
public class RecommendGoodsInfo implements Serializable {
    private Long goodsId;//卖品id
    private String goodsName;//卖品名称
    private String goodsDetail;//卖品描述
    private int price;//原价，单位分
    private int appPrice;//app价格，单位分
    private String goodsImg;//卖品图
    private Long startTime;//卖品优惠开始时间
    private Long endTime;//卖品优惠结束时间
    private int selected = 0;//是否选中，1是，0否
    private int num = 1;//数量

    public RecommendGoodsInfo() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(int appPrice) {
        this.appPrice = appPrice;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
