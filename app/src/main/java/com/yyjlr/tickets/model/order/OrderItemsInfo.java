package com.yyjlr.tickets.model.order;

import java.io.Serializable;

/**
 * Created by Elvira on 2017/2/27.
 */
public class OrderItemsInfo implements Serializable {
    /**
     * "name": "商品名称",
     * "price": 20, // 单价
     * "num":1, // 购买数量
     * "totalPrice":20, // 小计
     */
    private String name;//商品名称
    private long price;//单价
    private long totalPrice;// 小计
    private int num;//购买数量

    public OrderItemsInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
