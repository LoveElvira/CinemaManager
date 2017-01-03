package com.yyjlr.tickets.model.pay;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 */

public class PayModel {
    private List<SelectPay> channelList;

    public PayModel() {
    }

    public List<SelectPay> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<SelectPay> channelList) {
        this.channelList = channelList;
    }
}
