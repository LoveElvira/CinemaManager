package com.yyjlr.tickets.content.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.OldPaySelectActivity;
import com.yyjlr.tickets.activity.setting.SettingOrderDetailsActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.PayAdapter;
import com.yyjlr.tickets.content.BaseLinearLayout;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.ResponseStatus;
import com.yyjlr.tickets.model.pay.AlipayResponse;
import com.yyjlr.tickets.model.pay.PayResult;
import com.yyjlr.tickets.model.pay.SelectPay;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/17.
 * 网上支付
 */
public class OnLinePayContent extends BaseLinearLayout implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private final int SDK_PAY_FLAG = 1;
    private int times = 0;//轮询检查订单状态
    private LinearLayout showConfirmLayout;
    private TextView confirm;
    private TextView confirmPrice;
    private RecyclerView listView;
    private PayAdapter adapter;
    private List<SelectPay> payList;
    private String orderId;
    //    private CustomDialog customDialog;
    private int price;
    private int position = 0;
    private int positions = -1;

    public OnLinePayContent(Context context) {
        this(context, null);
    }

    public OnLinePayContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.content_pay_select_online_pay_way, this);
//        customDialog = new CustomDialog(Application.getInstance().getCurrentActivity(), "请稍等。。。");
        initView();
    }

    public void initDate(List<SelectPay> payList, int price, String orderId, int position) {
        this.orderId = orderId;
        this.payList = payList;
        this.price = price;
        this.positions = position;
        confirmPrice.setText(ChangeUtils.save2Decimal(price));
        adapter = new PayAdapter(this.payList);
        listView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemChildClickListener(OnLinePayContent.this);
    }

    public void setConfirmClickable() {
        confirm.setClickable(false);
        confirm.setBackgroundColor(getResources().getColor(R.color.gray_c7c7c7));
    }

    private void initView() {
        listView = (RecyclerView) findViewById(R.id.content_pay_select__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);

        showConfirmLayout = (LinearLayout) findViewById(R.id.content_pay_select__confirm_pay_layout);
        confirm = (TextView) findViewById(R.id.content_pay_select__confirm_pay);
        confirmPrice = (TextView) findViewById(R.id.content_pay_select__confirm_price);

        showConfirmLayout.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(this);
    }


    //预支付数据
    private void beforePay() {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setPayTypeId(payList.get(position).getId() + "");
        idRequest.setOrderId(orderId);
        OkHttpClientManager.postAsyn(Config.BEFORE_PAY_ORDER, new OkHttpClientManager.ResultCallback<AlipayResponse>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                if ("410".equals(info.getCode())) {
                    Application.getInstance().getCurrentActivity().setResult(0x10, new Intent()
                            .putExtra("isCancel", true)
                            .putExtra("position", positions));
                    OldPaySelectActivity.activity.finish();
                }
//                customDialog.dismiss();
            }

            @Override
            public void onResponse(final AlipayResponse response) {
//                Log.i("ee", new Gson().toJson(response));
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        //构造PayTask 对象
                        PayTask alipay = new PayTask(Application.getInstance().getCurrentActivity());
                        //调用支付接口，获取支付结果
                        Map<String, String> result = alipay.payV2(response.getData(), true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandlers.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }
        }, idRequest, AlipayResponse.class, Application.getInstance().getCurrentActivity());
    }

    private Handler mHandlers = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.v("xxxxxx", "status:" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        times = 0;
                        checkOrderStatus();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    //订单状态查询接口
    private void checkOrderStatus() {
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId);
        OkHttpClientManager.postAsyn(Config.CHECK_PAY_ORDER_STATUS, new OkHttpClientManager.ResultCallback<ResponseStatus>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                //                customDialog.dismiss();
            }

            @Override
            public void onResponse(final ResponseStatus response) {
                Log.i("ee", new Gson().toJson(response));
                if (response.getState() == 1) {//付款成功
                    Log.i("ee", "------------1111------------");
                    startActivity();
//                    Toast.makeText(Application.getInstance().getCurrentActivity(), "支付成功", Toast.LENGTH_SHORT).show();

                } else if (response.getState() == 0) {//待付款
                    Log.i("ee", "-----------2222-------------");
                    times++;
                    if (times == 20) {
                        startActivity();
                        Toast.makeText(Application.getInstance().getCurrentActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                checkOrderStatus();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                //                customDialog.dismiss();
            }
        }, idRequest, ResponseStatus.class, Application.getInstance().getCurrentActivity());
    }

    private void startActivity() {
        Log.i("ee", "-------------3333-----------");
//        customDialog.dismiss();
        Application.getInstance().getCurrentActivity().startActivityForResult(new Intent(getContext(), SettingOrderDetailsActivity.class)
                .putExtra("orderId", orderId), 0x09);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_pay_select__confirm_pay://确认支付
                if ("支付宝".equals(payList.get(position).getName()) && payList.get(position).getChecked() == 1) {
                    beforePay();
                } else {
                    if ("微信".equals(payList.get(position).getName())) {
                        Toast.makeText(getContext(), "微信支付暂未开放", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "请选择支付方式", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        this.position = position;
        for (int i = 0; i < payList.size(); i++) {
            payList.get(i).setChecked(0);
        }
        payList.get(position).setChecked(1);
        this.adapter.notifyDataSetChanged();
    }
}
