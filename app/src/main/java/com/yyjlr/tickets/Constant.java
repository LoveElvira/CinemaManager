package com.yyjlr.tickets;

import android.os.Build;

public class Constant {
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String PHONE_OR_WECHAT_REGIEST = "phoneOrWechat";
    public static final String FILE_NAME = "SFC";
    public static final String APP_ID = "wxe498f625e280868f";//微信
    public static final String APP_SECRERT = "8eb9d51f6462092b964879846c8adfa3";//微信
    public static final String REFRESH_TOKEN = "refreshToken";//微信
    public static final String ACCESS_TOKEN = "accessToken";//微信
    public static final String OPEN_ID = "openId";//微信
    //    public static String AppDomain = "33040301";//正式 影院ID
    public static String AppDomain = "50120255";//测试 影院ID
    //            public static String AppDomain = "50120257";//xiaoqiang 影院ID
    //设备品牌 设备显示的版本号  设备唯一标示  设备版本号   -上传地址
    public static final String AppVersion = Build.BRAND + "_" + Build.DISPLAY + "_" + Build.FINGERPRINT + "_" + Build.ID;

}
