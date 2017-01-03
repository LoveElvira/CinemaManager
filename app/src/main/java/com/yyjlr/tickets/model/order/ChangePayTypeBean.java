package com.yyjlr.tickets.model.order;

import java.util.List;

/**
 * Created by Richie on 2016/8/29.
 */
public class ChangePayTypeBean {
    private int totalPrice;//订单总金额，单位分
    private int price;//支付金额，单位分
    private List<OrderItemsInfo> items;//商品信息
    private List<PaymentType> paymentTypes;
    private int showMemberCardPay;// 是否显示会员卡支付，1：显示；0：不显示

    public List<PaymentType> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(List<PaymentType> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public int getShowMemberCardPay() {
        return showMemberCardPay;
    }

    public void setShowMemberCardPay(int showMemberCardPay) {
        this.showMemberCardPay = showMemberCardPay;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<OrderItemsInfo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemsInfo> items) {
        this.items = items;
    }
}
