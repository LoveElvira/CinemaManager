package com.yyjlr.tickets.content.coupon;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.NoUseCouponAdapter;
import com.yyjlr.tickets.content.BaseLinearLayout;
import com.yyjlr.tickets.model.coupon.CouponBean;
import com.yyjlr.tickets.model.coupon.CouponInfo;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/6/20.
 * 未使用优惠券
 */

public class NoUseCouponContent extends BaseLinearLayout implements SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {
    private NoUseCouponAdapter adapter = null;
    private SuperSwipeRefreshLayout refresh;//刷新
    private RecyclerView listView;
    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;
    private boolean hasMore = false;
    private String pagable = "0";

    private List<CouponInfo> couponList;
    private List<CouponInfo> couponLists;
    private int position;

    public NoUseCouponContent(Context context) {
        super(context);
    }

    public NoUseCouponContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_listview, this);
        initView();
    }

    private void initView() {
        isFirst = true;
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_listview__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        listView = (RecyclerView) findViewById(R.id.content_listview__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);

        couponLists = new ArrayList<>();

    }

    public void updateView(boolean isUpdate) {
        if (isUpdate) {
            isFirst = isUpdate;
        }
        if (isFirst) {
            isFirst = false;
            pagable = "0";
            getCoupon(pagable);
        }
    }

    //获取优惠券
    private void getCoupon(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        pagableRequest.setStatus("1");
        OkHttpClientManager.postAsyn(Config.GET_MY_COUPON, new OkHttpClientManager.ResultCallback<CouponBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(CouponBean response) {
                if (response != null) {
                    couponList = response.getCoupons();
                    if (couponList != null && couponList.size() > 0) {
                        if ("0".equals(pagables)) {//第一页
                            couponLists.clear();
                            couponLists.addAll(couponList);
                            Log.i("ee", couponList.size() + "----" + couponList.size());
                            adapter = new NoUseCouponAdapter(couponList);
                            adapter.openLoadAnimation();
                            listView.setAdapter(adapter);
                            adapter.openLoadMore(couponList.size(), true);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            pagable = response.getPagable();
                        } else {
                            couponLists.addAll(couponList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                pagable = response.getPagable();
                                adapter.notifyDataChangedAfterLoadMore(couponList, true);
                            } else {
                                adapter.notifyDataChangedAfterLoadMore(couponList, true);
                                hasMore = false;
                                pagable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(NoUseCouponContent.this);
                        adapter.setOnRecyclerViewItemChildClickListener(NoUseCouponContent.this);
                    } else {
                        if (adapter != null) {
                            couponLists.clear();
                            couponList = new ArrayList<>();
                            couponLists.addAll(couponList);
                            adapter = new NoUseCouponAdapter(couponList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, pagableRequest, CouponBean.class, Application.getInstance().getCurrentActivity());
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
                getCoupon(pagable);
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
                            getCoupon(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {

    }

}
