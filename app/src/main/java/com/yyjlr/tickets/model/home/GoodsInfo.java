package com.yyjlr.tickets.model.home;

/**
 * Created by Elvira on 2017/4/7.
 */

public class GoodsInfo {
    /**
     * "id": 1,//卖品id
     * "name": "名称",//卖品名称
     * "price":1000, // 卖品价格，单位分
     * "imageUrl": "http://xxxx1.jpg" //活动图
     * "startTime": 1234567888, //卖品优惠开始时间
     * "endTime": 12345678, //卖品优惠结束时间
     * "limitedCount":5, //卖品限制数量，不限制为-1
     * "goodsDesc": "xxxxx" //卖品描述
     */
    private long id;
    private String name;
    private long price;
    private String imageUrl;
    private long startTime;
    private long endTime;
    private int limitedCount;
    private String goodsDesc;

    public GoodsInfo() {
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getLimitedCount() {
        return limitedCount;
    }

    public void setLimitedCount(int limitedCount) {
        this.limitedCount = limitedCount;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
