package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.VipBoundActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.pay.MemberCard;
import com.yyjlr.tickets.model.pay.MemberCardList;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/3.
 * 我的会员卡
 */
public class SettingVipActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;

    private TextView boundVip;//绑定会员卡
    private RelativeLayout noHaveVipLayout;//没有会员卡的布局
    private LinearLayout cardLayout;//存放会员卡列表的父布局
    private String cardNo = "";//解绑的会员卡卡号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_vip);
        customDialog = new CustomDialog(SettingVipActivity.this, "加载中...");
        initView();
        getCardList();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_card_title));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        cardLayout = (LinearLayout) findViewById(R.id.content_setting_vip__layout);

        boundVip = (TextView) findViewById(R.id.content_setting_vip__bound);
        noHaveVipLayout = (RelativeLayout) findViewById(R.id.content_setting_vip__no_card_layout);
        boundVip.setOnClickListener(this);
    }

    //动态添加会员卡信息
    private View addVipCard(final MemberCard memberCard) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_card_, null, false);
        LinearLayout parent = (LinearLayout) view.findViewById(R.id.item_card__item_layout);
        TextView cardNo = (TextView) view.findViewById(R.id.item_card__num);
        TextView cardPrice = (TextView) view.findViewById(R.id.item_card__price);
        TextView recharge = (TextView) view.findViewById(R.id.item_card__recharge);
        TextView unBound = (TextView) view.findViewById(R.id.item_card__unbound);
        cardNo.setText("No." + memberCard.getCardNo());
        cardPrice.setText(ChangeUtils.save2Decimal(memberCard.getBalance()) + " ¥");
//        parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(
//                        new Intent(SettingVipActivity.this, VipUnBoundActivity.class)
//                                .putExtra("cardInfo", memberCard),
//                        CODE_REQUEST_TWO);
//            }
//        });
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(SettingVipActivity.this, RechargeActivity.class)
                                .putExtra("cardNo", memberCard.getCardNo()),
                        CODE_REQUEST_TWO);
            }
        });
        unBound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingVipActivity.this.cardNo = memberCard.getCardNo();
                cancelPopupWindow();
            }
        });
        return view;
    }


    //获取会员卡列表
    private void getCardList() {
        customDialog.show();
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_CARD, new OkHttpClientManager.ResultCallback<MemberCardList>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MemberCardList response) {
                if (response != null) {
                    if (response.getMemberCardList() != null && response.getMemberCardList().size() > 0) {
                        cardLayout.removeAllViews();
                        cardLayout.setVisibility(View.VISIBLE);
                        noHaveVipLayout.setVisibility(View.GONE);
                        for (int i = 0; i < response.getMemberCardList().size(); i++) {
                            cardLayout.addView(addVipCard(response.getMemberCardList().get(i)));
                        }
                    }
                } else {
                    cardLayout.setVisibility(View.GONE);
                    noHaveVipLayout.setVisibility(View.VISIBLE);
                }
                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, requestNull, MemberCardList.class, Application.getInstance().getCurrentActivity());
    }

    //解绑会员卡
    private void removeCard(String cardNo, String pwd) {
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setCardNo(cardNo);
        OkHttpClientManager.postAsyn(Config.REMOVE_CARD, new OkHttpClientManager.ResultCallback<MemberCardList>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MemberCardList response) {
                if (response != null) {
                    if (response.getMemberCardList() != null && response.getMemberCardList().size() > 0) {
                        cardLayout.removeAllViews();
                        cardLayout.setVisibility(View.VISIBLE);
                        noHaveVipLayout.setVisibility(View.GONE);
                        for (int i = 0; i < response.getMemberCardList().size(); i++) {
                            cardLayout.addView(addVipCard(response.getMemberCardList().get(i)));
                        }
                    } else {
                        cardLayout.removeAllViews();
                        cardLayout.setVisibility(View.GONE);
                        noHaveVipLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    cardLayout.setVisibility(View.GONE);
                    noHaveVipLayout.setVisibility(View.VISIBLE);
                }
                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, MemberCardList.class, Application.getInstance().getCurrentActivity());
    }


    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.base_toolbar__left:
                    SettingVipActivity.this.finish();
                    break;
                case R.id.content_setting_vip__bound://绑定会员卡
                    startActivityForResult(
                            new Intent(SettingVipActivity.this, VipBoundActivity.class),
                            CODE_REQUEST_ONE);
                    break;
                case R.id.popup_unbound__confirm:
                    removeCard(SettingVipActivity.this.cardNo, null);
                    mPopupWindow.dismiss();
                    break;
                case R.id.popup_unbound__cancel:
                    mPopupWindow.dismiss();
                    break;
            }
        }
    }

    PopupWindow mPopupWindow;

    //弹出分享选择
    private void cancelPopupWindow() {

        View parent = View
                .inflate(SettingVipActivity.this, R.layout.activity_mysetting_vip, null);
        View view = View
                .inflate(SettingVipActivity.this, R.layout.popupwindows_unbound, null);
        view.startAnimation(AnimationUtils.loadAnimation(SettingVipActivity.this,
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
                lp.alpha = 1.0f;
                SettingVipActivity.this.getWindow().setAttributes(lp);
            }
        });


        TextView unBound = (TextView) view.findViewById(R.id.popup_unbound__confirm);
        TextView cancel = (TextView) view.findViewById(R.id.popup_unbound__cancel);

        unBound.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE:
                List<MemberCard> memberCardList = (List<MemberCard>) data.getSerializableExtra("cardList");
                cardLayout.removeAllViews();
                cardLayout.setVisibility(View.VISIBLE);
                noHaveVipLayout.setVisibility(View.GONE);
                for (int i = 0; i < memberCardList.size(); i++) {
                    cardLayout.addView(addVipCard(memberCardList.get(i)));
                }
                break;
            case CODE_REQUEST_TWO:
                if (data.getBooleanExtra("isUpdate", false)) {
                    getCardList();
                }
                break;
        }
    }
}
