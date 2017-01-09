package com.yyjlr.tickets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.advert.AdvertModel;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
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
    private int currentItem;
    private AdvertModel advertModel;//获取后台的数据
    private ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        view = findViewById(R.id.splash__view);
        dealStatusBar(view);
        initView();
        getAdvert();
        initListener();
    }

    private void initView() {
        jump = (TextView) findViewById(R.id.splash__jump);
        splash = (ImageView) findViewById(R.id.splash);
        splash.setVisibility(View.GONE);
        viewpager = (LockableViewPager) findViewById(R.id.splsh__viewpager);
        viewpager.setSwipeable(true);
        dotLayout = (LinearLayout) findViewById(R.id.splsh__dot_layout);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.class);
                SplashActivity.this.finish();
            }
        });
    }

    //获取开屏广告数据接口
    private void getAdvert() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_ADVERT, new OkHttpClientManager.ResultCallback<AdvertModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(AdvertModel response) {
                advertModel = response;
                views = new ArrayList<>();
                if (advertModel != null) {
                    if (advertModel.getAdvertList().size() > 0) {
                        for (int i = 0; i < response.getAdvertList().size(); i++) {
                            views.add(getPageView(response.getAdvertList().get(i).getImageUrl()));
                        }
                        adapter = new ContentAdapter(views, null);
                        viewpager.setAdapter(adapter);
                        addDot();
                    } else {
                        splash.setVisibility(View.VISIBLE);
                        autoJump();
                    }
                }else{
                    splash.setVisibility(View.VISIBLE);
                    autoJump();
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, AdvertModel.class, SplashActivity.this);
    }

    private void autoJump(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //监听事件
    private void initListener() {
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                initFirstDot();
                dotLayout.getChildAt(position).setBackgroundResource(R.drawable.circle_white);
                if (position == views.size() - 1) {
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
                currentItem = position;//获取位置，即第几页
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;//没有用到
            float endX;
            float endY;//没有用到

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        if (startX == endX && startY == endY) {// 点击跳转地址

                        }

                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (views.size() - 1) && startX - endX >= (AppManager.getInstance().getWidth() / 4)) {
                            Log.i("ee", "进入了触摸");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
//                            goToMainActivity();//进入主页
//                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);//这部分代码是切换Activity时的动画，看起来就不会很生硬
                        }
                        break;
                }
                return false;
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
        if (views.size() > 0) {
            dotLayout.getChildAt(0).setBackgroundResource(R.drawable.circle_white);
        }
    }


    private void initFirstDot() {
        for (int i = 0; i < dotLayout.getChildCount(); i++) {
            dotLayout.getChildAt(i).setBackgroundResource(R.drawable.circle_gray);
        }
    }


    private View getPageView(String imagePath) {
        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(getBaseContext())
                .load(imagePath).into(imageView);
        return imageView;
    }

}
