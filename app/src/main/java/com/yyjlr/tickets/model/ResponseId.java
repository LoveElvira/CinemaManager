package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2017/1/4.
 */

public class ResponseId {
    private int orderId;//订单id
    private int orderType;

    public ResponseId() {
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
