package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.activity.FilmDetailsActivity;
import com.yyjlr.tickets.activity.FilmScheduleActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FollowFilmAdapter;
import com.yyjlr.tickets.adapter.FollowGrabAdapter;
import com.yyjlr.tickets.model.ChosenFilmEntity;
import com.yyjlr.tickets.model.FilmEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/8/2.
 * 我的关注
 */
public class SettingFollowActivity extends AbstractActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private TextView filmTitle;
    private TextView grabTicket;
    private ImageView filmImage;
    private ImageView grabImage;
    private LinearLayout filmLayout;
    private LinearLayout grabLayout;
    private SwipeRefreshLayout refresh;//刷新
    private RecyclerView listView;
    private FollowFilmAdapter followFilmAdapter;
    private FollowGrabAdapter followGrabAdapter;
    private List<FilmEntity> filmEntityList;
    private List<ChosenFilmEntity> chosenFilmEntityList;
    private TextView title;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_follow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_guanzhu));
        filmLayout = (LinearLayout) findViewById(R.id.content_setting_follow__film_layout);
        grabLayout = (LinearLayout) findViewById(R.id.content_setting_follow__grab_layout);
        filmTitle = (TextView) findViewById(R.id.content_setting_follow__film);
        grabTicket = (TextView) findViewById(R.id.content_setting_follow__grab_ticket);
        filmImage = (ImageView) findViewById(R.id.content_setting_follow__film_image);
        grabImage = (ImageView) findViewById(R.id.content_setting_follow__grab_image);
        listView = (RecyclerView) findViewById(R.id.content_setting_follow__listview);
        filmEntityList = Application.getiDataService().getFilmList();
        chosenFilmEntityList = Application.getiDataService().getChosenMovieList(PAGE_SIZE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SettingFollowActivity.this);
        listView.setLayoutManager(linearLayoutManager);

        refresh = (SwipeRefreshLayout) findViewById(R.id.content_setting_follow__refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refresh.setOnRefreshListener(this);

        filmLayout.setOnClickListener(this);
        grabLayout.setOnClickListener(this);

        initAdapter();;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.content_setting_follow__film_layout:
                filmTitle.setTextColor(getResources().getColor(R.color.btn_orange));
                filmImage.setBackgroundResource(R.mipmap.yingpian_select);
                grabTicket.setTextColor(getResources().getColor(R.color.gray_alpha1));
                grabImage.setBackgroundResource(R.mipmap.qiangpiao);
                flag = true;
                break;
            case R.id.content_setting_follow__grab_layout:
                filmTitle.setTextColor(getResources().getColor(R.color.gray_alpha1));
                filmImage.setBackgroundResource(R.mipmap.yingpian);
                grabTicket.setTextColor(getResources().getColor(R.color.btn_orange));
                grabImage.setBackgroundResource(R.mipmap.qiangpiao_select);
                flag = false;
                break;
        }
        initAdapter();
    }

    private void initAdapter() {
        if (flag) {
            followFilmAdapter = new FollowFilmAdapter(filmEntityList);
            followFilmAdapter.openLoadAnimation();
            listView.setAdapter(followFilmAdapter);
            mCurrentCounter = followFilmAdapter.getData().size();
            followFilmAdapter.setOnLoadMoreListener(this);
            followFilmAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            followFilmAdapter.setOnRecyclerViewItemChildClickListener(this);
        } else {
            followGrabAdapter = new FollowGrabAdapter(chosenFilmEntityList);
            followGrabAdapter.openLoadAnimation();
            listView.setAdapter(followGrabAdapter);
            mCurrentCounter = followGrabAdapter.getData().size();
            followGrabAdapter.setOnLoadMoreListener(this);
            followGrabAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        }
    }


    @Override
    public void onRefresh() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    followFilmAdapter.setNewData(filmEntityList);
                    followFilmAdapter.openLoadMore(PAGE_SIZE, true);
                    followFilmAdapter.removeAllFooterView();
                } else {
                    followGrabAdapter.setNewData(chosenFilmEntityList);
                    followGrabAdapter.openLoadMore(PAGE_SIZE, true);
                    followGrabAdapter.removeAllFooterView();
                }

                mCurrentCounter = PAGE_SIZE;
                refresh.setRefreshing(false);
            }
        }, delayMillis);
    }


    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    notLoadingView = LayoutInflater.from(SettingFollowActivity.this).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
                    if (flag) {
                        followFilmAdapter.notifyDataChangedAfterLoadMore(false);
                        followFilmAdapter.addFooterView(notLoadingView);
                    } else {
                        followGrabAdapter.notifyDataChangedAfterLoadMore(false);
                        followGrabAdapter.addFooterView(notLoadingView);
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                followFilmAdapter.notifyDataChangedAfterLoadMore(filmEntityList, true);
                                mCurrentCounter = followFilmAdapter.getData().size();
                            } else {
                                followGrabAdapter.notifyDataChangedAfterLoadMore(chosenFilmEntityList, true);
                                mCurrentCounter = followGrabAdapter.getData().size();
                            }

                        }
                    }, delayMillis);
                }
            }

        });

    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.item_follow_film__buy_ticket:
                intent.setClass(SettingFollowActivity.this, FilmScheduleActivity.class);
                break;
            case R.id.item_follow_film__layout:
                intent.setClass(SettingFollowActivity.this, FilmDetailsActivity.class);
                break;
            case R.id.item_follow_grab__enter:
                intent.setClass(SettingFollowActivity.this, EventActivity.class);
                break;
        }
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SettingFollowActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
