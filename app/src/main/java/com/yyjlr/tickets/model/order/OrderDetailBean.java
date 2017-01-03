package com.yyjlr.tickets.model.order;

/**
 * Created by Richie on 2016/9/7.
 */
public class OrderDetailBean {
    private String orderNo;//订单号
    private String payPhone;//付款手机
    private String payType;//支付方式
    private Long payTime;//支付时间
    private int payMoney;//支付金额，单位分
    private MovieOrderDetailInfo movieDetail;//电影订单详情
    private GoodsDetailBean goodsDetail;//卖品订单列表

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayPhone() {
        return payPhone;
    }

    public void setPayPhone(String payPhone) {
        this.payPhone = payPhone;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public int getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public MovieOrderDetailInfo getMovieDetail() {
        return movieDetail;
    }

    public void setMovieDetail(MovieOrderDetailInfo movieDetail) {
        this.movieDetail = movieDetail;
    }

    public GoodsDetailBean getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(GoodsDetailBean goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}
