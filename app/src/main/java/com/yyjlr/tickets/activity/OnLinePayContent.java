package com.yyjlr.tickets.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;

/**
 * Created by Elvira on 2016/8/17.
 * 网上支付
 */
public class OnLinePayContent extends LinearLayout implements View.OnClickListener {

    private View view;
    private LinearLayout zhifubaoLayout;
    private LinearLayout weinxinLayout;
    private ImageView zhifubaoImage;
    private ImageView weixinImage;
    private LinearLayout showConfirmLayout;
    private TextView confirm;
    private TextView confirmPrice;

    public OnLinePayContent(Context context) {
        this(context, null);
    }

    public OnLinePayContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_pay_select_online_pay_way, this);
        zhifubaoLayout = (LinearLayout) findViewById(R.id.content_pay_select__zhifubao);
        weinxinLayout = (LinearLayout) findViewById(R.id.content_pay_select__weixin);
        zhifubaoImage = (ImageView) findViewById(R.id.content_pay_select__zhifubao_select);
        weixinImage = (ImageView) findViewById(R.id.content_pay_select__weixin_select);
        showConfirmLayout = (LinearLayout) findViewById(R.id.content_pay_select__confirm_pay_layout);
        confirm = (TextView) findViewById(R.id.content_pay_select__confirm_pay);
        confirmPrice = (TextView) findViewById(R.id.content_pay_select__confirm_price);
        showConfirmLayout.setVisibility(View.VISIBLE);
        zhifubaoLayout.setOnClickListener(this);
        weinxinLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.content_pay_select__zhifubao:
                zhifubaoImage.setImageResource(R.mipmap.sale_select);
                weixinImage.setImageResource(R.mipmap.sale_no_select);
                break;
            case R.id.content_pay_select__weixin:
                zhifubaoImage.setImageResource(R.mipmap.sale_no_select);
                weixinImage.setImageResource(R.mipmap.sale_select);
                break;
        }
    }
}
