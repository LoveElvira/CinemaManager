package com.yyjlr.tickets.requestdata.register;

import com.yyjlr.tickets.service.IRequestMainData;

/**
 * Created by Elvira on 2016/12/29.
 */

public class CodeRequest implements IRequestMainData {
    private String phone;

    public CodeRequest() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
