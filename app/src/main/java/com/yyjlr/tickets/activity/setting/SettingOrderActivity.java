package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.OrderCompleteAdapter;
import com.yyjlr.tickets.adapter.OrderUncompleteAdapter;
import com.yyjlr.tickets.model.OrderEntity;
import com.yyjlr.tickets.model.order.MyOrderBean;
import com.yyjlr.tickets.model.order.MyOrderInfo;
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
 * Created by Elvira on 2016/8/2.
 * 我的订单
 */
public class SettingOrderActivity extends AbstractActivity
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener,
        OrderCompleteAdapter.SlidingViewClickListener, OrderUncompleteAdapter.SlidingViewClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener {

    private TextView complete;
    private TextView unComplete;
    private ImageView completeImage;
    private ImageView unCompleteImage;
    private LinearLayout completeLayout;
    private LinearLayout unCompleteLayout;
    private SuperSwipeRefreshLayout refresh;//刷新
    private RecyclerView listView;
    private TextView title;
    private ImageView leftArrow;
    private List<OrderEntity> orderEntityList;
    private OrderCompleteAdapter completeAdapter;
    private OrderUncompleteAdapter unCompleteAdapter;
    private String flag = "1";//订单类别,1:已完成；2：未完成

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    private List<MyOrderInfo> orderInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_order);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的订单");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
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

        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_setting_order__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        completeLayout.setOnClickListener(this);
        unCompleteLayout.setOnClickListener(this);
        getOrder("0", "1");
//        initAdapter();
    }

    //获取订单 订单类别,1:已完成；2：未完成
    private void getOrder(String pagable, String type) {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagable);
        pagableRequest.setType(type);
        OkHttpClientManager.postAsyn(Config.GET_MY_ORDER, new OkHttpClientManager.ResultCallback<MyOrderBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MyOrderBean response) {
                customDialog.dismiss();
                orderInfoList = response.getOrders();
                initAdapter(orderInfoList);

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, pagableRequest, MyOrderBean.class, SettingOrderActivity.this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                SettingOrderActivity.this.finish();
                break;
            case R.id.content_setting_order__complete_layout:
                complete.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                completeImage.setBackgroundResource(R.mipmap.complete_select);
                unComplete.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
                unCompleteImage.setBackgroundResource(R.mipmap.uncomplete);
                flag = "1";
                break;
            case R.id.content_setting_order__uncomplete_layout:
                complete.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
                completeImage.setBackgroundResource(R.mipmap.complete);
                unComplete.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                unCompleteImage.setBackgroundResource(R.mipmap.uncomplete_select);
                flag = "2";
                break;
        }
        initAdapter(orderInfoList);
    }

    private void initAdapter(List<MyOrderInfo> orderInfoList) {
        if (flag.equals("1")) {
            completeAdapter = new OrderCompleteAdapter(orderInfoList, SettingOrderActivity.this);
            completeAdapter.openLoadAnimation();
            listView.setAdapter(completeAdapter);
            mCurrentCounter = completeAdapter.getData().size();
            completeAdapter.setOnLoadMoreListener(this);
            completeAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            completeAdapter.setOnRecyclerViewItemChildClickListener(this);
        } else {
            unCompleteAdapter = new OrderUncompleteAdapter(orderInfoList, SettingOrderActivity.this);
            unCompleteAdapter.openLoadAnimation();
            listView.setAdapter(unCompleteAdapter);
            mCurrentCounter = unCompleteAdapter.getData().size();
            unCompleteAdapter.setOnLoadMoreListener(this);
            unCompleteAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            unCompleteAdapter.setOnRecyclerViewItemChildClickListener(this);
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
        orderEntityList = Application.getiDataService().getOrderList();
        headerSta.setText("正在刷新数据中");
        headerImage.setVisibility(View.GONE);
        headerProgressBar.setVisibility(View.VISIBLE);
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag.equals("1")) {
//                    completeAdapter.setNewData(orderEntityList);
//                    completeAdapter.openLoadMore(PAGE_SIZE, true);
                    completeAdapter.removeAllFooterView();
                    getOrder("0", "1");
                } else {
//                    unCompleteAdapter.setNewData(orderEntityList);
//                    unCompleteAdapter.openLoadMore(PAGE_SIZE, true);
                    unCompleteAdapter.removeAllFooterView();
                    getOrder("0", "2");
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
//                if (mCurrentCounter >= TOTAL_COUNTER) {
//                    if (notLoadingView == null) {
//                        notLoadingView = LayoutInflater.from(getBaseContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
//                    }
                if (flag.equals("1")) {
                    completeAdapter.notifyDataChangedAfterLoadMore(false);
//                        completeAdapter.addFooterView(notLoadingView);
                } else {
                    unCompleteAdapter.notifyDataChangedAfterLoadMore(false);
//                        unCompleteAdapter.addFooterView(notLoadingView);
                }
//                } else {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (flag.equals("1")) {
//                                completeAdapter.notifyDataChangedAfterLoadMore(orderEntityList, true);
//                                mCurrentCounter = completeAdapter.getData().size();
//                            } else {
//                                unCompleteAdapter.notifyDataChangedAfterLoadMore(orderEntityList, true);
//                                mCurrentCounter = unCompleteAdapter.getData().size();
//                            }

//                        }
//                    }, delayMillis);
//                }
            }

        });

    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_order_nocomplete__cancel:
                showIsDelete(position);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.item_order_complete__ll_layout:
                intent.setClass(SettingOrderActivity.this, SettingOrderDetailsActivity.class);
                break;
            case R.id.item_order_nocomplete__ll_layout:
                intent.setClass(SettingOrderActivity.this, SettingOrderDetailsActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        if (flag.equals("1")) {
            completeAdapter.notifyItemRemoved(position);
        } else {
            unCompleteAdapter.notifyItemRemoved(position);
        }
    }

    /**
     * show Dialog 是否确定删除
     */
    private void showIsDelete(final int position) {
        LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        TextView cancel = (TextView) layout.findViewById(R.id.alert_dialog__cancel);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
        confirm.setText("确定");
        title.setText("提示");
        message.setText("是否取消订单?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消订单
                orderEntityList.get(position).setOrderComplete("1");
                unCompleteAdapter.notifyItemChanged(position);
                builder.dismiss();

            }
        });
    }
}
