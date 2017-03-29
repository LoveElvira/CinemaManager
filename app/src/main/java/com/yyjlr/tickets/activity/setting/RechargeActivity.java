package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.PayAdapter;

/**
 * Created by Elvira on 2017/3/28.
 * 充值
 */

public class RechargeActivity extends AbstractActivity implements View.OnClickListener {
    private TextView title;
    private ImageView leftArrow;
    private RecyclerView listView;
    private PayAdapter adapter;
    private EditText price;//充值金额
    private TextView confirm;
    private LinearLayout delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("修改昵称");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        listView = (RecyclerView) findViewById(R.id.content_recharge__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        price = (EditText) findViewById(R.id.content_recharge__price);
        delete = (LinearLayout) findViewById(R.id.content_recharge__delete);
        confirm = (TextView) findViewById(R.id.content_recharge__confirm);
        price.addTextChangedListener(textWatcher);
        delete.setVisibility(View.GONE);
        confirm.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                price.setHint("建议充值100以上金额");
                delete.setVisibility(View.GONE);
            }else{
                delete.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                RechargeActivity.this.finish();
                break;
            case R.id.content_recharge__confirm:
                break;
            case R.id.content_recharge__delete:
                break;
        }
    }
}
