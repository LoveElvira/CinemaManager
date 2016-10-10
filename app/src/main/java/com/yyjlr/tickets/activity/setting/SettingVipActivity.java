package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.VipBoundActivity;
import com.yyjlr.tickets.activity.VipPayContent;

/**
 * Created by Elvira on 2016/8/3.
 * 我的会员卡
 */
public class SettingVipActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;

    private TextView boundVip;//绑定会员卡
    private LinearLayout haveVipLayout;//有会员卡的布局
    private RelativeLayout noHaveVipLayout;//没有会员卡的布局

    private EditText cardNum;//卡号
    private EditText cardMoney;//余额
    private TextView unBoundVip;//解绑会员卡
    private TextView boundTip;//绑定提示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_vip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_card_title));

        boundVip = (TextView) findViewById(R.id.content_setting_vip__bound);
        haveVipLayout = (LinearLayout) findViewById(R.id.content_setting_vip__bound_layout);
        noHaveVipLayout = (RelativeLayout) findViewById(R.id.content_setting_vip__no_card_layout);
        cardNum = (EditText) findViewById(R.id.content_setting_vip__card);
        cardMoney = (EditText) findViewById(R.id.content_setting_vip__money);
        unBoundVip = (TextView) findViewById(R.id.content_setting_vip__unbound_or_bound);
        boundTip = (TextView) findViewById(R.id.content_setting_vip__tip);
        cardNum.setEnabled(false);
        cardMoney.setEnabled(false);
        boundVip.setOnClickListener(this);
        unBoundVip.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_setting_vip__bound://绑定会员卡
                startActivity(VipBoundActivity.class);
                break;
            case R.id.content_setting_vip__unbound_or_bound://解除绑定
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SettingVipActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
