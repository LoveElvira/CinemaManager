package com.yyjlr.tickets.content.order;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.setting.SettingOrderActivity;
import com.yyjlr.tickets.activity.setting.SettingOrderDetailsActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.OrderCompleteAdapter;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.order.MyOrderBean;
import com.yyjlr.tickets.model.order.MyOrderInfo;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by Elvira on 2017/1/3.
 * 订单未完成
 */

public class CompleteOrderContent extends LinearLayout implements SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener, OrderCompleteAdapter.SlidingViewClickListener {

    private View view;
    private CustomDialog customDialog;
    private OrderCompleteAdapter completeAdapter;
    private SuperSwipeRefreshLayout refresh;//刷新
    private RecyclerView listView;
    private View notLoadingView;
    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;
    private boolean hasMore = false;
    private String pagable = "0";
    private int delayMillis = 1000;
    private String type = "1";//订单类别,1:已完成；2：未完成


    private List<MyOrderInfo> orderList;
    private List<MyOrderInfo> orderLists;


    public CompleteOrderContent(Context context) {
        this(context, null);
    }

    public CompleteOrderContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_listview, this);
        initView();
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.content_listview__listview);
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_listview__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);

        orderLists = new ArrayList<>();

        getOrder(pagable, type);
    }

    //获取订单 订单类别,1:已完成；2：未完成
    private void getOrder(final String pagables, String type) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        pagableRequest.setType(type);
        OkHttpClientManager.postAsyn(Config.GET_MY_ORDER, new OkHttpClientManager.ResultCallback<MyOrderBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getContext(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(MyOrderBean response) {
                orderList = response.getOrders();
                if (orderList != null) {
                    if ("0".equals(pagables)) {//第一页
                        orderLists.clear();
                        orderLists.addAll(orderList);
                        Log.i("ee", orderList.size() + "----" + orderLists.size());
                        completeAdapter = new OrderCompleteAdapter(orderList, CompleteOrderContent.this);
                        completeAdapter.openLoadAnimation();
                        listView.setAdapter(completeAdapter);
                        completeAdapter.openLoadMore(orderList.size(), true);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                        } else {
                            hasMore = false;
                        }
                        pagable = response.getPagable();
                    } else {
                        orderLists.addAll(orderList);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                            pagable = response.getPagable();
                            completeAdapter.notifyDataChangedAfterLoadMore(orderList, true);
                        } else {
                            completeAdapter.notifyDataChangedAfterLoadMore(orderList, true);
                            hasMore = false;
                            pagable = "";
                        }
                    }
                    completeAdapter.setOnLoadMoreListener(CompleteOrderContent.this);
                    completeAdapter.setOnRecyclerViewItemChildClickListener(CompleteOrderContent.this);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, pagableRequest, MyOrderBean.class, Application.getInstance().getCurrentActivity());
    }

    //删除订单
    private void removeOrder(final int position) {
        customDialog = new CustomDialog(Application.getInstance().getCurrentActivity(), "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderLists.get(position).getOrderId() + "");
        OkHttpClientManager.postAsyn(Config.REMOVE_ORDER, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getContext(), info.getInfo(), Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
                completeAdapter.notifyItemRemoved(position);
                pagable = "0";
                getOrder(pagable, type);
                customDialog.dismiss();

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, Application.getInstance().getCurrentActivity());
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
                getOrder(pagable, type);
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
                    completeAdapter.notifyDataChangedAfterLoadMore(false);
//                    saleAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getOrder(pagable, type);
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.item_order_complete__ll_layout:
                intent.setClass(getContext(), SettingOrderDetailsActivity.class);
                intent.putExtra("orderId", orderLists.get(position).getOrderId() + "");
                intent.putExtra("status",orderLists.get(position).getOrderStatus());
                break;
        }
        Application.getInstance().getCurrentActivity().startActivity(intent);
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        showDelete(position);
    }

    /**
     * show Dialog 是否确定 删除订单
     */
    private void showDelete(final int position) {
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
        message.setText("是否删除此订单?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除订单
                removeOrder(position);
                builder.dismiss();

            }
        });
    }
}
