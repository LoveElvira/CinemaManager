package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class SettingPointsActivity extends AbstractActivity implements View.OnClickListener {

    private RecyclerView listView;//列表
    //private SwipeRefreshLayout refresh;//刷新
    private List<PointsEntity> pointsEntityList;
    private PointsAdapter pointsAdapter;
    private TextView title;
    private ImageView leftArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_points);
        initView();
    }

    private void initView(){
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的积分");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_toolbar__left:
                SettingPointsActivity.this.finish();
                break;
        }
    }
}
