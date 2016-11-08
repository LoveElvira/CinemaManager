package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FilmScheduleImageAdapter;
import com.yyjlr.tickets.adapter.FilmScheduleSeasonAdapter;
import com.yyjlr.tickets.adapter.FilmScheduleTimeAdapter;
import com.yyjlr.tickets.model.FilmSeasonEntity;
import com.yyjlr.tickets.model.FilmTimeEntity;
import com.yyjlr.tickets.viewutils.gallery.WGallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/2.
 * 影片排期
 */
public class FilmScheduleActivity extends AbstractActivity implements BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private RecyclerView timeList;
    private RecyclerView listView;
    private FilmScheduleTimeAdapter timeAdapter;
    private FilmScheduleSeasonAdapter seasonAdapter;
    private List<FilmSeasonEntity> filmSeasonEntityList;
    private List<FilmTimeEntity> filmTimeEntityList;
    private List<Map<String, String>> mapList;

    private FilmScheduleImageAdapter imageAdapter;
    WGallery wGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_schedule);

        AppManager.getInstance().initWidthHeight(getBaseContext());
        initView();

    }

    private void initView(){
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("愤怒的小鸟");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        wGallery = (WGallery) findViewById(R.id.content_film_schedule__wgallery);
        imageAdapter = new FilmScheduleImageAdapter(initData());
        wGallery.setAdapter(imageAdapter);

        timeList = (RecyclerView) findViewById(R.id.content_film_schedule__time);
        listView = (RecyclerView) findViewById(R.id.content_film_schedule__listview);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        timeList.setLayoutManager(linearLayoutManager);
        mapList = Application.getiDataService().getMapList();
        timeAdapter = new FilmScheduleTimeAdapter(mapList);
        timeList.setAdapter(timeAdapter);
        timeAdapter.setOnRecyclerViewItemChildClickListener(this);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager1);
        filmSeasonEntityList = Application.getiDataService().getSeasonList();
        initAdapter();
    }

    private List<Integer> initData() {
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.image_1);
        list.add(R.mipmap.image_2);
        list.add(R.mipmap.image_3);
        list.add(R.mipmap.image_4);
        list.add(R.mipmap.image_1);
        list.add(R.mipmap.image_2);
        list.add(R.mipmap.image_3);
        list.add(R.mipmap.image_4);
        return list;
    }

    private void initAdapter() {
        seasonAdapter = new FilmScheduleSeasonAdapter(filmSeasonEntityList);
        seasonAdapter.openLoadAnimation();
        listView.setAdapter(seasonAdapter);
        mCurrentCounter = seasonAdapter.getData().size();
        seasonAdapter.setOnLoadMoreListener(this);
        seasonAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        seasonAdapter.setOnRecyclerViewItemChildClickListener(this);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    seasonAdapter.notifyDataChangedAfterLoadMore(false);
                    if (notLoadingView == null) {
                        notLoadingView = getLayoutInflater().inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
                    }
                    seasonAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            seasonAdapter.notifyDataChangedAfterLoadMore(filmSeasonEntityList, true);
                            mCurrentCounter = seasonAdapter.getData().size();
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_schedule__buy_ticket://购票
                startActivity(FilmSelectSeatActivity.class);
                break;
            case R.id.item_schedule__parent://购票
                startActivity(FilmSelectSeatActivity.class);
                break;
            case R.id.item_film_schedule__layout_parent://选择时间item
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    if (i == position)
                        map.put("show", "0");
                    else
                        map.put("show", "1");
                    map.put("week", mapList.get(i).get("week"));
                    map.put("time", mapList.get(i).get("time"));
                    list.add(map);
                }
                mapList.clear();
                mapList = list;
                timeAdapter.setNewData(mapList);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_toolbar__left:
                FilmScheduleActivity.this.finish();
                break;
        }
    }
}
