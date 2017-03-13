package com.yyjlr.tickets.activity.film;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.order.AddMovieOrderBean;
import com.yyjlr.tickets.model.order.ConfirmOrderBean;
import com.yyjlr.tickets.model.sale.GoodMoreList;
import com.yyjlr.tickets.model.sale.RecommendGoodsInfo;
import com.yyjlr.tickets.model.sale.RecommendList;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.requestdata.confirmfilmorder.ConfirmFilmOrder;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/10.
 * 完成选座
 */
public class FilmCompleteActivity extends AbstractActivity implements BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener, BaseAdapter.RequestLoadMoreListener {

    private TextView title;
    private ImageView leftArrow;
    private TextView payPrice;//应付金额
    private ImageView addPackage;//添加套餐
    private LinearLayout addPackageLayout;
    private View addPackageLine;
    private EditText phone;
    private TextView confirmOrder;
    private ImageView deletePhone;//清除号码

    private List<FilmSaleEntity> allDate;
    private List<FilmSaleEntity> partDate = new ArrayList<FilmSaleEntity>();
    private FilmSaleAdapter filmSaleAdapter;

    private RelativeLayout addLayout;
    private LinearLayout discountLayout;
    private AddMovieOrderBean movieOrderBean;
    //电影名称 类型语言 日期 时间 几号厅 座位 单价共几张 总价
    private TextView filmName, filmType, filmDate, filmTime, filmSeat, filmHall, filmPrice, filmTotalPrice;

    private RecyclerView listView;
    private List<RecommendGoodsInfo> goodList;
    private List<RecommendGoodsInfo> goodMoreList;
    private List<RecommendGoodsInfo> goodMoreLists;
    private String pagable = "0";
    private boolean hasMore = false;
    private long totlalPrice = 0;
    private String orderId = "";
    private boolean isNoPay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_complete_seat);
        customDialog = new CustomDialog(this, "加载中...");
        orderId = getIntent().getStringExtra("orderId");
        isNoPay = getIntent().getBooleanExtra("isNoPay", false);
        movieOrderBean = (AddMovieOrderBean) getIntent().getSerializableExtra("movieOrderBean");
        AppManager.getInstance().initWidthHeight(getBaseContext());
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("完成选座");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        //头部的电影票信息
        filmName = (TextView) findViewById(R.id.content_film_complete_seat__film_name);
        filmType = (TextView) findViewById(R.id.content_film_complete_seat__type);
        filmDate = (TextView) findViewById(R.id.content_film_complete_seat__date);
        filmTime = (TextView) findViewById(R.id.content_film_complete_seat__time);
        filmSeat = (TextView) findViewById(R.id.content_film_complete_seat__seat);
        filmHall = (TextView) findViewById(R.id.content_film_complete_seat__hall);
        filmPrice = (TextView) findViewById(R.id.content_film_complete_seat__unit_price);
        filmTotalPrice = (TextView) findViewById(R.id.content_film_complete_seat__price);


        payPrice = (TextView) findViewById(R.id.content_film_complete_seat__pay_price);
        addPackage = (ImageView) findViewById(R.id.content_film_complete_seat__add);
        addPackageLayout = (LinearLayout) findViewById(R.id.content_film_complete_seat__sale_layout);
        addPackageLine = findViewById(R.id.content_film_complete_seat__sale_line);
        phone = (EditText) findViewById(R.id.content_sale_bill__phone);
        deletePhone = (ImageView) findViewById(R.id.content_sale_bill__delete_phone);
        confirmOrder = (TextView) findViewById(R.id.content_sale_bill__confirm_order);
        addLayout = (RelativeLayout) findViewById(R.id.content_film_complete_seat__add_layout);
        discountLayout = (LinearLayout) findViewById(R.id.content_film_complete_seat__discount_layout);
        deletePhone.setOnClickListener(this);

        String phoneStr = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PHONE, "", FilmCompleteActivity.this);
        phone.setText(phoneStr);
        phone.setSelection(phoneStr.length());
        phone.addTextChangedListener(textWatcher);


        addLayout.setVisibility(View.GONE);
        addPackageLayout.setVisibility(View.GONE);
        addPackageLine.setVisibility(View.GONE);
        if (movieOrderBean != null) {
            initData();
        } else {
            getOrderInfo();
        }

        confirmOrder.setOnClickListener(this);
        addPackage.setOnClickListener(this);

        allDate = Application.getiDataService().getFileSaleList();
        partDate.add(allDate.get(0));
        partDate.add(allDate.get(1));
//        getPackageRecommend();

        //卖品
//        if (flag) {
//            discountLayout.setVisibility(View.VISIBLE);
//            addLayout.setVisibility(View.GONE);
//            initAddDiscountPackage();
//        } else {
//            discountLayout.setVisibility(View.GONE);
//            addLayout.setVisibility(View.VISIBLE);
//            initAddPackage(0);
//        }
//        flag = !flag;
    }


    private void initData() {
        filmName.setText(movieOrderBean.getOrderInfo().getMovieName());
        filmDate.setText(movieOrderBean.getOrderInfo().getPlayDate());
        filmTime.setText(movieOrderBean.getOrderInfo().getStartTime() + "~" + movieOrderBean.getOrderInfo().getEndTime());
        filmType.setText(movieOrderBean.getOrderInfo().getLanguage() + movieOrderBean.getOrderInfo().getMovieType());
        filmHall.setText(movieOrderBean.getOrderInfo().getHallName());
        String seatStr = "";
        if (movieOrderBean.getOrderInfo().getSeatInfos() != null && movieOrderBean.getOrderInfo().getSeatInfos().length > 0) {
            for (int i = 0; i < movieOrderBean.getOrderInfo().getSeatInfos().length; i++) {
                if (i == movieOrderBean.getOrderInfo().getSeatInfos().length - 1) {
                    seatStr = seatStr + movieOrderBean.getOrderInfo().getSeatInfos()[i];
                } else {
                    seatStr = seatStr + movieOrderBean.getOrderInfo().getSeatInfos()[i] + ",";
                }
            }
        }
        filmSeat.setText(seatStr);
        filmPrice.setText("单价：" + ChangeUtils.save2Decimal(movieOrderBean.getOrderInfo().getPrice())
                + "元，共" + movieOrderBean.getOrderInfo().getNums() + "张");
        filmTotalPrice.setText("¥" + ChangeUtils.save2Decimal(movieOrderBean.getOrderInfo().getTotalPrice()));
        totlalPrice = movieOrderBean.getOrderInfo().getTotalPrice();
        payPrice.setText("¥" + ChangeUtils.save2Decimal(movieOrderBean.getOrderInfo().getTotalPrice()));

        if (movieOrderBean.getGoods().size() > 0) {

        }
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
            } else {
                deletePhone.setVisibility(View.VISIBLE);
            }
        }
    };

    //获取待处理订单详情
    private void getOrderInfo() {
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId);
        OkHttpClientManager.postAsyn(Config.GET_UNPAID_DETAIL, new OkHttpClientManager.ResultCallback<AddMovieOrderBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(AddMovieOrderBean response) {

                if (response != null) {
                    movieOrderBean = response;
                    initData();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, idRequest, AddMovieOrderBean.class, FilmCompleteActivity.this);
    }


    //添加优惠活动产品
    private void initAddDiscountPackage() {
        View view = LayoutInflater.from(FilmCompleteActivity.this).inflate(R.layout.item_film_sale_package_, null, false);
        final ImageView selectImage = (ImageView) view.findViewById(R.id.item_film_sale__select);
        ImageView saleImage = (ImageView) view.findViewById(R.id.item_film_sale__image);
        TextView salePackage = (TextView) view.findViewById(R.id.item_film_sale__package);
        TextView salePackagePrice = (TextView) view.findViewById(R.id.item_film_sale__price);
        TextView salePackageContent = (TextView) view.findViewById(R.id.item_film_sale__package_content);
        TextView saleDiscount = (TextView) view.findViewById(R.id.item_film_sale__discount);
        LinearLayout saleParent = (LinearLayout) view.findViewById(R.id.item_film_sale__layout);

        saleImage.setImageResource(R.mipmap.mihua);
        salePackage.setText(goodList.get(0).getGoodsName());
        salePackagePrice.setText("￥ " + ChangeUtils.save2Decimal(goodList.get(0).getAppPrice()));
        salePackageContent.setText(goodList.get(0).getGoodsDetail());
//        saleDiscount.setText("");
        selectImage.setImageResource(R.mipmap.sale_no_select);
        saleParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (goodList.get(0).getSelected() == 1) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    goodList.get(0).setSelected(0);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    goodList.get(0).setSelected(1);
                }
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (goodList.get(0).getSelected() == 1) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    goodList.get(0).setSelected(0);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    goodList.get(0).setSelected(1);
                }
            }
        });

        addPackageLayout.addView(view);
    }

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
                if (goodList.size() > 0) {
                    discountLayout.setVisibility(View.GONE);
                    addLayout.setVisibility(View.VISIBLE);
                    addPackageLayout.setVisibility(View.VISIBLE);
                    addPackageLine.setVisibility(View.VISIBLE);
                    initAddPackage(0);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, requestNull, RecommendList.class, FilmCompleteActivity.this);
    }


    //添加套餐
    private void initAddPackage(int i) {
        if (i >= goodList.size()) {
            return;
        }
        View view = LayoutInflater.from(FilmCompleteActivity.this).inflate(R.layout.item_film_sale_package, null, false);
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
        salePackagePrice.setText("￥ " + ChangeUtils.save2Decimal(goodList.get(i).getAppPrice()));
        salePackageContent.setText(goodList.get(i).getGoodsDetail());
        saleTime.setText(ChangeUtils.changeTimeYear(goodList.get(i).getStartTime()) + "~" + ChangeUtils.changeTimeYear(goodList.get(i).getStartTime()));


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
                totlalPrice = totlalPrice + goodList.get(finalI).getAppPrice();
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
                totlalPrice = totlalPrice - goodList.get(finalI).getAppPrice();
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
                    totlalPrice = totlalPrice - goodList.get(finalI).getAppPrice() * goodList.get(finalI).getNum();
                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    goodList.get(finalI).setSelected(0);
                    addLayout.setVisibility(View.GONE);
                } else {
                    totlalPrice = totlalPrice + goodList.get(finalI).getAppPrice();
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

                if (goodList.get(finalI).getSelected() == 1) {
                    totlalPrice = totlalPrice - goodList.get(finalI).getAppPrice() * goodList.get(finalI).getNum();
                    payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    goodList.get(finalI).setSelected(0);
                    addLayout.setVisibility(View.GONE);
                } else {
                    totlalPrice = totlalPrice - goodList.get(finalI).getAppPrice();
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

        View parent = LayoutInflater.from(FilmCompleteActivity.this).inflate(R.layout.content_film_complete_seat, null, false);
        View view = LayoutInflater.from(FilmCompleteActivity.this).inflate(
                R.layout.popupwindow_film_sale, null, false);

        view.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                R.anim.fade_in));
//        RelativeLayout ll_popup = (RelativeLayout) view
//                .findViewById(R.id.ll_popup);
//        ll_popup.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
//                R.anim.fade_out));

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
        getWindow().setAttributes(lp);
        mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                restAddPackage();
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });


        ImageView cancel = (ImageView) view.findViewById(R.id.content_film_sale__cancel);
        listView = (RecyclerView) view.findViewById(R.id.content_film_sale__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FilmCompleteActivity.this);
        listView.setLayoutManager(linearLayoutManager);
//        initAdapter(listView);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                restAddPackage();
                mPopupWindow.dismiss();
            }
        });
        goodMoreLists = new ArrayList<>();
        pagable = "0";
        getGoodMore(pagable);
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
                    if (goodMoreList != null) {
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


//    private void initAdapter(RecyclerView listView) {
//        filmSaleAdapter = new FilmSaleAdapter(allDate);
//        filmSaleAdapter.openLoadAnimation();
//        listView.setAdapter(filmSaleAdapter);
//        mCurrentCounter = filmSaleAdapter.getData().size();
////        filmSaleAdapter.setOnLoadMoreListener(this);
//        filmSaleAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
//        filmSaleAdapter.setOnRecyclerViewItemChildClickListener(this);
//    }

    /**
     * 重新整理数据
     */
    private void resetGoodMoreList(List<RecommendGoodsInfo> goodMoreList, String pagables, GoodMoreList response) {
        for (int i = 0; i < goodList.size(); i++) {
            for (int j = 0; j < goodMoreList.size(); j++) {
                if (goodList.get(i).getGoodsId() == goodMoreList.get(j).getGoodsId() && goodList.get(i).getSelected() == 1) {
                    goodMoreList.get(j).setSelected(1);
                    break;
                } else if (goodList.get(i).getGoodsId() == goodMoreList.get(j).getGoodsId() && goodList.get(i).getSelected() == 0) {
                    goodMoreList.get(j).setSelected(0);
                    break;
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
        filmSaleAdapter.setOnLoadMoreListener(FilmCompleteActivity.this);
        filmSaleAdapter.setOnRecyclerViewItemChildClickListener(FilmCompleteActivity.this);
    }

    private void restAddPackage() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < goodList.size(); i++) {
            list.add(goodList.get(i).getGoodsId() + "");
        }
        for (int i = 0; i < goodMoreLists.size(); i++) {
            for (int j = 0; j < goodList.size(); j++) {
                if (goodMoreLists.get(i).getGoodsId() == goodList.get(j).getGoodsId() && goodMoreLists.get(i).getSelected() == 1) {
                    if (goodList.get(j).getSelected() == 0) {
                        goodList.get(j).setSelected(1);
                        totlalPrice = totlalPrice + goodList.get(j).getAppPrice();
                    }
                    break;
                } else if (goodMoreLists.get(i).getSelected() == 1 && !list.contains(goodMoreLists.get(i).getGoodsId() + "")) {
                    goodList.add(goodMoreLists.get(i));
                    totlalPrice = totlalPrice + goodMoreLists.get(i).getAppPrice();
                    break;
                } else if (goodMoreLists.get(i).getGoodsId() == goodList.get(j).getGoodsId() && goodMoreLists.get(i).getSelected() == 0) {
                    if (goodList.get(j).getSelected() == 1) {
                        goodList.get(j).setSelected(0);
                        totlalPrice = totlalPrice - goodList.get(j).getAppPrice() * goodList.get(j).getNum();
                        goodList.get(j).setNum(1);
                    }
                    break;
                }
            }
        }
        payPrice.setText("¥ " + ChangeUtils.save2Decimal(totlalPrice));
        addPackageLayout.removeAllViews();
        Log.i("ee", partDate.size() + "----------------" + addPackageLayout.getChildCount());
        initAddPackage(0);
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


    //确认订单
    private void confirmOrder() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        ConfirmFilmOrder confirmFilmOrder = new ConfirmFilmOrder();
        confirmFilmOrder.setPhone(phone.getText().toString().trim());
        confirmFilmOrder.setOrderId(movieOrderBean.getOrderInfo().getId() + "");
        OkHttpClientManager.postAsyn(Config.CONFIRM_ORDER, new OkHttpClientManager.ResultCallback<ConfirmOrderBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ConfirmOrderBean response) {
                customDialog.dismiss();
                startActivity(new Intent(getBaseContext(),
                        PaySelectActivity.class)
                        .putExtra("orderId", "")
                        .putExtra("orderBean", response));
                if (FilmSelectSeatActivity.activity != null) {
                    Application.getInstance().finishActivity((AbstractActivity) FilmSelectSeatActivity.activity);
                }
                Application.getInstance().finishActivity(FilmCompleteActivity.this);
                // FilmCompleteActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, confirmFilmOrder, ConfirmOrderBean.class, FilmCompleteActivity.this);
    }

    //取消订单
    private void cancelOrder() {
        customDialog = new CustomDialog(this, "请稍后...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(movieOrderBean.getOrderInfo().getId() + "");
        OkHttpClientManager.postAsyn(Config.CANCEL_ORDER, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
                customDialog.dismiss();
                FilmCompleteActivity.this.finish();
                startActivity(new Intent(getBaseContext(), FilmSelectSeatActivity.class)
                        .putExtra("planId", getIntent().getStringExtra("planId"))
                        .putExtra("isFirst", false));

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, FilmCompleteActivity.this);
    }

    /**
     * show Dialog 是否确定 取消订单
     */
    private void showCancelOrder() {
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
        message.setText("是否取消此订单?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消订单
                cancelOrder();
                builder.dismiss();

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
                    if (isNoPay) {
                        FilmCompleteActivity.this.finish();
                    } else {
                        showCancelOrder();
                    }
                    break;
                case R.id.content_sale_bill__delete_phone:
                    phone.setText("");
                    break;
                case R.id.content_sale_bill__confirm_order:
                    String num = phone.getText().toString().trim();
                    if (isMobileNum(num)) {
                        confirmOrder();
                    } else {
                        showShortToast("手机号码不对");
                    }
                    break;
                case R.id.content_film_complete_seat__add:
                    showAllDatePopupWindow();
                    break;
            }
        }
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
}
