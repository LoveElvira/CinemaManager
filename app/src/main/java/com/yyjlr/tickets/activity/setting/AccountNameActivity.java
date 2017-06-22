package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.content.MySettingContent;

/**
 * Created by Elvira on 2016/9/27.
 * 修改昵称
 */

public class AccountNameActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private TextView rightSave;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_name);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("修改昵称");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        rightArrow = (ImageView) findViewById(R.id.base_toolbar__right);
        rightArrow.setVisibility(View.GONE);
        rightSave = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightSave.setText("保存");
        rightSave.setOnClickListener(this);
        rightSave.setVisibility(View.VISIBLE);

        userName = (EditText) findViewById(R.id.content_account_name__name);
        userName.setText(getIntent().getStringExtra("userName"));
        userName.setSelection(getIntent().getStringExtra("userName").length());
        userName.addTextChangedListener(textWatcher);
        imm.showSoftInput(userName, 0);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                userName.setHint("填写昵称");
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                AccountNameActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text:
                String nick = userName.getText().toString().trim();
                if (nick.length() > 0) {
                    setResult(CODE_RESULT, new Intent().putExtra("nickName", nick));
                    AccountNameActivity.this.finish();
                } else {
                    showShortToast("昵称不能为空!");
                }
                break;
        }
    }
}
