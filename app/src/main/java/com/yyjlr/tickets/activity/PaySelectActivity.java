package com.yyjlr.tickets.activity;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/8/17.
 * 支付界面
 */
public class PaySelectActivity extends AbstractActivity {

    private final String[] titles = {"网上支付", "会员卡支付"};
    private OnLinePayContent onLinePayContent;//网上支付
    private VipPayContent vipPayContent;//会员卡支付
    private LockableViewPager viewPager;
    private ContentAdapter adapter;
    private TextView title;
    private LinearLayout addPackageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("订单支付");

        addPackageLayout = (LinearLayout) findViewById(R.id.content_pay_select__add_layout);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.content_pay_select__tab_layout);
        final TabLayout.Tab[] tabs = {tabLayout.newTab().setText(titles[0]), tabLayout.newTab().setText(titles[1])};

        tabLayout.addTab(tabs[0]);
        tabLayout.addTab(tabs[1]);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (LockableViewPager) findViewById(R.id.content_pay_select__viewpager);

        onLinePayContent = new OnLinePayContent(getBaseContext());
        vipPayContent = new VipPayContent(getBaseContext());

        List<View> list = new ArrayList<View>();
        list.add(onLinePayContent);
        list.add(vipPayContent);

        adapter = new ContentAdapter(list, titles);
//        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);

        TabLayout.TabLayoutOnPageChangeListener onPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {

            @Override
            public void onPageSelected(int position) {
                setTitle(titles[position]);
                invalidateOptionsMenu();
            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
        onPageChangeListener.onPageSelected(0);

        addPackageList(2);
    }


    //动态添加酒列表
    private void addPackageList(int size) {
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(PaySelectActivity.this).inflate(R.layout.item_pay_select_film_sale, addPackageLayout, false);
            TextView packageName = (TextView) view.findViewById(R.id.item_pay_select_filmorsale__name);
            TextView packageOriginalPrice = (TextView) view.findViewById(R.id.item_pay_select_filmorsale__original_price);
            TextView packageVipPrice = (TextView) view.findViewById(R.id.item_pay_select_filmorsale__discount_price);
            addPackageLayout.addView(view);
//            if (size > 1 && i < size - 1) {
//                View lineView = new View(PaySelectActivity.this);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                lineView.setLayoutParams(lp);
//                lineView.setBackgroundColor(ContextCompat.getColor(PaySelectActivity.this, R.color.gray));
//                addPackageLayout.addView(lineView);
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!"".equals(vipPayContent.vipCardNum.getText().toString())){
            vipPayContent.boundVipCard.setVisibility(View.GONE);
            vipPayContent.noVipCard.setVisibility(View.GONE);
            vipPayContent.showVipCardLayout.setVisibility(View.VISIBLE);
            vipPayContent.confirmLayout.setVisibility(View.VISIBLE);
            vipPayContent.vipCardNum.setText(vipPayContent.vipCardNum.getText().toString());
            vipPayContent.vipPrice.setText("998");
        }

        if ("".equals(vipPayContent.vipCardNum.getText().toString()) && "".equals(vipPayContent.vipPrice.getText().toString())){
            vipPayContent.boundVipCard.setVisibility(View.VISIBLE);
            vipPayContent.noVipCard.setVisibility(View.VISIBLE);
            vipPayContent.showVipCardLayout.setVisibility(View.GONE);
            vipPayContent.confirmLayout.setVisibility(View.GONE);
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
                PaySelectActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
