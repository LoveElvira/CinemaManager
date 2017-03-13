package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.register.RegisterModel;
import com.yyjlr.tickets.requestdata.register.RegisterRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.Calendar;

/**
 * Created by Elvira on 2016/9/23.
 * 找回密码
 */

public class UpdatePasswordActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private EditText oldPwd;
    private EditText newPwd;
    private EditText resetPwd;
    private TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        customDialog = new CustomDialog(this, "修改中...");
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("修改密码");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        oldPwd = (EditText) findViewById(R.id.content_update__old_pwd);
        newPwd = (EditText) findViewById(R.id.content_update__new_pwd);
        resetPwd = (EditText) findViewById(R.id.content_update__reset_pwd);
        confirm = (TextView) findViewById(R.id.content_update__confirm);

        leftArrow.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    private boolean isNull() {
        if ("".equals(oldPwd.getText().toString().trim())) {
            showShortToast("旧密码不能为空");
            return false;
        } else if ("".equals(newPwd.getText().toString().trim())) {
            showShortToast("新密码不能为空");
            return false;
        } else if ("".equals(resetPwd.getText().toString().trim())) {
            showShortToast("确认密码不能为空");
            return false;
        } else if (newPwd.getText().toString().trim().length() > 12) {
            showShortToast("密码过长");
            return false;
        } else if (newPwd.getText().toString().trim().length() < 6) {
            showShortToast("密码过短");
            return false;
        } else if (!(newPwd.getText().toString().trim()).equals(resetPwd.getText().toString().trim())) {
            showShortToast("新密码和确认密码不一致");
            return false;
        }
        return true;
    }

    //修改密码
    private void updatePwd() {
        customDialog.show();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setOldPwd(oldPwd.getText().toString().trim());
        registerRequest.setNewPwd(newPwd.getText().toString().trim());
        OkHttpClientManager.postAsyn(Config.CHANGE_PWD, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                customDialog.dismiss();
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(ResponeNull response) {
                customDialog.dismiss();
                UpdatePasswordActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, registerRequest, ResponeNull.class, UpdatePasswordActivity.this);
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (v.getId()) {
                case R.id.base_toolbar__left:
                    UpdatePasswordActivity.this.finish();
                    break;
                case R.id.content_update__confirm:
                    if (isNull()) {
                        updatePwd();
                    }
                    break;
            }
        }
    }
}
