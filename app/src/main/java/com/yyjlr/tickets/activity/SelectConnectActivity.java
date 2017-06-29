package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.SharePrefUtil;

/**
 * Created by Elvira on 2017/6/26.
 * 选择服务环境
 */

public class SelectConnectActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;

    private TextView test, formal, confirm;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_url);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("选择服务环境");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        test = (TextView) findViewById(R.id.select_url__test);
        formal = (TextView) findViewById(R.id.select_url__formal);
        confirm = (TextView) findViewById(R.id.select_url__confrim);

        edit = (EditText) findViewById(R.id.select_url__edit);

        test.setOnClickListener(this);
        formal.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                SelectConnectActivity.this.finish();
                break;
            case R.id.select_url__test:
                SharePrefUtil.putString(Constant.FILE_NAME, "appDomain", "50120255", SelectConnectActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, "url", "http://139.196.51.15", SelectConnectActivity.this);
                SelectConnectActivity.this.finish();
                break;
            case R.id.select_url__formal:
                SharePrefUtil.putString(Constant.FILE_NAME, "appDomain", "33040301", SelectConnectActivity.this);
                SharePrefUtil.putString(Constant.FILE_NAME, "url", "http://139.196.249.63", SelectConnectActivity.this);
                SelectConnectActivity.this.finish();
                break;
            case R.id.select_url__confrim:
                if (!"".equals(edit.getText().toString().trim())) {
                    SharePrefUtil.putString(Constant.FILE_NAME, "appDomain", "50120255", SelectConnectActivity.this);
                    SharePrefUtil.putString(Constant.FILE_NAME, "url", edit.getText().toString().trim(), SelectConnectActivity.this);
                    SelectConnectActivity.this.finish();
                } else {
                    showShortToast("请先自定义IP");
                }
                break;
        }
    }
}
