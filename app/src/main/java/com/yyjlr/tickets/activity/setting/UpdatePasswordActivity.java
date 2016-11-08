package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;

/**
 * Created by Elvira on 2016/9/23.
 * 找回密码
 */

public class UpdatePasswordActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("修改密码");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_toolbar__left:
                UpdatePasswordActivity.this.finish();
                break;
        }
    }
}
