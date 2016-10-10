package com.yyjlr.tickets.model;

/**
 * Created by Elvira on 2016/8/11.
 * 我的订单的实体
 */
public class OrderEntity {
    private String orderId;
    private String orderNum;
    private String orderFilmName;
    private String orderPackage;
    private String orderComplete;//1  已完成   2  未完成 待支付   3  已失效

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderFilmName() {
        return orderFilmName;
    }

    public void setOrderFilmName(String orderFilmName) {
        this.orderFilmName = orderFilmName;
    }

    public String getOrderPackage() {
        return orderPackage;
    }

    public void setOrderPackage(String orderPackage) {
        this.orderPackage = orderPackage;
    }

    public String getOrderComplete() {
        return orderComplete;
    }

    public void setOrderComplete(String orderComplete) {
        this.orderComplete = orderComplete;
    }
}
