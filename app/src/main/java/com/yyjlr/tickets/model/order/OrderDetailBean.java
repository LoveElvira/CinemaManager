package com.yyjlr.tickets.model.order;

/**
 * Created by Elvira on 2017/03/01.
 */
public class OrderDetailBean {
    private long id;//订单id
    private int orderType;//订单类型
    private String orderNo;//订单号
    private int status;//订单状态
    private String payPhone;//付款手机
    private String payType;//支付方式
    private Long payTime;//支付时间
    private int payMoney;//支付金额，单位分
    private MovieOrderDetailInfo movieDetail;//电影订单详情
    private GoodsDetailBean goodsDetail;//卖品订单列表

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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
