package com.yyjlr.tickets.model.pay;

/**
 * Created by Elvira on 2017/3/2.
 */

public class CouponInfo {
    /**
     * "couponNo":"2454545454",//券号
     * "flag":1,/是否可用/1 可用，0 不可用
     * "errMsg":"券不存在或已使用"//错误信息
     */
    private String couponNo;//券号
    private int flag;//是否可用/1 可用，0 不可用
    private String errMsg;//错误信息

    public CouponInfo() {
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
