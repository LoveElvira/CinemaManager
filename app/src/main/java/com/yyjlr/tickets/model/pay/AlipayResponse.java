package com.yyjlr.tickets.model.pay;

/**
 * Created by Elvira on 2017/1/5.
 * 支付宝返回
 */

public class AlipayResponse {
    private String data;
    private String memberCardOrderId;//为会员卡充值是返回的数值  方便轮询查询是否充值成功

    public AlipayResponse() {
    }

    public String getMemberCardOrderId() {
        return memberCardOrderId;
    }

    public void setMemberCardOrderId(String memberCardOrderId) {
        this.memberCardOrderId = memberCardOrderId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
