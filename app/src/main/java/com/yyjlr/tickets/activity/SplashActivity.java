package com.yyjlr.tickets.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.viewutils.banner.BGABanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/29.
 * 闪屏
 */
public class SplashActivity extends AbstractActivity {

    private BGABanner banner;
    private TextView jump;//跳过
    float firstX = 0;
    float endX = 0;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        jump = (TextView) findViewById(R.id.splash__banner_jump);
        banner = (BGABanner) findViewById(R.id.splash__banner_pager);
        List<View> views = new ArrayList<>();
        views.add(getPageView(R.mipmap.image_1));
        views.add(getPageView(R.mipmap.image_2));
        views.add(getPageView(R.mipmap.image_3));

        View lastView = getLayoutInflater().inflate(R.layout.splash_last_view, null);
        views.add(lastView);
//        lastView.findViewById(R.id.splash__image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(MainActivity.class);
//                finish();
//            }
//        });
        lastView.findViewById(R.id.splash__image).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    firstX = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    endX = event.getX();
                }
                if(firstX!=0 && endX!=0){
                    if (endX - firstX < 0) {
                        startActivity(MainActivity.class);
                        finish();
                    }
                }

                return true;
            }
        });
        banner.setViews(views);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MainActivity.class);
                finish();
            }
        });
    }

    private View getPageView(@DrawableRes int resid) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(resid);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

}
