package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.message.MyMessageDetailsBean;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;

/**
 * Created by Elvira on 2016/8/3.
 * 消息详情
 */
public class SettingMessageDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private TextView messageTitle;
    private TextView message;
    private TextView time;

    private TextView title;
    private ImageView leftArrow;

    private long messageId = 0;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        messageId = getIntent().getLongExtra("messageId", -1);
        position = getIntent().getIntExtra("position", -1);
        initView();
        getMessageDetails();
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
    }

    //我的消息详情
    private void getMessageDetails() {
        IdRequest idRequest = new IdRequest();
        idRequest.setId(messageId + "");
        OkHttpClientManager.postAsyn(Config.GET_MESSAGE_DETAILS, new OkHttpClientManager.ResultCallback<MyMessageDetailsBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(MyMessageDetailsBean response) {

                if (response != null) {
                    if (response.getDetail() != null) {
                        messageTitle.setText(response.getDetail().getTitle());
                        message.setText(response.getDetail().getContent());
                        time.setText(ChangeUtils.changeTime(response.getDetail().getSendDate()));
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, idRequest, MyMessageDetailsBean.class, SettingMessageDetailsActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                setResult(CODE_RESULT, new Intent()
                        .putExtra("position", position));
                SettingMessageDetailsActivity.this.finish();
                break;
        }
    }
}
