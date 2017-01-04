package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.sale.PackageDetailsActivity;
import com.yyjlr.tickets.activity.sale.SaleCompleteActivity;
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
import java.util.Date;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 卖品页面
 */
public class SaleContent extends LinearLayout implements View.OnClickListener {
    private View view;
    private TextView title;

    private LinearLayout packageLayout;
    private LinearLayout saleLayout;
    private ImageView packageImage;
    private ImageView saleImage;
    private LockableViewPager viewPager;
    private ContentAdapter adapter;

    private com.yyjlr.tickets.content.sale.SaleContent saleContent;
    private com.yyjlr.tickets.content.sale.PackageContent packageContent;

    private boolean flag = true;


    public SaleContent(Context context) {
        this(context, null);
    }

    public SaleContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_sale, this);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_sale_title));

        packageLayout = (LinearLayout) findViewById(R.id.fragment_sale__package_layout);
        saleLayout = (LinearLayout) findViewById(R.id.fragment_sale__sale_layout);
        packageImage = (ImageView) findViewById(R.id.fragment_sale__package_image);
        saleImage = (ImageView) findViewById(R.id.fragment_sale__sale_image);

        viewPager = (LockableViewPager) findViewById(R.id.content_sale__viewpager);

        saleContent = new com.yyjlr.tickets.content.sale.SaleContent(getContext());
        packageContent = new PackageContent(getContext());

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

            @Override
            public void onPageSelected(int position) {
                initFirstView();
                if (position == 0) {
                    packageImage.setImageResource(R.mipmap.sale_package_select);
                } else if (position == 1) {
                    saleImage.setImageResource(R.mipmap.sale_sale_select);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        packageLayout.setOnClickListener(this);
        saleLayout.setOnClickListener(this);
        viewPager.setCurrentItem(1);

    }

    private void initFirstView() {
        saleImage.setImageResource(R.mipmap.sale_sale);
        packageImage.setImageResource(R.mipmap.sale_package);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_sale__package_layout:
                viewPager.setCurrentItem(0);
                break;
            case R.id.fragment_sale__sale_layout:
                viewPager.setCurrentItem(1);
                break;
        }
    }
}
