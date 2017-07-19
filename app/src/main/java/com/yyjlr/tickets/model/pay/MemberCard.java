package com.yyjlr.tickets.model.pay;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/3/2.
 * 会员卡
 */

public class MemberCard implements Serializable {
    /**
     * "checked":1,//是否默认选中，1选中，0不选中
     * "cardNo":"18646434324133",//卡号
     * "balance":1500,//余额，单位分
     * "state":1,//1可支付，0不可支付
     * "msg":"该会员卡不支持此订单"，//如果是不可支付，需要有说明
     */
    private String cardNo;//卡号
    private long balance;//余额，单位分

    private int checked;//是否默认选中，1选中，0不选中
    private int state;//是否够支付 1可支付，0不可支付
    private String msg;////如果是不可支付，需要有说明

    public MemberCard() {
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
