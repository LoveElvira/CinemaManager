package com.yyjlr.tickets.content.sale;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.sale.PackageDetailsActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.SalePackageAdapter;
import com.yyjlr.tickets.model.SaleEntity;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/1/3.
 * 套餐
 */

public class PackageContent extends LinearLayout implements SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private View view;
    private RecyclerView listView;//列表
    private SuperSwipeRefreshLayout refresh;//刷新
    private SalePackageAdapter salePackageAdapter;
    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;
    private View notLoadingView;

    protected int TOTAL_COUNTER = 20;

    protected static final int PAGE_SIZE = 10;

    protected int delayMillis = 1000;

    protected int mPackageCounter = 0;

    private List<SaleEntity> saleEntityList;


    public PackageContent(Context context) {
        this(context, null);
    }

    public PackageContent(Context context, AttributeSet attrs) {
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

//        saleEntityList = Application.getiDataService().getSaleList();
        saleEntityList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);


        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int first = linearManager.findFirstCompletelyVisibleItemPosition();
                    salePackageAdapter.changeBgFristAndLast(firstItemPosition, lastItemPosition, first);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        salePackageAdapter = new SalePackageAdapter(saleEntityList);
        salePackageAdapter.openLoadAnimation();
        listView.setAdapter(salePackageAdapter);
        mPackageCounter = salePackageAdapter.getData().size();
//        salePackageAdapter.setOnLoadMoreListener(this);
        salePackageAdapter.setOnRecyclerViewItemChildClickListener(this);
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
                salePackageAdapter.setNewData(saleEntityList);
                salePackageAdapter.openLoadMore(PAGE_SIZE, true);
                salePackageAdapter.removeAllFooterView();
                mPackageCounter = PAGE_SIZE;
//                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//                Date curDate = new Date(System.currentTimeMillis());
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
                if (mPackageCounter >= TOTAL_COUNTER) {
                    salePackageAdapter.notifyDataChangedAfterLoadMore(false);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            salePackageAdapter.notifyDataChangedAfterLoadMore(saleEntityList, true);
                            mPackageCounter = salePackageAdapter.getData().size();
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_sale_package__right:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PackageDetailsActivity.class));
                break;
            case R.id.item_sale_package__cardview:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PackageDetailsActivity.class));
                break;
        }
    }
}
