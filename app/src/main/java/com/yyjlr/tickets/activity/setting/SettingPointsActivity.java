package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.PointsAdapter;
import com.yyjlr.tickets.model.PointsEntity;

import java.util.List;

import static com.yyjlr.tickets.Application.getInstance;

/**
 * Created by Elvira on 2016/9/23.
 * 我的积分
 */

public class SettingPointsActivity extends AbstractActivity {

    private RecyclerView listView;//列表
    //private SwipeRefreshLayout refresh;//刷新
    private List<PointsEntity> pointsEntityList;
    private PointsAdapter pointsAdapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_points);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的积分");
        listView = (RecyclerView) findViewById(R.id.content_setting_points__listview);
        pointsEntityList = Application.getiDataService().getPointsList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        initAdapter();
    }

    private void initAdapter() {
        pointsAdapter = new PointsAdapter(pointsEntityList);
        pointsAdapter.openLoadAnimation();
        listView.setAdapter(pointsAdapter);
        mCurrentCounter = pointsAdapter.getData().size();
        //pointsAdapter.setOnLoadMoreListener(this);
        //pointsAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        //pointsAdapter.setOnRecyclerViewItemChildClickListener(this);
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
                SettingPointsActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
