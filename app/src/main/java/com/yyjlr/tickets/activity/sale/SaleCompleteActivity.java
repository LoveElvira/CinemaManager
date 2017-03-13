package com.yyjlr.tickets.activity.sale;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.PaySelectActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FilmSaleAdapter;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.FilmSaleEntity;
import com.yyjlr.tickets.model.order.ConfirmOrderBean;
import com.yyjlr.tickets.model.sale.GoodInfo;
import com.yyjlr.tickets.model.sale.GoodMoreList;
import com.yyjlr.tickets.model.sale.RecommendGoodsInfo;
import com.yyjlr.tickets.model.sale.RecommendList;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.requestdata.confirmfilmorder.ConfirmGoods;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/10.
 * 确认卖品
 */
public class SaleCompleteActivity extends AbstractActivity implements BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener, BaseAdapter.RequestLoadMoreListener {

    private TextView title;
    private ImageView leftArrow;
    private TextView payPrice;//应付金额
    private ImageView addPackage;//添加套餐
    private ImageView deletePhone;//删除电话
    private LinearLayout addPackageLayout, addLayout;
    private EditText phone;
    private TextView confirmOrder;

    private TextView packageName;//商品名字
    private TextView packageContent;//商品内容
    private TextView packageDate;//商品时间
    private TextView packageUnitPrice;//单价：20元，共1份
    private TextView packageTotalPrice;//商品总价

    private List<FilmSaleEntity> allDate;
    private List<FilmSaleEntity> partDate = new ArrayList<FilmSaleEntity>();
    private FilmSaleAdapter filmSaleAdapter;

    private GoodInfo goodInfo;//商品信息
    private int goodNum;//商品数量

    private RecyclerView listView;
    private List<RecommendGoodsInfo> goodList = null;
    private List<RecommendGoodsInfo> goodMoreList;
    private List<RecommendGoodsInfo> goodMoreLists;
    private String pagable = "0";
    private boolean hasMore = false;
    private long totlalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_confirm);
        customDialog = new CustomDialog(this, "加载中...");
        AppManager.getInstance().initWidthHeight(getBaseContext());
        goodInfo = (GoodInfo) getIntent().getSerializableExtra("goodInfo");
        goodNum = getIntent().getIntExtra("num", 0);
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("卖品确认");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        packageName = (TextView) findViewById(R.id.content_sale_confirm__package);
        packageContent = (TextView) findViewById(R.id.content_sale_confirm__package_content);
        packageDate = (TextView) findViewById(R.id.content_sale_confirm__package_date);
        packageUnitPrice = (TextView) findViewById(R.id.content_sale_confirm__package_unit_price);
        packageTotalPrice = (TextView) findViewById(R.id.content_sale_confirm__package__price);

        payPrice = (TextView) findViewById(R.id.content_sale_confirm__pay_price);
        addPackage = (ImageView) findViewById(R.id.content_sale_confirm__add);
        addPackageLayout = (LinearLayout) findViewById(R.id.content_sale_confirm__sale_layout);
        addLayout = (LinearLayout) findViewById(R.id.content_sale_confirm__add_layout);
        addLayout.setVisibility(View.VISIBLE);
        phone = (EditText) findViewById(R.id.content_sale_bill__phone);
        deletePhone = (ImageView) findViewById(R.id.content_sale_bill__delete_phone);
        confirmOrder = (TextView) findViewById(R.id.content_sale_bill__confirm_order);
        deletePhone.setOnClickListener(this);

        String phoneStr = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PHONE, "", SaleCompleteActivity.this);
        phone.setText(phoneStr);
        phone.setSelection(phoneStr.length());
        phone.addTextChangedListener(textWatcher);

        confirmOrder.setOnClickListener(this);
        addPackage.setOnClickListener(this);

        allDate = Application.getiDataService().getFileSaleList();
        partDate.add(allDate.get(0));
        partDate.add(allDate.get(1));
//        initAddPackage(0);
        if (goodInfo != null) {
            initTopDate();
        }
        getPackageRecommend();
    }

    //顶部商品数据
    private void initTopDate() {
        packageName.setText(goodInfo.getGoodsName());
        packageContent.setText(goodInfo.getGoodsDesc());
        if (goodInfo.getStartTime() != 0 && goodInfo.getEndTime() != 0) {
            packageDate.setText(ChangeUtils.changeTimeYear(goodInfo.getStartTime()) + "~" + ChangeUtils.changeTimeYear(goodInfo.getEndTime()));
        } else if (goodInfo.getStartTime() != 0 && goodInfo.getEndTime() == 0) {
            packageDate.setText(ChangeUtils.changeTimeYear(goodInfo.getStartTime()));
        }
        packageUnitPrice.setText("单价：" + ChangeUtils.save2Decimal(goodInfo.getPrice()) + "元，共 " + goodNum + " 份");
        packageTotalPrice.setText(ChangeUtils.save2Decimal(goodInfo.getPrice() * goodNum) + " 元");
        totlalPrice = goodInfo.getPrice() * goodNum;
        payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().equals("") || editable.toString() == null) {
                deletePhone.setVisibility(View.GONE);
                phone.setText("请输入接受订单信息的手机号码");
            } else {
                deletePhone.setVisibility(View.VISIBLE);
            }
        }
    };


    //获取推荐套餐
    private void getPackageRecommend() {
        customDialog.show();
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_SALE_RECOMMEND, new OkHttpClientManager.ResultCallback<RecommendList>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(RecommendList response) {
                customDialog.dismiss();
                goodList = response.getGoodsList();
                if (goodList != null && goodList.size() > 0) {
                    initAddPackage(0);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, requestNull, RecommendList.class, SaleCompleteActivity.this);
    }


    //添加套餐
    private void initAddPackage(int i) {
        if (i >= goodList.size()) {
            return;
        }
        View view = LayoutInflater.from(SaleCompleteActivity.this).inflate(R.layout.item_film_sale_package, null, false);
        final ImageView selectImage = (ImageView) view.findViewById(R.id.item_film_sale__select);
        ImageView saleImage = (ImageView) view.findViewById(R.id.item_film_sale__image);
        TextView salePackage = (TextView) view.findViewById(R.id.item_film_sale__package);
        TextView salePackagePrice = (TextView) view.findViewById(R.id.item_film_sale__price);
        TextView salePackageContent = (TextView) view.findViewById(R.id.item_film_sale__package_content);
        TextView saleTime = (TextView) view.findViewById(R.id.item_film_sale__time);
        LinearLayout saleLayout = (LinearLayout) view.findViewById(R.id.item_film_sale__layout);
        final LinearLayout addLayout = (LinearLayout) view.findViewById(R.id.item_film_sale__add_layout);
        TextView saleLost = (TextView) view.findViewById(R.id.item_film_sale__lost);
        final TextView saleNum = (TextView) view.findViewById(R.id.item_film_sale__num);
        TextView saleAdd = (TextView) view.findViewById(R.id.item_film_sale__add);

        if (goodList.get(i).getGoodsImg() != null && !"".equals(goodList.get(i).getGoodsImg())) {
            Picasso.with(getBaseContext())
                    .load(goodList.get(i).getGoodsImg())
                    .into(saleImage);
        }
//        saleImage.setImageResource(R.mipmap.mihua);
        salePackage.setText(goodList.get(i).getGoodsName());
        salePackagePrice.setText("￥ " + ChangeUtils.save2Decimal(goodList.get(i).getPrice()));
        salePackageContent.setText(goodList.get(i).getGoodsDetail());
        if (goodList.get(i).getStartTime() != 0 && goodList.get(i).getEndTime() != 0) {
            saleTime.setText(ChangeUtils.changeTimeYear(goodList.get(i).getStartTime()) + "~" + ChangeUtils.changeTimeYear(goodList.get(i).getEndTime()));
        } else if (goodList.get(i).getStartTime() != 0 && goodList.get(i).getEndTime() == 0) {
            saleTime.setText(ChangeUtils.changeTimeYear(goodList.get(i).getStartTime()));
        }
        selectImage.setImageResource(R.mipmap.sale_no_select);
        addLayout.setVisibility(View.GONE);
        if (goodList.get(i).getSelected() == 1) {
            selectImage.setImageResource(R.mipmap.sale_select);
            addLayout.setVisibility(View.VISIBLE);
            saleNum.setText("1");
        }
        final int finalI = i;
        saleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(saleNum.getText().toString()) + 1;
                saleNum.setText(num + "");
                goodList.get(finalI).setNum(num);
                totlalPrice = totlalPrice + goodList.get(finalI).getPrice();
                payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
            }
        });
        saleLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(saleNum.getText().toString());
                if (num != 0)
                    num = num - 1;
                saleNum.setText(num + "");
                goodList.get(finalI).setNum(num);
                totlalPrice = totlalPrice - goodList.get(finalI).getPrice();
                payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
                if (num == 0) {
                    payPrice.setText("¥ " + (ChangeUtils.save2Decimal(totlalPrice)));
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    goodList.get(finalI).setSelected(0);
                    addLayout.setVisibility(View.GONE);
                }
            }
        });
        saleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (goodList.get(finalI).getSelected() == 1) {
                    totlalPrice = totlalPrice - goodList.get(finalI).getPrice() * goodList.get(finalI).getNum();
                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    goodList.get(finalI).setSelected(0);
                    addLayout.setVisibility(View.GONE);
                } else {
                    totlalPrice = totlalPrice + goodList.get(finalI).getPrice();
                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
                    selectImage.setImageResource(R.mipmap.sale_select);
                    goodList.get(finalI).setSelected(1);
                    goodList.get(finalI).setNum(1);
                    addLayout.setVisibility(View.VISIBLE);
                    saleNum.setText("1");
                }
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (goodList.get(finalI).getSelected() == 1) {
//                    totlalPrice = totlalPrice - goodList.get(finalI).getPrice() * goodList.get(finalI).getNum();
//                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
//                    selectImage.setImageResource(R.mipmap.sale_no_select);
//                    goodList.get(finalI).setSelected(0);
//                    addLayout.setVisibility(View.GONE);
//                } else {
//                    totlalPrice = totlalPrice - goodList.get(finalI).getPrice();
//                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
//                    selectImage.setImageResource(R.mipmap.sale_select);
//                    goodList.get(finalI).setSelected(1);
//                    goodList.get(finalI).setNum(1);
//                    addLayout.setVisibility(View.VISIBLE);
//                    saleNum.setText("1");
//                }
                if (goodList.get(finalI).getSelected() == 1) {
                    totlalPrice = totlalPrice - goodList.get(finalI).getPrice() * goodList.get(finalI).getNum();
                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    goodList.get(finalI).setSelected(0);
                    addLayout.setVisibility(View.GONE);
                } else {
                    totlalPrice = totlalPrice + goodList.get(finalI).getPrice();
                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
                    selectImage.setImageResource(R.mipmap.sale_select);
                    goodList.get(finalI).setSelected(1);
                    goodList.get(finalI).setNum(1);
                    addLayout.setVisibility(View.VISIBLE);
                    saleNum.setText("1");
                }
            }
        });

        addPackageLayout.addView(view);
        i++;
        initAddPackage(i);
    }

    private void showAllDatePopupWindow() {

        View parent = LayoutInflater.from(SaleCompleteActivity.this).inflate(R.layout.content_film_complete_seat, null, false);
        View view = LayoutInflater.from(SaleCompleteActivity.this).inflate(
                R.layout.popupwindow_film_sale, null, false);

        view.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                R.anim.fade_in));

        final PopupWindow mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(AppManager.getInstance().getWidth() - AppManager.getInstance().getWidth() / 6);
        mPopupWindow.setHeight(AppManager.getInstance().getHeight() - AppManager.getInstance().getHeight() / 4);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                resetAddPackage();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        ImageView cancel = (ImageView) view.findViewById(R.id.content_film_sale__cancel);
        listView = (RecyclerView) view.findViewById(R.id.content_film_sale__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SaleCompleteActivity.this);
        listView.setLayoutManager(linearLayoutManager);
//        initAdapter(listView);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        goodMoreLists = new ArrayList<>();
        pagable = "0";
        getGoodMore(pagable);
    }

    private void initAdapter(RecyclerView listView) {
//        filmSaleAdapter = new FilmSaleAdapter(allDate);
//        filmSaleAdapter.openLoadAnimation();
//        listView.setAdapter(filmSaleAdapter);
//        mCurrentCounter = filmSaleAdapter.getData().size();
////        filmSaleAdapter.setOnLoadMoreListener(this);
//        filmSaleAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
//        filmSaleAdapter.setOnRecyclerViewItemChildClickListener(this);
    }

    //获取更多卖品列表
    private void getGoodMore(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.GET_GOOD_MORE, new OkHttpClientManager.ResultCallback<GoodMoreList>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(GoodMoreList response) {
                if (response != null) {
                    goodMoreList = response.getGoodsList();
                    if (goodMoreList != null && goodMoreList.size() > 0) {
                        resetGoodMoreList(goodMoreList, pagables, response);
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, pagableRequest, GoodMoreList.class, Application.getInstance().getCurrentActivity());
    }

    /**
     * 重新整理数据
     */
    private void resetGoodMoreList(List<RecommendGoodsInfo> goodMoreList, String pagables, GoodMoreList response) {
        if (goodList != null) {
            for (int i = 0; i < goodList.size(); i++) {
                for (int j = 0; j < goodMoreList.size(); j++) {

                    if ((goodList.get(i).getGoodsId() + "").equals(goodMoreList.get(j).getGoodsId() + "")) {

                        if (goodList.get(i).getSelected() == 1) {
                            goodMoreList.get(j).setSelected(1);
                        } else {
                            goodMoreList.get(j).setSelected(0);
                        }

                    }
                }
            }
        }
        if ("0".equals(pagables)) {//第一页
            goodMoreLists.clear();
            goodMoreLists.addAll(goodMoreList);
            Log.i("ee", goodMoreList.size() + "----" + goodMoreLists.size());
            filmSaleAdapter = new FilmSaleAdapter(goodMoreList);
            filmSaleAdapter.openLoadAnimation();
            listView.setAdapter(filmSaleAdapter);
            filmSaleAdapter.openLoadMore(goodMoreList.size(), true);
            if (response.getHasMore() == 1) {
                hasMore = true;
            } else {
                hasMore = false;
            }
            pagable = response.getPagable();
        } else {
            goodMoreLists.addAll(goodMoreList);
            if (response.getHasMore() == 1) {
                hasMore = true;
                pagable = response.getPagable();
                filmSaleAdapter.notifyDataChangedAfterLoadMore(goodMoreList, true);
            } else {
                filmSaleAdapter.notifyDataChangedAfterLoadMore(goodMoreList, true);
                hasMore = false;
                pagable = "";
            }
        }
        filmSaleAdapter.setOnLoadMoreListener(SaleCompleteActivity.this);
        filmSaleAdapter.setOnRecyclerViewItemChildClickListener(SaleCompleteActivity.this);
    }

    private void resetAddPackage() {
        List<String> list = new ArrayList<String>();
        if (goodList != null) {
            for (int i = 0; i < goodList.size(); i++) {
                list.add(goodList.get(i).getGoodsId() + "");
            }
            for (int i = 0; i < goodMoreLists.size(); i++) {
                for (int j = 0; j < goodList.size(); j++) {
                    if ((goodMoreLists.get(i).getGoodsId() + "").equals(goodList.get(j).getGoodsId() + "") && goodMoreLists.get(i).getSelected() == 1) {
                        if (goodList.get(j).getSelected() == 0) {
                            goodList.get(j).setSelected(1);
                            totlalPrice = totlalPrice + goodList.get(j).getPrice();
                        }
                        break;
                    } else if (goodMoreLists.get(i).getSelected() == 1 && !list.contains(goodMoreLists.get(i).getGoodsId() + "")) {
                        goodList.add(goodMoreLists.get(i));
                        totlalPrice = totlalPrice + goodMoreLists.get(i).getPrice();
                        break;
                    } else if ((goodMoreLists.get(i).getGoodsId() + "").equals(goodList.get(j).getGoodsId() + "") && goodMoreLists.get(i).getSelected() == 0) {
                        if (goodList.get(j).getSelected() == 1) {
                            goodList.get(j).setSelected(0);
                            totlalPrice = totlalPrice - goodList.get(j).getPrice() * goodList.get(j).getNum();
                            goodList.get(j).setNum(1);
                        }
                        break;
                    }
                }
            }
        } else {
            goodList = new ArrayList<>();
            for (int i = 0; i < goodMoreLists.size(); i++) {
                if (goodMoreLists.get(i).getSelected() == 1) {
                    goodList.add(goodMoreLists.get(i));
                    totlalPrice = totlalPrice + goodMoreLists.get(i).getPrice();
                }
            }
        }
        payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
        addPackageLayout.removeAllViews();
        Log.i("ee", partDate.size() + "----------------" + addPackageLayout.getChildCount());
        initAddPackage(0);
    }

    //卖品下单接口
    private void confirmGoods(List<com.yyjlr.tickets.requestdata.confirmfilmorder.GoodInfo> goodInfoList, String phone) {
        customDialog.show();
        ConfirmGoods confirmGoods = new ConfirmGoods();
        confirmGoods.setData(goodInfoList);
        confirmGoods.setPhone(phone);
        OkHttpClientManager.postAsyn(Config.CONFIRM_GOODS, new OkHttpClientManager.ResultCallback<ConfirmOrderBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ConfirmOrderBean response) {
                customDialog.dismiss();
                if (response != null) {
                    startActivity(new Intent(getBaseContext(),
                            PaySelectActivity.class)
                            .putExtra("orderId", "")
                            .putExtra("orderBean", response));
                    SaleCompleteActivity.this.finish();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, confirmGoods, ConfirmOrderBean.class, SaleCompleteActivity.this);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        if (goodMoreLists.get(position).getSelected() == 1) {
            goodMoreLists.get(position).setSelected(0);
        } else {
            goodMoreLists.get(position).setSelected(1);
        }
        filmSaleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    filmSaleAdapter.notifyDataChangedAfterLoadMore(false);
//                    saleAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getGoodMore(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.base_toolbar__left:
                    SaleCompleteActivity.this.finish();
                    break;
                case R.id.content_sale_bill__delete_phone:
                    phone.setText("");
                    break;
                case R.id.content_sale_bill__confirm_order:
                    String num = phone.getText().toString().trim();
                    if (isMobileNum(num)) {
                        List<com.yyjlr.tickets.requestdata.confirmfilmorder.GoodInfo> goodInfoList = new ArrayList<>();
                        com.yyjlr.tickets.requestdata.confirmfilmorder.GoodInfo goods = new com.yyjlr.tickets.requestdata.confirmfilmorder.GoodInfo();
                        goods.setId(SaleCompleteActivity.this.goodInfo.getGoodsId() + "");
                        goods.setNum(SaleCompleteActivity.this.goodNum + "");
                        goodInfoList.add(goods);
                        if (goodList != null) {
                            for (int i = 0; i < goodList.size(); i++) {
                                if (goodList.get(i).getSelected() == 1) {
                                    com.yyjlr.tickets.requestdata.confirmfilmorder.GoodInfo goodInfo = new com.yyjlr.tickets.requestdata.confirmfilmorder.GoodInfo();
                                    goodInfo.setId(goodList.get(i).getGoodsId() + "");
                                    goodInfo.setNum(goodList.get(i).getNum() + "");
                                    goodInfoList.add(goodInfo);
                                }
                            }
                        }
                        confirmGoods(goodInfoList, num);

                    } else {
                        showShortToast("手机号码不对");
                    }
                    break;
                case R.id.content_sale_confirm__add:
                    showAllDatePopupWindow();
                    break;
            }
        }
    }

}
