package com.yyjlr.tickets.model.order;

import java.util.List;

/**
 * Created by Elvira on 2017/1/5.
 * 待处理订单详情
 */

public class BeforePayOrderInfo {
    private List<GoodsRecommend> goods;
    private MovieOrderInfo orderInfo;

    public BeforePayOrderInfo() {
    }

    public List<GoodsRecommend> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsRecommend> goods) {
        this.goods = goods;
    }

    public MovieOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(MovieOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
