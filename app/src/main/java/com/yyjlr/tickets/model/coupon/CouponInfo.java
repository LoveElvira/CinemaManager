package com.yyjlr.tickets.model.coupon;

/**
 * Created by Elvira on 2017/6/21.
 */

public class CouponInfo {
    /**
     * "type":"1",//1影片兑换券，2代金券/抵扣券
     * "discount":2000, //低佣金额单位分 （只有抵用券有）
     * "couponNumber":"1", //券号
     * "overTime":"54654622265"//到期时间
     */
    private String type;
    private long discount;
    private String couponNumber;
    private long overTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public long getOverTime() {
        return overTime;
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }
}
