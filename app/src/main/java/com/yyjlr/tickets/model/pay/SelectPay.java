package com.yyjlr.tickets.model.pay;

/**
 * Created by Elvira on 2016/12/20.
 * 选择支付方式
 */

public class SelectPay {
    /**
     * "id":1, // 渠道id
     "image": "http:xxxxxxxxxxxxxxx", //图片
     * */
    private long id;
    private String name;
    private int checked;

    public SelectPay() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
