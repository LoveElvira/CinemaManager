package com.yyjlr.tickets.content.pay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.PayAdapter;
import com.yyjlr.tickets.model.pay.PayModel;
import com.yyjlr.tickets.model.pay.SelectPay;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;

import java.util.List;

/**
 * Created by Elvira on 2016/8/17.
 * 网上支付
 */
public class OnLinePayContent extends LinearLayout implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private View view;
    private LinearLayout showConfirmLayout;
    private TextView confirm;
    private TextView confirmPrice;
    private RecyclerView listView;
    private PayAdapter adapter;
    private List<SelectPay> payList;

    public OnLinePayContent(Context context) {
        this(context, null);
    }

    public OnLinePayContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_pay_select_online_pay_way, this);
        initView();
        getPay();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.content_pay_select__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);

        showConfirmLayout = (LinearLayout) findViewById(R.id.content_pay_select__confirm_pay_layout);
        confirm = (TextView) findViewById(R.id.content_pay_select__confirm_pay);
        confirmPrice = (TextView) findViewById(R.id.content_pay_select__confirm_price);
        showConfirmLayout.setVisibility(View.VISIBLE);
    }

    //获取卖品数据
    private void getPay() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_PAY, new OkHttpClientManager.ResultCallback<PayModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(PayModel response) {
                Log.i("ee", new Gson().toJson(response));
                payList = response.getChannelList();

                adapter = new PayAdapter(payList);
                listView.setAdapter(adapter);
                adapter.setOnRecyclerViewItemChildClickListener(OnLinePayContent.this);
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, PayModel.class, Application.getInstance().getCurrentActivity());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        for (int i = 0; i < payList.size(); i++) {
            payList.get(i).setChecked(0);
        }
        payList.get(position).setChecked(1);
        this.adapter.notifyDataSetChanged();
    }
}
