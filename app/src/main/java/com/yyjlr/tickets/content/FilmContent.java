package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.yyjlr.tickets.activity.FilmDetailsActivity;
import com.yyjlr.tickets.activity.FilmScheduleActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FilmAdapter;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.yyjlr.tickets.Application.*;

/**
 * Created by Elvira on 2016/7/28.
 * 影片页面
 */
public class FilmContent extends LinearLayout implements BaseAdapter.OnRecyclerViewItemChildClickListener, BaseAdapter.RequestLoadMoreListener, SuperSwipeRefreshLayout.OnPullRefreshListener {

    private View view;
    private RecyclerView listView;//列表
    private SuperSwipeRefreshLayout refresh;//刷新
    private List<FilmEntity> filmEntityList;
    private FilmAdapter filmAdapter;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    protected int TOTAL_COUNTER = 20;

    protected static final int PAGE_SIZE = 10;

    protected int delayMillis = 1000;

    protected int mCurrentCounter = 0;

    protected View notLoadingView;

    private TextView title;

    public FilmContent(Context context) {
        this(context, null);
    }

    public FilmContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_film, this);

        title = (TextView) view.findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_film_title));

        listView = (RecyclerView) view.findViewById(R.id.fragment_film__listview);
        refresh = (SuperSwipeRefreshLayout) view.findViewById(R.id.fragment_film__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);
        filmEntityList = Application.getiDataService().getFilmList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        initAdapter();
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
                    /*if (foodsArrayList.get(firstItemPosition) instanceof Foods) {
                        int foodTypePosion = ((Foods) foodsArrayList.get(firstItemPosition)).getFood_stc_posion();
                        FoodsTypeListview.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
                    }*/
                    filmAdapter.changeBgFristAndLast(firstItemPosition, lastItemPosition, first);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    filmAdapter.notifyDataChangedAfterLoadMore(false);
//                    if (notLoadingView == null) {
//                        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
//                    }
//                    filmAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            filmAdapter.notifyDataChangedAfterLoadMore(filmEntityList, true);
                            mCurrentCounter = filmAdapter.getData().size();
                        }
                    }, delayMillis);
                }
            }

        });
    }

    private void initAdapter() {
        filmAdapter = new FilmAdapter(filmEntityList);
        filmAdapter.openLoadAnimation();
        listView.setAdapter(filmAdapter);
        mCurrentCounter = filmAdapter.getData().size();
        filmAdapter.setOnLoadMoreListener(this);
        filmAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        filmAdapter.setOnRecyclerViewItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.item_film__buy_ticket:
                intent.setClass(getInstance().getCurrentActivity(), FilmScheduleActivity.class);
                break;
            case R.id.item_film__cardview:
            case R.id.item_film__image:
                intent.setClass(getInstance().getCurrentActivity(), FilmDetailsActivity.class);
                break;
        }
        getInstance().getCurrentActivity().startActivity(intent);
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
                filmAdapter.setNewData(filmEntityList);
                filmAdapter.openLoadMore(PAGE_SIZE, true);
                filmAdapter.removeAllFooterView();
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
}
