package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.R;

/**
 * Created by Elvira on 2016/8/3.
 * 活动 明星见面会
 */
public class EventActivity extends AbstractActivity/* implements View.OnClickListener*/ {

    private TextView title;
    private NestedScrollView nestedScrollView;
    private View mContentView;
    private CardView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        AppManager.getInstance().initWidthHeight(this);
        title = (TextView) findViewById(R.id.base_toolbar__text);
//        initWidget();
        title.setText("XXX明星见面会");
    }


    private void initWidget() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.content_event__nested_scroll_view);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                        , RelativeLayout.LayoutParams.MATCH_PARENT);
        nestedScrollView.setLayoutParams(params);
        mContentView = findViewById(R.id.content_event__relayout);
        mContentView.setPadding(0, AppManager.getInstance().getHeight() / 3, 0, mContentView.getPaddingBottom() * 2);
        mCardView = (CardView) findViewById(R.id.content_event__cardview);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                        , AppManager.getInstance().getWidth() * 105 / 100);
        layoutParams.setMargins(AppManager.getInstance().getWidth() * 5 / 100, AppManager.getInstance().getHeight()/4
                , AppManager.getInstance().getWidth() * 5 / 100, 0);
        mCardView.setLayoutParams(layoutParams);
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
                EventActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
