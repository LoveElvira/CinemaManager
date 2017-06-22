package com.yyjlr.tickets.content.film;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.yyjlr.tickets.activity.film.FilmScheduleActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FilmAdapter;
import com.yyjlr.tickets.content.BaseLinearLayout;
import com.yyjlr.tickets.model.film.MovieBean;
import com.yyjlr.tickets.model.film.MovieInfo;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.yyjlr.tickets.Application.getInstance;

/**
 * Created by Elvira on 2016/7/28.
 * 热映 影片 页面
 */
public class HotFilmContent extends BaseLinearLayout implements BaseAdapter.OnRecyclerViewItemChildClickListener, BaseAdapter.RequestLoadMoreListener, SuperSwipeRefreshLayout.OnPullRefreshListener {

    private RecyclerView listView;//列表
    private SuperSwipeRefreshLayout refresh;//刷新
    private FilmAdapter filmAdapter;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    private List<MovieInfo> movieInfoList;
    private List<MovieInfo> movieInfoLists = new ArrayList<>();//总条数
    private boolean hasMore = false;
    private String pagable;


    public HotFilmContent(Context context) {
        this(context, null);
    }

    public HotFilmContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_film, this);
        initView();
    }

    private void initView() {
        isFirst = true;
        listView = (RecyclerView) view.findViewById(R.id.fragment_film__listview);
        refresh = (SuperSwipeRefreshLayout) view.findViewById(R.id.fragment_film__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
//        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                //判断是当前layoutManager是否为LinearLayoutManager
//                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                if (layoutManager instanceof LinearLayoutManager) {
//                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                    //获取最后一个可见view的位置
//                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
//                    //获取第一个可见view的位置
//                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    int first = linearManager.findFirstCompletelyVisibleItemPosition();
//                    filmAdapter.changeBgFristAndLast(firstItemPosition, lastItemPosition, first);
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });

    }

    public void updateView(boolean isUpdate) {
        if (isUpdate) {
            isFirst = isUpdate;
        }
        if (isFirst) {
            isFirst = false;
            pagable = "0";
            getFilm(pagable);
        }
    }

//    public void hideInput() {
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
////            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//            imm.hideSoftInputFromWindow(title.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }


    //获取热门影片数据
    private void getFilm(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.GET_FILM, new OkHttpClientManager.ResultCallback<MovieBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(MovieBean response) {
//                Log.i("ee", new Gson().toJson(response));

                if (response != null) {
                    movieInfoList = response.getMovieList();

                    if (movieInfoList != null && movieInfoList.size() > 0) {
                        if ("0".equals(pagables)) {//第一页
                            movieInfoLists.clear();
                            movieInfoLists.addAll(movieInfoList);
                            Log.i("ee", movieInfoLists.size() + "----" + movieInfoList.size());
                            filmAdapter = new FilmAdapter(movieInfoList);
                            filmAdapter.openLoadAnimation();
                            listView.setAdapter(filmAdapter);
                            filmAdapter.openLoadMore(movieInfoList.size(), true);
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
                                filmAdapter.notifyDataChangedAfterLoadMore(movieInfoList, true);
                            } else {
                                filmAdapter.notifyDataChangedAfterLoadMore(movieInfoList, true);
                                hasMore = false;
                                pagable = "";
                            }
                        }
                        filmAdapter.setOnLoadMoreListener(HotFilmContent.this);
                        filmAdapter.setOnRecyclerViewItemChildClickListener(HotFilmContent.this);
                    } else {
                        movieInfoList = new ArrayList<>();
                        filmAdapter = new FilmAdapter(movieInfoList);
                        listView.setAdapter(filmAdapter);
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


    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    filmAdapter.notifyDataChangedAfterLoadMore(false);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getFilm(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.item_film__buy_ticket:
                    intent.setClass(getInstance().getCurrentActivity(), FilmScheduleActivity.class);
                    intent.putExtra("filmId", movieInfoLists.get(position).getMovieId() + "");
                    break;
                case R.id.item_film__cardview:
                case R.id.item_film__image:
                    intent.setClass(getInstance().getCurrentActivity(), FilmDetailsActivity.class);
                    intent.putExtra("filmId", movieInfoLists.get(position).getMovieId() + "");
                    intent.putExtra("isHot", -1);
                    break;
            }
            getInstance().getCurrentActivity().startActivity(intent);
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
                getFilm(pagable);
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
}
