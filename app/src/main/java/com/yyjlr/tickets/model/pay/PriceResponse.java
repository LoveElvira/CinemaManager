package com.yyjlr.tickets.model.pay;

/**
 * Created by Elvira on 2017/3/2.
 */

public class PriceResponse {
    private String vipPrice;//会员卡价格,字符串

    public PriceResponse() {
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }
}
