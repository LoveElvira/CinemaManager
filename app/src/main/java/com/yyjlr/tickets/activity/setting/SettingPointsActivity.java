package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.PointsAdapter;
import com.yyjlr.tickets.model.PointsEntity;
import com.yyjlr.tickets.model.point.PointDetail;
import com.yyjlr.tickets.model.point.PointList;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;

import java.util.ArrayList;
import java.util.List;

import static com.yyjlr.tickets.Application.getInstance;

/**
 * Created by Elvira on 2016/9/23.
 * 我的积分
 */

public class SettingPointsActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.RequestLoadMoreListener {

    private RecyclerView listView;//列表
    //private SwipeRefreshLayout refresh;//刷新
    private List<PointsEntity> pointsEntityList;
    private PointsAdapter adapter;
    private TextView title;
    private ImageView leftArrow;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    private List<PointDetail> pointDetailList;
    private List<PointDetail> pointDetailLists;//总条数
    private boolean hasMore = false;
    private String pagable = "0";
    private int delayMillis = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_points);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的积分");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        listView = (RecyclerView) findViewById(R.id.content_setting_points__listview);

        pointDetailLists = new ArrayList<>();

//        pointsEntityList = Application.getiDataService().getPointsList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        pagable = "0";
        getPoints(pagable);
//        initAdapter();
    }

    private void initAdapter() {
//        pointsAdapter = new PointsAdapter(pointsEntityList);
//        pointsAdapter.openLoadAnimation();
//        listView.setAdapter(pointsAdapter);
//        mCurrentCounter = pointsAdapter.getData().size();
        //pointsAdapter.setOnLoadMoreListener(this);
        //pointsAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        //pointsAdapter.setOnRecyclerViewItemChildClickListener(this);
    }

    //我的消息
    private void getPoints(final String pagables) {
        customDialog.show();
        PagableRequest pagableRequest = new PagableRequest();
        OkHttpClientManager.postAsyn(Config.GET_MY_POINT, new OkHttpClientManager.ResultCallback<PointList>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(PointList response) {
                customDialog.dismiss();
                pointDetailList = response.getPointsDetail();
                if (pointDetailList != null && pointDetailList.size() > 0) {
                    if ("0".equals(pagables)) {//第一页
                        pointDetailLists.clear();
                        pointDetailLists.addAll(pointDetailList);
                        Log.i("ee", pointDetailLists.size() + "----" + pointDetailList.size());
                        adapter = new PointsAdapter(pointDetailList);
                        adapter.openLoadAnimation();
                        listView.setAdapter(adapter);
                        adapter.openLoadMore(pointDetailList.size(), true);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                        } else {
                            hasMore = false;
                        }
                        pagable = response.getPagable();
                    } else {
                        pointDetailLists.addAll(pointDetailList);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                            pagable = response.getPagable();
                            adapter.notifyDataChangedAfterLoadMore(pointDetailList, true);
                        } else {
                            adapter.notifyDataChangedAfterLoadMore(pointDetailList, true);
                            hasMore = false;
                            pagable = "";
                        }
                    }
                    adapter.setOnLoadMoreListener(SettingPointsActivity.this);
//                    adapter.setOnRecyclerViewItemChildClickListener(SettingPointsActivity.this);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, pagableRequest, PointList.class, Application.getInstance().getCurrentActivity());
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
                            getPoints(pagable);
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

//    @Override
//    public void onRefresh() {
//        headerSta.setText("正在刷新数据中");
//        headerImage.setVisibility(View.GONE);
//        headerProgressBar.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pagable = "0";
//                getMessage(pagable);
//                refresh.setRefreshing(false);
//                headerProgressBar.setVisibility(View.GONE);
//            }
//        }, delayMillis);
//    }
//
//    @Override
//    public void onPullDistance(int distance) {
//
//    }
//
//    @Override
//    public void onPullEnable(boolean enable) {
//        headerSta.setText(enable ? "松开立即刷新" : "下拉刷新");
//        headerImage.setVisibility(View.VISIBLE);
//        headerImage.setRotation(enable ? 180 : 0);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                SettingPointsActivity.this.finish();
                break;
        }
    }

}
