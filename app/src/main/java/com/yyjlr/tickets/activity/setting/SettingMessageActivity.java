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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.MessageAdapter;
import com.yyjlr.tickets.model.MessageEntity;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Elvira on 2016/8/3.
 * 我的消息
 */
public class SettingMessageActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener {

    private RecyclerView listView;
    private SuperSwipeRefreshLayout refresh;//刷新
    private TextView title;
    private ImageView leftArrow;
    private MessageAdapter adapter;
    private List<MessageEntity> messageEntityList;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_message);
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的消息");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        listView = (RecyclerView) findViewById(R.id.content_setting_message__listview);
        messageEntityList = Application.getiDataService().getMessageList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        TOTAL_COUNTER = 40;
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_setting_message__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        initAdapter();
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    adapter.notifyDataChangedAfterLoadMore(false);
//                    if (notLoadingView == null) {
//                        notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
//                    }
//                    adapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataChangedAfterLoadMore(messageEntityList, true);
                            mCurrentCounter = adapter.getData().size();
                        }
                    }, delayMillis);
                }
            }

        });
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setNewData(messageEntityList);
                adapter.openLoadMore(PAGE_SIZE, true);
                adapter.removeAllFooterView();
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

    private void initAdapter() {
        adapter = new MessageAdapter(messageEntityList);
        adapter.openLoadAnimation();
        listView.setAdapter(adapter);
        mCurrentCounter = adapter.getData().size();
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        adapter.setOnRecyclerViewItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        Intent intent = new Intent(SettingMessageActivity.this, MessageDetailsActivity.class);
        intent.putExtra("title", messageEntityList.get(position).getTitle());
        intent.putExtra("time", messageEntityList.get(position).getTime());
        intent.putExtra("message", messageEntityList.get(position).getMessage());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                SettingMessageActivity.this.finish();
                break;
        }
    }
}
