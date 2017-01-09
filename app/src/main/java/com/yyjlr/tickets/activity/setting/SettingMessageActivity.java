package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.MessageAdapter;
import com.yyjlr.tickets.model.MessageEntity;
import com.yyjlr.tickets.model.message.MyMessageBean;
import com.yyjlr.tickets.model.message.MyMessageInfo;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.io.Serializable;
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

    private List<MyMessageInfo> messageInfoList;
    private List<MyMessageInfo> messageInfoLists;//总条数
    private boolean hasMore = false;
    private String pagable = "0";
    private int delayMillis = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_message);
        customDialog = new CustomDialog(Application.getInstance().getCurrentActivity(), "加载中。。。");
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的消息");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        listView = (RecyclerView) findViewById(R.id.content_setting_message__listview);
//        messageEntityList = Application.getiDataService().getMessageList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
//        TOTAL_COUNTER = 40;
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_setting_message__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

//        initAdapter();
        getMessage(pagable);
    }

    //我的消息
    private void getMessage(final String pagables) {
        customDialog.show();
        PagableRequest pagableRequest = new PagableRequest();
        OkHttpClientManager.postAsyn(Config.GET_MESSAGE, new OkHttpClientManager.ResultCallback<MyMessageBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MyMessageBean response) {
                customDialog.dismiss();
                messageInfoList = response.getMessages();
                if (messageInfoList != null) {
                    if ("0".equals(pagables)) {//第一页
                        messageInfoLists.clear();
                        messageInfoLists.addAll(messageInfoList);
                        Log.i("ee", messageInfoLists.size() + "----" + messageInfoList.size());
                        adapter = new MessageAdapter(messageInfoList);
                        adapter.openLoadAnimation();
                        listView.setAdapter(adapter);
                        adapter.openLoadMore(messageInfoList.size(), true);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                        } else {
                            hasMore = false;
                        }
                        pagable = response.getPagable();
                    } else {
                        messageInfoLists.addAll(messageInfoList);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                            pagable = response.getPagable();
                            adapter.notifyDataChangedAfterLoadMore(messageInfoList, true);
                        } else {
                            adapter.notifyDataChangedAfterLoadMore(messageInfoList, true);
                            hasMore = false;
                            pagable = "";
                        }
                    }
                    adapter.setOnLoadMoreListener(SettingMessageActivity.this);
                    adapter.setOnRecyclerViewItemChildClickListener(SettingMessageActivity.this);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, pagableRequest, MyMessageBean.class, Application.getInstance().getCurrentActivity());
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    adapter.notifyDataChangedAfterLoadMore(false);
//                    if (notLoadingView == null) {
//                        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
//                    }
//                    filmAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getMessage(pagable);
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
                pagable = "0";
                getMessage(pagable);
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

//    private void initAdapter() {
//        adapter = new MessageAdapter(messageEntityList);
//        adapter.openLoadAnimation();
//        listView.setAdapter(adapter);
//        mCurrentCounter = adapter.getData().size();
//        adapter.setOnLoadMoreListener(this);
//        adapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
//        adapter.setOnRecyclerViewItemChildClickListener(this);
//    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        Intent intent = new Intent(SettingMessageActivity.this, MessageDetailsActivity.class);
        intent.putExtra("messageInfo", messageInfoLists.get(position));
//        intent.putExtra("title", messageEntityList.get(position).getTitle());
//        intent.putExtra("time", messageEntityList.get(position).getTime());
//        intent.putExtra("message", messageEntityList.get(position).getMessage());
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
