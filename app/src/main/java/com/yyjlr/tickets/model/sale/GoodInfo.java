package com.yyjlr.tickets.model.sale;

/**
 * Created by Elvira on 2016/12/20.
 * 商品信息
 */

public class GoodInfo {
    /**
     *  "goodsId": 300674, //卖品ID
     "goodsName": "卖品名称",
     "goodsDesc": "xxxxx", //卖品描述
     "price": 56, //原价格，单位分
     "appPrice": 56, //APP价格，单位分
     "memberPrice": 56, //会员价格，单位分
     "goodsImg": "http://xxxx1.jpg", //卖品图
     "startTime": 1234567888, //卖品优惠开始时间
     "endTime": 12345678 //卖品优惠结束时间
     * */
    private long goodsId;
    private String goodsName;
    private String goodsDesc;
    private long price;
    private long appPrice;
    private long memberPrice;
    private String goodsImg;
    private long startTime;
    private long endTime;

    public GoodInfo() {
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getAppPrice() {
        return appPrice;
    }

    public void setAppPrice(long appPrice) {
        this.appPrice = appPrice;
    }

    public long getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(long memberPrice) {
        this.memberPrice = memberPrice;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
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
}
