package com.yyjlr.tickets.model.pay;

/**
 * Created by Elvira on 2017/7/12.
 */

public class PaySummary {
    /**
     * "amount":49900, // 订单总金额，单位分
     * "goodNum":1, // 订单中商品数量
     * "voucherAmount":0, // 券支付金额，单位分
     * "cashAmount":49900, // 现金支付金额，单位分
     */
    private long amount;// 订单总金额，单位分
    private int goodNum;// 订单中商品数量
    private long voucherAmount;//券支付金额，单位分
    private long cashAmount;// 现金支付金额，单位分

    public PaySummary() {
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public long getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(long voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(long cashAmount) {
        this.cashAmount = cashAmount;
    }
}
