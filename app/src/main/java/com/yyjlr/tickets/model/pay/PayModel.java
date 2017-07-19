package com.yyjlr.tickets.model.pay;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 * 支付方式
 */

public class PayModel {

    /**
     * "channelList":[
     * {
     * "id":1, // 支付平台id
     * "name": "支付宝", //支付平台名称
     * "checked":1 //是否默认选中，1选中，0不选中
     * }
     * ],
     * "showMemberCard":1,//是否显示会员卡支付，1显示，0不显示
     * "memberCardList":[
     * {
     * "checked":1,//是否默认选中，1选中，0不选中
     * "cardNo":"18646434324133",//卡号
     * "balance":1500,//余额，单位分
     * "state":1,//1可支付，0不可支付
     * "msg":"该会员卡不支持此订单"，//如果是不可支付，需要有说明
     * }
     * ],
     * "showCoupon":1,//是否显示兑换券支付，1显示，0不显示
     * "showCouponNum":2//最多可使用兑换券数
     * "couponList":["157457424","5457542"],//已选择兑换券
     * "paySummary":{ // 支付摘要信息
     * "amount":49900, // 订单总金额，单位分
     * "goodNum":1, // 订单中商品数量
     * "voucherAmount":0, // 券支付金额，单位分
     * "cashAmount":49900, // 现金支付金额，单位分
     * }
     */

    private List<SelectPay> channelList;//网络支付
    private int showMemberCard;//是否显示会员卡支付，1显示，0不显示
    //    private int memberCardTypeId;//会员卡支付渠道ID
    private List<MemberCard> memberCardList;//会员卡
    //    private int couponTypeId;//兑换券渠道支付ID
    private int showCoupon;//是否显示兑换券支付，1显示，0不显示
    private int showCouponNum;//显示兑换券数量
    private List<String> couponList;//已选择兑换券
    private PaySummary paySummary;

    public PayModel() {
    }

    public List<SelectPay> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<SelectPay> channelList) {
        this.channelList = channelList;
    }

    public int getShowMemberCard() {
        return showMemberCard;
    }

    public void setShowMemberCard(int showMemberCard) {
        this.showMemberCard = showMemberCard;
    }

    public List<MemberCard> getMemberCardList() {
        return memberCardList;
    }

    public void setMemberCardList(List<MemberCard> memberCardList) {
        this.memberCardList = memberCardList;
    }

    public int getShowCoupon() {
        return showCoupon;
    }

    public void setShowCoupon(int showCoupon) {
        this.showCoupon = showCoupon;
    }

    public int getShowCouponNum() {
        return showCouponNum;
    }

    public void setShowCouponNum(int showCouponNum) {
        this.showCouponNum = showCouponNum;
    }

    public List<String> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<String> couponList) {
        this.couponList = couponList;
    }

    public PaySummary getPaySummary() {
        return paySummary;
    }

    public void setPaySummary(PaySummary paySummary) {
        this.paySummary = paySummary;
    }
}
