package com.yyjlr.tickets.model.sale;

import java.util.List;

/**
 * Created by Elvira on 2017/2/14.
 * 套餐详情
 */

public class PackageInfo {
    private PackageDetails goodsInfo;

    public PackageInfo() {
    }

    public PackageDetails getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(PackageDetails goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
}
