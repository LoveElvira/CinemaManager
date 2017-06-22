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
import android.view.inputmethod.InputMethodManager;
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
import com.yyjlr.tickets.activity.setting.SettingCouponActivity;
import com.yyjlr.tickets.activity.setting.SettingFollowActivity;
import com.yyjlr.tickets.activity.setting.SettingMessageActivity;
import com.yyjlr.tickets.activity.setting.SettingOrderActivity;
import com.yyjlr.tickets.activity.setting.SettingPointsActivity;
import com.yyjlr.tickets.activity.setting.SettingVipActivity;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.message.MessageNumEntity;
import com.yyjlr.tickets.model.myinfo.MyInfoModel;
import com.yyjlr.tickets.model.myinfo.SettingEntity;
import com.yyjlr.tickets.model.myinfo.SettingInfoEntity;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CircleImageView;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.yyjlr.tickets.Application.getInstance;
import static com.yyjlr.tickets.R.mipmap.phone;

/**
 * Created by Elvira on 2016/7/28.
 * 我的页面
 */
public class MySettingContent extends BaseLinearLayout implements View.OnClickListener {

    private TextView title;
    private CircleImageView headImage;
    private ImageView sex;
    private TextView userName;
    private LinearLayout itemParent;
    private TextView msgNum = null;

    public MySettingContent(Context context) {
        this(context, null);
    }

    public MySettingContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_new_mysetting, this);
        initView();
    }

    private void initView() {
        isFirst = true;
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的");
        headImage = (CircleImageView) findViewById(R.id.fragment_setting__head_img);
        sex = (ImageView) findViewById(R.id.fragment_setting__sex);
        userName = (TextView) findViewById(R.id.fragment_setting__username);
        itemParent = (LinearLayout) findViewById(R.id.fragment_setting__item);
        headImage.setOnClickListener(this);
    }

    public void updateView(boolean isUpdate) {
        if (isUpdate) {
            isFirst = isUpdate;
        }
        if (isFirst) {
            isFirst = false;
            initBgTitle(bgTitle);
            getMyInfo();
        }
        String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "0", Application.getInstance().getCurrentActivity());
        if (isLogin.equals("1")) {
            getMyMessageNum();
        } else {
            sex.setVisibility(GONE);
            headImage.setImageResource(R.mipmap.head_image_default);
            userName.setText("未登录");
        }
    }

    public void updateTopView(boolean isLogin, String headImage, String sex, String name) {
        if (isLogin) {
            Picasso.with(getContext())
                    .load(headImage)
                    .into(this.headImage);
            this.sex.setVisibility(VISIBLE);
            // 性别，1：男；2：女
            if ("1".equals(sex)) {
                this.sex.setImageResource(R.mipmap.boy);
            } else {
                this.sex.setImageResource(R.mipmap.girl);
            }
            userName.setText(name);
        } else {
            this.sex.setVisibility(GONE);
            this.headImage.setImageResource(R.mipmap.head_image_default);
            userName.setText("未登录");
        }
    }

    //我的列表
    private void initList(final List<SettingInfoEntity> modelList) {
        View viewParent = LayoutInflater.from(getContext()).inflate(R.layout.item_setting_parent, null);
        LinearLayout parent = (LinearLayout) viewParent.findViewById(R.id.item_setting__parent);
        parent.removeAllViews();
        for (int i = 0; i < modelList.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_setting, null);
            LinearLayout itemLayout = (LinearLayout) view.findViewById(R.id.item_setting__layout);
            ImageView leftImage = (ImageView) view.findViewById(R.id.item_setting__image);
            TextView title = (TextView) view.findViewById(R.id.item_setting__title);
            TextView msgNum = (TextView) view.findViewById(R.id.item_setting__message_num);
            title.setText(modelList.get(i).getModName());
            View line = view.findViewById(R.id.item_setting__line);
            line.setVisibility(VISIBLE);
            msgNum.setVisibility(GONE);
            if (i == modelList.size() - 1) {
                line.setVisibility(GONE);
            }

            if (modelList.get(i).getModNo() == 5) {
                this.msgNum = msgNum;
            }

            final int finalI = i;
            final Intent intent = new Intent();
            itemLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (modelList.get(finalI).getModNo() == 7) {//联系客服
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
                        switch (modelList.get(finalI).getModNo()) {

                            case 1://我的账号
                                intent.setClass(Application.getInstance().getCurrentActivity(), SettingAccountActivity.class);
                                Application.getInstance().getCurrentActivity().startActivityForResult(intent, 0x06);
                                break;
                            case 2://我的订单
                                intent.setClass(Application.getInstance().getCurrentActivity(), SettingOrderActivity.class);
                                Application.getInstance().getCurrentActivity().startActivity(intent);
                                break;
                            case 3://会员中心
                                intent.setClass(Application.getInstance().getCurrentActivity(), SettingVipActivity.class);
                                Application.getInstance().getCurrentActivity().startActivity(intent);
                                break;
                            case 4://我的积分
                                intent.setClass(Application.getInstance().getCurrentActivity(), SettingPointsActivity.class);
                                Application.getInstance().getCurrentActivity().startActivity(intent);
                                break;
                            case 5://我的消息
                                intent.setClass(Application.getInstance().getCurrentActivity(), SettingMessageActivity.class);
                                Application.getInstance().getCurrentActivity().startActivityForResult(intent, 0x08);
                                break;
                            case 6://我的收藏
                                intent.setClass(Application.getInstance().getCurrentActivity(), SettingFollowActivity.class);
                                Application.getInstance().getCurrentActivity().startActivity(intent);
                                break;
                            case 8://我的优惠券
                                intent.setClass(Application.getInstance().getCurrentActivity(), SettingCouponActivity.class);
                                Application.getInstance().getCurrentActivity().startActivity(intent);
                                break;
                        }
                    }
                }
            });
            parent.addView(view);
        }
        itemParent.addView(viewParent);
    }

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

                if (response.getAllModuleInfo() != null) {
                    itemParent.removeAllViews();
                    for (int i = 0; i < response.getAllModuleInfo().size(); i++) {
                        if (response.getAllModuleInfo().get(i) != null && response.getAllModuleInfo().get(i).size() > 0) {
                            initList(response.getAllModuleInfo().get(i));
                        }
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, requestNull, SettingEntity.class, Application.getInstance().getCurrentActivity());
    }

    //获取我的未读消息数
    public void getMyMessageNum() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_UNREAD_MSG_NUM, new OkHttpClientManager.ResultCallback<MessageNumEntity>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(MessageNumEntity response) {
                if (response != null) {
                    if (response.getUnreadmsg() != -1) {
                        if (msgNum != null) {
                            msgNum.setText(response.getUnreadmsg() + "");
                            msgNum.setVisibility(VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, requestNull, MessageNumEntity.class, Application.getInstance().getCurrentActivity());
    }

    @Override
    public void onClick(View view) {
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
                case R.id.fragment_setting__head_img:
                    intent.setClass(Application.getInstance().getCurrentActivity(), SettingAccountActivity.class);
                    Application.getInstance().getCurrentActivity().startActivityForResult(intent, 0x06);
                    break;
            }
        }
    }

    /**
     * show Dialog 呼叫服务电话
     */

    private void showPhoneService() {
        final String phoneNumber = appConfig != null ? appConfig.getTel() : "";
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
