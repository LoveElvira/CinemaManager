package com.yyjlr.tickets;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 主页
 */
public class MainActivity extends AbstractActivity {

    private ContentAdapter adapter;
    private LockableViewPager viewPager;
    private int[] tabIcons = {R.mipmap.jingxuan, R.mipmap.yingpian, R.mipmap.qiangpiao,R.mipmap.maipin, R.mipmap.wode};
    private int[] tabSelectedIcons = {R.mipmap.jingxuan_select, R.mipmap.yingpian_select, R.mipmap.qiangpiao_select, R.mipmap.maipin_select, R.mipmap.wode_select};
    private final String[] titles = {"精选", "影片", "抢票", "卖品", "我的"};

    private ChosenContent chosenContent;
    private FilmContent filmContent;
    private MySettingContent mySettingContent;
    private GrabTicketContent grabTicketContent;
    private SaleContent saleContent;
    private Toolbar toolbar;
    public Context context;

//    private ImageView enterCinema;//进入影院
//    private ImageView leftArrow;
//    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        context = MainActivity.this;
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        title = (TextView) findViewById(R.id.base_toolbar__text);
//        enterCinema = (ImageView) findViewById(R.id.base_toolbar__right);
//        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
//        title.setText(getResources().getText(R.string.text_cinema_name));
//        enterCinema.setBackgroundResource(R.mipmap.enter_cinema);
//        enterCinema.setVisibility(View.VISIBLE);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.content_main_tab_layout);
        final TabLayout.Tab[] tabs = {tabLayout.newTab().setText(titles[0]).setIcon(tabIcons[0]), tabLayout.newTab().setText(titles[1]).setIcon(tabIcons[1]), tabLayout.newTab().setText(titles[2]).setIcon(tabIcons[2]), tabLayout.newTab().setText(titles[3]).setIcon(tabIcons[3]), tabLayout.newTab().setText(titles[4]).setIcon(tabIcons[4])};

        tabLayout.addTab(tabs[0]);
        tabLayout.addTab(tabs[1]);
        tabLayout.addTab(tabs[2]);
        tabLayout.addTab(tabs[3]);
        tabLayout.addTab(tabs[4]);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (LockableViewPager) findViewById(R.id.content_main_viewpager);
        initPageContent();
        List<View> list = new ArrayList<View>();
        list.add(chosenContent);
        list.add(filmContent);
        list.add(grabTicketContent);
        list.add(saleContent);
        list.add(mySettingContent);
        adapter = new ContentAdapter(list, titles);
        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);
        TabLayout.TabLayoutOnPageChangeListener onPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            private int lastIndex = -1;

            @Override
            public void onPageSelected(int position) {
                if (lastIndex >= 0) {
                    tabs[lastIndex].setIcon(tabIcons[lastIndex]);
                }
                tabs[position].setIcon(tabSelectedIcons[position]);
                lastIndex = position;
                setTitle(titles[position]);
                invalidateOptionsMenu();
            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                toolbar.setVisibility(View.GONE);
//                enterCinema.setVisibility(View.GONE);
//                leftArrow.setVisibility(View.GONE);
//                if (tab.getPosition() == 4) {
//                    toolbar.setVisibility(View.GONE);
//                } else if(tab.getPosition() == 0) {
//                    title.setText(getResources().getText(R.string.text_cinema_name));
//                    enterCinema.setVisibility(View.VISIBLE);
//                }else if(tab.getPosition() == 1) {
//                    title.setText(getResources().getText(R.string.text_film_title));
//                }else if(tab.getPosition() == 2) {
//                    title.setText(getResources().getText(R.string.text_grab_title));
//                    grabTicketContent.adapter.set();
//                }else if(tab.getPosition() == 3) {
//                    title.setText(getResources().getText(R.string.text_sale_title));
//                }
                if (tab.getPosition() == 2){
                    grabTicketContent.adapter.set();
                }

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
//        enterCinema.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(CinemaDetailsActivity.class);
//            }
//        });
    }

    private void initPageContent() {
        Context context = getBaseContext();
        chosenContent = new ChosenContent(context);
        mySettingContent = new MySettingContent(context);
        filmContent = new FilmContent(context);
        grabTicketContent = new GrabTicketContent(context);
        saleContent = new SaleContent(context);
    }


}
