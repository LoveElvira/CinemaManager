package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.adapter.OrderCompleteAdapter;
import com.yyjlr.tickets.adapter.OrderUncompleteAdapter;
import com.yyjlr.tickets.content.order.CompleteOrderContent;
import com.yyjlr.tickets.content.order.UnCompleteOrderContent;
import com.yyjlr.tickets.content.sale.PackageContent;
import com.yyjlr.tickets.model.OrderEntity;
import com.yyjlr.tickets.model.order.MyOrderBean;
import com.yyjlr.tickets.model.order.MyOrderInfo;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.LockableViewPager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Elvira on 2016/8/2.
 * 我的订单
 */
public class SettingOrderActivity extends AbstractActivity implements View.OnClickListener {

    private TextView complete;
    private TextView unComplete;
    private ImageView completeImage;
    private ImageView unCompleteImage;
    private LinearLayout completeLayout;
    private LinearLayout unCompleteLayout;
    private TextView title;
    private ImageView leftArrow;
    private LockableViewPager viewPager;
    private ContentAdapter adapter;
    private CompleteOrderContent completeOrderContent;
    private UnCompleteOrderContent unCompleteOrderContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_order);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的订单");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        completeLayout = (LinearLayout) findViewById(R.id.content_setting_order__complete_layout);
        unCompleteLayout = (LinearLayout) findViewById(R.id.content_setting_order__uncomplete_layout);
        complete = (TextView) findViewById(R.id.content_setting_order__complete_title);
        unComplete = (TextView) findViewById(R.id.content_setting_order__uncomplete_title);
        completeImage = (ImageView) findViewById(R.id.content_setting_order__complete);
        unCompleteImage = (ImageView) findViewById(R.id.content_setting_order__uncomplete);

        viewPager = (LockableViewPager) findViewById(R.id.content_order__viewpager);

        completeOrderContent = new CompleteOrderContent(getBaseContext());
        unCompleteOrderContent = new UnCompleteOrderContent(getBaseContext());

        List<View> list = new ArrayList<View>();
        list.add(completeOrderContent);
        list.add(unCompleteOrderContent);

        adapter = new ContentAdapter(list, null);
        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initFirstView();
                if (position == 0) {
                    complete.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    completeImage.setImageResource(R.mipmap.complete_select);
                } else if (position == 1) {
                    unComplete.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    unCompleteImage.setImageResource(R.mipmap.uncomplete_select);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        completeLayout.setOnClickListener(this);
        unCompleteLayout.setOnClickListener(this);
    }


    private void initFirstView() {
        unComplete.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
        unCompleteImage.setImageResource(R.mipmap.uncomplete);
        complete.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
        completeImage.setImageResource(R.mipmap.complete);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                SettingOrderActivity.this.finish();
                break;
            case R.id.content_setting_order__complete_layout:
                viewPager.setCurrentItem(0);
                break;
            case R.id.content_setting_order__uncomplete_layout:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE:
                if (viewPager.getCurrentItem() == 1 && data.getBooleanExtra("isCancel", false)) {
                    unCompleteOrderContent.cancelOrderSuccess(data.getIntExtra("position", -1));
                }
                break;
        }
    }
}
