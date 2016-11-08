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
 * Created by Elvira on 2016/10/28.
 * 忘记密码
 */

public class FindPasswordActivity extends AbstractActivity implements View.OnClickListener {

    private EditText phoneNum;
    private EditText code;
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

    private void initView(){
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("忘记密码");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        timeCount = new TimeCount(60000, 1000);
        phoneNum = (EditText) findViewById(R.id.content_find_pwd__phone);
        code = (EditText) findViewById(R.id.content_find_pwd__code);
        next = (Button) findViewById(R.id.content_find_pwd__next);
        getCode = (Button) findViewById(R.id.content_find_pwd__getcode);

        getCode.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.base_toolbar__left:
                FindPasswordActivity.this.finish();
                break;
            case R.id.content_find_pwd__getcode:
                timeCount.start();
                break;
            case R.id.content_find_pwd__next:
                startActivity(ResetPasswordActivity.class);
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
