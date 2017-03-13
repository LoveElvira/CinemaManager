package com.yyjlr.tickets.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.yyjlr.tickets.adapter.PayAdapter;
import com.yyjlr.tickets.content.pay.OnLinePayContent;
import com.yyjlr.tickets.content.pay.VipPayContent;
import com.yyjlr.tickets.content.pay.VoucherPayContent;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.order.ConfirmOrderBean;
import com.yyjlr.tickets.model.order.OrderItemsInfo;
import com.yyjlr.tickets.model.pay.MemberCard;
import com.yyjlr.tickets.model.pay.PayModel;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/17.
 * 支付界面
 */
public class PaySelectActivity extends AbstractActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_select);
        activity = this;
        orderId = getIntent().getStringExtra("orderId");
        orderBean = (ConfirmOrderBean) getIntent().getSerializableExtra("orderBean");
        if ("".equals(orderId)) {
            orderId = orderBean.getId() + "";
        }
        initView();
        getPay();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("订单支付");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

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

        onLinePayContent = new OnLinePayContent(getBaseContext(), orderId);
        vipPayContent = new VipPayContent(getBaseContext(), orderId);
        voucherPayContent = new VoucherPayContent(getBaseContext(), orderId);

        price.setText(ChangeUtils.save2Decimal(orderBean.getPrice()));
        addPackageList(orderBean.getItems());

        cardLayout.setVisibility(View.GONE);
        voucherLayout.setVisibility(View.GONE);
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
            onLinePayContent.initDate(payModel.getChannelList(), orderBean.getPrice());
            onlinePay.setOnClickListener(this);
        }
        //是否显示会员卡支付
        if (payModel.getShowMemberCard() == 1) {
            list.add(vipPayContent);
            cardLayout.setVisibility(View.VISIBLE);
            cardPosition = onlinePosition + 1;
            vipPayContent.initDate(payModel.getMemberCardList(), orderBean.getPrice(), payModel.getMemberCardTypeId());
            cardPay.setOnClickListener(this);
        }
        //是否显示兑换券支付
        if (payModel.getShowCoupon() == 1) {
            list.add(voucherPayContent);
            voucherLayout.setVisibility(View.VISIBLE);
            voucherPosition = cardPosition + 1;
            voucherPayContent.initDate(payModel.getShowCouponNum(), orderBean.getPrice(), payModel.getCouponTypeId());
            voucherPay.setOnClickListener(this);
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
        }, idRequest, PayModel.class, Application.getInstance().getCurrentActivity());
    }


    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (v.getId()) {
                case R.id.base_toolbar__left:
                    showCancelPay();
                    break;
                case R.id.content_pay_select__online_pay:
//                initFirstView();
//                onlinePay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
//                onlineLine.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(onlinePosition);
                    break;
                case R.id.content_pay_select__card:
//                initFirstView();
//                cardPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
//                cardLine.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(cardPosition);
                    break;
                case R.id.content_pay_select__voucher:
//                initFirstView();
//                cardPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
//                cardLine.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(voucherPosition);
                    break;
            }
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
                PaySelectActivity.this.finish();
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
            case CODE_REQUEST_ONE://获取绑定的会员卡信息
                List<MemberCard> memberCardList = (List<MemberCard>) data.getSerializableExtra("cardList");
                vipPayContent.initDate(memberCardList, orderBean.getPrice(), payModel.getMemberCardTypeId());
                break;
        }
    }
}
