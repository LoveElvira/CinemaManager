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
     * "id":1, // 渠道id
     * "name": "支付宝", //渠道名称
     * "checked":1 //1选中，0未选中
     * }
     * ],
     * "showMemberCard":1,//是否显示会员卡支付，1显示，0不显示
     * "memberCardList":[
     * {
     * "cardNo":"18646434324133",//卡号
     * "balance":12465656 //余额，单位分
     * }
     * ],
     * "showCoupon":1,//是否显示兑换券支付，1显示，0不显示
     * "showCouponNum":2//显示兑换券数量
     */

    private List<SelectPay> channelList;//网络支付
    private int showMemberCard;//是否显示会员卡支付，1显示，0不显示
    private int memberCardTypeId;//会员卡支付渠道ID
    private List<MemberCard> memberCardList;//会员卡
    private int couponTypeId;//兑换券渠道支付ID
    private int showCoupon;//是否显示兑换券支付，1显示，0不显示
    private int showCouponNum;//显示兑换券数量

    public PayModel() {
    }

    public int getMemberCardTypeId() {
        return memberCardTypeId;
    }

    public void setMemberCardTypeId(int memberCardTypeId) {
        this.memberCardTypeId = memberCardTypeId;
    }

    public int getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(int couponTypeId) {
        this.couponTypeId = couponTypeId;
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

    public List<SelectPay> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<SelectPay> channelList) {
        this.channelList = channelList;
    }
}
