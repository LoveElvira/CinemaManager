package com.yyjlr.tickets.model.order;

import java.util.List;

/**
 * Created by Richie on 2016/9/7.
 */
public class GoodsDetailBean {
    /**
     * "ticketCode":"46454242", // 取票码（卖品）
     * "ticketNo":"4524", // 验证码（卖品）
     */
    private String ticketCode;//卖品兑换券码
    private String ticketNo;// 验证码（卖品）
    private String state;// 子订单状态
    private List<GoodsOrderListInfo> goodsList;//卖品列表

    public String getTicketCode() {
        return ticketCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public List<GoodsOrderListInfo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsOrderListInfo> goodsList) {
        this.goodsList = goodsList;
    }
}
