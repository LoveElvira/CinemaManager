package com.yyjlr.tickets.model.appversion;

/**
 * Created by Elvira on 2016/12/19.
 */

public class Readme {
    private String register;// 注册
    private String returnPolicy;// 退货条款
    private String credit;// 积分

    public Readme() {
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getReturnPolicy() {
        return returnPolicy;
    }

    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
