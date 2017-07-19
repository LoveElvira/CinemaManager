package com.yyjlr.tickets.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.BasePhotoActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.helputils.ImageFileUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.myinfo.MyInfoModel;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.requestdata.UpdateMyInfoRequest;
import com.yyjlr.tickets.requestdata.UploadRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private TextView sex;
    private TextView userName;
    private LinearLayout headImageLayout;
    private LinearLayout userNameLayout;
    private LinearLayout sexLayout;
    private LinearLayout findPwdLayout;
    private ImageView headImage;
    private TextView phone;
    private TextView quitLogin;//退出登录
    private boolean isUpdate = false;
    private MyInfoModel myInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_account);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
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
        quitLogin = (TextView) findViewById(R.id.content_setting_account__quit);
        showBirthdayLayout.setOnClickListener(this);
        headImageLayout.setOnClickListener(this);
        userNameLayout.setOnClickListener(this);
        sexLayout.setOnClickListener(this);
        findPwdLayout.setOnClickListener(this);
        quitLogin.setOnClickListener(this);
        isUpdate = false;
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
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MyInfoModel response) {
                customDialog.dismiss();
                myInfoModel = response;
                if (myInfoModel != null) {
                    if (myInfoModel.getHeadImgUrl() != null && !"".equals(myInfoModel.getHeadImgUrl())) {
                        Picasso.with(getBaseContext())
                                .load(myInfoModel.getHeadImgUrl())
                                .into(headImage);
                    }
                    userName.setText(myInfoModel.getNickname());
                    // 性别，1：男；2：女
                    if ("1".equals(myInfoModel.getSex())) {
                        sex.setText("男");
                    } else {
                        sex.setText("女");
                    }

                    if (myInfoModel.getBirthday() != null && !"".equals(myInfoModel.getBirthday())) {
                        birthday.setText(myInfoModel.getBirthday());
                    }

                    phone.setText(myInfoModel.getPhone());

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, requestNull, MyInfoModel.class, Application.getInstance().getCurrentActivity());
    }

    //修改个人信息
    private void updateInfo(final String flag, final String str) {
        customDialog = new CustomDialog(SettingAccountActivity.this, "请稍等...");
        customDialog.show();
        String sexStr = "";
        if (sex.getText().equals("男")) {
            sexStr = "1";
        } else {
            sexStr = "2";
        }
        UpdateMyInfoRequest updateMyInfoRequest = new UpdateMyInfoRequest();
        updateMyInfoRequest.setPhone(phone.getText().toString().trim());
        if (flag.equals("nickName")) {
            updateMyInfoRequest.setHeadImgUrl(myInfoModel.getHeadImgUrl());
            updateMyInfoRequest.setNickname(str);
            updateMyInfoRequest.setSex(sexStr);
            updateMyInfoRequest.setBirthday(birthday.getText().toString().trim());
        } else if (flag.equals("birthday")) {
            updateMyInfoRequest.setHeadImgUrl(myInfoModel.getHeadImgUrl());
            updateMyInfoRequest.setBirthday(str);
            updateMyInfoRequest.setNickname(userName.getText().toString().trim());
            updateMyInfoRequest.setSex(sexStr);
        } else if (flag.equals("sex")) {
            updateMyInfoRequest.setHeadImgUrl(myInfoModel.getHeadImgUrl());
            updateMyInfoRequest.setSex(str);
            updateMyInfoRequest.setNickname(userName.getText().toString().trim());
            updateMyInfoRequest.setBirthday(birthday.getText().toString().trim());
        } else if (flag.equals("headImage")) {
            updateMyInfoRequest.setHeadImgUrl(str);
            updateMyInfoRequest.setNickname(userName.getText().toString().trim());
            updateMyInfoRequest.setBirthday(birthday.getText().toString().trim());
            updateMyInfoRequest.setSex(sexStr);
        }
        OkHttpClientManager.postAsyn(Config.UPDATE_MY_INFO, new OkHttpClientManager.ResultCallback<MyInfoModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(MyInfoModel response) {
                customDialog.dismiss();
                isUpdate = true;
                myInfoModel = response;
                if (myInfoModel != null) {
                    if (myInfoModel.getHeadImgUrl() != null && !"".equals(myInfoModel.getHeadImgUrl())) {
                        Picasso.with(getBaseContext())
                                .load(myInfoModel.getHeadImgUrl())
                                .into(headImage);
                    }
                    userName.setText(myInfoModel.getNickname());
                    // 性别，1：男；2：女
                    if ("1".equals(myInfoModel.getSex())) {
                        SettingAccountActivity.this.sex.setText("男");
                    } else {
                        SettingAccountActivity.this.sex.setText("女");
                    }

                    if (response.getBirthday() != null && !"".equals(myInfoModel.getBirthday())) {
                        SettingAccountActivity.this.birthday.setText(myInfoModel.getBirthday());
                    }

                    SettingAccountActivity.this.phone.setText(myInfoModel.getPhone());

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, updateMyInfoRequest, MyInfoModel.class, Application.getInstance().getCurrentActivity());
    }

    private void upLoadImage(String path) {
        List<File> files = new ArrayList<File>();
        final CustomDialog customerDialog = new CustomDialog(SettingAccountActivity.this, "请稍后。。。");
        customerDialog.show();
        File file = new File(path);
        files.add(file);
        UploadRequest upload = new UploadRequest();
        upload.setType("my");
        OkHttpClientManager.postAsyn(Config.UPDATE_MY_HEAD_IMAGE, new OkHttpClientManager.ResultCallback<List<String>>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , e = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customerDialog.dismiss();
            }

            @Override
            public void onResponse(List<String> response) {
                customerDialog.dismiss();
                isUpdate = true;
                Log.i("ee", "----------------------------");
                if (response.get(0) != null && !"".equals(response.get(0))) {
                    updateInfo("headImage", response.get(0));
                }
//                updateInfo("headImage", response.get(0).getType());
//                showShortToast("上传成功");
                // Intent intent = new Intent(PublishWishActivity.this, PublishConsigneeInfoActivity.class);
                // startActivity(intent);
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customerDialog.dismiss();
//                showShortToast(exception.getMessage());
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, upload, files, new TypeReference<List<String>>() {
        }, SettingAccountActivity.this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                if (isUpdate) {
                    setResult(CODE_RESULT, new Intent()
                            .putExtra("headImage", myInfoModel.getHeadImgUrl())
                            .putExtra("sex", myInfoModel.getSex())
                            .putExtra("name", myInfoModel.getNickname()));
                }
                SettingAccountActivity.this.finish();
                break;
            case R.id.content_setting_account__head_image_layout:
                View parent = LayoutInflater.from(SettingAccountActivity.this).inflate(R.layout.activity_mysetting_account, null);
                new PopupWindows(SettingAccountActivity.this, parent);
//                startActivity(SettingPhotoActivity.class);
                break;
            case R.id.content_setting_account__username_layout:
                intent.setClass(getBaseContext(), AccountNameActivity.class);
                intent.putExtra("userName", userName.getText().toString().trim());
                SettingAccountActivity.this.startActivityForResult(intent, CODE_REQUEST_ONE);
                break;
            case R.id.content_setting_account__sex_layout:
                intent.setClass(getBaseContext(), AccountSexActivity.class);
                intent.putExtra("sex", sex.getText().toString());
                SettingAccountActivity.this.startActivityForResult(intent, CODE_REQUEST_TWO);
                break;
            case R.id.content_setting_account__birthday_layout://时间选择器
                showPopWindowDatePicker();
                break;
            case R.id.content_setting_account__password://修改密码
                startActivity(UpdatePasswordActivity.class);
                break;
            case R.id.content_setting_account__quit://退出登录
                SharePrefUtil.putString(Constant.FILE_NAME, "token", "", SettingAccountActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, "flag", "0", SettingAccountActivity.this);
                startActivityForResult(new Intent(SettingAccountActivity.this, LoginActivity.class), CODE_REQUEST_THREE);
                break;
        }
    }

    //时间的popwindow
    //popwindow显示
    private void showPopWindowDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_datepicker, null);
        View parent = inflater.inflate(R.layout.activity_mysetting_account, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar calendarMax = Calendar.getInstance();
        datePicker.setMaxDate(calendarMax.getTimeInMillis());
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
//                birthday.setText(date);
                updateInfo("birthday", date);
                window.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CODE_RESULT) {
            switch (requestCode) {
                case CODE_REQUEST_ONE:
                    String nickName = data.getStringExtra("nickName");
                    Log.i("ee", "------------------------" + nickName);
                    updateInfo("nickName", nickName);
                    break;
                case CODE_REQUEST_TWO:
                    String sex = data.getStringExtra("sex");
                    this.sex.setText(sex);
                    if (sex.equals("男"))
                        updateInfo("sex", "1");
                    else if (sex.equals("女"))
                        updateInfo("sex", "2");
                    break;
                case CODE_REQUEST_THREE:
                    if (data.getBooleanExtra("isFinish", false)) {
                        setResult(CODE_RESULT, getIntent()
                                .putExtra("isExit", true));
                        SettingAccountActivity.this.finish();
                    }
                    break;
                case CODE_REQUEST_DIALOG:
                    getMyInfo();
                    break;
            }
        }

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
        upLoadImage(path);
//        try {
////            headImage.setImageBitmap(BitmapUtils.revitionImageSize(path));
////            MySettingContent.headImage.setImageBitmap(BitmapUtils.revitionImageSize(path));
//            upLoadImage(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isUpdate) {
                setResult(CODE_RESULT, new Intent()
                        .putExtra("headImage",myInfoModel.getHeadImgUrl())
                        .putExtra("sex",myInfoModel.getSex())
                        .putExtra("name",myInfoModel.getNickname()));
            }
            SettingAccountActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
