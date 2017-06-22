package com.yyjlr.tickets.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.pay.OnLinePayContent;
import com.yyjlr.tickets.content.pay.VipPayContent;
import com.yyjlr.tickets.content.pay.VoucherPayContent;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.order.ConfirmOrderBean;
import com.yyjlr.tickets.model.order.OrderItemsInfo;
import com.yyjlr.tickets.model.pay.MemberCard;
import com.yyjlr.tickets.model.pay.PayModel;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.LockableViewPager;
import com.yyjlr.tickets.viewutils.countdown.CountdownView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Elvira on 2016/8/17.
 * 支付界面
 */
public class PaySelectActivity extends AbstractActivity implements View.OnClickListener, CountdownView.OnCountdownEndListener {

    private OnLinePayContent onLinePayContent;//网上支付
    private VipPayContent vipPayContent;//会员卡支付
    private VoucherPayContent voucherPayContent;//兑换券支付
    private LockableViewPager viewPager;
    private ContentAdapter adapter;
    private TextView title;
    private ImageView leftArrow;
    private LinearLayout addPackageLayout;
    //网上支付  会员卡支付  兑换券支付
    private LinearLayout onlineLayout, cardLayout, voucherLayout;
    private TextView onlinePay, cardPay, voucherPay;
    private View onlineLine, cardLine, voucherLine;
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
    private int position = -1;
    private boolean isClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_select);
        initView();
        activity = this;
        orderId = getIntent().getStringExtra("orderId");
        orderBean = (ConfirmOrderBean) getIntent().getSerializableExtra("orderBean");
        position = getIntent().getIntExtra("position", -1);
        if ("".equals(orderId)) {
            if (orderBean != null) {
                initDetailsDate();
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

        onlineLayout = (LinearLayout) findViewById(R.id.content_pay_select__online_pay_layout);
        cardLayout = (LinearLayout) findViewById(R.id.content_pay_select__card_layout);
        voucherLayout = (LinearLayout) findViewById(R.id.content_pay_select__voucher_layout);

        onlinePay = (TextView) findViewById(R.id.content_pay_select__online_pay);
        cardPay = (TextView) findViewById(R.id.content_pay_select__card);
        voucherPay = (TextView) findViewById(R.id.content_pay_select__voucher);
        onlineLine = findViewById(R.id.content_pay_select__online_pay_line);
        cardLine = findViewById(R.id.content_pay_select__card_line);
        voucherLine = findViewById(R.id.content_pay_select__voucher_line);

        viewPager = (LockableViewPager) findViewById(R.id.content_pay_select__viewpager);

        onLinePayContent = new OnLinePayContent(getBaseContext());
        vipPayContent = new VipPayContent(getBaseContext());
        voucherPayContent = new VoucherPayContent(getBaseContext());

        cardLayout.setVisibility(View.GONE);
        voucherLayout.setVisibility(View.GONE);
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
        getPay();
        price.setText(ChangeUtils.save2Decimal(orderBean.getPrice()));
        addPackageList(orderBean.getItems());
    }

    public void startRefreshTime() {
        if (!isCancel) return;

        if (null != mTimer) {
            mTimer.cancel();
        }

        isCancel = false;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mRefreshTimeRunnable);
            }
        }, 0, 1000);
    }

    public void cancelRefreshTime() {
        isCancel = true;
        if (null != mTimer) {
            mTimer.cancel();
        }
        mHandler.removeCallbacks(mRefreshTimeRunnable);
    }


    private Runnable mRefreshTimeRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) {
                // 倒计时结束
                endTime(0);
            } else {
                refreshTime(currentTime);
            }
        }
    };

    public void refreshTime(long curTimeMillis) {
        if (endTime <= curTimeMillis) return;
        countDownTime.updateShow(endTime - curTimeMillis);
    }

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
        List<View> list = new ArrayList<View>();
        if (payModel.getChannelList() != null && payModel.getChannelList().size() > 0) {
            list.add(onLinePayContent);
            onlinePosition = 0;
            onLinePayContent.initDate(payModel.getChannelList(), orderBean.getPrice(), orderId, position);
            onlineLayout.setOnClickListener(this);
        }
        //是否显示会员卡支付
        if (payModel.getShowMemberCard() == 1) {
            list.add(vipPayContent);
            cardLayout.setVisibility(View.VISIBLE);
            cardPosition = onlinePosition + 1;
            vipPayContent.initDate(payModel.getMemberCardList(), orderBean.getPrice(), payModel.getMemberCardTypeId(), orderId, position);
            cardLayout.setOnClickListener(this);
        }
        //是否显示兑换券支付
        if (payModel.getShowCoupon() == 1) {
            list.add(voucherPayContent);
            voucherLayout.setVisibility(View.VISIBLE);
            voucherPosition = cardPosition + 1;
            voucherPayContent.initDate(payModel.getShowCouponNum(), orderBean.getPrice(), payModel.getCouponTypeId(), orderId, position);
            voucherLayout.setOnClickListener(this);
        }

        if (list.size() > 0) {
            adapter = new ContentAdapter(list, null);
            viewPager.setSwipeable(true);
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    initFirstView();
                    if (position == onlinePosition) {
                        onlinePay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                        onlineLine.setVisibility(View.VISIBLE);
                    }
                    if (payModel.getShowMemberCard() == 1) {
                        if (position == cardPosition) {
                            cardPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                            cardLine.setVisibility(View.VISIBLE);
                        }
                    }
                    if (payModel.getShowCoupon() == 1) {
                        if (position == voucherPosition) {
                            voucherPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                            voucherLine.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

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

//                payList = response.getChannelList();
//
//                adapter = new PayAdapter(payList);
//                listView.setAdapter(adapter);
//                adapter.setOnRecyclerViewItemChildClickListener(OnLinePayContent.this);
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, idRequest, ConfirmOrderBean.class, Application.getInstance().getCurrentActivity());
    }

    //获取支付数据
    private void getPay() {
        IdRequest idRequest = new IdRequest();
        idRequest.setId(orderId);
        OkHttpClientManager.postAsyn(Config.GET_PAY, new OkHttpClientManager.ResultCallback<PayModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(PayModel response) {
                Log.i("ee", new Gson().toJson(response));
                payModel = response;
                if (payModel != null) {
                    initDate();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, idRequest, PayModel.class, Application.getInstance().getCurrentActivity());
    }

    //取消订单
    private void cancelOrder() {
        customDialog = new CustomDialog(this, "请稍后...");
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
                            .putExtra("position", position));
                    PaySelectActivity.activity.finish();
                }
            }

            @Override
            public void onResponse(ResponeNull response) {
                customDialog.dismiss();
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isCancel", true)
                        .putExtra("position", position));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                if (isClick) {
                    showCancelPay();
                } else {
                    setResult(CODE_RESULT, new Intent()
                            .putExtra("isCancel", true)
                            .putExtra("position", position));
                    PaySelectActivity.activity.finish();
                }
                break;
            case R.id.content_pay_select__online_pay_layout:
//                initFirstView();
//                onlinePay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
//                onlineLine.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(onlinePosition);
                break;
            case R.id.content_pay_select__card_layout:
//                initFirstView();
//                cardPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
//                cardLine.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(cardPosition);
                break;
            case R.id.content_pay_select__voucher_layout:
//                initFirstView();
//                cardPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
//                cardLine.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(voucherPosition);
                break;
        }
    }

    private void initFirstView() {
        onlinePay.setTextColor(getResources().getColor(R.color.gray_cbcbcb));
        cardPay.setTextColor(getResources().getColor(R.color.gray_cbcbcb));
        voucherPay.setTextColor(getResources().getColor(R.color.gray_cbcbcb));
        onlineLine.setVisibility(View.GONE);
        cardLine.setVisibility(View.GONE);
        voucherLine.setVisibility(View.GONE);
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
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_THREE://获取绑定的会员卡信息
                List<MemberCard> memberCardList = (List<MemberCard>) data.getSerializableExtra("cardList");
                vipPayContent.initDate(memberCardList, orderBean.getPrice(), payModel.getMemberCardTypeId(), orderId, position);
                break;
            case CODE_REQUEST_FOUR:
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isCancel", false)
                        .putExtra("position", position)
                        .putExtra("isPay", data.getBooleanExtra("isPay", false)));
                PaySelectActivity.this.finish();
                break;
        }
    }

    @Override
    public void onEnd(CountdownView cv) {
        //倒计时结束
        isClick = false;
        onLinePayContent.setConfirmClickable();
        vipPayContent.setConfirmClickable();
        voucherPayContent.setConfirmClickable();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCancelPay();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
