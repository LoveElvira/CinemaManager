package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout haveVipLayout;//有会员卡的布局
    private RelativeLayout noHaveVipLayout;//没有会员卡的布局
    private LinearLayout cardLayout;//存放会员卡列表的父布局

    private EditText cardNum;//卡号
    private EditText cardMoney;//余额
    private TextView unBoundVip;//解绑会员卡
    private TextView boundTip;//绑定提示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_vip);
        initView();
        getCardList();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_card_title));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        cardLayout = (LinearLayout) findViewById(R.id.content_setting_vip__layout);

        boundVip = (TextView) findViewById(R.id.content_setting_vip__bound);
//        haveVipLayout = (LinearLayout) findViewById(R.id.content_setting_vip__bound_layout);
        noHaveVipLayout = (RelativeLayout) findViewById(R.id.content_setting_vip__no_card_layout);
//        cardNum = (EditText) findViewById(R.id.content_setting_vip__card);
//        cardMoney = (EditText) findViewById(R.id.content_setting_vip__money);
//        unBoundVip = (TextView) findViewById(R.id.content_setting_vip__unbound_or_bound);
//        boundTip = (TextView) findViewById(R.id.content_setting_vip__tip);
//        cardNum.setEnabled(false);
//        cardMoney.setEnabled(false);
        boundVip.setOnClickListener(this);
//        unBoundVip.setOnClickListener(this);
//        if (!"".equals(VipPayContent.vipCardNum.getText().toString()) && !"".equals(VipPayContent.vipPrice.getText().toString())) {
//            haveVipLayout.setVisibility(View.VISIBLE);
//            noHaveVipLayout.setVisibility(View.GONE);
//            cardNum.setText(VipPayContent.vipCardNum.getText().toString());
//            cardMoney.setText(VipPayContent.vipPrice.getText().toString());
//            unBoundVip.setText("解绑会员卡");
//            boundTip.setVisibility(View.GONE);
//        } else {
//            haveVipLayout.setVisibility(View.GONE);
//            noHaveVipLayout.setVisibility(View.VISIBLE);
//        }
    }

    //动态添加会员卡信息
    private View addVipCard(MemberCard memberCard) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_card, null, false);
        TextView cardNo = (TextView) view.findViewById(R.id.item_card__num);
        TextView cardPrice = (TextView) view.findViewById(R.id.item_card__price);
        cardNo.setText("No." + memberCard.getCardNo());
        cardPrice.setText(ChangeUtils.save2Decimal(memberCard.getBalance()) + " ¥");
        return view;
    }


    //获取会员卡列表
    private void getCardList() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_CARD, new OkHttpClientManager.ResultCallback<MemberCardList>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
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

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, requestNull, MemberCardList.class, Application.getInstance().getCurrentActivity());
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
                case R.id.content_setting_vip__unbound_or_bound://解除绑定
                    break;
            }
        }
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
        }
    }
}
