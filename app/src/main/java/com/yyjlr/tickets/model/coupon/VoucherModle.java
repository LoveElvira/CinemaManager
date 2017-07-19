package com.yyjlr.tickets.model.coupon;

/**
 * Created by Elvira on 2017/7/12.
 * 兑换券
 */

public class VoucherModle {
    private String couponNumber;
    private String overTime;
    private boolean isChecked;

    public VoucherModle() {
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "VoucherModle{" +
                "couponNumber='" + couponNumber + '\'' +
                ", overTime=" + overTime +
                ", isChecked=" + isChecked +
                '}';
    }
}
