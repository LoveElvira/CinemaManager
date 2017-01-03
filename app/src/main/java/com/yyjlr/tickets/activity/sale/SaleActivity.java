package com.yyjlr.tickets.activity.sale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.SaleAdapter;
import com.yyjlr.tickets.adapter.SalePackageAdapter;
import com.yyjlr.tickets.model.SaleEntity;
import com.yyjlr.tickets.model.sale.GoodInfo;
import com.yyjlr.tickets.model.sale.Goods;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.IRequestMainData;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Elvira on 2016/11/10.
 * 从影院详情里面进行跳转的卖品页面
 */

public class SaleActivity extends AbstractActivity implements SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private RecyclerView listView;//列表
    private SuperSwipeRefreshLayout refresh;//刷新
    private List<SaleEntity> saleEntityList;
    private SaleAdapter saleAdapter;
    private SalePackageAdapter salePackageAdapter;

    protected int TOTAL_COUNTER = 20;

    protected static final int PAGE_SIZE = 10;

    protected int delayMillis = 1000;

    protected int mSaleCounter = 0;
    protected int mPackageCounter = 0;

    protected View notLoadingView;
    private TextView title;

    private LinearLayout packageLayout;
    private LinearLayout saleLayout;
    private ImageView packageImage;
    private ImageView saleImage;
    private boolean flag = true;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;
    private List<GoodInfo> goodInfoList;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_sale);
        initView();
        getSale();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_sale_title));
        back = (ImageView) findViewById(R.id.base_toolbar__left);
        back.setAlpha(1.0f);
        back.setOnClickListener(this);

        packageLayout = (LinearLayout) findViewById(R.id.fragment_sale__package_layout);
        saleLayout = (LinearLayout) findViewById(R.id.fragment_sale__sale_layout);
        packageImage = (ImageView) findViewById(R.id.fragment_sale__package_image);
        saleImage = (ImageView) findViewById(R.id.fragment_sale__sale_image);

        listView = (RecyclerView) findViewById(R.id.fragment_sale__listview);
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.fragment_sale__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

        saleEntityList = Application.getiDataService().getSaleList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        initAdapter();
        notLoadingView = LayoutInflater.from(getBaseContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);

        packageLayout.setOnClickListener(this);
        saleLayout.setOnClickListener(this);

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
                    if (flag) {
                        salePackageAdapter.changeBgFristAndLast(firstItemPosition, lastItemPosition, first);
                    } else {
                        saleAdapter.changeBgFristAndLast(firstItemPosition, lastItemPosition, first);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    //获取卖品数据
    private void getSale() {
        PagableRequest pagableRequest = new PagableRequest();
        OkHttpClientManager.postAsyn(Config.GET_SALE, new OkHttpClientManager.ResultCallback<Goods>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(Goods response) {
                Log.i("ee", new Gson().toJson(response));

                goodInfoList = response.getGoodsList();

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, (IRequestMainData) pagableRequest, Goods.class, SaleActivity.this);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mSaleCounter >= TOTAL_COUNTER && !flag) {
                    saleAdapter.notifyDataChangedAfterLoadMore(false);
                } else if (mPackageCounter >= TOTAL_COUNTER && flag) {
                    salePackageAdapter.notifyDataChangedAfterLoadMore(false);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                salePackageAdapter.notifyDataChangedAfterLoadMore(saleEntityList, true);
                                mPackageCounter = salePackageAdapter.getData().size();
                            } else {
                                saleAdapter.notifyDataChangedAfterLoadMore(goodInfoList, true);
                                mSaleCounter = saleAdapter.getData().size();
                            }

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
                if (flag) {
                    salePackageAdapter.setNewData(saleEntityList);
                    salePackageAdapter.openLoadMore(PAGE_SIZE, true);
                    salePackageAdapter.removeAllFooterView();
                    mPackageCounter = PAGE_SIZE;
                } else {
                    saleAdapter.setNewData(goodInfoList);
                    saleAdapter.openLoadMore(PAGE_SIZE, true);
                    saleAdapter.removeAllFooterView();
                    mSaleCounter = PAGE_SIZE;
                }

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

    private void initAdapter() {

        if (flag) {
            salePackageAdapter = new SalePackageAdapter(saleEntityList);
            salePackageAdapter.openLoadAnimation();
            listView.setAdapter(salePackageAdapter);
            mPackageCounter = salePackageAdapter.getData().size();
            salePackageAdapter.setOnLoadMoreListener(this);
            salePackageAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            salePackageAdapter.setOnRecyclerViewItemChildClickListener(this);
        } else {
            saleAdapter = new SaleAdapter(goodInfoList);
            saleAdapter.openLoadAnimation();
            listView.setAdapter(saleAdapter);
            mSaleCounter = saleAdapter.getData().size();
            saleAdapter.setOnLoadMoreListener(this);
            saleAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            saleAdapter.setOnRecyclerViewItemChildClickListener(this);
        }

        Log.i("ee", mSaleCounter + "--------------------------" + mPackageCounter);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_sale_package__right:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PackageDetailsActivity.class));
                break;
            case R.id.item_sale__shopping_cart:
                selectPopupWindow(saleEntityList.get(position));
                break;
            case R.id.item_sale_package__cardview:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PackageDetailsActivity.class));
                break;
            case R.id.item_sale__cardview:
                selectPopupWindow(saleEntityList.get(position));
                break;
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

    TextView price;
    TextView saleNum;
    PopupWindow mPopupWindow;

    //弹出popwindow 选择数量
    private void selectPopupWindow(SaleEntity saleEntity) {

        View parent = View
                .inflate(getBaseContext(), R.layout.fragment_sale, null);
        View view = View
                .inflate(getBaseContext(), R.layout.popupwindows_sale, null);
        view.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
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


        TextView buy = (TextView) view.findViewById(R.id.popup_sale__buy);
        TextView lost = (TextView) view.findViewById(R.id.popup_sale__lost);
        TextView add = (TextView) view.findViewById(R.id.popup_sale__add);
        TextView salePackage = (TextView) view.findViewById(R.id.popup_sale__package);
        TextView salePackageContent = (TextView) view.findViewById(R.id.popup_sale__package_content);
        saleNum = (TextView) view.findViewById(R.id.popup_sale__num);
        price = (TextView) view.findViewById(R.id.popup_sale_price);

        buy.setOnClickListener(this);
        lost.setOnClickListener(this);
        add.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int num = 0;
        if (saleNum != null) {
            num = Integer.parseInt(saleNum.getText().toString());
        }
        switch (view.getId()) {
            case R.id.fragment_sale__package_layout:
                packageImage.setImageResource(R.mipmap.sale_package_select);
                saleImage.setImageResource(R.mipmap.sale_sale);
                flag = true;
                initAdapter();
                break;
            case R.id.fragment_sale__sale_layout:
                flag = false;
                packageImage.setImageResource(R.mipmap.sale_package);
                saleImage.setImageResource(R.mipmap.sale_sale_select);
                getSale();
                initAdapter();
                break;
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
            case R.id.base_toolbar__left://增加数量
                SaleActivity.this.finish();
                break;
        }
    }
}
