package com.yyjlr.tickets.content;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import com.yyjlr.tickets.model.myinfo.SettingEntity;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CircleImageView;

import java.util.Calendar;

/**
 * Created by Elvira on 2016/7/28.
 * 我的页面
 */
public class MySettingOldContent extends BaseLinearLayout implements View.OnClickListener {

    private TextView title;
    private LinearLayout myService;
    private CircleImageView headImage;
    private ImageView sex;
    private TextView userName;

    //我的账号 我的订单 我的VIP 我的消息 我的关注 我的积分
    private RelativeLayout myAccountLayout, myOrderLayout, myVipLayout, myMessageLayout, myFollowLayout, myPointsLayout;

    private TextView messageNum;//消息数量

    public MySettingOldContent(Context context) {
        this(context, null);
    }

    public MySettingOldContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_new_mysetting, this);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的");
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
        messageNum = (TextView) findViewById(R.id.fragment_setting__message_num);

        headImage.setOnClickListener(this);
        myAccountLayout.setOnClickListener(this);
        myOrderLayout.setOnClickListener(this);
        myVipLayout.setOnClickListener(this);
        myMessageLayout.setOnClickListener(this);
        myFollowLayout.setOnClickListener(this);
        myService.setOnClickListener(this);
        myPointsLayout.setOnClickListener(this);
        updateView();
    }

    public void updateView() {
        String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "0", Application.getInstance().getCurrentActivity());
        if (isLogin.equals("1")) {
            getMyInfo();
        } else {
            sex.setVisibility(GONE);
            messageNum.setVisibility(GONE);
            headImage.setImageResource(R.mipmap.head_image_default);
            userName.setText("未登录");
        }
    }

//    public void hideInput() {
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
////            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }


    //获取我的账号信息
    public void getMyInfo() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_MY_SETTING, new OkHttpClientManager.ResultCallback<SettingEntity>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(SettingEntity response) {
//                if (response != null) {
//                    if (response.getHeadImgUrl() != null && !"".equals(response.getHeadImgUrl())) {
//                        Picasso.with(getContext())
//                                .load(response.getHeadImgUrl())
//                                .into(headImage);
//                    }
//                    if (response.getSexImgUrl() != null && !"".equals(response.getSexImgUrl())) {
//                        sex.setVisibility(VISIBLE);
//                        Picasso.with(getContext())
//                                .load(response.getSexImgUrl())
//                                .into(sex);
//                    }
//                    if ("0".equals(response.getUnReadMsg()) || "".equals(response.getUnReadMsg()) || response.getUnReadMsg() == null) {
//                        messageNum.setVisibility(GONE);
//                    } else {
//                        messageNum.setVisibility(VISIBLE);
//                        messageNum.setText(response.getUnReadMsg());
//                    }
//                    userName.setText(response.getNickname());
//                }


                if (response != null) {
                    if (response.getHeadImgUrl() != null && !"".equals(response.getHeadImgUrl())) {
                        Picasso.with(getContext())
                                .load(response.getHeadImgUrl())
                                .into(headImage);
                    } else {
                        headImage.setImageResource(R.mipmap.head_image_default);
                    }
                    if (response.getSexImgUrl() != null && !"".equals(response.getSexImgUrl())) {
                        sex.setVisibility(VISIBLE);
                        Picasso.with(getContext())
                                .load(response.getSexImgUrl())
                                .into(sex);
                    } else {
                        sex.setVisibility(GONE);
                    }

                    if (response.getNickname() != null && !"".equals(response.getSexImgUrl())) {
                        userName.setText(response.getNickname());
                    } else {
                        userName.setText("未登录");
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, requestNull, MyInfoModel.class, Application.getInstance().getCurrentActivity());
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fragment_setting__service) {
            showPhoneService();
            return;
        }

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
            if (!isLogin.equals("1")) {
                Application.getInstance().getCurrentActivity().startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }

            Intent intent = new Intent();
            switch (view.getId()) {
//            case R.id.fragment_setting__myaccount:
//                intent.setClass(Application.getInstance().getCurrentActivity(), LoginActivity.class);
//                Application.getInstance().getCurrentActivity().startActivity(intent);
//                break;
                case R.id.fragment_setting__myorder:
                    intent.setClass(Application.getInstance().getCurrentActivity(), SettingOrderActivity.class);
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    break;
                case R.id.fragment_setting__vip:
//                Toast.makeText(getContext(), "会员功能正在开放中", Toast.LENGTH_SHORT).show();
                    intent.setClass(Application.getInstance().getCurrentActivity(), SettingVipActivity.class);
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    break;
                case R.id.fragment_setting__message:
//                Toast.makeText(getContext(), "消息功能正在开放中", Toast.LENGTH_SHORT).show();
//
                    intent.setClass(Application.getInstance().getCurrentActivity(), SettingMessageActivity.class);
                    Application.getInstance().getCurrentActivity().startActivityForResult(intent, 0x08);
                    break;
                case R.id.fragment_setting__follow:
//                Toast.makeText(getContext(), "收藏功能正在开放中", Toast.LENGTH_SHORT).show();
//
                    intent.setClass(Application.getInstance().getCurrentActivity(), SettingFollowActivity.class);
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    break;
//                case R.id.fragment_setting__service:
//
//                    showPhoneService();
//
//                    break;
                case R.id.fragment_setting__points:
//                    Toast.makeText(getContext(), "积分功能正在开放中", Toast.LENGTH_SHORT).show();
//
                    intent.setClass(Application.getInstance().getCurrentActivity(), SettingPointsActivity.class);
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    break;
                case R.id.fragment_setting__myaccount:
                case R.id.fragment_setting__head_img:
                    intent.setClass(Application.getInstance().getCurrentActivity(), SettingAccountActivity.class);
                    Application.getInstance().getCurrentActivity().startActivityForResult(intent, 0x06);
                    break;
            }
        }
//        Application.getInstance().getCurrentActivity().startActivity(intent);
    }

    /**
     * show Dialog 呼叫服务电话
     */

    private void showPhoneService() {
        final String phoneNumber = "4006075588";
        LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        title.setText("联系客服人员");
        message.setText("拨打   " + phoneNumber);
        layout.findViewById(R.id.alert_dialog__cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        layout.findViewById(R.id.alert_dialog__submit).setOnClickListener(new OnClickListener() {
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
