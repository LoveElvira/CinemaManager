package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.content.MySettingContent;

/**
 * Created by Elvira on 2016/9/27.
 * 修改昵称
 */

public class AccountNameActivity extends AbstractActivity{

    private TextView title;
    private TextView saveName;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("修改昵称");

        userName = (EditText) findViewById(R.id.content_account_name__name);
        userName.setText(getIntent().getStringExtra("userName"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        menu.getItem(0).setTitle("保存");
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String name = userName.getText().toString();
                if (!"".equals(name)){
                    SettingAccountActivity.userName.setText(name);
                    MySettingContent.userName.setText(name);
                    AccountNameActivity.this.finish();
                }else{
                    showShortToast("昵称不能为空");
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AccountNameActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
