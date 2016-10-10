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
    private LinearLayout QQLogin;
    private LinearLayout xinlangLogin;
    private LinearLayout forgetPassword;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_login));
        phoneNum = (EditText) findViewById(R.id.content_login__phone);
        password = (EditText) findViewById(R.id.content_login__password);
        register = (Button) findViewById(R.id.content_login__register);
        login = (Button) findViewById(R.id.content_login__login);
        weixinLogin = (LinearLayout) findViewById(R.id.content_login__weixin);
        QQLogin = (LinearLayout) findViewById(R.id.content_login__qq);
        xinlangLogin = (LinearLayout) findViewById(R.id.content_login__xinlang);
        forgetPassword = (LinearLayout) findViewById(R.id.content_login__forger_password);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
        weixinLogin.setOnClickListener(this);
        QQLogin.setOnClickListener(this);
        xinlangLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_login__register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.content_login__login:
                startActivity(MainActivity.class);
                break;
            case R.id.content_login__weixin:
                break;
            case R.id.content_login__qq:
                break;
            case R.id.content_login__xinlang:
                break;
            case R.id.content_login__forger_password:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoginActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
