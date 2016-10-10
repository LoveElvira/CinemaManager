package com.yyjlr.tickets.activity.setting;

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
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.OrderCompleteAdapter;
import com.yyjlr.tickets.adapter.OrderUncompleteAdapter;
import com.yyjlr.tickets.model.OrderEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/8/2.
 * 我的订单
 */
public class SettingOrderActivity extends AbstractActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener,
        OrderCompleteAdapter.SlidingViewClickListener,OrderUncompleteAdapter.SlidingViewClickListener
{

    private TextView complete;
    private TextView unComplete;
    private ImageView completeImage;
    private ImageView unCompleteImage;
    private LinearLayout completeLayout;
    private LinearLayout unCompleteLayout;
    private SwipeRefreshLayout refresh;//刷新
    private RecyclerView listView;
    private TextView title;
    private List<OrderEntity> orderEntityList;
    private OrderCompleteAdapter completeAdapter;
    private OrderUncompleteAdapter unCompleteAdapter;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的订单");
        completeLayout = (LinearLayout) findViewById(R.id.content_setting_order__complete_layout);
        unCompleteLayout = (LinearLayout) findViewById(R.id.content_setting_order__uncomplete_layout);
        complete = (TextView) findViewById(R.id.content_setting_order__complete_title);
        unComplete = (TextView) findViewById(R.id.content_setting_order__uncomplete_title);
        completeImage = (ImageView) findViewById(R.id.content_setting_order__complete);
        unCompleteImage = (ImageView) findViewById(R.id.content_setting_order__uncomplete);
        listView = (RecyclerView) findViewById(R.id.content_setting_order__listview);

        orderEntityList = Application.getiDataService().getOrderList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SettingOrderActivity.this);
        listView.setLayoutManager(linearLayoutManager);

        refresh = (SwipeRefreshLayout) findViewById(R.id.content_setting_order__refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refresh.setOnRefreshListener(this);

        completeLayout.setOnClickListener(this);
        unCompleteLayout.setOnClickListener(this);

        initAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_setting_order__complete_layout:
                complete.setTextColor(getResources().getColor(R.color.btn_orange));
                completeImage.setBackgroundResource(R.mipmap.complete_select);
                unComplete.setTextColor(getResources().getColor(R.color.gray_alpha1));
                unCompleteImage.setBackgroundResource(R.mipmap.uncomplete);
                flag = true;
                break;
            case R.id.content_setting_order__uncomplete_layout:
                complete.setTextColor(getResources().getColor(R.color.gray_alpha1));
                completeImage.setBackgroundResource(R.mipmap.complete);
                unComplete.setTextColor(getResources().getColor(R.color.btn_orange));
                unCompleteImage.setBackgroundResource(R.mipmap.uncomplete_select);
                flag = false;
                break;
        }
        initAdapter();
    }
    private void initAdapter() {
        if (flag) {
            completeAdapter = new OrderCompleteAdapter(orderEntityList,SettingOrderActivity.this);
            completeAdapter.openLoadAnimation();
            listView.setAdapter(completeAdapter);
            mCurrentCounter = completeAdapter.getData().size();
            completeAdapter.setOnLoadMoreListener(this);
            completeAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            completeAdapter.setOnRecyclerViewItemChildClickListener(this);
        } else {
            unCompleteAdapter = new OrderUncompleteAdapter(orderEntityList);
            unCompleteAdapter.openLoadAnimation();
            listView.setAdapter(unCompleteAdapter);
            mCurrentCounter = unCompleteAdapter.getData().size();
            unCompleteAdapter.setOnLoadMoreListener(this);
            unCompleteAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        }
    }


    @Override
    public void onRefresh() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    completeAdapter.setNewData(orderEntityList);
                    completeAdapter.openLoadMore(PAGE_SIZE, true);
                    completeAdapter.removeAllFooterView();
                } else {
                    unCompleteAdapter.setNewData(orderEntityList);
                    unCompleteAdapter.openLoadMore(PAGE_SIZE, true);
                    unCompleteAdapter.removeAllFooterView();
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
                    notLoadingView = LayoutInflater.from(SettingOrderActivity.this).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
                    if (flag) {
                        completeAdapter.notifyDataChangedAfterLoadMore(false);
                        completeAdapter.addFooterView(notLoadingView);
                    } else {
                        unCompleteAdapter.notifyDataChangedAfterLoadMore(false);
                        unCompleteAdapter.addFooterView(notLoadingView);
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                completeAdapter.notifyDataChangedAfterLoadMore(orderEntityList, true);
                                mCurrentCounter = completeAdapter.getData().size();
                            } else {
                                unCompleteAdapter.notifyDataChangedAfterLoadMore(orderEntityList, true);
                                mCurrentCounter = unCompleteAdapter.getData().size();
                            }

                        }
                    }, delayMillis);
                }
            }

        });

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
                SettingOrderActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(SettingOrderDetailsActivity.class);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        if (flag) {
            completeAdapter.notifyItemRemoved(position);
        } else {
            unCompleteAdapter.notifyItemRemoved(position);
        }
    }
}
