package com.yyjlr.tickets.model.order;

/**
 * Created by Richie on 2016/9/7.
 */
public class GoodsOrderListInfo {
    private String goodsName;//套餐名称
    private String goodsDesc;//套餐明细
    private int count;//购买数量
    private String goodsImg;//卖品图

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }
}
