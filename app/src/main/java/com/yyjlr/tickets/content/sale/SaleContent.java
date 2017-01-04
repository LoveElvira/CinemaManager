package com.yyjlr.tickets.content.sale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.sale.PackageDetailsActivity;
import com.yyjlr.tickets.activity.sale.SaleCompleteActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.SaleAdapter;
import com.yyjlr.tickets.model.sale.GoodInfo;
import com.yyjlr.tickets.model.sale.Goods;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2017/1/3.
 * 卖品
 */

public class SaleContent extends LinearLayout implements SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.OnRecyclerViewItemChildClickListener, BaseAdapter.RequestLoadMoreListener, View.OnClickListener {

    private View view;
    private RecyclerView listView;//列表
    private SuperSwipeRefreshLayout refresh;//刷新
    private SaleAdapter saleAdapter;
    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;
    private List<GoodInfo> goodInfoList;
    private List<GoodInfo> goodInfoLists;
    private View notLoadingView;
    private boolean hasMore = false;
    private String pagable = "0";
    private int delayMillis = 1000;


    public SaleContent(Context context) {
        this(context, null);
    }

    public SaleContent(Context context, AttributeSet attrs) {
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
        goodInfoLists = new ArrayList<>();

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
                    saleAdapter.changeBgFristAndLast(firstItemPosition, lastItemPosition, first);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        getSale(pagable);
    }

    //获取卖品数据
    private void getSale(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        OkHttpClientManager.postAsyn(Config.GET_SALE, new OkHttpClientManager.ResultCallback<Goods>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getContext(),info.getInfo(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Goods response) {
                goodInfoList = response.getGoodsList();
                if ("0".equals(pagables)) {//第一页
                    goodInfoLists.clear();
                    goodInfoLists.addAll(goodInfoList);
                    Log.i("ee", goodInfoList.size() + "----" + goodInfoLists.size());
                    saleAdapter = new SaleAdapter(goodInfoList);
                    saleAdapter.openLoadAnimation();
                    listView.setAdapter(saleAdapter);
                    saleAdapter.openLoadMore(goodInfoList.size(), true);
                    if (response.getHasMore() == 1) {
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }
                    pagable = response.getPagable();
                } else {
                    goodInfoLists.addAll(goodInfoList);
                    if (response.getHasMore() == 1) {
                        hasMore = true;
                        pagable = response.getPagable();
                        saleAdapter.notifyDataChangedAfterLoadMore(goodInfoList, true);
                    } else {
                        saleAdapter.notifyDataChangedAfterLoadMore(goodInfoList, true);
                        hasMore = false;
                        pagable = "";
                    }
                }
                saleAdapter.setOnLoadMoreListener(SaleContent.this);
                saleAdapter.setOnRecyclerViewItemChildClickListener(SaleContent.this);
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, pagableRequest, Goods.class, Application.getInstance().getCurrentActivity());
    }

    TextView price;
    TextView saleNum;
    PopupWindow mPopupWindow;

    //弹出popwindow 选择数量
    private void selectPopupWindow(GoodInfo goodInfo) {

        View parent = View
                .inflate(getContext(), R.layout.fragment_sale, null);
        View view = View
                .inflate(getContext(), R.layout.popupwindows_sale, null);
        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in));
        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = Application.getInstance().getCurrentActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);

        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });

        ImageView saleImage = (ImageView) view.findViewById(R.id.popup_sale__image);
        TextView buy = (TextView) view.findViewById(R.id.popup_sale__buy);
        TextView lost = (TextView) view.findViewById(R.id.popup_sale__lost);
        TextView add = (TextView) view.findViewById(R.id.popup_sale__add);
        TextView salePackage = (TextView) view.findViewById(R.id.popup_sale__package);
        TextView salePackageContent = (TextView) view.findViewById(R.id.popup_sale__package_content);
        saleNum = (TextView) view.findViewById(R.id.popup_sale__num);
        price = (TextView) view.findViewById(R.id.popup_sale_price);

        salePackage.setText(goodInfo.getGoodsName());
        salePackageContent.setText(goodInfo.getGoodsDesc());
        if (goodInfo.getGoodsImg() != null && !"".equals(goodInfo.getGoodsImg())) {
            Picasso.with(getContext())
                    .load(goodInfo.getGoodsImg())
                    .into(saleImage);
        }

        buy.setOnClickListener(this);
        lost.setOnClickListener(this);
        add.setOnClickListener(this);

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
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    saleAdapter.notifyDataChangedAfterLoadMore(false);
//                    saleAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSale(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
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
                getSale(pagable);
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
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_sale__shopping_cart:
                selectPopupWindow(goodInfoLists.get(position));
                break;
            case R.id.item_sale_package__cardview:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PackageDetailsActivity.class));
                break;
            case R.id.item_sale__cardview:
                selectPopupWindow(goodInfoLists.get(position));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int num = 0;
        if (saleNum != null) {
            num = Integer.parseInt(saleNum.getText().toString());
        }
        switch (view.getId()) {
            case R.id.popup_sale__buy://购买
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), SaleCompleteActivity.class));
                mPopupWindow.dismiss();
                break;
            case R.id.popup_sale__lost://减少数量
                if (num != 0)
                    num = num - 1;
                saleNum.setText(num + "");
                break;
            case R.id.popup_sale__add://增加数量
                saleNum.setText((num + 1) + "");
                break;
        }

    }
}
