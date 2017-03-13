package com.yyjlr.tickets;

/**
 * Created by Elvira on 2016/10/10.
 * 网络连接路径
 */

public class Config {
    public static final boolean DEBUG = true;
    public static final String URL_SERVICE = "http://192.168.1.60:8080/cgi";//小强本地
    public static final String URL_SERVICE_UPLOAD = "http://192.168.1.60:8080/uploadFile";//上传文件

//    public static final String URL_SERVICE = "http://139.196.51.15/cgi";//测试环境
//    public static final String URL_SERVICE_UPLOAD = "http://139.196.51.15/uploadFile";//上传文件

//    public static final String URL_SERVICE = "http://139.196.249.63/cgi";//正式环境
//    public static final String URL_SERVICE_UPLOAD = "http://139.196.249.63/uploadFile";//上传文件

    public static final String REGISTER = "user/register";//用户注册
    public static final String GET_REGISTER_CODE = "user/register/getVerificationCode";//获取注册手机验证码
    public static final String LOGIN = "user/login";//手机号登录
    public static final String RESET_PWD_CODE = "user/resetPwd/getVerificationCode";//获取重置密码短信验证码
    public static final String RESET_PWD = "user/resetPwd";//重置密码
    public static final String CHANGE_PWD = "user/changePwd";//修改密码

    public static final String GET_ADVERT = "advert/query";//获取开屏广告数据接口
    public static final String GET_CHOSEN = "index";//获取精选数据
    public static final String GET_TICKET = "activity/index";//获取抢票数据
    public static final String GET_SALE = "commodity/index";//获取卖品数据  goods/index
    public static final String GET_CINEMA_INFO = "cinema/queryById";//获取影院数据
    public static final String GET_PAY = "pay/channel";//获取支付方式
    public static final String GET_EVENT_INFO = "activity/queryDetail";//活动详情
    public static final String GET_FILM = "movie/query";//影  片信息
    public static final String GET_FILM_INFO = "movie/queryDetail";//获取影片详情接口
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


}
