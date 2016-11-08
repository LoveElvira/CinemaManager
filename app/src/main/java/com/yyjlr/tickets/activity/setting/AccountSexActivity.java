package com.yyjlr.tickets.activity.setting;

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
    private LinearLayout boyLayout;
    private LinearLayout girlLayout;
    private ImageView leftArrow;
    private ImageView boyImage;
    private ImageView grilImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_sex);
        initView();
    }
    private void initView(){
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("性别");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        boyLayout = (LinearLayout) findViewById(R.id.content_account_sex__boy_layout);
        girlLayout = (LinearLayout) findViewById(R.id.content_account_sex__girl_layout);
        boyImage = (ImageView) findViewById(R.id.content_account_sex__boy);
        grilImage = (ImageView) findViewById(R.id.content_account_sex__girl);

        if ("男".equals(getIntent().getStringExtra("sex"))){
            boyImage.setVisibility(View.VISIBLE);
            grilImage.setVisibility(View.GONE);
        }else if ("女".equals(getIntent().getStringExtra("sex"))){
            boyImage.setVisibility(View.GONE);
            grilImage.setVisibility(View.VISIBLE);
        }

        boyLayout.setOnClickListener(this);
        girlLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_toolbar__left:
                AccountSexActivity.this.finish();
                break;
            case R.id.content_account_sex__boy_layout:
                boyImage.setVisibility(View.VISIBLE);
                grilImage.setVisibility(View.GONE);
                SettingAccountActivity.sex.setText("男");
                MySettingContent.sex.setImageResource(R.mipmap.boy);
                break;
            case R.id.content_account_sex__girl_layout:
                boyImage.setVisibility(View.GONE);
                grilImage.setVisibility(View.VISIBLE);
                SettingAccountActivity.sex.setText("女");
                MySettingContent.sex.setImageResource(R.mipmap.girl);
                break;
        }
    }
}
