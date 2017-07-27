package com.yyjlr.tickets.content.sale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.activity.sale.PackageDetailsActivity;
import com.yyjlr.tickets.activity.sale.SaleCompleteActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.SaleAdapter;
import com.yyjlr.tickets.adapter.SalePackageAdapter;
import com.yyjlr.tickets.content.BaseLinearLayout;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.SaleEntity;
import com.yyjlr.tickets.model.sale.GoodInfo;
import com.yyjlr.tickets.model.sale.Goods;
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
 * Created by Elvira on 2017/1/3.
 * 套餐
 */

public class PackageContent extends BaseLinearLayout implements SuperSwipeRefreshLayout.OnPullRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener {

    private LinearLayout noDate;
    private ImageView noDateImage;
    private RecyclerView listView;//列表
    private SuperSwipeRefreshLayout refresh;//刷新
    private SalePackageAdapter salePackageAdapter = null;
    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;
    private List<GoodInfo> goodInfoList;
    private List<GoodInfo> goodInfoLists;
    private boolean hasMore = false;
    private String pagable = "0";

    private int position = 0;
    private long totalPrice = 0;//popupwindow中的价格
    private String searchText = "";

    public PackageContent(Context context) {
        this(context, null);
    }

    public PackageContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_listview, this);
        initView();
    }

    public String getSearchText() {
        return searchText;
    }

    private void initView() {
        noDate = (LinearLayout) findViewById(R.id.content_listview__no_date);
        noDateImage = (ImageView) findViewById(R.id.content_listview__no_date_image);
        noDateImage.setBackgroundResource(R.mipmap.no_sale);

        isFirst = true;
        listView = (RecyclerView) findViewById(R.id.content_listview__listview);
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_listview__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);

//        saleEntityList = Application.getiDataService().getSaleList();
//        saleEntityList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
        goodInfoLists = new ArrayList<>();


    }

    public void updateView(boolean isUpdate) {
        if (isUpdate) {
            isFirst = isUpdate;
        }
        if (isFirst) {
            isFirst = false;
            pagable = "0";
            refresh.setVisibility(VISIBLE);
            noDate.setVisibility(GONE);
            getPackage(pagable, "");
        }
    }

    //获取套餐数据
    public void getPackage(final String pagables, String searchContent) {
        searchText = searchContent;
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setType("1");
        pagableRequest.setPagable(pagables);
        if (searchContent.length() > 0) {
            pagableRequest.setGoodsName(searchContent);
        }
        OkHttpClientManager.postAsyn(Config.GET_SALE, new OkHttpClientManager.ResultCallback<Goods>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Goods response) {
                if (response != null) {
                    goodInfoList = response.getGoodsList();
                    if (goodInfoList != null && goodInfoList.size() > 0) {
                        refresh.setVisibility(VISIBLE);
                        noDate.setVisibility(GONE);
                        if ("0".equals(pagables)) {//第一页
                            goodInfoLists.clear();
                            goodInfoLists.addAll(goodInfoList);
                            Log.i("ee", goodInfoList.size() + "----" + goodInfoLists.size());
                            salePackageAdapter = new SalePackageAdapter(goodInfoList);
                            salePackageAdapter.openLoadAnimation();
                            listView.setAdapter(salePackageAdapter);
                            salePackageAdapter.openLoadMore(goodInfoList.size(), true);
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
                                salePackageAdapter.notifyDataChangedAfterLoadMore(goodInfoList, true);
                            } else {
                                salePackageAdapter.notifyDataChangedAfterLoadMore(goodInfoList, true);
                                hasMore = false;
                                pagable = "";
                            }
                        }
                        salePackageAdapter.setOnLoadMoreListener(PackageContent.this);
                        salePackageAdapter.setOnRecyclerViewItemChildClickListener(PackageContent.this);
                    } else {
                        if (salePackageAdapter != null) {
                            goodInfoLists.clear();
                            goodInfoList = new ArrayList<>();
                            goodInfoLists.addAll(goodInfoList);
                            salePackageAdapter = new SalePackageAdapter(goodInfoList);
                            listView.setAdapter(salePackageAdapter);
                        }
                        refresh.setVisibility(GONE);
                        noDate.setVisibility(VISIBLE);
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, pagableRequest, Goods.class, Application.getInstance().getCurrentActivity());
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
                getPackage(pagable, searchText);
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
                    salePackageAdapter.notifyDataChangedAfterLoadMore(false);
//                    saleAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPackage(pagable, searchText);
                        }
                    }, delayMillis);
                }
            }

        });
    }

    TextView salePrice;
    TextView saleNum;
    PopupWindow mPopupWindow;
    private long goodPrice;

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
        salePrice = (TextView) view.findViewById(R.id.popup_sale_price);

        salePackage.setText(goodInfo.getGoodsName());
        salePackageContent.setText(goodInfo.getGoodsDesc());
        if (goodInfo.getGoodsImg() != null && !"".equals(goodInfo.getGoodsImg())) {
            Picasso.with(getContext())
                    .load(goodInfo.getGoodsImg())
                    .into(saleImage);
        }

        salePrice.setText(ChangeUtils.save2Decimal(goodInfo.getPrice()));
        goodPrice = goodInfo.getPrice();

        buy.setOnClickListener(PackageContent.this);
        lost.setOnClickListener(PackageContent.this);
        add.setOnClickListener(PackageContent.this);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        com.yyjlr.tickets.content.SaleContent.hideInput();
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            this.position = position;
            switch (view.getId()) {
                case R.id.item_sale__shopping_cart:
                case R.id.item_sale__cardview:
                    totalPrice = goodInfoLists.get(position).getPrice();
                    selectPopupWindow(goodInfoLists.get(position));
                    break;
//                case R.id.item_sale__cardview:
//                case R.id.item_sale_package__right:
//                case R.id.item_sale_package__cardview:
//                    Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PackageDetailsActivity.class)
//                            .putExtra("packageId", goodInfoLists.get(position).getGoodsId() + ""));
//                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.i("ee", "------------------");
        int num = 0;
        if (saleNum != null) {
            num = Integer.parseInt(saleNum.getText().toString());
        }
        switch (v.getId()) {
            case R.id.popup_sale__buy://购买
                Intent intent = new Intent();
                String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
                if (!isLogin.equals("1")) {
                    intent.setClass(getInstance().getCurrentActivity(), LoginActivity.class);
                } else {
                    intent.setClass(getInstance().getCurrentActivity(), SaleCompleteActivity.class);
                    intent.putExtra("goodInfo", goodInfoLists.get(position));
                    intent.putExtra("num", num);
                }
//                Toast.makeText(getContext(), "购买功能正在开放中", Toast.LENGTH_SHORT).show();
                Application.getInstance().getCurrentActivity().startActivity(intent);
                mPopupWindow.dismiss();
                break;
            case R.id.popup_sale__lost://减少数量
                if (num != 1) {
                    num -= 1;
                }
                salePrice.setText(ChangeUtils.save2Decimal(totalPrice * num));
                saleNum.setText(num + "");
                break;
            case R.id.popup_sale__add://增加数量
                if (goodInfoLists.get(this.position).getLimitedCount() == -1) {
                    saleNum.setText((num + 1) + "");
                    salePrice.setText(ChangeUtils.save2Decimal(totalPrice * (num + 1)));
                } else if (goodInfoLists.get(this.position).getLimitedCount() > 0) {
                    if (num < goodInfoLists.get(this.position).getLimitedCount()) {
                        saleNum.setText((num + 1) + "");
                        salePrice.setText(ChangeUtils.save2Decimal(totalPrice * (num + 1)));
                    } else {
                        Toast.makeText(getContext(), "最多可以购买" + goodInfoLists.get(this.position).getLimitedCount() + "份", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
