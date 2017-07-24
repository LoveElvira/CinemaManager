package com.yyjlr.tickets.content.collect;

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
import com.yyjlr.tickets.activity.film.FilmDetailsActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FollowFilmAdapter;
import com.yyjlr.tickets.content.BaseLinearLayout;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.film.MovieBean;
import com.yyjlr.tickets.model.film.MovieInfo;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2017/1/3.
 * 收藏 影片
 */

public class FollowFilmContent extends BaseLinearLayout implements SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener, FollowFilmAdapter.SlidingViewClickListener {

    private FollowFilmAdapter adapter = null;
    private SuperSwipeRefreshLayout refresh;//刷新
    private RecyclerView listView;
    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;
    private boolean hasMore = false;
    private String pagable = "0";

    private List<MovieInfo> movieInfoList;
    private List<MovieInfo> movieInfoLists;
    private int position;

    public FollowFilmContent(Context context) {
        this(context, null);
    }

    public FollowFilmContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_listview, this);
        lastClickTime = 0;
        initView();
    }

    public void refreshView(int position) {
        adapter.remove(position);
        movieInfoLists.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_listview__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        listView = (RecyclerView) findViewById(R.id.content_listview__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);

        movieInfoLists = new ArrayList<>();

        getFollowFilm(pagable);
    }

    //获取关注的影片
    private void getFollowFilm(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.GET_FOLLOW_FILM, new OkHttpClientManager.ResultCallback<MovieBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(MovieBean response) {
                if (response != null) {
                    movieInfoList = response.getMovieList();
                    if (movieInfoList != null && movieInfoList.size() > 0) {
                        if ("0".equals(pagables)) {//第一页
                            movieInfoLists.clear();
                            movieInfoLists.addAll(movieInfoList);
                            Log.i("ee", movieInfoList.size() + "----" + movieInfoLists.size());
                            adapter = new FollowFilmAdapter(movieInfoList, FollowFilmContent.this);
                            adapter.openLoadAnimation();
                            listView.setAdapter(adapter);
                            adapter.openLoadMore(movieInfoList.size(), true);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            pagable = response.getPagable();
                        } else {
                            movieInfoLists.addAll(movieInfoList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                pagable = response.getPagable();
                                adapter.notifyDataChangedAfterLoadMore(movieInfoList, true);
                            } else {
                                adapter.notifyDataChangedAfterLoadMore(movieInfoList, true);
                                hasMore = false;
                                pagable = "";
                            }
                        }
                        adapter.setOnLoadMoreListener(FollowFilmContent.this);
                        adapter.setOnRecyclerViewItemChildClickListener(FollowFilmContent.this);
                    } else {
                        if (adapter != null) {
                            movieInfoLists.clear();
                            movieInfoList = new ArrayList<>();
                            movieInfoLists.addAll(movieInfoList);
                            adapter = new FollowFilmAdapter(movieInfoList, FollowFilmContent.this);
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
        }, pagableRequest, MovieBean.class, Application.getInstance().getCurrentActivity());
    }

    //取消关注
    private void cancelCollectFilm() {
        customDialog = new CustomDialog(Application.getInstance().getCurrentActivity(), "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setId(movieInfoLists.get(position).getMovieId() + "");
        idRequest.setType("1");
        idRequest.setIsInterest("0");
        OkHttpClientManager.postAsyn(Config.GO_COLLECT, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
                pagable = "0";
                getFollowFilm(pagable);
                customDialog.dismiss();

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
                getFollowFilm(pagable);
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
//                    saleAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getFollowFilm(pagable);
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
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.item_follow_film__ll_layout:
                    intent.setClass(getContext(), FilmDetailsActivity.class);
                    intent.putExtra("filmId", movieInfoLists.get(position).getMovieId() + "");
                    intent.putExtra("position", position);
                    intent.putExtra("isHot", movieInfoLists.get(position).getPresell());
                    break;
            }
            Application.getInstance().getCurrentActivity().startActivityForResult(intent, 0x06);
        }
    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        this.position = position;
        showCancel();
    }

    /**
     * show Dialog 是否确定 取消收藏
     */
    private void showCancel() {
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
        message.setText("是否取消此收藏?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消收藏
                cancelCollectFilm();
                builder.dismiss();

            }
        });
    }
}
