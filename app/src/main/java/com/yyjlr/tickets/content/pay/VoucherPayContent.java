package com.yyjlr.tickets.content.pay;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.PaySelectActivity;
import com.yyjlr.tickets.activity.VipBoundActivity;
import com.yyjlr.tickets.activity.setting.SettingOrderDetailsActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.pay.CouponInfo;
import com.yyjlr.tickets.model.pay.CouponList;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.service.VoucherError;
import com.yyjlr.tickets.service.VoucherErrorInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2017/2/3.
 * 兑换券支付
 */
public class VoucherPayContent extends LinearLayout implements View.OnClickListener {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    private LinearLayout voucherLayout;
    private TextView confirm;//确认
    private TextView confirmPrice;//确认金额

    private View view;
    private String orderId;
    private int price;
    private int num;//兑换券数量
    private int payTypeId;//支付类型
    private List<String> couponList;
    private List<CouponInfo> couponInfoList = null;//后台返回的数据 兑换券是否可用

    public VoucherPayContent(Context context, String orderId) {
        this(context, null, orderId);
    }

    public VoucherPayContent(Context context, AttributeSet attrs, String orderId) {
        super(context, attrs);
        view = inflate(context, R.layout.content_pay_select_voucher_pay_way, this);
        this.orderId = orderId;
        lastClickTime = 0;
        initView();
    }

    public void initDate(int num, int price, int payTypeId) {
        this.num = num;
        this.price = price;
        this.payTypeId = payTypeId;
        if (num > 0) {
            voucherLayout.removeAllViews();
            couponList = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                couponList.add("");
                voucherLayout.addView(addVoucher(i, false));
            }
        }
        confirmPrice.setText(ChangeUtils.save2Decimal(price));
    }

    private void initView() {
        voucherLayout = (LinearLayout) findViewById(R.id.content_pay_select__layout);
        confirm = (TextView) findViewById(R.id.content_pay_select__confirm_pay);
        confirmPrice = (TextView) findViewById(R.id.content_pay_select__confirm_price);

        confirm.setOnClickListener(this);
    }

    //动态添加兑换券条数
    private View addVoucher(final int position, final boolean isShow) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.item_voucher, null, false);
        final EditText voucherNum = (EditText) view.findViewById(R.id.item_voucher__num);
        final ImageView isHave = (ImageView) view.findViewById(R.id.item_voucher__is_have);
        isHave.setVisibility(GONE);
        voucherNum.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
        voucherNum.setSelection(voucherNum.getText().toString().length());
        if (isShow) {
            isHave.setVisibility(VISIBLE);
            if (couponInfoList.get(position).getFlag() == 1) {
                voucherNum.setText("");
                isHave.setImageResource(R.mipmap.sale_select);
            } else {
                voucherNum.setText(couponInfoList.get(position).getErrMsg());
                voucherNum.setTextColor(getResources().getColor(R.color.red_de2500));
                isHave.setImageResource(R.mipmap.select_error);
            }
        }

        isHave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    isHave.setVisibility(GONE);
                    voucherNum.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
                    voucherNum.setText(couponInfoList.get(position).getCouponNo());
                    couponList.set(position, couponInfoList.get(position).getCouponNo());
                    voucherNum.setSelection(couponInfoList.get(position).getCouponNo().length());
                }
            }
        });

        voucherNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    isHave.setVisibility(GONE);
                    voucherNum.setTextColor(getResources().getColor(R.color.gray_c3c3c3));
                    voucherNum.setText(couponInfoList.get(position).getCouponNo());
                    couponList.set(position, couponInfoList.get(position).getCouponNo());
                    voucherNum.setSelection(couponInfoList.get(position).getCouponNo().length());
                }
            }
        });

        voucherNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
//                    voucherNum.setHint("请输入兑换券码");
                    couponList.set(position, "");
                } else {
                    couponList.set(position, s.toString());
                }
            }
        });
        return view;
    }

    //预支付数据
    private void beforePay() {
//        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setPayTypeId(payTypeId + "");
        idRequest.setOrderId(orderId);
        idRequest.setCouponList(couponList);
        OkHttpClientManager.postAsyn(Config.BEFORE_PAY_ORDER, new OkHttpClientManager.ResultVoucherCallback<CouponList>() {

            @Override
            public void onError(Request request, VoucherError info) {
//                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                if ("101".equals(info.getCode())) {
                    couponInfoList = info.getInfo().getCouponList();
                    if (couponInfoList != null && couponInfoList.size() > 0) {
                        voucherLayout.removeAllViews();
                        for (int i = 0; i < couponInfoList.size(); i++) {
                            voucherLayout.addView(addVoucher(i, true));
                        }
                    }
                } else {
                    Toast.makeText(getContext(), info.getInfo().getErrMsg().toString(), Toast.LENGTH_SHORT).show();
                }
//                if ("101".equals(info.getCode())) {
//                    Log.i("ee", "---------------------" + info.getInfo().toString());
//                    //第一层
//                    try {
//                        if (info.getInfo() != null) {
//                            JSONObject responseObj = new JSONObject(info.getInfo().toString());
//                            Toast.makeText(getContext(), responseObj.getString("errMsg"), Toast.LENGTH_SHORT).show();
//                            if (!"".equals(responseObj.getString("couponList"))) {
//                                JSONArray valueData = new JSONArray(responseObj.getString("couponList"));
//                                couponInfoList = new ArrayList<>();
//                                for (int i = 0; i < valueData.length(); i++) {
//                                    JSONObject datas = valueData.getJSONObject(i);
//                                    CouponInfo couponInfo = new CouponInfo();
//                                    couponInfo.setCouponNo(datas.getString("couponNo"));
//                                    couponInfo.setErrMsg(datas.getString("errMsg"));
//                                    couponInfo.setFlag(datas.getInt("flag"));
//                                    couponInfoList.add(couponInfo);
//                                }
//                            }
//                            if (couponInfoList != null && couponInfoList.size() > 0) {
//                                voucherLayout.removeAllViews();
//                                for (int i = 0; i < couponInfoList.size(); i++) {
//                                    voucherLayout.addView(addVoucher(i, true));
//                                }
//                            }
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(getContext(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
//                }
//                customDialog.dismiss();
            }

            @Override
            public void onResponse(CouponList response) {
//                Log.i("ee", new Gson().toJson(response));
//                couponInfoList = response.getCouponList();
                Application.getInstance().getCurrentActivity().startActivity(new Intent(getContext(), SettingOrderDetailsActivity.class)
                        .putExtra("orderId", orderId)
                        .putExtra("status", 3));
                PaySelectActivity.activity.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                customDialog.dismiss();
            }
        }, idRequest, CouponList.class, Application.getInstance().getCurrentActivity());
    }


    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.content_pay_select__confirm_pay:
                    for (int i = 0; i < couponList.size(); i++) {
                        if ("".equals(couponList.get(i))) {
                            Toast.makeText(getContext(), "请认真填写兑换券号", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    beforePay();
                    break;
            }
        }
    }
}