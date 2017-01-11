package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.order.GoodsOrderListInfo;
import com.yyjlr.tickets.model.order.MovieOrderDetailInfo;
import com.yyjlr.tickets.model.order.OrderDetailBean;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

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
    private TextView getGoodNum;//套餐取票码

    private OrderDetailBean orderDetailBean;

    private LinearLayout goodLayout, ticketCodeLayout;
    //付款电话 付款方式 付款时间 支付金额
    private TextView payPhone, payType, payTime, payPrice;
    private int status = -1;//订单状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        status = getIntent().getIntExtra("status", -1);
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
        getGoodNum = (TextView) findViewById(R.id.content_order_details__package_ticket_code);

        saleLayout = (LinearLayout) findViewById(R.id.content_order_details__package_layout);
        moreLayout = (LinearLayout) findViewById(R.id.item_order_sale_details__more_layout);
        ticketCodeLayout = (LinearLayout) findViewById(R.id.content_order_details__film_ticket_code_layout);
        moreImage = (ImageView) findViewById(R.id.item_order_sale_details_more__down);
        moreText = (TextView) findViewById(R.id.item_order_sale_details_more__text);

        goodLayout = (LinearLayout) findViewById(R.id.content_order_details_package);
        goodLayout.setVisibility(View.GONE);

        payPhone = (TextView) findViewById(R.id.item_order_details__pay_phone);
        payType = (TextView) findViewById(R.id.item_order_details__pay_way);
        payTime = (TextView) findViewById(R.id.item_order_details__pay_time);
        payPrice = (TextView) findViewById(R.id.item_order_details__pay_price);

        moreLayout.setOnClickListener(this);
        showStatus();

//        initSaleList(4, flag);
    }

    private void showStatus() {
        // 订单状态，1：待支付；2：待出票；3：已完成；4：用户取消；5：待退款；6：已退款；7：购买卖品失败；8：出票失败；9：超时失效
        switch (status) {
            case 1:
                statusText.setText("待支付");
                break;
            case 2:
                statusText.setText("待出票");
                break;
            case 3:
                statusText.setText("交易完成");
                break;
            case 4:
                statusText.setText("用户已取消");
                break;
            case 5:
                statusText.setText("待退款");
                break;
            case 6:
                statusText.setText("已退款");
                break;
            case 7:
                statusText.setText("购买卖品失败");
                break;
            case 8:
                statusText.setText("出票失败");
                break;
            case 9:
                statusText.setText("超时失效");
                break;
        }

    }

    private void initDate() {

        MovieOrderDetailInfo movieOrderDetailInfo = orderDetailBean.getMovieDetail();
        orderNum.setText(orderDetailBean.getOrderNo());
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

        if (movieOrderDetailInfo.getMovieImg() != null && !"".equals(movieOrderDetailInfo.getMovieImg())) {
            Picasso.with(getBaseContext())
                    .load(movieOrderDetailInfo.getMovieImg())
                    .into(filmImage);
        }

        getGoodNum.setText(orderDetailBean.getGoodsDetail().getFetchCode());
        if (orderDetailBean.getGoodsDetail() != null) {
            initSaleList(orderDetailBean.getGoodsDetail().getGoodsList(), flag);
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
        }
    }


    //卖品列表
    private void initSaleList(List<GoodsOrderListInfo> goodsList, boolean flag) {
        saleLayout.removeAllViews();
        int num = goodsList.size();
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
        idRequest.setOrderId(getIntent().getStringExtra("orderId"));
        OkHttpClientManager.postAsyn(Config.GET_MY_ORDER_INFO, new OkHttpClientManager.ResultCallback<OrderDetailBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(OrderDetailBean response) {
                customDialog.dismiss();
                orderDetailBean = response;
                initDate();

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, OrderDetailBean.class, SettingOrderDetailsActivity.this);
    }


    @Override
    public void onClick(View view) {
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
        }
    }
}
