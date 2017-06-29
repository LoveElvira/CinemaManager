package com.yyjlr.tickets.activity;

import android.graphics.Color;
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
import com.yyjlr.tickets.Application;
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
import com.yyjlr.tickets.viewutils.CircleTextProgressbar;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/7/29.
 * 闪屏
 */
public class SplashActivity extends AbstractActivity implements View.OnClickListener {

    private CircleTextProgressbar jump;//跳过
    private View view;
    private LockableViewPager viewpager;
    private ContentAdapter adapter;
    private List<View> views;
    private LinearLayout dotLayout;
    private int currentItem;
    private AdvertModel advertModel;//获取后台的数据
    private ImageView splash;
    private TextView enterMain;//进入主页
    private ImageView enterMainImage;//进入主页
    private String isFirstEnter = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        view = findViewById(R.id.splash__view);
        isFirstEnter = SharePrefUtil.getString(Constant.FILE_NAME, "isFirstEnter", "0", SplashActivity.this);
        //public static String AppDomain = "33040301";//正式 影院ID
        //public static String AppDomain = "50120255";//测试 影院ID
        //public static String AppDomain = "50120257";//xiaoqiang 影院ID
        //Constant.AppDomain = SharePrefUtil.getString(Constant.FILE_NAME, "appDomain", "33040301", SplashActivity.this);
        Config.URL = SharePrefUtil.getString(Constant.FILE_NAME, "url", "http://139.196.51.15",SplashActivity.this);
        Constant.AppDomain = SharePrefUtil.getString(Constant.FILE_NAME, "appDomain", "50120255", SplashActivity.this);
        dealStatusBar(view);
        getAppConfig(true);
        initView();
        if (!"0".equals(isFirstEnter)) {
            getAdvert();
        }
        initListener();
    }

    private void initView() {

        splash = (ImageView) findViewById(R.id.splash);
        enterMain = (TextView) findViewById(R.id.splash__enter);
        enterMainImage = (ImageView) findViewById(R.id.splash__enter_image);
//        splash.setVisibility(View.GONE);
        viewpager = (LockableViewPager) findViewById(R.id.splsh__viewpager);
        viewpager.setSwipeable(true);
        dotLayout = (LinearLayout) findViewById(R.id.splsh__dot_layout);
        jump = (CircleTextProgressbar) findViewById(R.id.splash__jump);
        jump.setOutLineColor(Color.TRANSPARENT);
        jump.setInCircleColor(getResources().getColor(R.color.black_363636));
        jump.setProgressColor(getResources().getColor(R.color.orange_ff7a0f));
        jump.setProgressLineWidth(5);
        jump.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
        jump.setCountdownProgressListener(2, progressListener);
        jump.setOnClickListener(this);
        enterMain.setOnClickListener(this);
        enterMainImage.setOnClickListener(this);

        if ("0".equals(isFirstEnter)) {
            initGuide();
            splash.setVisibility(View.GONE);
            dotLayout.setVisibility(View.GONE);
            jump.setVisibility(View.GONE);
            enterMain.setVisibility(View.GONE);
            enterMainImage.setVisibility(View.GONE);
        }
    }

    private void initGuide() {
        views = new ArrayList<>();
        views.add(getPageView(R.mipmap.guide_01));
        views.add(getPageView(R.mipmap.guide_02));
        views.add(getPageView(R.mipmap.guide_03));
        views.add(getPageView(R.mipmap.guide_04));
        adapter = new ContentAdapter(views, null);
        viewpager.setAdapter(adapter);
    }


    private CircleTextProgressbar.OnCountdownProgressListener progressListener = new CircleTextProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, int progress) {
            // 比如在首页，这里可以判断进度，进度到了100或者0的时候，你可以做跳过操作。
            if (!"0".equals(isFirstEnter)) {
                if (100 == progress) {
                    enterMain();
                }
            }
        }
    };

    //获取开屏广告数据接口
    private void getAdvert() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_ADVERT, new OkHttpClientManager.ResultCallback<AdvertModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(AdvertModel response) {
                advertModel = response;
                views = new ArrayList<>();
                if (advertModel != null) {
                    if (advertModel.getAdvertList().size() > 0) {
                        splash.setVisibility(View.GONE);
                        for (int i = 0; i < response.getAdvertList().size(); i++) {
                            views.add(getPageView(response.getAdvertList().get(i).getImageUrl()));
                        }
                        adapter = new ContentAdapter(views, null);
                        viewpager.setAdapter(adapter);
                        addDot();
                        if (advertModel.getAdvertList().size() == 1) {
                            dotLayout.setVisibility(View.GONE);
                        } else {
                            dotLayout.setVisibility(View.VISIBLE);
                        }

                        if (advertModel.getAutoSkip() == 1) {
                            jump.setTimeMillis(advertModel.getShowTime() * 1000);
                            jump.start();
                        }

                    } else {
                        splash.setVisibility(View.VISIBLE);
                        jump.start();
//                        autoJump();
                    }
                } else {
                    splash.setVisibility(View.VISIBLE);
                    jump.start();
//                    autoJump();
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, requestNull, AdvertModel.class, SplashActivity.this);
    }

    private void autoJump() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    enterMain();
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
                if (!"0".equals(isFirstEnter)) {
                    initFirstDot();
                    dotLayout.getChildAt(position).setBackgroundResource(R.drawable.circle_white);
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;//获取位置，即第几页
                if ("0".equals(isFirstEnter) && currentItem == (views.size() - 1)) {
//                    enterMain.setVisibility(View.VISIBLE);
                    enterMainImage.setVisibility(View.VISIBLE);
                } else {
//                    enterMain.setVisibility(View.GONE);
                    enterMainImage.setVisibility(View.GONE);
                }
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

                        if (!"0".equals(isFirstEnter) && currentItem == (views.size() - 1)) {
                            //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                            if (currentItem == (views.size() - 1) && startX - endX >= (AppManager.getInstance().getWidth() / 4)) {
                                Log.i("ee", "进入了触摸");
                                enterMain();
//                            goToMainActivity();//进入主页
//                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);//这部分代码是切换Activity时的动画，看起来就不会很生硬
                            }
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
//        View view = LayoutInflater.from(this).inflate(R.layout.item_splash, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.item_splash__image);
        Picasso.with(getBaseContext())
                .load(imagePath).into(imageView);
        return imageView;
    }

    private View getPageView(int resid) {
        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        View view = LayoutInflater.from(this).inflate(R.layout.item_splash, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.item_splash__image);
        imageView.setImageResource(resid);
        return imageView;
    }

    private void enterMain() {
        startActivity(MainActivity.class);
        SharePrefUtil.putString(Constant.FILE_NAME, "isFirstEnter", "1", SplashActivity.this);
        SplashActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash__enter_image:
            case R.id.splash__enter:
            case R.id.splash__jump:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    jump.stop();
                    lastClickTime = currentTime;
                    enterMain();
                }
                break;
        }
    }
}
