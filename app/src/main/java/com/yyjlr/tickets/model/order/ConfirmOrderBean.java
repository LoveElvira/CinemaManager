package com.yyjlr.tickets.model.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Elvira on 2017/2/27.
 */
public class ConfirmOrderBean implements Serializable {
    /**
     * "id":1,//订单id
     * "orderNo":"12875421232",//订单号
     * "totalPrice": 123, //订单中金额，单位分
     * "price":123, // 支付金额，单位分
     * "items": // 订单中商品信息
     * [
     * {
     * "name": "商品名称",
     * "price": 20, // 单价
     * "num":1, // 购买数量
     * "totalPrice":20, // 小计
     * }
     * ]
     */
    private long id;//订单id
    private long orderId;////订单号
    private long countdown = 0;//单位秒 倒计时
    private int totalPrice;//订单总金额，单位分
    private int price;//支付金额，单位分
    private List<OrderItemsInfo> items;//商品信息

    public long getCountdown() {
        return countdown;
    }

    public void setCountdown(long countdown) {
        this.countdown = countdown;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<OrderItemsInfo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsInfo> items) {
        this.items = items;
    }
}
