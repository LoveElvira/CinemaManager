package com.yyjlr.tickets.model.order;

import com.yyjlr.tickets.model.sale.RecommendGoodsInfo;

import java.util.List;

/**
 * Created by Richie on 2016/8/23.
 */
public class AddGoodsOrderBean {
    private GoodsOrderInfo orderInfo;//订单信息
    private List<RecommendGoodsInfo> goods;//推荐卖品信息

    public GoodsOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(GoodsOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<RecommendGoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<RecommendGoodsInfo> goods) {
        this.goods = goods;
    }
}
