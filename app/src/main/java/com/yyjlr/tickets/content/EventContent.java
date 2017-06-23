package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.EventAdapter;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.ticket.EventEntity;
import com.yyjlr.tickets.model.ticket.EventInfoEntity;
import com.yyjlr.tickets.model.ticket.GrabTicketModel;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 活动页面
 */
public class EventContent extends BaseLinearLayout implements BaseAdapter.OnRecyclerViewItemChildClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.RequestLoadMoreListener {

    private TextView title;
    private RelativeLayout parentView;
    private LinearLayout noDate;

    private RecyclerView listView;//列表
    private SuperSwipeRefreshLayout refresh;//刷新
    private EventAdapter adapter;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    private List<EventInfoEntity> activityList;
    private List<EventInfoEntity> activityLists;
    private boolean hasMore = false;
    private String pagable;

    public EventContent(Context context) {
        this(context, null);
    }

    public EventContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_grabticket, this);
        initView();
    }

    private void initView() {
        isFirst = true;
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_grab_title));
        noDate = (LinearLayout) findViewById(R.id.fragment_grab__no_date);
        listView = (RecyclerView) findViewById(R.id.content_listview__listview);
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_listview__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        activityLists = new ArrayList<>();
    }

    public void updateView(boolean isUpdate) {
        if (isUpdate) {
            isFirst = isUpdate;
        }

        if (isFirst) {
            isFirst = false;
            noDate.setVisibility(GONE);
            initBgTitle(bgTitle);
            pagable = "0";
            getTicket(pagable);
        }
    }

    public void updateAdapter(int position, int isHaveCollect) {
        if (isHaveCollect == 1) {
            activityLists.get(position).setFavorite(activityLists.get(position).getFavorite() - 1);
        } else if (isHaveCollect == 2) {
            activityLists.get(position).setFavorite(activityLists.get(position).getFavorite() + 1);
        }
        adapter.notifyItemChanged(position);
    }

    //获取首页抢票
    private void getTicket(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.GET_TICKET, new OkHttpClientManager.ResultCallback<EventEntity>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(EventEntity response) {
                Log.i("ee", "--" + new Gson().toJson(response));
                if (response != null) {
                    activityList = response.getActivityList();
                    if (activityList != null && activityList.size() > 0) {
                        if ("0".equals(pagables)) {//第一页
                            activityLists.clear();
                            activityLists.addAll(activityList);
                            Log.i("ee", activityLists.size() + "----" + activityLists.size());
                            adapter = new EventAdapter(activityList);
                            adapter.openLoadAnimation();
                            listView.setAdapter(adapter);
                            adapter.openLoadMore(activityList.size(), true);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            pagable = response.getPagable();
                        } else {
                            activityLists.addAll(activityList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                pagable = response.getPagable();
                                adapter.notifyDataChangedAfterLoadMore(activityList, true);
                            } else {
                                adapter.notifyDataChangedAfterLoadMore(activityList, true);
                                hasMore = false;
                                pagable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(EventContent.this);
                        adapter.setOnRecyclerViewItemChildClickListener(EventContent.this);
                    } else {
                        activityList = new ArrayList<>();
                        adapter = new EventAdapter(activityList);
                        listView.setAdapter(adapter);
                    }
                }


            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, pagableRequest, EventEntity.class, Application.getInstance().getCurrentActivity());
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
            Intent intent = new Intent();
            if (!isLogin.equals("1")) {
                intent.setClass(getContext(), LoginActivity.class);
            } else {
                intent.setClass(getContext(), EventActivity.class);
                intent.putExtra("eventId", activityLists.get(position).getActivityId());
                intent.putExtra("position", position);
            }
            Application.getInstance().getCurrentActivity().startActivityForResult(intent, 0x09);
        }
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(getContext())
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
                getTicket(pagable);
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
                if (!hasMore) {
                    adapter.notifyDataChangedAfterLoadMore(false);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getTicket(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
    }
}
