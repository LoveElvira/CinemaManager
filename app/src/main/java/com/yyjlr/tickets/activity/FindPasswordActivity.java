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
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.register.RegisterCode;
import com.yyjlr.tickets.model.register.RegisterModel;
import com.yyjlr.tickets.requestdata.register.CodeRequest;
import com.yyjlr.tickets.requestdata.register.RegisterRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

/**
 * Created by Elvira on 2016/10/28.
 * 忘记密码
 */

public class FindPasswordActivity extends AbstractActivity implements View.OnClickListener {

    private EditText phoneNum;
    private EditText code;
    private EditText password;
    private EditText confirmPassword;
    private Button getCode;
    private Button next;
    private TimeCount timeCount;
    private TextView title;
    private ImageView leftArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("找回密码");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        timeCount = new TimeCount(60000, 1000);
        phoneNum = (EditText) findViewById(R.id.content_find_pwd__phone);
        code = (EditText) findViewById(R.id.content_find_pwd__code);
        password = (EditText) findViewById(R.id.content_reset_pwd__password);
        confirmPassword = (EditText) findViewById(R.id.content_reset_pwd__confirm_password);
        next = (Button) findViewById(R.id.content_find_pwd__next);
        getCode = (Button) findViewById(R.id.content_find_pwd__getcode);

        getCode.setOnClickListener(this);
        next.setOnClickListener(this);
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

    //重置密码
    private void resetPwd() {
        customDialog = new CustomDialog(this, "设置中...");
        customDialog.show();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPhone(phoneNum.getText().toString().trim());
        registerRequest.setPwd(password.getText().toString().trim());
        registerRequest.setVerifyCode(code.getText().toString().trim());
        OkHttpClientManager.postAsyn(Config.RESET_PWD, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                customDialog.dismiss();
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ResponeNull response) {

                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PHONE, phoneNum.getText().toString().trim(), FindPasswordActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PASSWORD, password.getText().toString().trim(), FindPasswordActivity.this);
                customDialog.dismiss();
                startActivity(LoginActivity.class);
                FindPasswordActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, registerRequest, ResponeNull.class, FindPasswordActivity.this);
    }

    //获取重置密码短信验证码
    private void getResetCode() {
        customDialog = new CustomDialog(this, "获取验证码...");
        customDialog.show();
        CodeRequest codeRequest = new CodeRequest();
        codeRequest.setPhone(phoneNum.getText().toString().trim());
        OkHttpClientManager.postAsyn(Config.RESET_PWD_CODE, new OkHttpClientManager.ResultCallback<RegisterCode>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(RegisterCode response) {
                showShortToast(response.getVerifyCode());
                customDialog.dismiss();
                timeCount.start();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, codeRequest, RegisterCode.class, FindPasswordActivity.this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                FindPasswordActivity.this.finish();
                break;
            case R.id.content_find_pwd__getcode:
                if (isMobileNum(phoneNum.getText().toString().trim())) {
                    getResetCode();
                } else {
                    showShortToast("手机号码不对");
                }
                break;
            case R.id.content_find_pwd__next:
                if (isNull()) {
                    resetPwd();
                }
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
