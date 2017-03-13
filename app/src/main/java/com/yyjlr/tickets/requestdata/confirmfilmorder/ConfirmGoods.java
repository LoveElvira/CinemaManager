package com.yyjlr.tickets.requestdata.confirmfilmorder;

import com.yyjlr.tickets.service.IRequestMainData;

import java.util.List;

/**
 * Created by Elvira on 2017/2/15.
 * 购买卖品 下单
 */

public class ConfirmGoods implements IRequestMainData {
    private List<GoodInfo> data;
    private String phone;

    public ConfirmGoods() {
    }

    public List<GoodInfo> getData() {
        return data;
    }

    public void setData(List<GoodInfo> data) {
        this.data = data;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
