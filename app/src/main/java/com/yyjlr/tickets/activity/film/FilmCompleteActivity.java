package com.yyjlr.tickets.activity.film;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.squareup.okhttp.Request;
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
import com.yyjlr.tickets.model.order.ChangePayTypeBean;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.confirmfilmorder.ConfirmFilmOrder;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/8/10.
 * 完成选座
 */
public class FilmCompleteActivity extends AbstractActivity implements BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private TextView payPrice;//应付金额
    private ImageView addPackage;//添加套餐
    private LinearLayout addPackageLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_complete_seat);
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
        phone = (EditText) findViewById(R.id.content_sale_bill__phone);
        deletePhone = (ImageView) findViewById(R.id.content_sale_bill__delete_phone);
        confirmOrder = (TextView) findViewById(R.id.content_sale_bill__confirm_order);
        addLayout = (RelativeLayout) findViewById(R.id.content_film_complete_seat__add_layout);
        discountLayout = (LinearLayout) findViewById(R.id.content_film_complete_seat__discount_layout);
        deletePhone.setOnClickListener(this);

        String phoneStr = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PHONE, "", FilmCompleteActivity.this);
        phone.setText(phoneStr);

        phone.addTextChangedListener(textWatcher);


        addLayout.setVisibility(View.GONE);
        addPackageLayout.setVisibility(View.GONE);
        initData();

        confirmOrder.setOnClickListener(this);
        addPackage.setOnClickListener(this);

        allDate = Application.getiDataService().getFileSaleList();
        partDate.add(allDate.get(0));
        partDate.add(allDate.get(1));

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
        for (int i = 0; i < movieOrderBean.getOrderInfo().getSeatInfos().length; i++) {
            if (i == movieOrderBean.getOrderInfo().getSeatInfos().length - 1) {
                seatStr = seatStr + movieOrderBean.getOrderInfo().getSeatInfos()[i];
            } else {
                seatStr = seatStr + movieOrderBean.getOrderInfo().getSeatInfos()[i] + ",";
            }
        }
        filmSeat.setText(seatStr);
        filmPrice.setText("单价：" + ChangeUtils.save2Decimal(movieOrderBean.getOrderInfo().getPrice()) + "元，共" + movieOrderBean.getOrderInfo().getNums() + "张");
        filmTotalPrice.setText("¥" + ChangeUtils.save2Decimal(movieOrderBean.getOrderInfo().getTotalPrice()));
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
        salePackage.setText(partDate.get(0).getSalePackage());
        salePackagePrice.setText("￥" + partDate.get(0).getSalePrice());
        salePackageContent.setText(partDate.get(0).getSalePackageContent());
//        saleDiscount.setText("");
        selectImage.setImageResource(R.mipmap.sale_no_select);
        saleParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(0).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(0).setSaleSelect(false);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(0).setSaleSelect(true);
                }
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(0).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(0).setSaleSelect(false);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(0).setSaleSelect(true);
                }
            }
        });

        addPackageLayout.addView(view);
    }


    //添加套餐
    private void initAddPackage(int i) {
        if (i >= partDate.size()) {
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

        saleImage.setImageResource(R.mipmap.mihua);
        salePackage.setText(partDate.get(i).getSalePackage());
        salePackagePrice.setText("￥" + partDate.get(i).getSalePrice());
        salePackageContent.setText(partDate.get(i).getSalePackageContent());
        saleTime.setText(partDate.get(i).getSaleTime());

        selectImage.setImageResource(R.mipmap.sale_no_select);
        addLayout.setVisibility(View.GONE);
        if (partDate.get(i).isSaleSelect()) {
            selectImage.setImageResource(R.mipmap.sale_select);
            addLayout.setVisibility(View.VISIBLE);
            saleNum.setText("1");
        }
        saleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(saleNum.getText().toString()) + 1;
                saleNum.setText(num + "");
            }
        });
        saleLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(saleNum.getText().toString());
                if (num != 0)
                    num = num - 1;
                saleNum.setText(num + "");
            }
        });
        final int finalI = i;
        saleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(finalI).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(finalI).setSaleSelect(false);
                    addLayout.setVisibility(View.GONE);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(finalI).setSaleSelect(true);
                    addLayout.setVisibility(View.VISIBLE);
                    saleNum.setText("1");
                }
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(finalI).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(finalI).setSaleSelect(false);
                    addLayout.setVisibility(View.GONE);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(finalI).setSaleSelect(true);
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
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.content_film_sale__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FilmCompleteActivity.this);
        listView.setLayoutManager(linearLayoutManager);
        initAdapter(listView);
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

    }

    private void initAdapter(RecyclerView listView) {
        filmSaleAdapter = new FilmSaleAdapter(allDate);
        filmSaleAdapter.openLoadAnimation();
        listView.setAdapter(filmSaleAdapter);
        mCurrentCounter = filmSaleAdapter.getData().size();
//        filmSaleAdapter.setOnLoadMoreListener(this);
        filmSaleAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        filmSaleAdapter.setOnRecyclerViewItemChildClickListener(this);
    }

    private void restAddPackage() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < partDate.size(); i++) {
            list.add(partDate.get(i).getSaleId());
        }
        for (int i = 0; i < allDate.size(); i++) {
            for (int j = 0; j < partDate.size(); j++) {
                if (allDate.get(i).getSaleId().equals(partDate.get(j).getSaleId()) && allDate.get(i).isSaleSelect()) {
                    partDate.get(j).setSaleSelect(true);
                    break;
                } else if (allDate.get(i).isSaleSelect() && !list.contains(allDate.get(i).getSaleId())) {
                    partDate.add(allDate.get(i));
                    break;
                }
            }
        }
        addPackageLayout.removeAllViews();
        Log.i("ee", partDate.size() + "----------------" + addPackageLayout.getChildCount());
        initAddPackage(0);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        if (allDate.get(position).isSaleSelect()) {
            allDate.get(position).setSaleSelect(false);
        } else {
            allDate.get(position).setSaleSelect(true);
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
        OkHttpClientManager.postAsyn(Config.CONFIRM_ORDER, new OkHttpClientManager.ResultCallback<ChangePayTypeBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ChangePayTypeBean response) {
                customDialog.dismiss();
                startActivity(new Intent(getBaseContext(), PaySelectActivity.class)
                        .putExtra("orderId", movieOrderBean.getOrderInfo().getId() + ""));
                Application.getInstance().finishActivity((AbstractActivity) FilmSelectSeatActivity.activity);
                Application.getInstance().finishActivity(FilmCompleteActivity.this);
                // FilmCompleteActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, confirmFilmOrder, ChangePayTypeBean.class, FilmCompleteActivity.this);
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
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
                customDialog.dismiss();
                FilmCompleteActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
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
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                showCancelOrder();
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
