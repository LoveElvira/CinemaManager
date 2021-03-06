package com.yyjlr.tickets.model.coupon;

import com.yyjlr.tickets.model.film.MovieInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/6/21.
 */
public class CouponBean {
    private int hasMore;//是否还有卖品信息：1：有；0：所有卖品信息已经全部加载完成
    private String pagable;//分页参数
    private List<CouponInfo> coupons;//电影信息列表

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

    public List<CouponInfo> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponInfo> coupons) {
        this.coupons = coupons;
    }
}
