package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.PayAdapter;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.ResponseStatus;
import com.yyjlr.tickets.model.pay.AlipayResponse;
import com.yyjlr.tickets.model.pay.PayModel;
import com.yyjlr.tickets.model.pay.PayResult;
import com.yyjlr.tickets.model.pay.SelectPay;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2017/3/28.
 * 充值
 */

public class RechargeActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener {
    private TextView title;
    private ImageView leftArrow;
    private RecyclerView listView;
    private PayAdapter adapter;
    private EditText price;//充值金额
    private TextView confirm;
    private LinearLayout delete;
    private List<SelectPay> payList;
    private int position;
    private final int SDK_PAY_FLAG = 1;
    private int times = 0;//轮询检查订单状态
    private String cardNo;//卡号
    private String cardId = null;//轮询ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        cardNo = getIntent().getStringExtra("cardNo");
        initView();
        getPay();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("充值");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        listView = (RecyclerView) findViewById(R.id.content_recharge__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        price = (EditText) findViewById(R.id.content_recharge__price);
        delete = (LinearLayout) findViewById(R.id.content_recharge__delete);
        confirm = (TextView) findViewById(R.id.content_recharge__confirm);
        price.addTextChangedListener(textWatcher);
        delete.setVisibility(View.GONE);
        confirm.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                price.setHint("建议充值100以上金额");
                delete.setVisibility(View.GONE);
            } else {
                delete.setVisibility(View.VISIBLE);
            }
        }
    };

    //获取支付数据
    private void getPay() {
        IdRequest idRequest = new IdRequest();
        OkHttpClientManager.postAsyn(Config.GET_PAY, new OkHttpClientManager.ResultCallback<PayModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(PayModel response) {
                Log.i("ee", new Gson().toJson(response));
                if (response != null) {
                    payList = response.getChannelList();
                    if (payList != null && payList.size() > 0)
                        adapter = new PayAdapter(payList);
                    listView.setAdapter(adapter);
                    adapter.setOnRecyclerViewItemChildClickListener(RechargeActivity.this);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, idRequest, PayModel.class, Application.getInstance().getCurrentActivity());
    }

    //预支付数据
    private void beforePay() {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setPayTypeId(payList.get(position).getId() + "");
        idRequest.setCardNo(cardNo);
        idRequest.setAmount((Integer.parseInt(price.getText().toString().trim()) * 100) + "");
        OkHttpClientManager.postAsyn(Config.BEFORE_PAY_ORDER, new OkHttpClientManager.ResultCallback<AlipayResponse>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
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
                        cardId = response.getMemberCardOrderId();
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
                            showShortToast("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showShortToast("支付失败");
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
        idRequest.setOrderId(cardId);
        OkHttpClientManager.postAsyn(Config.CHECK_PAY_CARD_STATUS, new OkHttpClientManager.ResultCallback<ResponseStatus>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo().toString());
                //                customDialog.dismiss();
            }

            @Override
            public void onResponse(final ResponseStatus response) {
                Log.i("ee", new Gson().toJson(response));
                if (response.getState() == 1) {//付款成功
                    Log.i("ee", "------------1111------------");
                    startActivity(true);
//                    Toast.makeText(Application.getInstance().getCurrentActivity(), "支付成功", Toast.LENGTH_SHORT).show();

                } else if (response.getState() == 0) {//待付款
                    Log.i("ee", "-----------2222-------------");
                    times++;
                    if (times == 20) {
                        startActivity(false);
                        showShortToast("支付失败");
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

    private void startActivity(boolean isUpdate) {
        setResult(CODE_RESULT, new Intent()
                .putExtra("isUpdate", isUpdate));
        RechargeActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (v.getId()) {
                case R.id.base_toolbar__left:
                    RechargeActivity.this.finish();
                    break;
                case R.id.content_recharge__confirm://确认支付
                    String amount = price.getText().toString().trim();
                    if (!"".equals(amount) && Integer.parseInt(amount) > 0) {

                        if ("支付宝".equals(payList.get(position).getName()) && payList.get(position).getChecked() == 1) {
                            beforePay();
                        } else {
                            showShortToast("请选择支付方式");
                        }
                    } else {
                        showShortToast("充值金额必须大于0");
                    }
                    break;
                case R.id.content_recharge__delete:
                    price.setText("");
                    break;
            }
        }
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            RechargeActivity.this.position = position;
            for (int i = 0; i < payList.size(); i++) {
                payList.get(i).setChecked(0);
            }
            payList.get(position).setChecked(1);
            RechargeActivity.this.adapter.notifyDataSetChanged();
        }
    }
}
