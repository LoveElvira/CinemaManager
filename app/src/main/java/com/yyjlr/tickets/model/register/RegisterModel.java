package com.yyjlr.tickets.model.register;

/**
 * Created by Elvira on 2016/12/29.
 */

public class RegisterModel {
    private String userId;// 用户ID
    private String loginName;// 登录名
    private String nickName;// 姓名（昵称）
    private String headImgUrl;// 头像URL
    private String sex;// 性别(1：男；2：女)
    private String email;// EMAIL
    private String birthday;// 出生日期时间戳
    private String idNo;// 身份证号码
    private String birthplace;// 籍贯
    private String city; // 城市
    private String address;// 邮寄地址
    private String phone;// 手机号
    private String status;// 账号状态（1：正常，2：禁用, 3:删除）
    private String token;// token
    private long tokenInvalidTime;// token失效时间戳
    private long regTime;// 注册日期

    public RegisterModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenInvalidTime() {
        return tokenInvalidTime;
    }

    public void setTokenInvalidTime(long tokenInvalidTime) {
        this.tokenInvalidTime = tokenInvalidTime;
    }

    public long getRegTime() {
        return regTime;
    }

    public void setRegTime(long regTime) {
        this.regTime = regTime;
    }
}
