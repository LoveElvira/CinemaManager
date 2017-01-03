package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.register.RegisterCode;
import com.yyjlr.tickets.model.register.RegisterModel;
import com.yyjlr.tickets.requestdata.register.CodeRequest;
import com.yyjlr.tickets.requestdata.register.RegisterRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.ArrayList;

/**
 * Created by Elvira on 2016/7/29.
 * 注册
 */
public class RegisterActivity extends AbstractActivity implements View.OnClickListener {

    private EditText phoneNum;
    private EditText code;
    private EditText password;
    private EditText confirmPassword;
    private Button getCode;
    private Button register;
    private LinearLayout haveAccount;
    private TimeCount timeCount;
    private TextView title;
    private ImageView leftArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_register));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        timeCount = new TimeCount(60000, 1000);
        phoneNum = (EditText) findViewById(R.id.content_register__phone);
        code = (EditText) findViewById(R.id.content_register__code);
        password = (EditText) findViewById(R.id.content_register__password);
        confirmPassword = (EditText) findViewById(R.id.content_register__confirm_password);
        register = (Button) findViewById(R.id.content_register__register);
        getCode = (Button) findViewById(R.id.content_register__getcode);
        haveAccount = (LinearLayout) findViewById(R.id.content_regster__have_account);

        getCode.setOnClickListener(this);
        register.setOnClickListener(this);
        haveAccount.setOnClickListener(this);
    }

    private boolean isNull() {
        if (!isMobileNum(phoneNum.getText().toString().trim())) {
            showShortToast("手机号码不对");
            return false;
        } else if ("".equals(code.getText().toString().trim())) {
            showShortToast("验证码不能为空");
            return false;
        } else if ("".equals(password.getText().toString().trim())) {
            showShortToast("密码不能为空");
            return false;
        } else if ("".equals(confirmPassword.getText().toString().trim())) {
            showShortToast("确认密码不能为空");
            return false;
        } else if (password.getText().toString().trim().length() > 12) {
            showShortToast("密码过长");
            return false;
        } else if (password.getText().toString().trim().length() < 6) {
            showShortToast("密码过短");
            return false;
        } else if (!(password.getText().toString().trim()).equals(confirmPassword.getText().toString().trim())) {
            showShortToast("两次输入密码不一致");
            return false;
        }
        return true;
    }

    //用户注册
    private void register() {
        customDialog = new CustomDialog(this, "注册中...");
        customDialog.show();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName(phoneNum.getText().toString().trim());
        registerRequest.setPhone(phoneNum.getText().toString().trim());
        registerRequest.setPassword(password.getText().toString().trim());
        registerRequest.setVerifyCode(code.getText().toString().trim());
        OkHttpClientManager.postAsyn(Config.REGISTER, new OkHttpClientManager.ResultCallback<RegisterModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                customDialog.dismiss();
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(RegisterModel response) {

                SharePrefUtil.putString(Constant.FILE_NAME, "token", response.getToken(), RegisterActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PHONE, response.getPhone(), RegisterActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PASSWORD, password.getText().toString().trim(), RegisterActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, "flag", "1", RegisterActivity.this);
                customDialog.dismiss();
                startActivity(MainActivity.class);
                RegisterActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, registerRequest, RegisterModel.class, RegisterActivity.this);
    }

    //用户注册验证码
    private void getRegisterCode() {
        customDialog = new CustomDialog(this, "获取验证码...");
        customDialog.show();
        CodeRequest codeRequest = new CodeRequest();
        codeRequest.setPhone(phoneNum.getText().toString().trim());
        OkHttpClientManager.postAsyn(Config.GET_REGISTER_CODE, new OkHttpClientManager.ResultCallback<RegisterCode>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(RegisterCode response) {
                customDialog.dismiss();
                showShortToast(response.getVerifyCode());
                timeCount.start();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, codeRequest, RegisterCode.class, RegisterActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                RegisterActivity.this.finish();
                break;
            case R.id.content_register__getcode:
                if (isMobileNum(phoneNum.getText().toString().trim())) {
                    getRegisterCode();
                } else {
                    showShortToast("手机号码不对");
                }
                break;
            case R.id.content_register__register:
                if (isNull()) {
                    register();
                }
                break;
            case R.id.content_regster__have_account:
                startActivity(LoginActivity.class);
                break;
        }
    }

    //获取验证码倒计时
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            getCode.setText(getResources().getString(R.string.text_get_code));
            getCode.setEnabled(true);
            getCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setClickable(false);
            getCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
