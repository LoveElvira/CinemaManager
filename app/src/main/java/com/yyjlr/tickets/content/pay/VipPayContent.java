package com.yyjlr.tickets.content.pay;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.VipBoundActivity;

/**
 * Created by Elvira on 2016/8/17.
 * 会员卡支付
 */
public class VipPayContent extends LinearLayout implements View.OnClickListener {

    public static TextView boundVipCard;//绑定会员卡
    public static ImageView noVipCard;//没有会员卡
    public static LinearLayout showVipCardLayout;//显示会员卡
    public static TextView vipCardNum;//卡号
    public static TextView vipPrice;
    public static LinearLayout confirmLayout;//显示确认
    private TextView confirm;//确认
    private TextView confirmPrice;//确认金额

    private View view;

    public VipPayContent(Context context) {
        this(context, null);
    }

    public VipPayContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_pay_select_vip_pay, this);

        initView();


    }

    private void initView() {
        boundVipCard = (TextView) findViewById(R.id.content_pay_select__vip_bound);
        noVipCard = (ImageView) findViewById(R.id.content_pay_select__no_vip_card);
        showVipCardLayout = (LinearLayout) findViewById(R.id.content_pay_select__vip_layout);
        vipCardNum = (TextView) findViewById(R.id.content_pay_select__vip_account);
        vipPrice = (TextView) findViewById(R.id.content_pay_select__vip_price);
        confirmLayout = (LinearLayout) findViewById(R.id.content_pay_select__confirm_pay_layout);
        confirm = (TextView) findViewById(R.id.content_pay_select__confirm_pay);
        confirmPrice = (TextView) findViewById(R.id.content_pay_select__confirm_price);

        boundVipCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), VipBoundActivity.class));
    }
}
