package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;

/**
 * Created by Elvira on 2016/8/3.
 * 绑定会员卡
 */
public class VipBoundActivity extends AbstractActivity implements View.OnClickListener {

    private ImageView rightService;
    private ImageView leftArrow;
    private TextView title;

    private LinearLayout haveVipLayout;//有会员卡的布局

    private EditText cardNum;//卡号
    private EditText cardPassword;//密码
    private TextView unBounOrBoundVip;//解绑会员卡
    private TextView pwdTitle;//解绑会员卡
    private TextView tipTitle;//提示
    public String card, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_unbound_vip);
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_card_bound_title));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        rightService = (ImageView) findViewById(R.id.base_toolbar__right);
        rightService.setImageResource(R.mipmap.service);
        rightService.setAlpha(1.0f);

        haveVipLayout = (LinearLayout) findViewById(R.id.content_setting_vip__bound_layout);
        haveVipLayout.setVisibility(View.VISIBLE);
        cardNum = (EditText) findViewById(R.id.content_setting_vip__card);
        cardPassword = (EditText) findViewById(R.id.content_setting_vip__money);
        unBounOrBoundVip = (TextView) findViewById(R.id.content_setting_vip__unbound_or_bound);
        unBounOrBoundVip.setText("绑定会员卡");
        pwdTitle = (TextView) findViewById(R.id.content_setting_vip__password);
        tipTitle = (TextView) findViewById(R.id.content_setting_vip__tip);
        pwdTitle.setText("密码");
        tipTitle.setVisibility(View.VISIBLE);
        cardPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        unBounOrBoundVip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_setting_vip__unbound_or_bound://绑定或解除绑定
                card = cardNum.getText().toString();
                if ("".equals(card)) {
                    showShortToast("请确认卡号");
                    return;
                }
                this.finish();
                break;
            case R.id.base_toolbar__left:
                VipBoundActivity.this.finish();
                break;
        }
    }
}