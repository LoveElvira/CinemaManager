package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;

/**
 * Created by Elvira on 2016/8/3.
 * 消息详情
 */
public class MessageDetailsActivity extends AbstractActivity {

    private TextView messageTitle;
    private TextView message;
    private TextView time;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("消息详情");

        messageTitle = (TextView) findViewById(R.id.content_message_details__title);
        message = (TextView) findViewById(R.id.content_message_details__message);
        time = (TextView) findViewById(R.id.content_message_details__time);

        messageTitle.setText(getIntent().getStringExtra("title"));
        message.setText(getIntent().getStringExtra("message"));
        time.setText(getIntent().getStringExtra("time"));

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
                MessageDetailsActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
