package com.yyjlr.tickets.model.order;

import java.util.List;

/**
 * Created by Richie on 2016/9/7.
 */
public class GoodsDetailBean {
    private String fetchCode;//卖品兑换券码
    private List<GoodsOrderListInfo> goodsList;//卖品列表

    public String getFetchCode() {
        return fetchCode;
    }

    public void setFetchCode(String fetchCode) {
        this.fetchCode = fetchCode;
    }

    public List<GoodsOrderListInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsOrderListInfo> goodsList) {
        this.goodsList = goodsList;
    }
}
