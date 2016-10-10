package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.PackageDetailsActivity;
import com.yyjlr.tickets.activity.SaleCompleteActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.SaleAdapter;
import com.yyjlr.tickets.adapter.SalePackageAdapter;
import com.yyjlr.tickets.model.SaleEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 卖品页面
 */
public class SaleContent extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener {
    private View view;
    private RecyclerView listView;//列表
    private SwipeRefreshLayout refresh;//刷新
    private List<SaleEntity> saleEntityList;
    private SaleAdapter saleAdapter;
    private SalePackageAdapter salePackageAdapter;

    protected int TOTAL_COUNTER = 20;

    protected static final int PAGE_SIZE = 10;

    protected int delayMillis = 1000;

    protected int mCurrentCounter = 0;

    protected View notLoadingView;
    private TextView title;

    private LinearLayout packageLayout;
    private LinearLayout saleLayout;
    private ImageView packageImage;
    private ImageView saleImage;
    private boolean flag = true;


    public SaleContent(Context context) {
        this(context, null);
    }

    public SaleContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_sale, this);

        title = (TextView) view.findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_sale_title));

        packageLayout = (LinearLayout) view.findViewById(R.id.fragment_sale__package_layout);
        saleLayout = (LinearLayout) view.findViewById(R.id.fragment_sale__sale_layout);
        packageImage = (ImageView) view.findViewById(R.id.fragment_sale__package_image);
        saleImage = (ImageView) view.findViewById(R.id.fragment_sale__sale_image);

        listView = (RecyclerView) view.findViewById(R.id.fragment_sale__listview);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.fragment_sale__refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refresh.setOnRefreshListener(this);
        saleEntityList = Application.getiDataService().getSaleList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        initAdapter();

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
                    /*if (foodsArrayList.get(firstItemPosition) instanceof Foods) {
                        int foodTypePosion = ((Foods) foodsArrayList.get(firstItemPosition)).getFood_stc_posion();
                        FoodsTypeListview.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
                    }*/
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

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    saleAdapter.notifyDataChangedAfterLoadMore(false);
                    if (notLoadingView == null) {
                        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
                    }
                    if (flag) {
                        salePackageAdapter.notifyDataChangedAfterLoadMore(false);
                        salePackageAdapter.addFooterView(notLoadingView);
                    } else {
                        saleAdapter.notifyDataChangedAfterLoadMore(false);
                        saleAdapter.addFooterView(notLoadingView);
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flag) {
                                salePackageAdapter.notifyDataChangedAfterLoadMore(saleEntityList, true);
                                mCurrentCounter = salePackageAdapter.getData().size();
                            } else {
                                saleAdapter.notifyDataChangedAfterLoadMore(saleEntityList, true);
                                mCurrentCounter = saleAdapter.getData().size();
                            }

                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    salePackageAdapter.setNewData(saleEntityList);
                    salePackageAdapter.openLoadMore(PAGE_SIZE, true);
                    salePackageAdapter.removeAllFooterView();
                } else {
                    saleAdapter.setNewData(saleEntityList);
                    saleAdapter.openLoadMore(PAGE_SIZE, true);
                    saleAdapter.removeAllFooterView();
                }
                mCurrentCounter = PAGE_SIZE;
                refresh.setRefreshing(false);
            }
        }, delayMillis);
    }

    private void initAdapter() {

        if (flag) {
            salePackageAdapter = new SalePackageAdapter(saleEntityList);
            salePackageAdapter.openLoadAnimation();
            listView.setAdapter(salePackageAdapter);
            mCurrentCounter = salePackageAdapter.getData().size();
            salePackageAdapter.setOnLoadMoreListener(this);
            salePackageAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            salePackageAdapter.setOnRecyclerViewItemChildClickListener(this);
        } else {
            saleAdapter = new SaleAdapter(saleEntityList);
            saleAdapter.openLoadAnimation();
            listView.setAdapter(saleAdapter);
            mCurrentCounter = saleAdapter.getData().size();
            saleAdapter.setOnLoadMoreListener(this);
            saleAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
            saleAdapter.setOnRecyclerViewItemChildClickListener(this);
        }

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
        }


    }

    TextView price;
    TextView saleNum;
    PopupWindow mPopupWindow;

    //弹出popwindow 选择数量
    private void selectPopupWindow(SaleEntity saleEntity) {

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
        lp.alpha = 0.5f;
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
        }
    }
}
