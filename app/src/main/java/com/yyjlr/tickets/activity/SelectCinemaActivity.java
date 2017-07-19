package com.yyjlr.tickets.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.CinemaAdapter;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.cinemainfo.CinemaListInfo;
import com.yyjlr.tickets.model.cinemainfo.CinemaModel;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/4/5.
 * 选择影院
 */

public class SelectCinemaActivity extends AbstractActivity implements View.OnClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private LocationManager locationManager;//定位管理
    private double latitude = 0.0;
    private double longitude = 0.0;
    private RecyclerView listView;
    private SuperSwipeRefreshLayout refresh;//刷新
    private TextView title;
    private ImageView leftArrow;
    private CinemaAdapter adapter;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta;

    private List<CinemaListInfo> cinemaList;
    private List<CinemaListInfo> cinemaLists;//总条数
    private boolean hasMore = false;
    private String pagable = "0";
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cinema);
        activity = this;
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        getLocation();
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("影城列表");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        listView = (RecyclerView) findViewById(R.id.content_select_cinema__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_select_cinema__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);
        cinemaLists = new ArrayList<>();
        getCinemaList(pagable);
    }

    //获取经纬度
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
        //info.setText("纬度：" + latitude + "\n" + "经度：" + longitude);
    }

    LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            Log.e("map", provider);
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.e("map", provider);
        }

        // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e("map", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
            }
        }
    };

    //我的影城列表
    private void getCinemaList(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.GET_CINEMA_LIST, new OkHttpClientManager.ResultCallback<CinemaModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(CinemaModel response) {
                cinemaList = response.getCinemaList();
                if (cinemaList != null && cinemaList.size() > 0) {
                    if ("0".equals(pagables)) {//第一页
                        cinemaLists.clear();
                        cinemaLists.addAll(cinemaList);
                        Log.i("ee", cinemaLists.size() + "----" + cinemaList.size());
                        adapter = new CinemaAdapter(cinemaList);
                        adapter.openLoadAnimation();
                        listView.setAdapter(adapter);
                        adapter.openLoadMore(cinemaList.size(), true);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                        } else {
                            hasMore = false;
                        }
                        pagable = response.getPagable();
                    } else {
                        cinemaLists.addAll(cinemaList);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                            pagable = response.getPagable();
                            adapter.notifyDataChangedAfterLoadMore(cinemaList, true);
                        } else {
                            adapter.notifyDataChangedAfterLoadMore(cinemaList, true);
                            hasMore = false;
                            pagable = "";
                        }
                    }
                    adapter.setOnLoadMoreListener(SelectCinemaActivity.this);
                    adapter.setOnRecyclerViewItemChildClickListener(SelectCinemaActivity.this);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, pagableRequest, CinemaModel.class, Application.getInstance().getCurrentActivity());
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
                            getCinemaList(pagable);
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
                getCinemaList(pagable);
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

    /**
     * show Dialog 提示
     */
    private void showTip(String msg, final int position) {
        LayoutInflater inflater = LayoutInflater.from(SelectCinemaActivity.this);
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(SelectCinemaActivity.this).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        TextView cancel = (TextView) layout.findViewById(R.id.alert_dialog__cancel);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
        confirm.setText("确定");
        title.setText("提示");
        message.setText(msg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                SharePrefUtil.putString(Constant.FILE_NAME, "appDomain", cinemaLists.get(position).getId() + "", SelectCinemaActivity.this);
                Constant.AppDomain = cinemaLists.get(position).getId() + "";
                getAppConfig(false);

            }
        });
    }

    /**
     * show Dialog 提示 门店正在装修，敬请期待~
     */
    private void showTip(String msg) {
        LayoutInflater inflater = LayoutInflater.from(SelectCinemaActivity.this);
        View layout = inflater.inflate(R.layout.alert_dialog_, null);
        final AlertDialog builder = new AlertDialog.Builder(SelectCinemaActivity.this).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog__title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog__message);
        ImageView tipImage = (ImageView) layout.findViewById(R.id.alert_dialog__image);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
        confirm.setText("确定");
        tipImage.setImageResource(R.mipmap.error);
        tipImage.setVisibility(View.VISIBLE);
//        title.setText("提示");
        message.setText(msg);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                SelectCinemaActivity.this.finish();
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        if (!Constant.AppDomain.equals(cinemaLists.get(position).getId() + "")) {
            if ("0".equals(cinemaLists.get(position).getState())) {
                String msg = "是否切换到" + cinemaLists.get(position).getName();
                showTip(msg, position);
            } else {
                showTip(cinemaLists.get(position).getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_DIALOG:
                pagable = "0";
                getCinemaList(pagable);
                break;
        }
    }
}
