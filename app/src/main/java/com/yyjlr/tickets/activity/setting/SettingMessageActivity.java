package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.MessageAdapter;
import com.yyjlr.tickets.model.MessageEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/8/3.
 * 我的消息
 */
public class SettingMessageActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private RecyclerView listView;
    private SwipeRefreshLayout refresh;//刷新
    private TextView title;
    private MessageAdapter adapter;
    private List<MessageEntity> messageEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的消息");

        listView = (RecyclerView) findViewById(R.id.content_setting_message__listview);
        messageEntityList = Application.getiDataService().getMessageList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        TOTAL_COUNTER = 40;
        refresh = (SwipeRefreshLayout) findViewById(R.id.content_setting_message__refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refresh.setOnRefreshListener(this);
        initAdapter();
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    adapter.notifyDataChangedAfterLoadMore(false);
                    if (notLoadingView == null) {
                        notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
                    }
                    adapter.addFooterView(notLoadingView);
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setNewData(messageEntityList);
                adapter.openLoadMore(PAGE_SIZE, true);
                adapter.removeAllFooterView();
                mCurrentCounter = PAGE_SIZE;
                refresh.setRefreshing(false);
            }
        }, delayMillis);
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
        Intent intent = new Intent(SettingMessageActivity.this,MessageDetailsActivity.class);
        intent.putExtra("title",messageEntityList.get(position).getTitle());
        intent.putExtra("time",messageEntityList.get(position).getTime());
        intent.putExtra("message",messageEntityList.get(position).getMessage());
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
                SettingMessageActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
