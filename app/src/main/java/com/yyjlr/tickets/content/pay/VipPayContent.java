package com.yyjlr.tickets.content.pay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
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
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.activity.PaySelectActivity;
import com.yyjlr.tickets.activity.VipBoundActivity;
import com.yyjlr.tickets.activity.setting.SettingOrderDetailsActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.pay.MemberCard;
import com.yyjlr.tickets.model.pay.PriceResponse;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/17.
 * 会员卡支付
 */
public class VipPayContent extends LinearLayout implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    private TextView boundVipCard;//绑定会员卡
    private ImageView noVipCard;//没有会员卡
    private LinearLayout showVipCardLayout;//显示会员卡
    private TextView vipCardNum;//卡号
    private TextView vipPrice;
    private LinearLayout confirmLayout;//显示确认
    private TextView confirm;//确认
    private TextView confirmPrice;//确认金额
    private LinearLayout cardLayout;//会员卡列表

    private PopupWindow mPopupWindow = null;
    private TextView msg;
    private EditText pwd;

    private View view;
    private String orderId;
    private int price;
    private List<MemberCard> cardList;
    private int payTypeId;//支付分类
    private String cardNo = "";

    public VipPayContent(Context context) {
        this(context, null);
    }

    public VipPayContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_pay_select_vip_pay, this);
        lastClickTime = 0;
        initView();
    }

    public void initDate(List<MemberCard> cardList, int price, int payTypeId, String orderId) {
        this.orderId = orderId;
        this.cardList = cardList;
        this.price = price;
        this.payTypeId = payTypeId;
        if (cardList.size() > 0) {
            confirmLayout.setVisibility(VISIBLE);
            cardLayout.setVisibility(VISIBLE);
            boundVipCard.setVisibility(GONE);
            noVipCard.setVisibility(GONE);
            cardLayout.removeAllViews();
            for (int i = 0; i < this.cardList.size(); i++) {
                cardLayout.addView(addVipCard(this.cardList.get(i)));
            }
        }
        confirmPrice.setText(ChangeUtils.save2Decimal(price));
    }

    private void initView() {
        cardLayout = (LinearLayout) findViewById(R.id.content_pay_select__vip_layout);
        boundVipCard = (TextView) findViewById(R.id.content_pay_select__vip_bound);
        noVipCard = (ImageView) findViewById(R.id.content_pay_select__no_vip_card);
//        showVipCardLayout = (LinearLayout) findViewById(R.id.content_pay_select__vip_layout);
//        vipCardNum = (TextView) findViewById(R.id.content_pay_select__vip_account);
//        vipPrice = (TextView) findViewById(R.id.content_pay_select__vip_price);
        confirmLayout = (LinearLayout) findViewById(R.id.content_pay_select__confirm_pay_layout);
        confirm = (TextView) findViewById(R.id.content_pay_select__confirm_pay);
        confirmPrice = (TextView) findViewById(R.id.content_pay_select__confirm_price);
        confirmLayout.setVisibility(GONE);
        boundVipCard.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    //动态添加会员卡信息
    private View addVipCard(final MemberCard memberCard) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_card, null, false);
        LinearLayout cardLayout = (LinearLayout) view.findViewById(R.id.item_card__layout);
        final TextView cardNo = (TextView) view.findViewById(R.id.item_card__num);
        TextView cardPrice = (TextView) view.findViewById(R.id.item_card__price);
        final ImageView select = (ImageView) view.findViewById(R.id.item_card__select);
        cardNo.setText("No." + memberCard.getCardNo());
        cardPrice.setText("¥ " + ChangeUtils.save2Decimal(memberCard.getBalance()));
        select.setVisibility(GONE);
        //添加点击事件
        cardLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    VipPayContent.this.cardNo = memberCard.getCardNo();
                    getVipPrice(memberCard.getCardNo());
                    select.setVisibility(VISIBLE);
                }
            }
        });

        return view;
    }

    //获取会员卡价格
    private void getVipPrice(String cardNo) {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setCardNo(cardNo);
        idRequest.setOrderId(orderId);
        OkHttpClientManager.postAsyn(Config.GET_CARD_PRICE, new OkHttpClientManager.ResultCallback<PriceResponse>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(PriceResponse response) {
                if (response != null) {
                    confirmPrice.setText(response.getVipPrice());
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }
        }, idRequest, PriceResponse.class, Application.getInstance().getCurrentActivity());
    }

    //会员卡支付密码校验
    private void checkoutPwd(String cardNo, String pwd) {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setCardNo(cardNo);
        idRequest.setPwd(pwd);
        OkHttpClientManager.postAsyn(Config.PAY_CARD_CHECKOUT, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResponeNull response) {
                beforePay();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, Application.getInstance().getCurrentActivity());
    }

    //预支付数据
    private void beforePay() {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setPayTypeId(payTypeId + "");
        idRequest.setOrderId(orderId);
        idRequest.setCardNo(cardNo);
        OkHttpClientManager.postAsyn(Config.BEFORE_PAY_ORDER, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
//                Log.i("ee", new Gson().toJson(response));
                Application.getInstance().getCurrentActivity().startActivity(new Intent(getContext(), SettingOrderDetailsActivity.class)
                        .putExtra("orderId", orderId)
                        .putExtra("status", 3));
                PaySelectActivity.activity.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(),exception.getInfo(),Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, Application.getInstance().getCurrentActivity());
    }

    //弹出输入密码框选择
    private void showEditPwd() {

        View parent = View
                .inflate(Application.getInstance().getCurrentActivity(), R.layout.content_pay_select_vip_pay, null);
        View view = View
                .inflate(Application.getInstance().getCurrentActivity(), R.layout.popupwindows_checkout_pwd, null);
        view.startAnimation(AnimationUtils.loadAnimation(Application.getInstance().getCurrentActivity(),
                R.anim.fade_in));
        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = Application.getInstance().getCurrentActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);

        mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });


        pwd = (EditText) view.findViewById(R.id.popup_pwd__pwd);
        TextView cancel = (TextView) view.findViewById(R.id.popup_pwd__cancel);
        TextView confirm = (TextView) view.findViewById(R.id.popup_pwd__confirm);
        msg = (TextView) view.findViewById(R.id.popup_pwd__errow_msg);
        msg.setVisibility(GONE);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.content_pay_select__vip_bound:
                    Application.getInstance().getCurrentActivity()
                            .startActivityForResult(new Intent(
                                    Application.getInstance().getCurrentActivity(),
                                    VipBoundActivity.class), 0x06);
                    break;
                case R.id.content_pay_select__confirm_pay://确认支付
                    showEditPwd();
                    break;

                case R.id.popup_pwd__cancel:
                    mPopupWindow.dismiss();
                    break;
                case R.id.popup_pwd__confirm:

                    String pwd = this.pwd.getText().toString().trim();
                    if ("".equals(pwd)) {
                        msg.setText("请输入正确的密码");
                        msg.setVisibility(VISIBLE);
                        return;
                    }

                    checkoutPwd(cardNo, pwd);
                    break;
            }
        }
    }
}
