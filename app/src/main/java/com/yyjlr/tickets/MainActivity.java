package com.yyjlr.tickets;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.CinemaDetailsActivity;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.ChosenContent;
import com.yyjlr.tickets.content.FilmContent;
import com.yyjlr.tickets.content.GrabTicketContent;
import com.yyjlr.tickets.content.MySettingContent;
import com.yyjlr.tickets.content.SaleContent;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 主页
 */
public class MainActivity extends AbstractActivity implements View.OnClickListener {

    private ContentAdapter adapter;
    private LockableViewPager viewPager;

    private ChosenContent chosenContent;
    private FilmContent filmContent;
    private MySettingContent mySettingContent;
    private GrabTicketContent grabTicketContent;
    private SaleContent saleContent;

    private View view;

    private LinearLayout chosenLayout, filmLayout, grabLayout, saleLayout, myLayout;
    private ImageView chosenImage, filmImage, grabImage, saleImage, myImage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.content_main_view);
        dealStatusBar(view);

        initView();
//        initPagerContent();
//        List<View> list = new ArrayList<View>();
//        list.add(chosenContent);
//        list.add(filmContent);
//        list.add(grabTicketContent);
//        list.add(saleContent);
//        list.add(mySettingContent);
//        adapter = new ContentAdapter(list, null);
//        viewPager.setSwipeable(false);
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
    }

    private void initView() {
//        viewPager = (LockableViewPager) findViewById(R.id.content_main_viewpager);
        chosenContent = (ChosenContent) findViewById(R.id.content_main__chosen);
        filmContent = (FilmContent) findViewById(R.id.content_main__film);
        grabTicketContent = (GrabTicketContent) findViewById(R.id.content_main__ticket);
        saleContent = (SaleContent) findViewById(R.id.content_main__sale);
        mySettingContent = (MySettingContent) findViewById(R.id.content_main__my);


        chosenLayout = (LinearLayout) findViewById(R.id.bottom_button__chosen);
        filmLayout = (LinearLayout) findViewById(R.id.bottom_button__film);
        grabLayout = (LinearLayout) findViewById(R.id.bottom_button__grab);
        saleLayout = (LinearLayout) findViewById(R.id.bottom_button__sale);
        myLayout = (LinearLayout) findViewById(R.id.bottom_button__my);
        chosenImage = (ImageView) findViewById(R.id.bottom_button__chosen_image);
        filmImage = (ImageView) findViewById(R.id.bottom_button__film_image);
        grabImage = (ImageView) findViewById(R.id.bottom_button__grab_image);
        saleImage = (ImageView) findViewById(R.id.bottom_button__sale_image);
        myImage = (ImageView) findViewById(R.id.bottom_button__my_image);

        chosenLayout.setOnClickListener(this);
        filmLayout.setOnClickListener(this);
        grabLayout.setOnClickListener(this);
        saleLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);

        initVisibility();
        chosenContent.setVisibility(View.VISIBLE);
        chosenContent.initView();
    }

    private void initVisibility() {
        chosenContent.setVisibility(View.GONE);
        filmContent.setVisibility(View.GONE);
        grabTicketContent.setVisibility(View.GONE);
        saleContent.setVisibility(View.GONE);
        mySettingContent.setVisibility(View.GONE);
    }


    private void initPagerContent() {
//        Context context = getBaseContext();
//        chosenContent = new ChosenContent(context);
//        mySettingContent = new MySettingContent(context);
//        filmContent = new FilmContent(context);
//        grabTicketContent = new GrabTicketContent(context);
//        saleContent = new SaleContent(context);
    }


    @Override
    public void onClick(View v) {
        setBottomView();
        initVisibility();
        switch (v.getId()) {
            case R.id.bottom_button__chosen://精选
                chosenImage.setImageResource(R.mipmap.jingxuan_select);
                chosenContent.setVisibility(View.VISIBLE);
                chosenContent.initView();
//                viewPager.setCurrentItem(0);
                break;
            case R.id.bottom_button__film://影片
                filmImage.setImageResource(R.mipmap.yingpian_select);
                filmContent.setVisibility(View.VISIBLE);
//                filmContent.initView();
//                viewPager.setCurrentItem(1);
                break;
            case R.id.bottom_button__grab://抢票
                grabImage.setImageResource(R.mipmap.qiangpiao_select);
                grabTicketContent.setVisibility(View.VISIBLE);
                grabTicketContent.initView();
//                viewPager.setCurrentItem(2);
//                grabTicketContent.adapter.set();
                break;
            case R.id.bottom_button__sale://卖品
                saleImage.setImageResource(R.mipmap.maipin_select);
                saleContent.setVisibility(View.VISIBLE);

//                viewPager.setCurrentItem(3);
                break;
            case R.id.bottom_button__my://我的
                myImage.setImageResource(R.mipmap.wode_select);
                mySettingContent.setVisibility(View.VISIBLE);
//                viewPager.setCurrentItem(4);
                break;
        }
    }

    /**
     * 初始底部View样式
     */
    private void setBottomView() {
        chosenImage.setImageResource(R.mipmap.jingxuan);
        filmImage.setImageResource(R.mipmap.yingpian);
        grabImage.setImageResource(R.mipmap.qiangpiao);
        saleImage.setImageResource(R.mipmap.maipin);
        myImage.setImageResource(R.mipmap.wode);
    }
}
