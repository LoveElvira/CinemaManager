package com.yyjlr.tickets.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.BasePhotoActivity;
import com.yyjlr.tickets.content.MySettingContent;
import com.yyjlr.tickets.model.myinfo.MyInfoModel;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.utils.BitmapUtils;
import com.yyjlr.tickets.utils.ImageFileUtils;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Elvira on 2016/8/12.
 * 我的账户
 */
public class SettingAccountActivity extends BasePhotoActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private int year, monthOfYear, dayOfMonth;
    private View showBirthdayLayout;
    private TextView birthday;
    public static TextView sex;
    public static TextView userName;
    private LinearLayout headImageLayout;
    private LinearLayout userNameLayout;
    private LinearLayout sexLayout;
    private LinearLayout findPwdLayout;
    public static ImageView headImage;
    private TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_account);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的账户");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        headImageLayout = (LinearLayout) findViewById(R.id.content_setting_account__head_image_layout);
        userNameLayout = (LinearLayout) findViewById(R.id.content_setting_account__username_layout);
        sexLayout = (LinearLayout) findViewById(R.id.content_setting_account__sex_layout);
        findPwdLayout = (LinearLayout) findViewById(R.id.content_setting_account__password);
        headImage = (ImageView) findViewById(R.id.content_setting_account__head_image);
        showBirthdayLayout = findViewById(R.id.content_setting_account__birthday_layout);
        birthday = (TextView) findViewById(R.id.content_setting_account__birthday);
        sex = (TextView) findViewById(R.id.content_setting_account__sex);
        userName = (TextView) findViewById(R.id.content_setting_account__username);
        phone = (TextView) findViewById(R.id.content_setting_account__phone);
        showBirthdayLayout.setOnClickListener(this);
        headImageLayout.setOnClickListener(this);
        userNameLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        findPwdLayout.setOnClickListener(this);

//        userName.setText(getIntent().getStringExtra("userName"));
        getMyInfo();
    }

    //获取我的账号信息
    private void getMyInfo() {
        customDialog = new CustomDialog(SettingAccountActivity.this, "加载中...");
        customDialog.show();
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_MY_INFO, new OkHttpClientManager.ResultCallback<MyInfoModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MyInfoModel response) {
                customDialog.dismiss();
                if (response != null) {
                    if (response.getHeadImgUrl() != null && !"".equals(response.getHeadImgUrl())) {
                        Picasso.with(getBaseContext())
                                .load(response.getHeadImgUrl())
                                .into(headImage);
                    }
                    userName.setText(response.getNickname());
                    // 性别，1：男；2：女
                    if ("1".equals(response.getSex())) {
                        sex.setText("男");
                    } else {
                        sex.setText("女");
                    }

                    if (response.getBirthday() != null && !"".equals(response.getBirthday())) {
                        birthday.setText(response.getBirthday());
                    }

                    phone.setText(response.getPhone());

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, requestNull, MyInfoModel.class, Application.getInstance().getCurrentActivity());
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                SettingAccountActivity.this.finish();
                break;
            case R.id.content_setting_account__head_image_layout:
                View parent = LayoutInflater.from(SettingAccountActivity.this).inflate(R.layout.activity_mysetting_account, null);
                new PopupWindows(SettingAccountActivity.this, parent);
//                startActivity(SettingPhotoActivity.class);
                break;
            case R.id.content_setting_account__username_layout:
                intent.setClass(getBaseContext(), AccountNameActivity.class);
                intent.putExtra("userName", userName.getText().toString());
                startActivity(intent);
                break;
            case R.id.content_setting_account__sex_layout:
                intent.setClass(getBaseContext(), AccountSexActivity.class);
                intent.putExtra("sex", sex.getText().toString());
                startActivity(intent);
                break;
            case R.id.content_setting_account__birthday_layout://时间选择器
                showPopWindowDatePicker();
                break;
            case R.id.content_setting_account__password://修改密码
                startActivity(UpdatePasswordActivity.class);
                break;
        }
    }

    //时间的popwindow
    //popwindow显示
    private void showPopWindowDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_datepicker, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.content_setting_account__phone),
                Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int years,
                                      int monthOfYears, int dayOfMonths) {
                year = years;
                monthOfYear = monthOfYears;
                dayOfMonth = dayOfMonths;
            }

        });
        TextView dateCancel = (TextView) view.findViewById(R.id.date_cancel);
        TextView datesubmit = (TextView) view.findViewById(R.id.date_submit);
        dateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        datesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = "";
                String day = "";
                String date = "";
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                if (monthOfYear < 9) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                date = year + "-" + month + "-" + day;
                birthday.setText(date);
                window.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (!mIsKitKat) {//低于4.4的版本
            switch (requestCode) {
                case CODE_CAMERA_REQUEST_TWO://调用相机返回
                    if (resultCode == RESULT_OK) {
                        ClipPhotoActivity.startActivity(SettingAccountActivity.this, mPublishPhotoPath, CODE_CLIP_REQUEST);
//                        updateImage(mPublishPhotoPath);
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
                case CODE_GALLERY_REQUEST_TWO://调用相册返回

                    if (resultCode == RESULT_CANCELED) {
                        showShortToast("取消了选择图片");
                    } else if (resultCode == RESULT_OK) {
                        if (data != null && data.getData() != null) {
                            String path = getPhotoName(data.getData(), true, "");
                            ClipPhotoActivity.startActivity(SettingAccountActivity.this, path, CODE_CLIP_REQUEST);
//                            updateImage(path);
                        }
                    }
                    break;

                case CODE_CLIP_REQUEST:
                    updateImage(data.getStringExtra("path"));
                    break;
            }
        } else {//高于4.4
            switch (requestCode) {
                case CODE_CAMERA_REQUEST_ONE:
                    if (resultCode == RESULT_OK) {
                        ClipPhotoActivity.startActivity(SettingAccountActivity.this, mPublishPhotoPath, CODE_CLIP_REQUEST);
//                        updateImage(mPublishPhotoPath);
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
                case CODE_GALLERY_REQUEST_ONE:

                    if (resultCode == RESULT_CANCELED) {
                        showShortToast("取消了选择图片");
                    } else if (resultCode == RESULT_OK) {
                        if (data != null && data.getData() != null) {
                            String path = ImageFileUtils.getPath(this, data.getData());
                            Log.i("ee", path);
                            ClipPhotoActivity.startActivity(SettingAccountActivity.this, path, CODE_CLIP_REQUEST);
//                            updateImage(path);
                        }
                    }
                    break;
                case CODE_CLIP_REQUEST:
                    updateImage(data.getStringExtra("path"));
                    break;
            }
        }
    }

    //更新头像
    private void updateImage(String path) {
        try {
            headImage.setImageBitmap(BitmapUtils.revitionImageSize(path));
            MySettingContent.headImage.setImageBitmap(BitmapUtils.revitionImageSize(path));
//            upLoadImage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
