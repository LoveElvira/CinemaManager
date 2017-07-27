package com.yyjlr.tickets;

import com.yyjlr.tickets.helputils.SharePrefUtil;

/**
 * Created by Elvira on 2016/10/10.
 * 网络连接路径
 */

public class Config {

    public static final boolean DEBUG = true;
//    public static String URL = "http://192.168.1.74:8080";//小强本地
//        public static String URL = "http://139.196.51.15";//测试
    public static String URL = "http://139.196.249.63";//正式

    public static String URL_SERVICE = URL + "/cgi";//测试环境
    public static String URL_SERVICE_UPLOAD = URL + "/uploadFile";//上传文件


//    public static final String URL_SERVICE = "http://192.168.1.57:8080/cgi";//小强本地
//    public static final String URL_SERVICE_UPLOAD = "http://192.168.1.57:8080/uploadFile";//上传文件

//    public static final String URL_SERVICE = "http://139.196.51.15/cgi";//测试环境
//    public static final String URL_SERVICE_UPLOAD = "http://139.196.51.15/uploadFile";//上传文件

//    public static final String URL_SERVICE = "http://192.168.1.53:8080/cgi";//唐静本地
//    public static final String URL_SERVICE_UPLOAD = "http://192.168.1.53:8080/uploadFile";//唐静本地

//    public static final String URL_SERVICE = "http://192.168.1.72:8080/cgi";//万多杨本地
//    public static final String URL_SERVICE_UPLOAD = "http://192.168.1.72:8080/uploadFile";//万多杨本地

//    public static final String URL_SERVICE = "http://139.196.249.63/cgi";//正式环境
//    public static final String URL_SERVICE_UPLOAD = "http://139.196.249.63/uploadFile";//上传文件

    public static final String REGISTER = "member/register";//用户注册
    public static final String GET_REGISTER_CODE = "member/register/getVerificationCode";//获取注册手机验证码
    public static final String LOGIN = "member/login";//手机号登录
    public static final String RESET_PWD_CODE = "member/resetPwd/getVerificationCode";//获取重置密码短信验证码
    public static final String RESET_PWD = "member/resetPwd";//重置密码
    public static final String CHANGE_PWD = "member/changePwd";//修改密码

    public static final String GET_ADVERT = "advert/query";//获取开屏广告数据接口
    public static final String GET_CHOSEN = "index";//获取精选数据
    public static final String GET_HOME = "newIndex";//获取首页数据
    public static final String GET_NEW_HOME = "newestIndex";//获取首页数据
    public static final String GET_TICKET = "activity/index";//获取抢票数据
    public static final String GET_SALE = "commodity/index";//获取卖品数据  goods/index
    public static final String GET_CINEMA_INFO = "cinema/queryById";//获取影院数据
    public static final String GET_PAY = "pay/channel";//获取支付方式
    public static final String GET_EVENT_INFO = "activity/queryDetail";//活动详情
    public static final String GET_FILM = "movie/query";//影片信息
    public static final String GET_FILM_COMING = "movie/upComing";//即将上映影片信息
    public static final String GET_FILM_INFO = "movie/queryDetail";//获取影片详情接口
    public static final String GET_FILM_COMMENT = "movie/commentList";//影片评论列表
    public static final String ADD_FILM_COMMENT = "movie/addComment";//写影片评论
    public static final String GET_FILM_PLAN = "movie/queryPlan";//获取影片排期接口
    public static final String GET_FILM_SEAT = "movie/querySeat";//获取影片（抢票）座位信息接口
    public static final String LOCK_FILM_SEAT = "movie/lockSeat";//影片（抢票）锁座下单接口
    public static final String CONFIRM_ORDER = "order/saveOrder";//确认订单接口
    public static final String GET_MY_ORDER = "user/order";//订单列表
    public static final String GET_MY_ORDER_INFO = "user/orderDetail";//订单详情
    public static final String CHECK_NO_PAY_ORDER = "order/unpaid";//检查同场次未支付订单
    public static final String GET_MY_INFO = "user/detail";//个人中心首页 以及个人中心
    public static final String CANCEL_ORDER = "order/cancel";//取消订单接口
    public static final String REMOVE_ORDER = "order/delete";//删除订单
    public static final String COLLECT_FILM = "movie/interest";//影片关注（取消关注）接口
    public static final String GO_COLLECT = "user/favorite";//关注（取消关注）接口
    public static final String COLLECT_EVENT = "activity/interest";//抢票关注（取消关注）接口
    public static final String CONFIRM_PAY = "order/changePayType";//确认支付金额接口
    public static final String GET_MESSAGE = "user/message";//我的消息
    public static final String DELETE_MESSAGE = "user/message/delete";//我的消息  删除
    public static final String GET_UNPAID_DETAIL = "order/unpaid/detail";//待处理订单详情
    public static final String BEFORE_PAY_ORDER = "pay/payData";//订单预支付接口
    public static final String CHECK_PAY_ORDER_STATUS = "order/queryState";//订单状态查询接口
    public static final String GET_FOLLOW_FILM = "user/interest/movie";//关注的影片
    public static final String GET_FOLLOW_EVENT = "user/interest/activity";//关注的活动
    public static final String UPDATE_MY_INFO = "user/updateUserInfo";//修改用户信息
    public static final String UPDATE_MY_HEAD_IMAGE = "base/uploadImage";//修改头像信息
    public static final String GET_SALE_RECOMMEND = "commodity/recommend";//获取推荐卖品数据接口
    public static final String GET_SALE_GOOD_INFO = "commodity/detail";//套餐详情
    public static final String GET_GOOD_MORE = "commodity/more";//更多卖品列表
    public static final String CONFIRM_GOODS = "commodity/addOrder";//卖品下单接口
    public static final String GET_MESSAGE_DETAILS = "user/message/detail";//消息详情
    public static final String GET_MY_POINT = "user/points";//我的积分
    public static final String BIND_CARD = "pay/card/binding";//绑定会员卡
    public static final String GET_CARD_PRICE = "pay/card/price";//获取会员卡价格
    public static final String PAY_CARD_CHECKOUT = "pay/card/checkout";//会员卡支付密码校验
    public static final String GET_CARD = "user/memberCard";//会员中心
    public static final String REMOVE_CARD = "pay/card/remove";//解绑会员卡
    public static final String CHECK_PAY_CARD_STATUS = "pay/card/status";//会员卡状态查询接口
    public static final String GET_UNPAID_GOODS_DETAILS = "order/unpaid/goodsDetail";//待处理卖品订单
    public static final String GET_CINEMA_LIST = "cinema/cinemaList";//获取兄弟影院列表
    public static final String GET_APP_CONFIG = "app/config";//获取APP设置信息
    public static final String GET_UNREAD_MSG_NUM = "user/unreadmsg";//未读消息
    public static final String GET_MY_SETTING = "user/index";//个人中心首页
    public static final String GET_MY_COUPON = "user/coupon";//我的优惠券
    public static final String GET_PAY_COUPON = "coupon/gain";//获取优惠券
    public static final String GET_CINEMA_STATUS = "cinema/warm";//影院运营状态
    public static final String CHECK_PAY_PRICE = "newPay/queryFactAmount";//订单实际价格查询接口
    public static final String BEFORE_PAY = "newPay/newPayData";//订单预支付接口
    public static final String GET_APP_VERSION = "app/version/current";//获取最新APP版本
    public static final String GET_NEW_PAY = "pay/newChannel";//获取支付方式（新版）
    public static final String CHECK_COUPON_USABLE = "pay/couponIsUsable";//检查兑换券是否可以用
    public static final String GET_COUPONS = "coupon/availableCoupon";//获取用户未使用的优惠券


}
