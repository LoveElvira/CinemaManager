package com.yyjlr.tickets.requestdata;

import com.yyjlr.tickets.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 */

public class IdRequest implements IRequestMainData {
    private String activityId;
    private String id;//套餐的id
    private String movieId;//获取影院详情
    private String planId;//排期ID
    private String orderId;//订单号
    private String payTypeId;//支付类别ID
    private String isInterest;//是否关注(0:取消关注;1:关注)
    private String activityType;//活动类别，1：电影；2：特价票；3：明星见面会；4：商户合作活动
    private String type;
    //绑定会员卡
    private String cardNo;//卡号
    private String pwd;//密码
    private String amount;//充值金额 以分的形式录入
    //评论
    private String grade;//评分
    private String content;//评论内容

    private List<String> couponList;//兑换券券号,字符串数组

    /**
     * orderId	是	订单ID（当orderId为空，默认会员卡充值，String型）
     * payChannelNo	否	网上支付渠道：微信1，支付宝2（Integer型）
     * source	否	支付源，app：从APP发起（默认）；h5：从H5页面发起
     * openid	否	从H5页面发起，必须上传
     * cardNo	否	会员卡卡号（有值默认会员卡支付，String类型）
     * couponList	否	兑换券券号,字符串d数组
     * amount	否	会员充值金额(充值时，要求：orderId为空，payChannelNo必填，cardNo必填)
     */
    private String payChannelNo;

    private String payType;//网上支付 渠道ID


    public IdRequest() {
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayChannelNo() {
        return payChannelNo;
    }

    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<String> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<String> couponList) {
        this.couponList = couponList;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(String payTypeId) {
        this.payTypeId = payTypeId;
    }

    public String getIsInterest() {
        return isInterest;
    }

    public void setIsInterest(String isInterest) {
        this.isInterest = isInterest;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
