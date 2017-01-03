package com.yyjlr.tickets.model.sale;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 * 卖品
 */

public class Goods {
    private int hasMore;//是否还有卖品信息：1：有；0：所有卖品信息已经全部加载完成
    private String pagable;//分页参数
    private List<GoodInfo> goodsList;//卖品信息列表

    public Goods() {
    }

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

    public List<GoodInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodInfo> goodsList) {
        this.goodsList = goodsList;
    }
}
