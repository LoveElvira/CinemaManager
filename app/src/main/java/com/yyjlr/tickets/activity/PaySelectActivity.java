package com.yyjlr.tickets.activity;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.view.ViewPager;
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
import java.util.Comparator;
import java.util.List;

/**
 * Created by Elvira on 2016/8/17.
 * 支付界面
 */
public class PaySelectActivity extends AbstractActivity implements View.OnClickListener {

    private OnLinePayContent onLinePayContent;//网上支付
    private VipPayContent vipPayContent;//会员卡支付
    private LockableViewPager viewPager;
    private ContentAdapter adapter;
    private TextView title;
    private ImageView leftArrow;
    private LinearLayout addPackageLayout;

    private TextView onlinePay,cardPay;
    private View onlineLine,cardLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_select);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("订单支付");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        addPackageLayout = (LinearLayout) findViewById(R.id.content_pay_select__add_layout);

        onlinePay = (TextView) findViewById(R.id.content_pay_select__online_pay);
        cardPay = (TextView) findViewById(R.id.content_pay_select__card);
        onlineLine = findViewById(R.id.content_pay_select__online_pay_line);
        cardLine = findViewById(R.id.content_pay_select__card_line);

        onlinePay.setOnClickListener(this);
        cardPay.setOnClickListener(this);

        viewPager = (LockableViewPager) findViewById(R.id.content_pay_select__viewpager);

        onLinePayContent = new OnLinePayContent(getBaseContext());
        vipPayContent = new VipPayContent(getBaseContext());

        List<View> list = new ArrayList<View>();
        list.add(onLinePayContent);
        list.add(vipPayContent);

        adapter = new ContentAdapter(list, null);
        viewPager.setSwipeable(true);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                initFirstView();
                if (position==0){
                    onlinePay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    onlineLine.setVisibility(View.VISIBLE);
                }else if(position==1){
                    cardPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    cardLine.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addPackageList(2);
    }


    //动态添加商品列表
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
        if (!"".equals(vipPayContent.vipCardNum.getText().toString())) {
            vipPayContent.boundVipCard.setVisibility(View.GONE);
            vipPayContent.noVipCard.setVisibility(View.GONE);
            vipPayContent.showVipCardLayout.setVisibility(View.VISIBLE);
            vipPayContent.confirmLayout.setVisibility(View.VISIBLE);
            vipPayContent.vipCardNum.setText(vipPayContent.vipCardNum.getText().toString());
            vipPayContent.vipPrice.setText("998");
        }

        if ("".equals(vipPayContent.vipCardNum.getText().toString()) && "".equals(vipPayContent.vipPrice.getText().toString())) {
            vipPayContent.boundVipCard.setVisibility(View.VISIBLE);
            vipPayContent.noVipCard.setVisibility(View.VISIBLE);
            vipPayContent.showVipCardLayout.setVisibility(View.GONE);
            vipPayContent.confirmLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_toolbar__left:
                PaySelectActivity.this.finish();
                break;
            case R.id.content_pay_select__online_pay:
                initFirstView();
                onlinePay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                onlineLine.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(0);
                break;
            case R.id.content_pay_select__card:
                initFirstView();
                cardPay.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                cardLine.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(1);
                break;
        }
    }

    private void initFirstView(){
        onlinePay.setTextColor(getResources().getColor(R.color.gray_cbcbcb));
        cardPay.setTextColor(getResources().getColor(R.color.gray_cbcbcb));
        onlineLine.setVisibility(View.GONE);
        cardLine.setVisibility(View.GONE);
    }
}
