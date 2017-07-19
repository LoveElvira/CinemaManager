package com.yyjlr.tickets.model.coupon;

/**
 * Created by Elvira on 2017/7/12.
 * 兑换券
 */

public class VoucherModle extends CouponInfo {
    private boolean isChecked;

    public VoucherModle() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
