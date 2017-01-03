package com.yyjlr.tickets.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.Timer;

public class WXPayEntryActivity extends AbstractActivity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Timer timer;
    private TextView payResult;
    private int times = 0;
    private ProgressBar progressBar;
    private TextView title;
    private CustomDialog customDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.pay_result);
        //title = (TextView) findViewById(R.id.base_toolbar__text);
        //title.setText("支付结果");
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);
        // payResult = (TextView) findViewById(R.id.wexin_pay_result);
        // progressBar = (ProgressBar) findViewById(R.id.pay_result_bar);
        //  customDialog = new CustomDialog(this, "支付中...");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    private void getOrderStatuss() {
//        RequestId requestId = new RequestId();
//        requestId.setOrderId(orderId + "");
//        OkHttpClientManager.postAsyn(Config.PAY_STATUS, new OkHttpClientManager.ResultCallback<PayStatusResponse>() {
//            @Override
//            public void onError(Request request, Error info) {
//                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
//                showShortToast(info.getInfo());
//            }
//
//            @Override
//            public void onResponse(PayStatusResponse response) {
//                if (response.getPayStatus() == 1) {//已支付
//                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), PayEndActivity.class);
//                    intent.putExtra("orderId", SelectPayMethodActivity.orderId);
//                    intent.putExtra("payStatus", "1");
//                    if (!"1".equals(SelectPayMethodActivity.flag)) {//送朋友
//                        intent.putExtra("shareEnd", SelectPayMethodActivity.shareInfo);
//                        intent.putExtra(PayEndActivity.CART_OR_SEND, "0");
//                    } else {//普通购物车
//                        intent.putExtra(PayEndActivity.CART_OR_SEND, "1");
//                    }
//                    startActivity(intent);
//                    finish();
//                } else if (response.getPayStatus() == 0) {//未支付
//                    times++;
//                    if (times == 18) {
//                        if (timer != null) {
//                            timer.cancel();
//                            // 一定设置为null，否则定时器不会被回收
//                            timer = null;
//                        }
//                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), PayEndActivity.class);
//                        intent.putExtra("orderId", SelectPayMethodActivity.orderId);
//                        intent.putExtra("payStatus", "0");
//                        if (!"1".equals(SelectPayMethodActivity.flag)) {//送朋友
//                            intent.putExtra("shareEnd", SelectPayMethodActivity.shareInfo);
//                            intent.putExtra(PayEndActivity.CART_OR_SEND, "0");
//                        } else {//普通购物车
//                            intent.putExtra(PayEndActivity.CART_OR_SEND, "1");
//                        }
//                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(1000);
//                                    getOrderStatuss();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
//                    }
//                }
//            }
//
//            @Override
//            public void onOtherError(Request request, Exception exception) {
//                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//            }
//        }, requestId, PayStatusResponse.class);
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            times = 0;
            getOrderStatuss();
            //finish();
        }
    }
}