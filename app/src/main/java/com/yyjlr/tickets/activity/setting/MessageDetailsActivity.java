package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.message.MyMessageInfo;

/**
 * Created by Elvira on 2016/8/3.
 * 消息详情
 */
public class MessageDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private TextView messageTitle;
    private TextView message;
    private TextView time;

    private TextView title;
    private ImageView leftArrow;

    private MyMessageInfo messageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        messageInfo = (MyMessageInfo) getIntent().getSerializableExtra("messageInfo");
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("消息详情");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        messageTitle = (TextView) findViewById(R.id.content_message_details__title);
        message = (TextView) findViewById(R.id.content_message_details__message);
        time = (TextView) findViewById(R.id.content_message_details__time);

        messageTitle.setText(messageInfo.getTitle());
        message.setText(messageInfo.getContent());
        time.setText(ChangeUtils.changeTime(messageInfo.getSendDate()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                MessageDetailsActivity.this.finish();
                break;
        }
    }
}
