package com.yyjlr.tickets.model.order;

import java.util.List;

/**
 * Created by Richie on 2016/9/5.
 */
public class MyOrderInfo {
    private Long orderId;//订单id
    private String orderNo;//订单号
    private int orderType;//1电影，2卖品，3套餐。。。
    private int orderStatus;//订单状态
    private String movieName;//电影名称
    private List<String> goodsName;//卖品名称

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public List<String> getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(List<String> goodsName) {
        this.goodsName = goodsName;
    }
}
