package com.yyjlr.tickets.model.order;

/**
 * Created by Richie on 2016/8/25.
 */
public class OrderItemsInfo {
    private String name;//商品名称
    private int price;//单价
    private int couponPrice;//优惠价
    private int num;//购买数量
    private String memo;//卖品描述
    private String imageUtl;//卖品图片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(int couponPrice) {
        this.couponPrice = couponPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImageUtl() {
        return imageUtl;
    }

    public void setImageUtl(String imageUtl) {
        this.imageUtl = imageUtl;
    }
}
