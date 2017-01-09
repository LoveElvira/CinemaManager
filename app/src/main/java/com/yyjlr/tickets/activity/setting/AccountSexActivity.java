package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.content.MySettingContent;

/**
 * Created by Elvira on 2016/9/6.
 */
public class AccountSexActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private TextView confirm;
    private ImageView rightArrow;
    private LinearLayout boyLayout;
    private LinearLayout girlLayout;
    private ImageView leftArrow;
    private ImageView boyImage;
    private ImageView grilImage;
    private TextView girl, boy;
    private String sex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_sex);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("性别");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        confirm = (TextView) findViewById(R.id.base_toolbar__right_text);
        confirm.setText("保存");
        confirm.setVisibility(View.VISIBLE);
        rightArrow = (ImageView) findViewById(R.id.base_toolbar__right);
        rightArrow.setVisibility(View.GONE);

        boyLayout = (LinearLayout) findViewById(R.id.content_account_sex__boy_layout);
        girlLayout = (LinearLayout) findViewById(R.id.content_account_sex__girl_layout);
        boyImage = (ImageView) findViewById(R.id.content_account_sex__boy_img);
        grilImage = (ImageView) findViewById(R.id.content_account_sex__girl_img);
        girl = (TextView) findViewById(R.id.content_account_sex__girl);
        boy = (TextView) findViewById(R.id.content_account_sex__boy);

        sex = getIntent().getStringExtra("sex");
        if ("男".equals(sex)) {
            boyImage.setVisibility(View.VISIBLE);
            grilImage.setVisibility(View.GONE);
        } else if ("女".equals(sex)) {
            boyImage.setVisibility(View.GONE);
            grilImage.setVisibility(View.VISIBLE);
        }

        boyLayout.setOnClickListener(this);
        girlLayout.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                AccountSexActivity.this.finish();
                break;
            case R.id.content_account_sex__boy_layout:
                boyImage.setVisibility(View.VISIBLE);
                grilImage.setVisibility(View.GONE);
                sex = boy.getText().toString();
//                SettingAccountActivity.sex.setText("男");
//                MySettingContent.sex.setImageResource(R.mipmap.boy);
                break;
            case R.id.content_account_sex__girl_layout:
                boyImage.setVisibility(View.GONE);
                grilImage.setVisibility(View.VISIBLE);
                sex = girl.getText().toString();
//                SettingAccountActivity.sex.setText("女");
//                MySettingContent.sex.setImageResource(R.mipmap.girl);
                break;
            case R.id.base_toolbar__right_text://保存
                setResult(CODE_RESULT, new Intent().putExtra("sex", sex));
                AccountSexActivity.this.finish();
                break;
        }
    }
}
