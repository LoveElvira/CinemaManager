package com.yyjlr.tickets.activity.sale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.adapter.SaleAdapter;
import com.yyjlr.tickets.adapter.SalePackageAdapter;
import com.yyjlr.tickets.content.sale.PackageContent;
import com.yyjlr.tickets.model.SaleEntity;
import com.yyjlr.tickets.model.sale.GoodInfo;
import com.yyjlr.tickets.model.sale.Goods;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.IRequestMainData;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.LockableViewPager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Elvira on 2016/11/10.
 * 从影院详情里面进行跳转的卖品页面
 */

public class SaleActivity extends AbstractActivity implements View.OnClickListener {

    private ImageView back;
    private LockableViewPager viewPager;
    private ContentAdapter adapter;

    private TextView saleText;
    private TextView packageText;

    private com.yyjlr.tickets.content.sale.SaleContent saleContent;
    private com.yyjlr.tickets.content.sale.PackageContent packageContent;
    private EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_sale);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        back = (ImageView) findViewById(R.id.base_toolbar__left_image);
        back.setOnClickListener(this);

        saleText = (TextView) findViewById(R.id.base_toolbar__left);
        packageText = (TextView) findViewById(R.id.base_toolbar__right);

        viewPager = (LockableViewPager) findViewById(R.id.content_sale__viewpager);

        saleContent = new com.yyjlr.tickets.content.sale.SaleContent(getBaseContext());
        packageContent = new PackageContent(getBaseContext());

        List<View> list = new ArrayList<View>();
        list.add(packageContent);
        list.add(saleContent);

        adapter = new ContentAdapter(list, null);
        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                initFirstView();
                if (position == 0) {
                    packageText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    packageText.setBackground(getResources().getDrawable(R.drawable.bg_right_white));
                    //search = saleContent.getEditText();
                    //hideInput();
                } else if (position == 1) {
                    saleText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    saleText.setBackground(getResources().getDrawable(R.drawable.bg_left_white));
                   // search = packageContent.getEditText();
                   // hideInput();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        packageText.setOnClickListener(this);
        saleText.setOnClickListener(this);
        viewPager.setCurrentItem(1);

    }

    //隐藏虚拟键盘
    public void hideInput() {
        boolean isOpen = imm.isActive();
        if (isOpen) {
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initFirstView() {
        saleText.setTextColor(getResources().getColor(R.color.white));
        packageText.setTextColor(getResources().getColor(R.color.white));
        saleText.setBackground(getResources().getDrawable(R.drawable.bg_border_left_white));
        packageText.setBackground(getResources().getDrawable(R.drawable.bg_border_right_white));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__right:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.base_toolbar__left:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.base_toolbar__left_image:
                SaleActivity.this.finish();
                break;
        }
    }
}
