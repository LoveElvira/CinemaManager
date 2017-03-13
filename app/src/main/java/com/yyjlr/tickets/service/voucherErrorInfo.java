package com.yyjlr.tickets.service;

import com.yyjlr.tickets.model.pay.CouponInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/3/3.
 * 兑换券支付失败
 */

public class VoucherErrorInfo {
    /**
     * "errMsg":"部分兑换券使用失败",
     * "couponList":[{}]
     */
    private String errMsg;
    private List<CouponInfo> couponList;

    public VoucherErrorInfo() {
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<CouponInfo> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponInfo> couponList) {
        this.couponList = couponList;
    }
}
