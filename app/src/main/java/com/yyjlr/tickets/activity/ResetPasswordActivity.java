package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;

import java.util.Calendar;

/**
 * Created by Elvira on 2016/10/28.
 * 重设密码
 */

public class ResetPasswordActivity extends AbstractActivity implements View.OnClickListener {
    private EditText password;
    private EditText confirmPassword;
    private Button confirm;
    private TextView title;
    private ImageView leftArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();

    }

    private void initView(){
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("重设密码");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        password = (EditText) findViewById(R.id.content_reset_pwd__password);
        confirmPassword = (EditText) findViewById(R.id.content_reset_pwd__confirm_password);
        confirm = (Button) findViewById(R.id.content_reset_pwd__confirm);

        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            switch (view.getId()) {
                case R.id.base_toolbar__left:
                    ResetPasswordActivity.this.finish();
                    break;
                case R.id.content_reset_pwd__confirm:
                    startActivity(MainActivity.class);
                    break;
            }
        }
    }
}
