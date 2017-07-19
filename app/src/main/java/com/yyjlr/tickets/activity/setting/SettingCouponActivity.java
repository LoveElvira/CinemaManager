package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.content.coupon.NoUseCouponContent;
import com.yyjlr.tickets.content.coupon.OldCouponContent;
import com.yyjlr.tickets.content.coupon.UseCouponContent;

/**
 * Created by Elvira on 2017/6/20.
 * 优惠券
 */

public class SettingCouponActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;

    private TextView useText;
    private TextView noUseText;
    private TextView oldText;
    private View useLine;
    private View noUseLine;
    private View oldLine;

    private UseCouponContent useContent;
    private NoUseCouponContent noUseContent;
    private OldCouponContent oldContent;

    private boolean isNoUseUpdate;
    private boolean isUseUpdate;
    private boolean isOldUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_coupon);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的优惠券");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);

        useText = (TextView) findViewById(R.id.content_coupon__use_text);
        noUseText = (TextView) findViewById(R.id.content_coupon__no_use_text);
        oldText = (TextView) findViewById(R.id.content_coupon__old_text);
        useLine = findViewById(R.id.content_coupon__use_line);
        noUseLine = findViewById(R.id.content_coupon__no_use_line);
        oldLine = findViewById(R.id.content_coupon__old_line);
        useContent = (UseCouponContent) findViewById(R.id.content_coupon__use_content);
        noUseContent = (NoUseCouponContent) findViewById(R.id.content_coupon__no_use_content);
        oldContent = (OldCouponContent) findViewById(R.id.content_coupon__old_content);

        leftArrow.setOnClickListener(this);
        useText.setOnClickListener(this);
        noUseText.setOnClickListener(this);
        oldText.setOnClickListener(this);

        updateView();
    }

    private void updateView() {
        isNoUseUpdate = true;
        isUseUpdate = true;
        isOldUpdate = true;
        initBgTitle(bgTitle);
        initVisibility(0, isNoUseUpdate);
        isNoUseUpdate = false;
    }

    private void initVisibility(int type, boolean isUpdate) {
        useText.setTextColor(getResources().getColor(R.color.gray_929292));
        noUseText.setTextColor(getResources().getColor(R.color.gray_929292));
        oldText.setTextColor(getResources().getColor(R.color.gray_929292));

        useLine.setVisibility(View.GONE);
        noUseLine.setVisibility(View.GONE);
        oldLine.setVisibility(View.GONE);

        useContent.setVisibility(View.GONE);
        noUseContent.setVisibility(View.GONE);
        oldContent.setVisibility(View.GONE);

        switch (type) {
            case 0://未使用
                noUseText.setTextColor(getResources().getColor(R.color.orange_ff7b0f));
                noUseLine.setVisibility(View.VISIBLE);
                noUseContent.setVisibility(View.VISIBLE);
                noUseContent.updateView(isUpdate);
                break;
            case 1://已使用
                useText.setTextColor(getResources().getColor(R.color.orange_ff7b0f));
                useLine.setVisibility(View.VISIBLE);
                useContent.setVisibility(View.VISIBLE);
                useContent.updateView(isUpdate);
                break;
            case 2://已过期
                oldText.setTextColor(getResources().getColor(R.color.orange_ff7b0f));
                oldLine.setVisibility(View.VISIBLE);
                oldContent.setVisibility(View.VISIBLE);
                oldContent.updateView(isUpdate);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                SettingCouponActivity.this.finish();
                break;
            case R.id.content_coupon__no_use_text:
                if (noUseContent.getVisibility() == View.GONE) {
                    initVisibility(0, isNoUseUpdate);
                    isNoUseUpdate = false;
                }
                break;
            case R.id.content_coupon__use_text:
                if (useContent.getVisibility() == View.GONE) {
                    initVisibility(1, isUseUpdate);
                    isUseUpdate = false;
                }
                break;
            case R.id.content_coupon__old_text:
                if (oldContent.getVisibility() == View.GONE) {
                    initVisibility(2, isOldUpdate);
                    isOldUpdate = false;
                }
                break;
        }
    }
}
