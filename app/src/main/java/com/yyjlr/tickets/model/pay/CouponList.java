package com.yyjlr.tickets.model.pay;

import java.util.List;

/**
 * Created by Elvira on 2017/3/2.
 * 兑换券返回信息
 */

public class CouponList {
    private List<CouponInfo> couponList;

    public CouponList() {
    }

    public List<CouponInfo> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponInfo> couponList) {
        this.couponList = couponList;
    }
}
