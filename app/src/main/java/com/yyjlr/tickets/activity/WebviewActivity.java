package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;

/**
 * Created by Elvira on 2017/4/26.
 * 指向性的webview页面
 */

public class WebviewActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView back;
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getString(R.string.app_name));
        back = (ImageView) findViewById(R.id.base_toolbar__left);
        back.setAlpha(1.0f);
        back.setImageResource(R.mipmap.left_arrow);

        webView = (WebView) findViewById(R.id.content_webview__content);
        url = getIntent().getStringExtra("url");
        webView.getSettings().setDefaultTextEncodingName("gbk");
        webView.loadUrl(url);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                WebviewActivity.this.finish();
                break;
        }
    }
}
