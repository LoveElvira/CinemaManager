package com.yyjlr.tickets.model.order;

import java.util.List;

/**
 * Created by Richie on 2016/9/5.
 */
public class MyOrderBean {
    private int hasMore;//是否还有信息，1有，0全部加载完成
    private String pagable;//分页参数
    private List<MyOrderInfo> orders;//订单列表

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }

    public List<MyOrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(List<MyOrderInfo> orders) {
        this.orders = orders;
    }
}
