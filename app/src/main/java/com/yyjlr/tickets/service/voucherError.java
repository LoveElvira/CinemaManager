package com.yyjlr.tickets.service;

/**
 * Created by Elvira on 2017/3/3.
 * 兑换券支付失败
 */

public class VoucherError{
    /**
     * "errorCode":101,  // 错误代码（参见响应代码表）
     * "errorInfo":{
     * "errMsg":"部分兑换券使用失败",
     * "couponList":[
     * {
     * "couponNo":"2454545454",//券号
     * "flag":1,/是否可用/1 可用，0 不可用
     * "errMsg":"券不存在或已使用"//错误信息
     * }
     * ]
     * }
     */
    private String code;
    private VoucherErrorInfo info;

    public VoucherError() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public VoucherErrorInfo getInfo() {
        return info;
    }

    public void setInfo(VoucherErrorInfo info) {
        this.info = info;
    }
}
