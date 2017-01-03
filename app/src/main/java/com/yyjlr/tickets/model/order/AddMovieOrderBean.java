package com.yyjlr.tickets.model.order;


import com.yyjlr.tickets.model.sale.RecommendGoodsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Richie on 2016/8/25.
 */
public class AddMovieOrderBean implements Serializable {
    private MovieOrderInfo orderInfo;//电影下单信息
    private List<RecommendGoodsInfo> goods;//推荐卖品信息

    public AddMovieOrderBean() {
    }

    public MovieOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(MovieOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<RecommendGoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<RecommendGoodsInfo> goods) {
        this.goods = goods;
    }
}
