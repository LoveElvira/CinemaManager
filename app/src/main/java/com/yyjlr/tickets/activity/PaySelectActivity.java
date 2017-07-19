package com.yyjlr.tickets.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.setting.RechargeActivity;
import com.yyjlr.tickets.activity.setting.SettingOrderDetailsActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.NoUseCouponAdapter;
import com.yyjlr.tickets.adapter.PayAdapter;
import com.yyjlr.tickets.adapter.PayCardAdapter;
import com.yyjlr.tickets.adapter.VoucherAdapter;
import com.yyjlr.tickets.content.coupon.NoUseCouponContent;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.ResponseStatus;
import com.yyjlr.tickets.model.coupon.CouponBean;
import com.yyjlr.tickets.model.coupon.CouponInfo;
import com.yyjlr.tickets.model.coupon.VoucherModle;
import com.yyjlr.tickets.model.order.ConfirmOrderBean;
import com.yyjlr.tickets.model.order.OrderItemsInfo;
import com.yyjlr.tickets.model.pay.AlipayResponse;
import com.yyjlr.tickets.model.pay.CheckedPayPriceModel;
import com.yyjlr.tickets.model.pay.DefaultVoucher;
import com.yyjlr.tickets.model.pay.MemberCard;
import com.yyjlr.tickets.model.pay.PayModel;
import com.yyjlr.tickets.model.pay.PayResult;
import com.yyjlr.tickets.model.pay.SelectPay;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.countdown.CountdownView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Elvira on 2016/8/17.
 * 支付界面
 */
public class PaySelectActivity extends AbstractActivity implements View.OnClickListener, CountdownView.OnCountdownEndListener, BaseAdapter.OnRecyclerViewItemChildClickListener, BaseAdapter.RequestLoadMoreListener {

    private final int SDK_PAY_FLAG = 1;
    private int times = 0;//轮询检查订单状态
    private TextView title;
    private ImageView leftArrow;
    private LinearLayout addPackageLayout;
    //网上支付  会员卡支付  兑换券支付
    private LinearLayout onlineLayout, cardLayout, voucherLayout;
    private LinearLayout voucherSelectLayout, noCardLayout;
    private RecyclerView haveCardListView, onlineListView, voucherListView;

    //兑换券数量  支付提示  支付金额 确认支付
    private TextView voucherSelectNum, payMsg, payPrice, confirmPay;

    private PayAdapter onlinePayAdapter;
    private PayCardAdapter cardAdapter;
    private VoucherAdapter voucherAdapter;

    //编辑兑换券号码
    private EditText editNum;

    private LinearLayout countDownTimeLayout;
    private CountdownView countDownTime;//倒计时
    private Timer mTimer;
    private boolean isCancel = true;
    private Handler mHandler = new Handler();
    private long endTime;

    //    private ChangePayTypeBean changePayTypeBean;//确认订单传过来的参数
    private TextView price;//订单总金额
    private String orderId = "";
    public static Activity activity;

    //初始位置
    private int onlinePosition = 0;
    private int cardPosition = 0;
    private int voucherPosition = 0;

    private ConfirmOrderBean orderBean;
    private PayModel payModel;
    //会员卡列表
    private List<MemberCard> memberCardList = null;
    //网上支付列表
    private List<SelectPay> onlineList = null;
    private List<DefaultVoucher> voucherDefaultList = null;
    private List<VoucherModle> voucherList = null;
    private List<VoucherModle> voucherLists = null;
    private List<String> selectVoucherList = null;

    private List<CouponInfo> couponList = null;

    private int positions = -1;
    private int position = 0;
    private int rechargePosition = -1;
    private boolean isClick = true;
    private long payType = 0;
    private String cardNo = "";
    private boolean isConfirmVoucher;
    private boolean hasMore = false;
    private String pagable = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_select);
        customDialog = new CustomDialog(this, "请稍后...");
        initView();
        voucherLists = new ArrayList<>();
        voucherDefaultList = new ArrayList<>();
        activity = this;
        orderId = getIntent().getStringExtra("orderId");
        orderBean = (ConfirmOrderBean) getIntent().getSerializableExtra("orderBean");
        positions = getIntent().getIntExtra("position", -1);
        if ("".equals(orderId)) {
            if (orderBean != null) {
                initDetailsDate();
//                showTip();
            }
        } else {
            getGoodsDetails();
        }
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("订单支付");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        countDownTime = (CountdownView) findViewById(R.id.content_pay_select__time);
        countDownTimeLayout = (LinearLayout) findViewById(R.id.content_pay_select__time_layout);

        addPackageLayout = (LinearLayout) findViewById(R.id.content_pay_select__add_layout);

        price = (TextView) findViewById(R.id.content_pay_select__price);

        onlineLayout = (LinearLayout) findViewById(R.id.content_pay_select__online_layout);
        cardLayout = (LinearLayout) findViewById(R.id.content_pay_select__vip_layout);
        voucherLayout = (LinearLayout) findViewById(R.id.content_pay_select__voucher_layout);

        voucherSelectLayout = (LinearLayout) findViewById(R.id.content_pay_select__select_voucher_layout);
        voucherSelectNum = (TextView) findViewById(R.id.content_pay_select__voucher);

        noCardLayout = (LinearLayout) findViewById(R.id.content_pay_select__no_vip_layout);

        LinearLayoutManager linearLayoutManagerCard = new LinearLayoutManager(PaySelectActivity.this);
        LinearLayoutManager linearLayoutManagerOnline = new LinearLayoutManager(PaySelectActivity.this);

        haveCardListView = (RecyclerView) findViewById(R.id.content_pay_select__have_vip_layout);
        onlineListView = (RecyclerView) findViewById(R.id.content_pay_select__listview);

        haveCardListView.setLayoutManager(linearLayoutManagerCard);
        onlineListView.setLayoutManager(linearLayoutManagerOnline);


        payMsg = (TextView) findViewById(R.id.content_pay_select__msg);
        payPrice = (TextView) findViewById(R.id.content_pay_select__confirm_price);
        confirmPay = (TextView) findViewById(R.id.content_pay_select__confirm);

        voucherSelectLayout.setOnClickListener(this);
        noCardLayout.setOnClickListener(this);
        confirmPay.setOnClickListener(this);
    }

    //明细
    private void initDetailsDate() {
        orderId = orderBean.getId() + "";
        if (orderBean.getCountdown() != 0) {
            //校对倒计时
            long curTime = System.currentTimeMillis();
//            endTime = orderBean.getCountdown() + curTime;
            countDownTime.start(orderBean.getCountdown() * 1000);
            countDownTime.setOnCountdownEndListener(this);
//            startRefreshTime();
            countDownTimeLayout.setVisibility(View.VISIBLE);
        } else {
            countDownTimeLayout.setVisibility(View.GONE);
        }
        getPay(-1);
        price.setText(ChangeUtils.save2Decimal(orderBean.getPrice()));
        addPackageList(orderBean.getItems());
    }

//    public void startRefreshTime() {
//        if (!isCancel) return;
//
//        if (null != mTimer) {
//            mTimer.cancel();
//        }
//
//        isCancel = false;
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.post(mRefreshTimeRunnable);
//            }
//        }, 0, 1000);
//    }
//
//    public void cancelRefreshTime() {
//        isCancel = true;
//        if (null != mTimer) {
//            mTimer.cancel();
//        }
//        mHandler.removeCallbacks(mRefreshTimeRunnable);
//    }


//    private Runnable mRefreshTimeRunnable = new Runnable() {
//        @Override
//        public void run() {
//            long currentTime = System.currentTimeMillis();
//            if (currentTime >= endTime) {
//                // 倒计时结束
//                endTime(0);
//            } else {
//                refreshTime(currentTime);
//            }
//        }
//    };

//    public void refreshTime(long curTimeMillis) {
//        if (endTime <= curTimeMillis) return;
//        countDownTime.updateShow(endTime - curTimeMillis);
//    }

    public void endTime(long curTimeMillis) {
        countDownTime.updateShow(curTimeMillis);
    }

    //动态添加商品列表
    private void addPackageList(List<OrderItemsInfo> orderItemsInfoList) {
        for (int i = 0; i < orderItemsInfoList.size(); i++) {
            View view = LayoutInflater.from(PaySelectActivity.this).inflate(R.layout.item_pay_select_film_sale, addPackageLayout, false);
            TextView packageName = (TextView) view.findViewById(R.id.item_pay_select_filmorsale__name);
            TextView packageOriginalPrice = (TextView) view.findViewById(R.id.item_pay_select_filmorsale__original_price);
            TextView packageVipPrice = (TextView) view.findViewById(R.id.item_pay_select_filmorsale__discount_price);
            TextView packageTotalPrice = (TextView) view.findViewById(R.id.item_pay_select_filmorsale__total_price);
            View line = view.findViewById(R.id.item_pay_select_filmorsale__line);
            line.setVisibility(View.GONE);
            packageName.setText(orderItemsInfoList.get(i).getName());
            packageOriginalPrice.setText(ChangeUtils.save2Decimal(orderItemsInfoList.get(i).getPrice()) + " x " + orderItemsInfoList.get(i).getNum());
            packageTotalPrice.setText(ChangeUtils.save2Decimal(orderItemsInfoList.get(i).getTotalPrice()) + " 元");
//          packageVipPrice.setText(ChangeUtils.save2Decimal(orderItemsInfoList.get(i).getAppPrice()) + "元");

            addPackageLayout.addView(view);
            if (orderItemsInfoList.size() > 1 && i < orderItemsInfoList.size() - 1) {
                line.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initDate() {

        onlineLayout.setVisibility(View.GONE);
        cardLayout.setVisibility(View.GONE);
        voucherLayout.setVisibility(View.GONE);

        if (payModel.getChannelList() != null && payModel.getChannelList().size() > 0) {
            onlineList = payModel.getChannelList();
            onlinePosition = 0;
            onlineLayout.setVisibility(View.VISIBLE);
            onlinePayAdapter = new PayAdapter(onlineList);
            onlineListView.setAdapter(onlinePayAdapter);
            onlinePayAdapter.setOnRecyclerViewItemChildClickListener(this);
        }
        //是否显示会员卡支付
        if (payModel.getShowMemberCard() == 1) {
            cardLayout.setVisibility(View.VISIBLE);
            cardPosition = onlinePosition + 1;
            noCardLayout.setVisibility(View.VISIBLE);
            haveCardListView.setVisibility(View.GONE);
            if (payModel.getMemberCardList() != null && payModel.getMemberCardList().size() > 0) {
                memberCardList = payModel.getMemberCardList();
                haveCardListView.setVisibility(View.VISIBLE);
                noCardLayout.setVisibility(View.GONE);
                cardAdapter = new PayCardAdapter(memberCardList);
                haveCardListView.setAdapter(cardAdapter);
                cardAdapter.setOnRecyclerViewItemChildClickListener(this);
            }
        }
        //是否显示兑换券支付
        if (payModel.getShowCoupon() == 1) {
            voucherLayout.setVisibility(View.VISIBLE);
            voucherPosition = cardPosition + 1;
        }

        payPrice.setText(ChangeUtils.save2Decimal(payModel.getPaySummary().getCashAmount()));

    }

    //获取待处理订单数据 卖品
    private void getGoodsDetails() {
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId);
        OkHttpClientManager.postAsyn(Config.GET_UNPAID_GOODS_DETAILS, new OkHttpClientManager.ResultCallback<ConfirmOrderBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ConfirmOrderBean response) {
                Log.i("ee", new Gson().toJson(response));
                orderBean = response;
                if (orderBean != null) {
                    initDetailsDate();
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, idRequest, ConfirmOrderBean.class, Application.getInstance().getCurrentActivity());
    }

    //获取支付数据
    private void getPay(int status) {
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setId(orderId);
        switch (status) {
            case 0://兑换券
                idRequest.setCouponList(selectVoucherList);
                break;
            case 1://会员卡
                idRequest.setCardNo(cardNo);
                break;
            case 2:
                idRequest.setPayType(payType + "");
                break;
        }

        OkHttpClientManager.postAsyn(Config.GET_NEW_PAY, new OkHttpClientManager.ResultCallback<PayModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(PayModel response) {
                Log.i("ee", new Gson().toJson(response));
                payModel = response;
                if (payModel != null) {
                    initDate();
                }
                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
//                showShortToast(exception.getMessage());
            }
        }, idRequest, PayModel.class, Application.getInstance().getCurrentActivity());
    }

    //取消订单
    private void cancelOrder() {
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId + "");
        OkHttpClientManager.postAsyn(Config.CANCEL_ORDER, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
                if ("410".equals(info.getCode())) {
                    setResult(CODE_RESULT, new Intent()
                            .putExtra("isCancel", true)
                            .putExtra("isTimeOut", true)
                            .putExtra("position", positions));
                    PaySelectActivity.activity.finish();
                }
            }

            @Override
            public void onResponse(ResponeNull response) {
                customDialog.dismiss();
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isCancel", true)
                        .putExtra("isTimeOut", false)
                        .putExtra("position", positions));
                PaySelectActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, PaySelectActivity.this);
    }


    //订单实际价格查询接口
    private void checkedCoupon(final boolean isAdd, List<String> list) {
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId + "");
        if (list != null) {
            idRequest.setCouponList(list);
        } else {
            idRequest.setCouponList(selectVoucherList);
        }
        OkHttpClientManager.postAsyn(Config.CHECK_COUPON_USABLE, new OkHttpClientManager.ResultCallback<CheckedPayPriceModel>() {

            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
//                if ("410".equals(info.getCode())) {
//                    setResult(CODE_RESULT, new Intent()
//                            .putExtra("isCancel", true)
//                            .putExtra("position", positions));
//                    PaySelectActivity.activity.finish();
//                }
            }

            @Override
            public void onResponse(CheckedPayPriceModel response) {
                customDialog.dismiss();
                if (response != null) {
                    if (isAdd) {
                        if (response.getCouponList() != null && response.getCouponList().size() > 0) {
                            for (int i = 0; i < response.getCouponList().size(); i++) {
                                VoucherModle modle = new VoucherModle();
                                modle.setCouponNumber(response.getCouponList().get(i));
                                modle.setType(3);
                                modle.setOverTime(null);
                                modle.setChecked(false);
                                voucherLists.add(0, modle);
                            }
                            voucherAdapter.notifyDataSetChanged();
                        }
                    } else {
                        getPay(0);
                        mPopupWindow.dismiss();
                        voucherSelectNum.setText("已选择" + selectVoucherList.size() + "张兑换券");
                    }

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, CheckedPayPriceModel.class, PaySelectActivity.this);
    }


    //支付宝支付  预支付数据  网上支付
    private void beforePayOnline(String id, int status) {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
//        idRequest.setPayTypeId(payModel.getChannelList().get(position).getId() + "");
        idRequest.setOrderId(orderId);
        idRequest.setPayChannelNo(id);
        switch (status) {
            case 1://混合支付  兑换券+支付宝
                idRequest.setCouponList(selectVoucherList);
                break;
            case 2://混合支付  会员卡+支付宝
                idRequest.setCardNo(cardNo);
                break;
        }

        OkHttpClientManager.postAsyn(Config.BEFORE_PAY, new OkHttpClientManager.ResultCallback<AlipayResponse>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getBaseContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                if ("410".equals(info.getCode())) {
                    Application.getInstance().getCurrentActivity().setResult(CODE_RESULT, new Intent()
                            .putExtra("isCancel", true)
                            .putExtra("isTimeOut", true)
                            .putExtra("position", positions));
                    PaySelectActivity.this.finish();
                }
//                customDialog.dismiss();
            }

            @Override
            public void onResponse(final AlipayResponse response) {
//                Log.i("ee", new Gson().toJson(response));
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        //构造PayTask 对象
                        PayTask alipay = new PayTask(Application.getInstance().getCurrentActivity());
                        //调用支付接口，获取支付结果
                        Map<String, String> result = alipay.payV2(response.getData(), true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandlers.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }
        }, idRequest, AlipayResponse.class, Application.getInstance().getCurrentActivity());
    }

    private Handler mHandlers = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.v("xxxxxx", "status:" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        times = 0;
                        checkOrderStatus();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getBaseContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getBaseContext(), "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    //订单状态查询接口
    private void checkOrderStatus() {
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId);
        OkHttpClientManager.postAsyn(Config.CHECK_PAY_ORDER_STATUS, new OkHttpClientManager.ResultCallback<ResponseStatus>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getBaseContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                //                customDialog.dismiss();
            }

            @Override
            public void onResponse(final ResponseStatus response) {
                Log.i("ee", new Gson().toJson(response));
                if (response.getState() == 1) {//付款成功
                    Log.i("ee", "------------1111------------");
                    startActivity();
//                    Toast.makeText(Application.getInstance().getCurrentActivity(), "支付成功", Toast.LENGTH_SHORT).show();

                } else if (response.getState() == 0) {//待付款
                    Log.i("ee", "-----------2222-------------");
                    times++;
                    if (times == 20) {
                        startActivity();
                        Toast.makeText(Application.getInstance().getCurrentActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                checkOrderStatus();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                //                customDialog.dismiss();
            }
        }, idRequest, ResponseStatus.class, Application.getInstance().getCurrentActivity());
    }

    //以上是支付宝内容


    //预支付数据  会员卡支付
    private void beforePayVipOrCoupon(int status) {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId);
        switch (status) {
            case 0://兑换券
                idRequest.setCouponList(selectVoucherList);
                break;
            case 1://会员卡支付
                idRequest.setCardNo(cardNo);
                break;
        }
        OkHttpClientManager.postAsyn(Config.BEFORE_PAY, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getBaseContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                if ("410".equals(info.getCode())) {
                    Application.getInstance().getCurrentActivity().setResult(CODE_RESULT, new Intent()
                            .putExtra("isCancel", true)
                            .putExtra("isTimeOut", true)
                            .putExtra("position", positions));
                    PaySelectActivity.this.finish();
                }
//                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
//                Log.i("ee", new Gson().toJson(response));
                startActivity();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(),exception.getInfo(),Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, Application.getInstance().getCurrentActivity());
    }


    //获取优惠券
    private void getCoupon(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        pagableRequest.setStatus("1");
        OkHttpClientManager.postAsyn(Config.GET_MY_COUPON, new OkHttpClientManager.ResultCallback<CouponBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getBaseContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CouponBean response) {
                if (response != null) {
                    couponList = response.getCoupons();
                    if (couponList != null && couponList.size() > 0) {
                        voucherList = new ArrayList<>();
                        for (int i = 0; i < couponList.size(); i++) {
                            VoucherModle modle = new VoucherModle();
                            modle.setType(couponList.get(i).getType());
                            modle.setCouponNumber(couponList.get(i).getCouponNumber());
                            modle.setOverTime(couponList.get(i).getOverTime());
                            modle.setDiscount(couponList.get(i).getDiscount());
                            modle.setChecked(false);
                            voucherList.add(modle);
                        }

                        if ("0".equals(pagables)) {//第一页
                            voucherLists.addAll(voucherList);
                            voucherAdapter.openLoadMore(voucherList.size(), true);
                            voucherAdapter.notifyDataSetChanged();
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            pagable = response.getPagable();
                        } else {
                            voucherLists.addAll(voucherList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                pagable = response.getPagable();
                                voucherAdapter.notifyDataChangedAfterLoadMore(voucherList, true);
                            } else {
                                voucherAdapter.notifyDataChangedAfterLoadMore(voucherList, true);
                                hasMore = false;
                                pagable = "";
                            }
                        }
                        voucherAdapter.setOnLoadMoreListener(PaySelectActivity.this);
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, pagableRequest, CouponBean.class, Application.getInstance().getCurrentActivity());
    }


    private void startActivity() {
        Log.i("ee", "-------------3333-----------");
//        customDialog.dismiss();
        PaySelectActivity.this.startActivityForResult(new Intent(getBaseContext(), SettingOrderDetailsActivity.class)
                .putExtra("orderId", orderId), CODE_REQUEST_FOUR);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                if (isClick) {
                    showCancelPay();
                } else {
                    setResult(CODE_RESULT, new Intent()
                            .putExtra("isCancel", true)
                            .putExtra("isTimeOut", true)
                            .putExtra("position", positions));
                    PaySelectActivity.activity.finish();
                }
                break;
            case R.id.content_pay_select__select_voucher_layout://选择兑换券
                showSelectVoucher();
                break;
            case R.id.content_pay_select__no_vip_layout://绑定会员卡
                PaySelectActivity.this.startActivityForResult(new Intent(
                        PaySelectActivity.this,
                        VipBoundActivity.class), CODE_REQUEST_THREE);
                break;
            case R.id.content_select_coupon__cancel:
                mPopupWindow.dismiss();
                break;
            case R.id.content_select_coupon__add://添加兑换券
                List<String> list = new ArrayList<>();
                String num = editNum.getText().toString().trim();
                if (num != null) {
                    list.add(num);
                }
                for (int i = 0; i < voucherLists.size(); i++) {
                    if ((voucherLists.get(i).getCouponNumber()).equals(num)) {
                        showShortToast("不能重复添加同一张兑换券");
                        return;
                    }
                }
                checkedCoupon(true, list);
                break;
            case R.id.content_select_coupon__confirm://确认兑换券
                isConfirmVoucher = true;
                selectVoucherList = new ArrayList<>();
                for (int i = 0; i < voucherLists.size(); i++) {
                    if (voucherLists.get(i).isChecked()) {
                        selectVoucherList.add(voucherLists.get(i).getCouponNumber());
                    }
                }
                if (selectVoucherList.size() > 0) {
                    if (selectVoucherList.size() > payModel.getShowCouponNum()) {
                        showShortToast("最多可使用" + payModel.getShowCouponNum() + "张兑换券");
                    } else {
                        checkedCoupon(false, null);
                    }
                } else {
                    getPay(-1);
                    mPopupWindow.dismiss();
                    voucherSelectNum.setText("选择兑换券支付");
                }
                break;
            case R.id.content_pay_select__confirm://确认支付
                //兑换券+支付宝
                if (selectVoucherList != null && selectVoucherList.size() > 0) {
                    if (isPay()) {
                        beforePayOnline(onlineList.get(position).getId() + "", 1);
                    } else {//纯兑换券
                        beforePayVipOrCoupon(0);
                    }
                } else if (memberCardList != null && memberCardList.size() > 0) { //会员卡+支付宝
                    if (isPay()) {
                        beforePayOnline(onlineList.get(position).getId() + "", 2);
                    } else {//纯会员卡
                        beforePayVipOrCoupon(1);
                    }
                } else if (isPay()) {
                    beforePayOnline(onlineList.get(position).getId() + "", 0);
                } else {
                    showShortToast("请选择支付方式");
                }
                break;
        }
    }

    private boolean isPay() { //纯支付宝
        if ("支付宝".equals(onlineList.get(position).getName()) && onlineList.get(position).getChecked() == 1) {
//            beforePayOnline(onlineList.get(position).getId() + "", 0);
            return true;
        } else {
            if ("微信".equals(onlineList.get(position).getName())) {
                showShortToast("微信支付暂未开放");
            }
//            else {
//                Toast.makeText(getBaseContext(), "请选择支付方式", Toast.LENGTH_SHORT).show();
//            }
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_TWO://充值成功
                if (data.getBooleanExtra("isUpdate", false)) {
                    int position = data.getIntExtra("rechargePosition", -1);
                    int rechargePrice = data.getIntExtra("rechargePrice", -1);
                    memberCardList.get(position).setBalance(memberCardList.get(position).getBalance() + rechargePrice);
                    if (position == rechargePosition) {//证明此会员卡已是被选中状态
                        cardNo = memberCardList.get(position).getCardNo();
                        getPay(1);
                    } else {//未被选中
                        cardAdapter.notifyItemChanged(position);
                    }
                }
                break;
            case CODE_REQUEST_THREE://获取绑定的会员卡信息
                memberCardList = (List<MemberCard>) data.getSerializableExtra("cardList");

                noCardLayout.setVisibility(View.GONE);
                haveCardListView.setVisibility(View.VISIBLE);
                cardAdapter = new PayCardAdapter(memberCardList);
                haveCardListView.setAdapter(cardAdapter);
                cardAdapter.setOnRecyclerViewItemChildClickListener(this);

                break;
            case CODE_REQUEST_FOUR:
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isCancel", false)
                        .putExtra("position", position)
                        .putExtra("isPay", data.getBooleanExtra("isPay", false)));
                PaySelectActivity.this.finish();
                break;
            case CODE_REQUEST_DIALOG:
                if ("".equals(orderId)) {
                    if (orderBean != null) {
                        initDetailsDate();
                    }
                } else {
                    getGoodsDetails();
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onEnd(CountdownView cv) {
        //倒计时结束
        isClick = false;
        //确认支付按钮
        confirmPay.setClickable(false);
        confirmPay.setBackground(getResources().getDrawable(R.drawable.radius_gray_4));
//        confirmPay.setBackgroundColor(getResources().getColor(R.color.gray_c7c7c7));
    }

    //item点击事件  支付方式
    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_select_pay__layout://网上支付  支付宝
                this.position = position;
                if (onlineList.get(position).getChecked() == 1) {
                    payType = -1;
                    onlineList.get(position).setChecked(0);
                    getPay(-1);
                } else {
                    payType = onlineList.get(position).getId();
                    onlineList.get(position).setChecked(1);
                    getPay(2);
                }
//                for (int i = 0; i < onlineList.size(); i++) {
//                    onlineList.get(i).setChecked(0);
//                }
//                payType = onlineList.get(position).getId();
//                onlineList.get(position).setChecked(1);
                this.onlinePayAdapter.notifyDataSetChanged();
                break;
            case R.id.item_pay_card__layout://会员卡

                if (selectVoucherList != null && selectVoucherList.size() > 0) {
                    showShortToast("会员卡不能和兑换券同时使用");
                } else {
                    rechargePosition = position;

                    if (memberCardList.get(position).getChecked() == 1) {
                        payType = -1;
                        memberCardList.get(position).setChecked(0);
                        getPay(-1);
                    } else {
                        cardNo = memberCardList.get(position).getCardNo();
                        memberCardList.get(position).setChecked(1);
                        getPay(1);
                    }

//                for (int i = 0; i < memberCardList.size(); i++) {
//                    memberCardList.get(i).setChecked(0);
//                }
//                cardNo = memberCardList.get(position).getCardNo();
//                memberCardList.get(position).setChecked(1);
                    this.cardAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.item_pay_card__vip_recharge://充值
                startActivityForResult(
                        new Intent(PaySelectActivity.this, RechargeActivity.class)
                                .putExtra("cardNo", memberCardList.get(position).getCardNo())
                                .putExtra("rechargePosition", position),
                        CODE_REQUEST_TWO);
                break;
            case R.id.item_voucher__layout://兑换券

                boolean isCommon = false;
                for (int i = 0; i < voucherDefaultList.size(); i++) {
                    if (voucherDefaultList.get(i).getPosition() == position) {
                        isCommon = true;
                    }
                }

                if (!isCommon) {
                    DefaultVoucher model = new DefaultVoucher();
                    model.setPosition(position);
                    model.setChecked(voucherLists.get(position).isChecked());
                    voucherDefaultList.add(model);
                }

                if (voucherLists.get(position).isChecked()) {
                    voucherLists.get(position).setChecked(false);
                } else {
                    voucherLists.get(position).setChecked(true);
                }
                voucherAdapter.notifyDataSetChanged();
                break;
        }
    }

    private PopupWindow mPopupWindow;

    //弹出 选择兑换券
    private void showSelectVoucher() {
        isConfirmVoucher = false;
        View parent = View
                .inflate(PaySelectActivity.this, R.layout.activity_pay_select, null);
        View view = View
                .inflate(PaySelectActivity.this, R.layout.popupwindows_select_coupon, null);
        view.startAnimation(AnimationUtils.loadAnimation(PaySelectActivity.this,
                R.anim.fade_in));
        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                Log.i("ee", voucherDefaultList.toString() + "\n" + voucherLists.toString());

                if (isConfirmVoucher) {
                    voucherDefaultList = new ArrayList<>();
                } else {
                    for (int i = 0; i < voucherLists.size(); i++) {
                        for (int j = 0; j < voucherDefaultList.size(); j++) {
                            if (i == voucherDefaultList.get(j).getPosition()) {
                                voucherLists.get(i).setChecked(voucherDefaultList.get(j).isChecked());
                            }
                        }
                    }
                }
                Log.i("ee", voucherDefaultList.toString() + "\n" + voucherLists.toString());
                lp.alpha = 1.0f;
                PaySelectActivity.this.getWindow().setAttributes(lp);
            }
        });

        ImageView cancel = (ImageView) view.findViewById(R.id.content_select_coupon__cancel);
        editNum = (EditText) view.findViewById(R.id.content_select_coupon__edit);
        TextView add = (TextView) view.findViewById(R.id.content_select_coupon__add);
        TextView confirm = (TextView) view.findViewById(R.id.content_select_coupon__confirm);
        voucherListView = (RecyclerView) view.findViewById(R.id.content_select_coupon__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PaySelectActivity.this);
        voucherListView.setLayoutManager(linearLayoutManager);

        voucherAdapter = new VoucherAdapter(voucherLists);
        voucherListView.setAdapter(voucherAdapter);
        voucherAdapter.setOnRecyclerViewItemChildClickListener(this);
        getCoupon(pagable);
        add.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    /**
     * show Dialog 是否确定 取消支付
     */
    private void showCancelPay() {
        LayoutInflater inflater = LayoutInflater.from(PaySelectActivity.this);
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(PaySelectActivity.this).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        TextView cancel = (TextView) layout.findViewById(R.id.alert_dialog__cancel);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
        confirm.setText("确定");
        title.setText("提示");
        message.setText("是否取消支付?");
//        message.setText("返回后，您当前选中的座位将不再保留?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消支付
//                PaySelectActivity.this.finish();
                builder.dismiss();
                cancelOrder();

            }
        });
    }


    /**
     * show Dialog 温馨提示
     */
    private void showTip() {
        LayoutInflater inflater = LayoutInflater.from(PaySelectActivity.this);
        View layout = inflater.inflate(R.layout.alert_dialog_, null);
        final AlertDialog builder = new AlertDialog.Builder(PaySelectActivity.this).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog__title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog__message);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
        message.setGravity(View.SCROLL_INDICATOR_LEFT);
        title.setText("温馨提示");
        message.setText("座位选好了，别忘记检查你的购票信息，记得15分钟内完成付款，购买后不能退换哦~");
//        message.setText("返回后，您当前选中的座位将不再保留?");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消支付
                builder.dismiss();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isClick) {
                showCancelPay();
            } else {
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isCancel", true)
                        .putExtra("isTimeOut", true)
                        .putExtra("position", positions));
                PaySelectActivity.activity.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onLoadMoreRequested() {
        voucherListView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    voucherAdapter.notifyDataChangedAfterLoadMore(false);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCoupon(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
    }
}
