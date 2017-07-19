package com.yyjlr.tickets.model.pay;

import java.util.List;

/**
 * Created by Elvira on 2017/7/11.
 * 检查支付更新
 */

public class CheckedPayPriceModel {
//    private String factAmount;
    private List<String> couponList;

    public CheckedPayPriceModel() {
    }

//    public String getFactAmount() {
//        return factAmount;
//    }
//
//    public void setFactAmount(String factAmount) {
//        this.factAmount = factAmount;
//    }

    public List<String> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<String> couponList) {
        this.couponList = couponList;
    }
}
