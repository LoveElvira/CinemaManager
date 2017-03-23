package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.PaySelectActivity;
import com.yyjlr.tickets.activity.film.FilmCompleteActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.order.AddMovieOrderBean;
import com.yyjlr.tickets.model.order.ConfirmOrderBean;
import com.yyjlr.tickets.model.order.GoodsOrderListInfo;
import com.yyjlr.tickets.model.order.MovieOrderDetailInfo;
import com.yyjlr.tickets.model.order.OrderDetailBean;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/11.
 * 订单详情
 */
public class SettingOrderDetailsActivity extends AbstractActivity implements View.OnClickListener {
    private TextView title;
    private ImageView leftArrow;
    private LinearLayout saleLayout;
    private LinearLayout moreLayout;
    private ImageView moreImage;
    private TextView moreText;
    private boolean flag = false;

    private TextView orderNum;//订单号
    private TextView statusText;//状态
    //电影名称 日期 时间 类型 几号厅 座位 电话 图片
    private TextView filmName, filmDate, filmTime, filmType, filmHall, filmSeat, filmPhone;
    private ImageView filmImage;
    private TextView getFilmNum;//电影取票码
    private TextView getFilmCode;//电影序列号
    private TextView getGoodNum;//套餐取货码
    private TextView getGoodCode;//套餐验证码

    private OrderDetailBean orderDetailBean;
    private LinearLayout bottomPayLayout;//底部支付界面
    private TextView price;
    private TextView confirmPay;

    //goodCodeLayout卖品取货码
    private LinearLayout filmLayout, goodLayout, ticketCodeLayout, filmCodeLayout, goodNumLayout, goodCodeLayout;
    //付款电话 付款方式 付款时间 支付金额
    private TextView payPhone, payType, payTime, payPrice;
    private int status = -1;//订单状态
    private LinearLayout payLayout;//支付信息
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        orderId = getIntent().getStringExtra("orderId");
        initView();
        getOrderInfo();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("订单详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        orderNum = (TextView) findViewById(R.id.item_order_details__order_num);
        statusText = (TextView) findViewById(R.id.item_order_details__flag);

        filmName = (TextView) findViewById(R.id.content_order_details__film_name);
        filmDate = (TextView) findViewById(R.id.content_order_details__film_date);
        filmTime = (TextView) findViewById(R.id.content_order_details__film_time);
        filmType = (TextView) findViewById(R.id.content_order_details__film_type);
        filmHall = (TextView) findViewById(R.id.content_order_details__film_hall);
        filmSeat = (TextView) findViewById(R.id.content_order_details__film_seat);
        filmPhone = (TextView) findViewById(R.id.content_order_details__film_phone);
        filmImage = (ImageView) findViewById(R.id.content_order_details__film_image);
        getFilmNum = (TextView) findViewById(R.id.content_order_details__film_ticket_code);
        getFilmCode = (TextView) findViewById(R.id.content_order_details__film_code);
        getGoodNum = (TextView) findViewById(R.id.content_order_details__package_ticket_code);
        getGoodCode = (TextView) findViewById(R.id.content_order_details__package_code);

        saleLayout = (LinearLayout) findViewById(R.id.content_order_details__package_layout);
        moreLayout = (LinearLayout) findViewById(R.id.item_order_sale_details__more_layout);
        ticketCodeLayout = (LinearLayout) findViewById(R.id.content_order_details__film_ticket_code_layout);
        filmCodeLayout = (LinearLayout) findViewById(R.id.content_order_details__film_code_layout);
        goodNumLayout = (LinearLayout) findViewById(R.id.content_order_details__package_code_layout);
        goodCodeLayout = (LinearLayout) findViewById(R.id.content_order_details__package_ticket_code_layout);
        moreImage = (ImageView) findViewById(R.id.item_order_sale_details_more__down);
        moreText = (TextView) findViewById(R.id.item_order_sale_details_more__text);

        filmLayout = (LinearLayout) findViewById(R.id.content_order_details_film);
        goodLayout = (LinearLayout) findViewById(R.id.content_order_details_package);
        payLayout = (LinearLayout) findViewById(R.id.item_order_details__pay_layout);
        goodLayout.setVisibility(View.GONE);
        payLayout.setVisibility(View.GONE);
        filmLayout.setVisibility(View.GONE);
        moreLayout.setVisibility(View.GONE);

        payPhone = (TextView) findViewById(R.id.item_order_details__pay_phone);
        payType = (TextView) findViewById(R.id.item_order_details__pay_way);
        payTime = (TextView) findViewById(R.id.item_order_details__pay_time);
        payPrice = (TextView) findViewById(R.id.item_order_details__pay_price);

        bottomPayLayout = (LinearLayout) findViewById(R.id.content_order_details__bottom_layout);
        price = (TextView) findViewById(R.id.content_order_details__confirm_price);
        confirmPay = (TextView) findViewById(R.id.content_order_details__confirm_pay);
        bottomPayLayout.setVisibility(View.GONE);

        moreLayout.setOnClickListener(this);
        confirmPay.setOnClickListener(this);

//        initSaleList(4, flag);
    }

    private void showStatus() {
        // 订单状态，1：待支付；2：待出票；3：已完成；4：用户取消；5：待退款；6：已退款；7：购买卖品失败；8：出票失败；9：超时失效
        switch (status) {
            case 1:
                statusText.setText("待支付");
                bottomPayLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                statusText.setText("待出票");
                payLayout.setVisibility(View.VISIBLE);
                break;
            case 3:
                statusText.setText("交易完成");
                payLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                statusText.setText("已取消");
                break;
            case 5:
                statusText.setText("待退款");
                payLayout.setVisibility(View.VISIBLE);
                break;
            case 6:
                statusText.setText("已退款");
                payLayout.setVisibility(View.VISIBLE);
                break;
            case 7:
                statusText.setText("购买失败");
                payLayout.setVisibility(View.VISIBLE);
                break;
            case 8:
                statusText.setText("出票失败");
                payLayout.setVisibility(View.VISIBLE);
                break;
            case 9:
                statusText.setText("超时失效");
                break;
        }

    }

    private void initDate() {

        orderNum.setText(orderDetailBean.getOrderNo());
        status = orderDetailBean.getStatus();
        showStatus();

        if (orderDetailBean.getMovieDetail() != null) {
            filmLayout.setVisibility(View.VISIBLE);
            MovieOrderDetailInfo movieOrderDetailInfo = orderDetailBean.getMovieDetail();
            filmName.setText(movieOrderDetailInfo.getMovieName());
            filmDate.setText(ChangeUtils.changeTimeYear(movieOrderDetailInfo.getStartTime()));
            filmTime.setText(ChangeUtils.changeTimeTime(movieOrderDetailInfo.getStartTime()) + "~" + ChangeUtils.changeTimeTime(movieOrderDetailInfo.getEndTime()));
            filmType.setText("(" + movieOrderDetailInfo.getLanguage() + movieOrderDetailInfo.getMovieType() + ")");
            filmHall.setText("(" + movieOrderDetailInfo.getCinemaName() + ")" + movieOrderDetailInfo.getHallName());
            String seatStr = "";
            for (int i = 0; i < movieOrderDetailInfo.getSeatInfo().size(); i++) {
                if (i == movieOrderDetailInfo.getSeatInfo().size() - 1) {
                    seatStr = seatStr + movieOrderDetailInfo.getSeatInfo().get(i);
                } else {
                    seatStr = seatStr + movieOrderDetailInfo.getSeatInfo().get(i) + ",";
                }
            }
            filmSeat.setText(seatStr);
            filmPhone.setText(movieOrderDetailInfo.getPhone());
            if (movieOrderDetailInfo.getValidCode() != null && !"".equals(movieOrderDetailInfo.getValidCode())) {
                ticketCodeLayout.setVisibility(View.VISIBLE);
                getFilmNum.setText(movieOrderDetailInfo.getValidCode());
            }

            if (movieOrderDetailInfo.getSerialNumber() != null && !"".equals(movieOrderDetailInfo.getSerialNumber())) {
                filmCodeLayout.setVisibility(View.VISIBLE);
                getFilmCode.setText(movieOrderDetailInfo.getValidCode());
            }

            if (movieOrderDetailInfo.getMovieImg() != null && !"".equals(movieOrderDetailInfo.getMovieImg())) {
                Picasso.with(getBaseContext())
                        .load(movieOrderDetailInfo.getMovieImg())
                        .into(filmImage);
            }

        }

        if (orderDetailBean.getGoodsDetail() != null) {
            if (orderDetailBean.getGoodsDetail().getTicketCode() != null
                    && !"".equals(orderDetailBean.getGoodsDetail().getTicketCode())) {
                goodCodeLayout.setVisibility(View.VISIBLE);
                getGoodNum.setText(orderDetailBean.getGoodsDetail().getTicketCode());
            }
            if (orderDetailBean.getGoodsDetail().getTicketNo() != null
                    && !"".equals(orderDetailBean.getGoodsDetail().getTicketNo())) {
                goodNumLayout.setVisibility(View.VISIBLE);
                getGoodCode.setText(orderDetailBean.getGoodsDetail().getTicketNo());
            }
//            getGoodNum.setText(orderDetailBean.getGoodsDetail().getFetchCode());

            if (orderDetailBean.getGoodsDetail().getGoodsList() != null && orderDetailBean.getGoodsDetail().getGoodsList().size() > 0) {
                goodLayout.setVisibility(View.VISIBLE);
                initSaleList(orderDetailBean.getGoodsDetail().getGoodsList(), flag);
            }
        }

        if (!"".equals(orderDetailBean.getPayPhone()) && orderDetailBean.getPayPhone() != null) {
            payPhone.setText(orderDetailBean.getPayPhone());
        } else {
            payPhone.setText("无");
        }
        if (!"".equals(orderDetailBean.getPayType()) && orderDetailBean.getPayType() != null) {
            payType.setText(orderDetailBean.getPayType());
        } else {
            payType.setText("无");
        }
        if (orderDetailBean.getPayTime() != 0 && orderDetailBean.getPayTime() != null) {
            payTime.setText(ChangeUtils.changeTime(orderDetailBean.getPayTime()));
        } else {
            payTime.setText("无");

        }
        if (!"".equals(orderDetailBean.getPayMoney())) {
            payPrice.setText(ChangeUtils.save2Decimal(orderDetailBean.getPayMoney()));
            price.setText(ChangeUtils.save2Decimal(orderDetailBean.getPayMoney()));
        }
    }


    //卖品列表
    private void initSaleList(List<GoodsOrderListInfo> goodsList, boolean flag) {
        saleLayout.removeAllViews();
        int num = goodsList.size();
        if (num <= 2) {
            moreLayout.setVisibility(View.GONE);
        } else {
            moreLayout.setVisibility(View.VISIBLE);
        }
        if (!flag && goodsList.size() > 2) {
            num = 2;
        }

        for (int i = 0; i < num; i++) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_order_sale_details, null);
            TextView salePackageName = (TextView) view.findViewById(R.id.item_order_sale_details__package_name);
            TextView salePackageContent = (TextView) view.findViewById(R.id.item_order_sale_details__package_content);
            TextView saleNum = (TextView) view.findViewById(R.id.item_order_sale_details__package_num);
            ImageView salePackageImage = (ImageView) view.findViewById(R.id.item_order_sale_details__package_image);
            salePackageName.setText(goodsList.get(i).getGoodsName());
            salePackageContent.setText(goodsList.get(i).getGoodsDesc());
            saleNum.setText("x" + goodsList.get(i).getCount());
            if (goodsList.get(i).getGoodsImg() != null && !"".equals(goodsList.get(i).getGoodsImg())) {
                Picasso.with(getBaseContext())
                        .load(goodsList.get(i).getGoodsImg())
                        .into(salePackageImage);
            }

            saleLayout.addView(view);
        }
    }

    //获取订单详情
    private void getOrderInfo() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId);
        OkHttpClientManager.postAsyn(Config.GET_MY_ORDER_INFO, new OkHttpClientManager.ResultCallback<OrderDetailBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(OrderDetailBean response) {
                customDialog.dismiss();
                orderDetailBean = response;
                if (orderDetailBean != null) {
                    initDate();
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, OrderDetailBean.class, SettingOrderDetailsActivity.this);
    }


    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.base_toolbar__left:
                    SettingOrderDetailsActivity.this.finish();
                    break;
                case R.id.item_order_sale_details__more_layout:
                    if (!flag) {
                        moreImage.setImageResource(R.mipmap.more_up);
                        moreText.setText("隐藏部分卖品");
                        flag = true;
                    } else if (flag) {
                        moreImage.setImageResource(R.mipmap.more_down);
                        moreText.setText("显示更多卖品");
                        flag = false;
                    }

                    initSaleList(orderDetailBean.getGoodsDetail().getGoodsList(), flag);

                    break;
                case R.id.content_order_details__confirm_pay:
                    if (orderDetailBean.getOrderType() == 1) {
                        AddMovieOrderBean movieOrderBean = null;
                        startActivity(new Intent(SettingOrderDetailsActivity.this,
                                FilmCompleteActivity.class)
                                .putExtra("orderId", orderId)
                                .putExtra("movieOrderBean", movieOrderBean)
                                .putExtra("isNoPay", true));
                    } else {
                        ConfirmOrderBean confirmOrderBean = null;
                        startActivity(new Intent(SettingOrderDetailsActivity.this,
                                PaySelectActivity.class)
                                .putExtra("orderId", orderId)
                                .putExtra("orderBean", confirmOrderBean));
                    }

                    break;
            }
        }
    }
}
