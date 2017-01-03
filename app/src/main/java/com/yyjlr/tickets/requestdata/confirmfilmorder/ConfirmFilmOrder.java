package com.yyjlr.tickets.requestdata.confirmfilmorder;

import com.yyjlr.tickets.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2016/12/30.
 * 确认影院 票 订单
 */

public class ConfirmFilmOrder implements IRequestMainData {
    private String orderId;
    private String phone;
    private List<GoodInfo> goods;

    public ConfirmFilmOrder() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<GoodInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodInfo> goods) {
        this.goods = goods;
    }
}
