package com.yyjlr.tickets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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
import com.yyjlr.tickets.model.register.RegisterModel;
import com.yyjlr.tickets.requestdata.register.RegisterRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.Calendar;

/**
 * Created by Elvira on 2016/7/29.
 * 登陆
 */
public class LoginActivity extends AbstractActivity implements View.OnClickListener {

    private EditText phoneNum;
    private EditText password;
    private Button register;
    private Button login;
    private LinearLayout weixinLogin;
    private LinearLayout friendCircleLogin;
    private LinearLayout xinlangLogin;
    private LinearLayout forgetPassword;
    private TextView title;
    private ImageView leftArrow;
    private String pager = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pager = getIntent().getStringExtra("pager");
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_login));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        phoneNum = (EditText) findViewById(R.id.content_login__phone);
        password = (EditText) findViewById(R.id.content_login__password);
        register = (Button) findViewById(R.id.content_login__register);
        login = (Button) findViewById(R.id.content_login__login);
        weixinLogin = (LinearLayout) findViewById(R.id.content_login__weixin);
        friendCircleLogin = (LinearLayout) findViewById(R.id.content_login__friend_circle);
        xinlangLogin = (LinearLayout) findViewById(R.id.content_login__xinlang);
        forgetPassword = (LinearLayout) findViewById(R.id.content_login__forger_password);

        String userName = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PHONE, "", LoginActivity.this);
        String passWord = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PASSWORD, "", LoginActivity.this);
        phoneNum.setText(userName);
        password.setText(passWord);


        register.setOnClickListener(this);
        login.setOnClickListener(this);
        weixinLogin.setOnClickListener(this);
        friendCircleLogin.setOnClickListener(this);
        xinlangLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    private boolean isNull() {
        if (!isMobileNum(phoneNum.getText().toString().trim())) {
            showShortToast("手机号码不对");
            return false;
        } else if ("".equals(password.getText().toString().trim())) {
            showShortToast("密码不对");
            return false;
        }
        return true;
    }


    //用户登录
    private void login() {
        customDialog = new CustomDialog(this, "登录中...");
        customDialog.show();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPhone(phoneNum.getText().toString().trim());
        registerRequest.setPwd(password.getText().toString().trim());
        OkHttpClientManager.postAsyn(Config.LOGIN, new OkHttpClientManager.ResultCallback<RegisterModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                customDialog.dismiss();
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(RegisterModel response) {

                SharePrefUtil.putString(Constant.FILE_NAME, "token", response.getToken(), LoginActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PHONE, phoneNum.getText().toString().trim(), LoginActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, Constant.PASSWORD, password.getText().toString().trim(), LoginActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, "flag", "1", LoginActivity.this);
                customDialog.dismiss();
//                if (pager.equals("FilmSelectSeatActivity")) {
//                } else {
//                    startActivity(MainActivity.class);
//                }
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isFinish", true));
                MainActivity.mySettingContent.updateView(true);
                LoginActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, registerRequest, RegisterModel.class, LoginActivity.this);
    }

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.base_toolbar__left:
                    setResult(CODE_RESULT, new Intent()
                            .putExtra("isFinish", true));
                    LoginActivity.this.finish();
                    break;
                case R.id.content_login__register:
                    startActivity(RegisterActivity.class);
                    break;
                case R.id.content_login__login:
                    if (isNull()) {
                        login();
                    }
                    break;
                case R.id.content_login__weixin:
                    break;
                case R.id.content_login__friend_circle:
                    break;
                case R.id.content_login__xinlang:
                    break;
                case R.id.content_login__forger_password:
                    startActivityForResult(new Intent(LoginActivity.this, FindPasswordActivity.class), CODE_REQUEST_ONE);
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(CODE_RESULT, new Intent()
                    .putExtra("isFinish", true));
            LoginActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE://
                String userName = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PHONE, "", LoginActivity.this);
                String passWord = SharePrefUtil.getString(Constant.FILE_NAME, Constant.PASSWORD, "", LoginActivity.this);
                phoneNum.setText(userName);
                password.setText(passWord);
//                setResult(CODE_RESULT, new Intent()
//                        .putExtra("isFinish", true));
//                LoginActivity.this.finish();
                break;
        }
    }
}
