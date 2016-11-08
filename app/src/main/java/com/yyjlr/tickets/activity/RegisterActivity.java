package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
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

    private void initView(){
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_toolbar__left:
                RegisterActivity.this.finish();
                break;
            case R.id.content_register__getcode:
                timeCount.start();
                break;
            case R.id.content_register__register:
                startActivity(MainActivity.class);
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
