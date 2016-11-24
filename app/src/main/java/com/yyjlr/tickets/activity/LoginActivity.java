package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
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

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        weixinLogin.setOnClickListener(this);
        friendCircleLogin.setOnClickListener(this);
        xinlangLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                LoginActivity.this.finish();
                break;
            case R.id.content_login__register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.content_login__login:
                String phone = phoneNum.getText().toString().trim();
                if (isMobileNum(phone)){
                    startActivity(MainActivity.class);
                }else {
                    showShortToast("手机号码不对");
                }
                break;
            case R.id.content_login__weixin:
                break;
            case R.id.content_login__friend_circle:
                break;
            case R.id.content_login__xinlang:
                break;
            case R.id.content_login__forger_password:
                startActivity(FindPasswordActivity.class);
                break;
        }
    }
}
