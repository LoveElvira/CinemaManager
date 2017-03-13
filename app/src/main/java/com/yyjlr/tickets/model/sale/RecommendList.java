package com.yyjlr.tickets.model.sale;

import java.util.List;

/**
 * Created by Elvira on 2017/2/14.
 * 推荐套餐
 */

public class RecommendList {
    private List<RecommendGoodsInfo> goodsList;

    public RecommendList() {
    }

    public List<RecommendGoodsInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<RecommendGoodsInfo> goodsList) {
        this.goodsList = goodsList;
    }
}
