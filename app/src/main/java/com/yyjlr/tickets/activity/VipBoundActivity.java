package com.yyjlr.tickets.activity;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.yyjlr.tickets.model.pay.MemberCard;
import com.yyjlr.tickets.model.pay.MemberCardList;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/3.
 * 绑定会员卡
 */
public class VipBoundActivity extends AbstractActivity implements View.OnClickListener {

    private ImageView rightService;
    private ImageView leftArrow;
    private TextView title;

    private LinearLayout haveVipLayout;//有会员卡的布局

    private EditText cardNum;//卡号
    private EditText cardPassword;//密码
    private TextView unBounOrBoundVip;//解绑会员卡
    private TextView pwdTitle;//解绑会员卡
    private TextView tipTitle;//提示

    private List<MemberCard> memberCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_unbound_vip);
        customDialog = new CustomDialog(VipBoundActivity.this, "加载中...");
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_card_bound_title));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        rightService = (ImageView) findViewById(R.id.base_toolbar__right);
//        rightService.setImageResource(R.mipmap.service);
        rightService.setImageResource(R.mipmap.service_icon);
        rightService.setAlpha(1.0f);
        rightService.setOnClickListener(this);

        haveVipLayout = (LinearLayout) findViewById(R.id.content_setting_vip__bound_layout);
        haveVipLayout.setVisibility(View.VISIBLE);
        cardNum = (EditText) findViewById(R.id.content_setting_vip__card);
        cardPassword = (EditText) findViewById(R.id.content_setting_vip__money);
        unBounOrBoundVip = (TextView) findViewById(R.id.content_setting_vip__unbound_or_bound);
        unBounOrBoundVip.setText("绑定会员卡");
        pwdTitle = (TextView) findViewById(R.id.content_setting_vip__password);
        tipTitle = (TextView) findViewById(R.id.content_setting_vip__tip);
        pwdTitle.setText("密码");
        tipTitle.setVisibility(View.VISIBLE);
        cardPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        unBounOrBoundVip.setOnClickListener(this);
    }


    //绑定会员卡
    private void boundCard(String card, String pwd) {
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setCardNo(card);
        idRequest.setPwd(pwd);
        OkHttpClientManager.postAsyn(Config.BIND_CARD, new OkHttpClientManager.ResultCallback<MemberCardList>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MemberCardList response) {
                customDialog.dismiss();
                if (response != null) {
                    memberCardList = response.getMemberCardList();
                    if (memberCardList != null && memberCardList.size() > 0) {
                        setResult(CODE_RESULT, new Intent()
                                .putExtra("cardList", (Serializable) memberCardList));
                        VipBoundActivity.this.finish();
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, MemberCardList.class, Application.getInstance().getCurrentActivity());
    }


    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.content_setting_vip__unbound_or_bound://绑定或解除绑定
                    String card = cardNum.getText().toString();
                    String pwd = cardPassword.getText().toString();
                    if ("".equals(card)) {
                        showShortToast("请确认卡号");
                        return;
                    }
                    if ("".equals(pwd)) {
                        showShortToast("请输入正确的密码");
                        return;
                    }
                    boundCard(card, pwd);
                    break;
                case R.id.base_toolbar__left:
                    VipBoundActivity.this.finish();
                    break;
                case R.id.base_toolbar__right:
                    showPhoneService();
                    break;
            }
        }
    }

    /**
     * show Dialog 呼叫服务电话
     */
    private void showPhoneService() {
        final String phoneNumber = "15802171337";
        LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        title.setText("联系服务人员");
        message.setText("拨打   " + phoneNumber);
        layout.findViewById(R.id.alert_dialog__cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        layout.findViewById(R.id.alert_dialog__submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    //拨打电话
                    if (!"".equals(phoneNumber)) {
                        TelephonyManager mTelephonyManager = (TelephonyManager) Application.getInstance().getCurrentActivity().getSystemService(Service.TELEPHONY_SERVICE);
                        int absent = mTelephonyManager.getSimState();
                        if (absent == TelephonyManager.SIM_STATE_ABSENT) {
                            Toast.makeText(Application.getInstance().getCurrentActivity(), "请确认sim卡是否插入或者sim卡暂时不可用！",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent phoneIntent = new Intent(
                                    "android.intent.action.CALL", Uri.parse("tel:"
                                    + phoneNumber));
                            Application.getInstance().getCurrentActivity().startActivity(phoneIntent);
                        }
                        builder.dismiss();
                    } else {
                        Toast.makeText(Application.getInstance().getCurrentActivity(), "电话为空",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}