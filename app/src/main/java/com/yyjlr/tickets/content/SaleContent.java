package com.yyjlr.tickets.content;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 卖品页面
 */
public class SaleContent extends LinearLayout implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

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
        lastClickTime = 0;
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
//        packageContent = new PackageContent(getContext());

        List<View> list = new ArrayList<View>();
//        list.add(packageContent);
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
        viewPager.setCurrentItem(0);

    }

    private void initFirstView() {
        saleImage.setImageResource(R.mipmap.sale_sale);
        packageImage.setImageResource(R.mipmap.sale_package);
    }

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.fragment_sale__package_layout:
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    }
                    break;
                case R.id.fragment_sale__sale_layout:
                    if (viewPager.getCurrentItem() != 1) {
                        viewPager.setCurrentItem(1);
                    }
                    break;
            }
        }
    }
}
