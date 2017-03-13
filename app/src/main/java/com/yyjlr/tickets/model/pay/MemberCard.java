package com.yyjlr.tickets.model.pay;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/3/2.
 * 会员卡
 */

public class MemberCard implements Serializable{
    /**
     * "cardNo":"18646434324133",//卡号
     * "balance":12465656 //余额，单位分
     */
    private String cardNo;//卡号
    private long balance;//余额，单位分

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
}
