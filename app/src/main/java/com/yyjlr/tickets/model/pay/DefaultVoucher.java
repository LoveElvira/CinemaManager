package com.yyjlr.tickets.model.pay;

/**
 * Created by Elvira on 2016/12/20.
 * 选择支付方式
 */

public class DefaultVoucher {
    private int position;
    private boolean checked;

    public DefaultVoucher() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
