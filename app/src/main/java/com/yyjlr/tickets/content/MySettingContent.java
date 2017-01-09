package com.yyjlr.tickets.content;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.activity.setting.SettingAccountActivity;
import com.yyjlr.tickets.activity.setting.SettingFollowActivity;
import com.yyjlr.tickets.activity.setting.SettingMessageActivity;
import com.yyjlr.tickets.activity.setting.SettingOrderActivity;
import com.yyjlr.tickets.activity.setting.SettingPointsActivity;
import com.yyjlr.tickets.activity.setting.SettingVipActivity;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.myinfo.MyInfoModel;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CircleImageView;
import com.yyjlr.tickets.viewutils.CustomDialog;

import static com.yyjlr.tickets.R.mipmap.phone;

/**
 * Created by Elvira on 2016/7/28.
 * 我的页面
 */
public class MySettingContent extends LinearLayout implements View.OnClickListener {

    private View view;
    private LinearLayout myService;
    public static CircleImageView headImage;
    public static ImageView sex;
    public static TextView userName;

    //我的账号 我的订单 我的VIP 我的消息 我的关注 我的积分
    private RelativeLayout myAccountLayout, myOrderLayout, myVipLayout, myMessageLayout, myFollowLayout, myPointsLayout;


    private CustomDialog customDialog;

    public MySettingContent(Context context) {
        this(context, null);
    }

    public MySettingContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_mysetting, this);
        initView();
    }

    private void initView() {
        headImage = (CircleImageView) findViewById(R.id.fragment_setting__head_img);
        sex = (ImageView) findViewById(R.id.fragment_setting__sex);
        userName = (TextView) findViewById(R.id.fragment_setting__username);
        myAccountLayout = (RelativeLayout) findViewById(R.id.fragment_setting__myaccount);
        myOrderLayout = (RelativeLayout) findViewById(R.id.fragment_setting__myorder);
        myVipLayout = (RelativeLayout) findViewById(R.id.fragment_setting__vip);
        myMessageLayout = (RelativeLayout) findViewById(R.id.fragment_setting__message);
        myFollowLayout = (RelativeLayout) findViewById(R.id.fragment_setting__follow);
        myPointsLayout = (RelativeLayout) findViewById(R.id.fragment_setting__points);
        myService = (LinearLayout) findViewById(R.id.fragment_setting__service);

        headImage.setOnClickListener(this);
        myAccountLayout.setOnClickListener(this);
        myOrderLayout.setOnClickListener(this);
        myVipLayout.setOnClickListener(this);
        myMessageLayout.setOnClickListener(this);
        myFollowLayout.setOnClickListener(this);
        myService.setOnClickListener(this);
        myPointsLayout.setOnClickListener(this);
        String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "0", Application.getInstance().getCurrentActivity());
        if (isLogin.equals("1")) {
            getMyInfo();
        }
    }


    //获取我的账号信息
    public void getMyInfo() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_MY_INFO, new OkHttpClientManager.ResultCallback<MyInfoModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(getContext(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(MyInfoModel response) {
                if (response != null) {
                    if (response.getHeadImgUrl() != null && !"".equals(response.getHeadImgUrl())) {
                        Picasso.with(getContext())
                                .load(response.getHeadImgUrl())
                                .into(headImage);
                    }
                    if (response.getSexImgUrl() != null && !"".equals(response.getSexImgUrl())) {
                        Picasso.with(getContext())
                                .load(response.getSexImgUrl())
                                .into(sex);
                    }
                    userName.setText(response.getNickname());
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, MyInfoModel.class, Application.getInstance().getCurrentActivity());
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.fragment_setting__myaccount:
                intent.setClass(Application.getInstance().getCurrentActivity(), LoginActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_setting__myorder:
                intent.setClass(Application.getInstance().getCurrentActivity(), SettingOrderActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_setting__vip:
                intent.setClass(Application.getInstance().getCurrentActivity(), SettingVipActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_setting__message:
                intent.setClass(Application.getInstance().getCurrentActivity(), SettingMessageActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_setting__follow:
                intent.setClass(Application.getInstance().getCurrentActivity(), SettingFollowActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_setting__service:

                showPhoneService();

                break;
            case R.id.fragment_setting__points:
                intent.setClass(Application.getInstance().getCurrentActivity(), SettingPointsActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_setting__head_img:
                intent.setClass(Application.getInstance().getCurrentActivity(), SettingAccountActivity.class);
                Application.getInstance().getCurrentActivity().startActivityForResult(intent,0x06);
                break;
        }
//        Application.getInstance().getCurrentActivity().startActivity(intent);
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
        });
    }
}
