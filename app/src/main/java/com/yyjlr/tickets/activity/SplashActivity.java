package com.yyjlr.tickets.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/29.
 * 闪屏
 */
public class SplashActivity extends AbstractActivity {

    private TextView jump;//跳过
    float firstX = 0;
    float endX = 0;
    private View view;
    private LockableViewPager viewpager;
    private ContentAdapter adapter;
    private List<View> views;
    private LinearLayout dotLayout;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        view = findViewById(R.id.splash__view);
        dealStatusBar(view);
        initView();
    }

    private void initView() {
        jump = (TextView) findViewById(R.id.splash__jump);
        viewpager = (LockableViewPager) findViewById(R.id.splsh__viewpager);
        viewpager.setSwipeable(true);
        dotLayout = (LinearLayout) findViewById(R.id.splsh__dot_layout);
        views = new ArrayList<>();
        views.add(getPageView(R.mipmap.image_1));
        views.add(getPageView(R.mipmap.image_2));
        views.add(getPageView(R.mipmap.image_3));
        adapter = new ContentAdapter(views, null);
        viewpager.setAdapter(adapter);
        addDot();
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.class);
                SplashActivity.this.finish();
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                initFirstDot();
                dotLayout.getChildAt(position).setBackgroundResource(R.drawable.circle_white);
                if (position == views.size()-1) {
                    views.get(position).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                firstX = event.getX();
                            }
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                endX = event.getX();
                            }
                            if (firstX != 0 && endX != 0) {
                                if (endX - firstX < 0) {
                                    startActivity(MainActivity.class);
                                    SplashActivity.this.finish();
                                }
                            }

                            return true;
                        }
                    });
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 添加小圆点
     */
    private void addDot() {
        // 1.根据图片多少，添加多少小圆点
        for (int i = 0; i < views.size(); i++) {
            LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i < 1) {
                pointParams.setMargins(0, 0, 0, 0);
            } else {
                pointParams.setMargins(10, 0, 0, 0);
            }
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(pointParams);
            iv.setBackgroundResource(R.drawable.circle_gray);
            dotLayout.addView(iv);
        }
        dotLayout.getChildAt(0).setBackgroundResource(R.drawable.circle_white);
    }


    private void initFirstDot() {
        for (int i = 0; i < dotLayout.getChildCount(); i++) {
            dotLayout.getChildAt(i).setBackgroundResource(R.drawable.circle_gray);
        }
    }


    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

}
