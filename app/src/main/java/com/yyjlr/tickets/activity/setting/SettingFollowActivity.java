package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.activity.film.FilmDetailsActivity;
import com.yyjlr.tickets.activity.film.FilmScheduleActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.adapter.FollowFilmAdapter;
import com.yyjlr.tickets.adapter.FollowGrabAdapter;
import com.yyjlr.tickets.content.collect.FollowEventContent;
import com.yyjlr.tickets.content.collect.FollowFilmContent;
import com.yyjlr.tickets.model.ChosenFilmEntity;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.viewutils.LockableViewPager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Elvira on 2016/8/2.
 * 我的收藏
 */
public class SettingFollowActivity extends AbstractActivity implements View.OnClickListener {

    private TextView filmText;
    private TextView eventText;
    private ImageView filmImage;
    private ImageView eventImage;
    private LinearLayout filmLayout;
    private LinearLayout grabLayout;
    private TextView title;
    private ImageView leftArrow;
    private LockableViewPager viewPager;
    private ContentAdapter adapter;
    private FollowFilmContent filmContent;
    private FollowEventContent eventContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_follow);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_collect));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        filmLayout = (LinearLayout) findViewById(R.id.content_setting_follow__film_layout);
        grabLayout = (LinearLayout) findViewById(R.id.content_setting_follow__grab_layout);
        filmText = (TextView) findViewById(R.id.content_setting_follow__film);
        eventText = (TextView) findViewById(R.id.content_setting_follow__grab_ticket);
        filmImage = (ImageView) findViewById(R.id.content_setting_follow__film_image);
        eventImage = (ImageView) findViewById(R.id.content_setting_follow__grab_image);

        viewPager = (LockableViewPager) findViewById(R.id.content_follow__viewpager);

        filmContent = new FollowFilmContent(getBaseContext());
        eventContent = new FollowEventContent(getBaseContext());

        List<View> list = new ArrayList<View>();
        list.add(filmContent);
        list.add(eventContent);

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
                    filmText.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    filmImage.setImageResource(R.mipmap.collect_film_select);
                } else if (position == 1) {
                    eventText.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    eventImage.setImageResource(R.mipmap.collect_grab_select);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        filmLayout.setOnClickListener(this);
        grabLayout.setOnClickListener(this);

    }


    private void initFirstView() {
        filmText.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
        filmImage.setImageResource(R.mipmap.collect_film);
        eventText.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
        eventImage.setImageResource(R.mipmap.collect_grab);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                SettingFollowActivity.this.finish();
                break;
            case R.id.content_setting_follow__film_layout:
                viewPager.setCurrentItem(0);
                break;
            case R.id.content_setting_follow__grab_layout:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE:
                if (data.getBooleanExtra("isUpdate", false)) {
                    filmContent.refreshView(data.getIntExtra("position", -1));
                }
                break;
            case CODE_REQUEST_FOUR:
                if (data.getBooleanExtra("isUpdate", false)) {
                    eventContent.refreshView(data.getIntExtra("position", -1));
                }
                break;
        }
    }
}
