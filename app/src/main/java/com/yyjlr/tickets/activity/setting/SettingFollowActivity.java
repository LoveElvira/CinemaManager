package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.yyjlr.tickets.adapter.FollowFilmAdapter;
import com.yyjlr.tickets.adapter.FollowGrabAdapter;
import com.yyjlr.tickets.model.ChosenFilmEntity;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Elvira on 2016/8/2.
 * 我的关注
 */
public class SettingFollowActivity extends AbstractActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        BaseAdapter.RequestLoadMoreListener,
        BaseAdapter.OnRecyclerViewItemChildClickListener,
        FollowFilmAdapter.SlidingViewClickListener,
        FollowGrabAdapter.SlidingViewClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener {

    private TextView filmTitle;
    private TextView grabTicket;
    private ImageView filmImage;
    private ImageView grabImage;
    private LinearLayout filmLayout;
    private LinearLayout grabLayout;
    private SuperSwipeRefreshLayout refresh;//刷新
    private RecyclerView listView;
    private FollowFilmAdapter followFilmAdapter;
    private FollowGrabAdapter followGrabAdapter;
    private List<FilmEntity> filmEntityList;
    private List<ChosenFilmEntity> chosenFilmEntityList;
    private TextView title;
    private ImageView leftArrow;
    private boolean flag = true;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_follow);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_collect));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

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

        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_setting_follow__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        filmLayout.setOnClickListener(this);
        grabLayout.setOnClickListener(this);

        initAdapter();
        ;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                SettingFollowActivity.this.finish();
                break;
            case R.id.content_setting_follow__film_layout:
                filmTitle.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                filmImage.setBackgroundResource(R.mipmap.collect_film_select);
                grabTicket.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
                grabImage.setBackgroundResource(R.mipmap.collect_grab);
                flag = true;
                break;
            case R.id.content_setting_follow__grab_layout:
                filmTitle.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
                filmImage.setBackgroundResource(R.mipmap.collect_film);
                grabTicket.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                grabImage.setBackgroundResource(R.mipmap.collect_grab_select);
                flag = false;
                break;
        }
        initAdapter();
    }

    private void initAdapter() {
        if (flag) {
            followFilmAdapter = new FollowFilmAdapter(filmEntityList, SettingFollowActivity.this);
            followFilmAdapter.openLoadAnimation();
            listView.setAdapter(followFilmAdapter);
            mCurrentCounter = followFilmAdapter.getData().size();
            followFilmAdapter.setOnLoadMoreListener(this);
            followFilmAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            followFilmAdapter.setOnRecyclerViewItemChildClickListener(this);
        } else {
            followGrabAdapter = new FollowGrabAdapter(chosenFilmEntityList, SettingFollowActivity.this);
            followGrabAdapter.openLoadAnimation();
            listView.setAdapter(followGrabAdapter);
            mCurrentCounter = followGrabAdapter.getData().size();
            followGrabAdapter.setOnLoadMoreListener(this);
            followGrabAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            followGrabAdapter.setOnRecyclerViewItemChildClickListener(this);
        }
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(getBaseContext())
                .inflate(R.layout.header_loading, null);
        headerProgressBar = (ProgressBar) headerView.findViewById(R.id.header_loading_progress);
        headerSta = (TextView) headerView.findViewById(R.id.header_loading_text);
//        headerTime = (TextView) headerView.findViewById(R.id.header_loading_time);
        headerSta.setText("下拉刷新");
        headerImage = (ImageView) headerView.findViewById(R.id.header_loading_image);
        headerImage.setVisibility(View.VISIBLE);
        headerProgressBar.setVisibility(View.GONE);
        return headerView;
    }

    @Override
    public void onRefresh() {
        headerSta.setText("正在刷新数据中");
        headerImage.setVisibility(View.GONE);
        headerProgressBar.setVisibility(View.VISIBLE);
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
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                Date curDate = new Date(System.currentTimeMillis());
//                headerTime.setText("最后更新：今天"+formatter.format(curDate));
                refresh.setRefreshing(false);
                headerProgressBar.setVisibility(View.GONE);
            }
        }, delayMillis);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {
        headerSta.setText(enable ? "松开立即刷新" : "下拉刷新");
        headerImage.setVisibility(View.VISIBLE);
        headerImage.setRotation(enable ? 180 : 0);
    }


    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
//                    if (notLoadingView == null) {
//                        notLoadingView = LayoutInflater.from(getBaseContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
//                    }
                    if (flag) {
                        followFilmAdapter.notifyDataChangedAfterLoadMore(false);
//                        followFilmAdapter.addFooterView(notLoadingView);
                    } else {
                        followGrabAdapter.notifyDataChangedAfterLoadMore(false);
//                        followGrabAdapter.addFooterView(notLoadingView);
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
        switch (view.getId()) {
            case R.id.item_follow_film__buy_ticket:
                intent.setClass(SettingFollowActivity.this, FilmScheduleActivity.class);
                break;
            case R.id.item_follow_grab__enter:
                intent.setClass(SettingFollowActivity.this, EventActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.item_follow_film__ll_layout:
                intent.setClass(SettingFollowActivity.this, FilmDetailsActivity.class);
                break;
            case R.id.item_follow_grab__ll_layout:
                intent.setClass(SettingFollowActivity.this, EventActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        if (flag) {
            followFilmAdapter.notifyItemRemoved(position);
        } else {
            followGrabAdapter.notifyItemRemoved(position);
        }
    }
}
