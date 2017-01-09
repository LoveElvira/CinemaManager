package com.yyjlr.tickets.model.pay;

/**
 * Created by Elvira on 2017/1/5.
 * 支付宝返回
 */

public class AlipayResponse {
    private String data;

    public AlipayResponse() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
