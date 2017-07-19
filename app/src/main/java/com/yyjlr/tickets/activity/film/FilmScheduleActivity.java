package com.yyjlr.tickets.activity.film;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FilmScheduleImageAdapter;
import com.yyjlr.tickets.adapter.FilmScheduleSeasonAdapter;
import com.yyjlr.tickets.adapter.FilmScheduleTimeAdapter;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.FilmSeasonEntity;
import com.yyjlr.tickets.model.FilmTimeEntity;
import com.yyjlr.tickets.model.film.FilmPlan;
import com.yyjlr.tickets.model.film.FilmPlanContent;
import com.yyjlr.tickets.model.film.FilmPlanModel;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.gallery.WGallery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/2.
 * 影片排期
 */
public class FilmScheduleActivity extends AbstractActivity implements BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private RecyclerView timeList;
    private RecyclerView listView;
    private FilmScheduleTimeAdapter timeAdapter;
    private FilmScheduleSeasonAdapter seasonAdapter;
    private List<FilmSeasonEntity> filmSeasonEntityList;
    private List<FilmTimeEntity> filmTimeEntityList;
    private List<Map<String, String>> mapList;
    private FilmPlanModel filmPlanModel;//排期实体类
    private List<FilmPlan> filmPlanList;//排期列表

    private FilmScheduleImageAdapter imageAdapter;
    WGallery wGallery;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_schedule);
        AppManager.getInstance().initWidthHeight(getBaseContext());
        initView();

    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

//        wGallery = (WGallery) findViewById(R.id.content_film_schedule__wgallery);
//        imageAdapter = new FilmScheduleImageAdapter(initData());
//        wGallery.setAdapter(imageAdapter);

        timeList = (RecyclerView) findViewById(R.id.content_film_schedule__time);
        listView = (RecyclerView) findViewById(R.id.content_film_schedule__listview);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        timeList.setLayoutManager(linearLayoutManager);
//        mapList = Application.getiDataService().getMapList();
//        timeAdapter = new FilmScheduleTimeAdapter(mapList);
//        timeList.setAdapter(timeAdapter);
//        timeAdapter.setOnRecyclerViewItemChildClickListener(this);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager1);
        filmSeasonEntityList = Application.getiDataService().getSeasonList();
//        initAdapter();
        getFilmPlan();
    }

    //获取影片排期
    private void getFilmPlan() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setMovieId(getIntent().getStringExtra("filmId"));
        OkHttpClientManager.postAsyn(Config.GET_FILM_PLAN, new OkHttpClientManager.ResultCallback<FilmPlanModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(FilmPlanModel response) {
                title.setText(response.getName());
                mapList = new ArrayList<>();
                filmPlanList = new ArrayList<>();
                if (response.getPlanList() != null && response.getPlanList().size() > 0) {
                    for (int i = 0; i < response.getPlanList().size(); i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("date", response.getPlanList().get(i).getDate());
                        if (i == 0) {
                            map.put("show", "1");
                        } else {
                            map.put("show", "0");
                        }
                        mapList.add(map);
                        filmPlanList.add(response.getPlanList().get(i));
                    }

                    timeAdapter = new FilmScheduleTimeAdapter(mapList);
                    timeList.setAdapter(timeAdapter);
                    timeAdapter.setOnRecyclerViewItemChildClickListener(FilmScheduleActivity.this);

                    seasonAdapter = new FilmScheduleSeasonAdapter(filmPlanList.get(0).getSessionList());
                    listView.setAdapter(seasonAdapter);
                    seasonAdapter.setOnRecyclerViewItemChildClickListener(FilmScheduleActivity.this);
                }
                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, FilmPlanModel.class, FilmScheduleActivity.this);
    }

//    private List<Integer> initData() {
//        List<Integer> list = new ArrayList<>();
//        list.add(R.mipmap.image_1);
//        list.add(R.mipmap.image_2);
//        list.add(R.mipmap.image_3);
//        list.add(R.mipmap.image_4);
//        list.add(R.mipmap.image_1);
//        list.add(R.mipmap.image_2);
//        list.add(R.mipmap.image_3);
//        list.add(R.mipmap.image_4);
//        return list;
//    }

    private void initAdapter() {
//        seasonAdapter = new FilmScheduleSeasonAdapter(filmSeasonEntityList);
//        seasonAdapter.openLoadAnimation();
//        listView.setAdapter(seasonAdapter);
//        mCurrentCounter = seasonAdapter.getData().size();
//        seasonAdapter.setOnLoadMoreListener(this);
//        seasonAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
//        seasonAdapter.setOnRecyclerViewItemChildClickListener(this);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {

        switch (view.getId()) {

            case R.id.item_schedule__buy_ticket://购票
            case R.id.item_schedule__parent://购票
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", FilmScheduleActivity.this);
                    if (isLogin.equals("1")) {
                        startActivity(new Intent(getBaseContext(), FilmSelectSeatActivity.class)
                                .putExtra("planId", filmPlanList.get(this.position).getSessionList().get(position).getPlanId())
                                .putExtra("isFirst", true));
                    } else {
                        startActivity(LoginActivity.class);
                    }
                }
                break;
            case R.id.item_film_schedule__layout_parent://选择时间item
                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, String> map = new HashMap<String, String>();
                    if (i == position)
                        map.put("show", "1");
                    else
                        map.put("show", "0");
                    map.put("date", mapList.get(i).get("date"));
//                    map.put("time", mapList.get(i).get("time"));
                    list.add(map);
                }
                mapList.clear();
                mapList = list;
                timeAdapter.setNewData(mapList);

                this.position = position;
                if (filmPlanList.get(this.position).getSessionList() != null && filmPlanList.get(this.position).getSessionList().size() > 0) {
                    seasonAdapter = new FilmScheduleSeasonAdapter(filmPlanList.get(this.position).getSessionList());
                    listView.setAdapter(seasonAdapter);
                    seasonAdapter.setOnRecyclerViewItemChildClickListener(FilmScheduleActivity.this);
                    seasonAdapter.notifyDataSetChanged();
                } else {
                    List<FilmPlanContent> filmPlanContentList = new ArrayList<>();
                    seasonAdapter = new FilmScheduleSeasonAdapter(filmPlanContentList);
                    listView.setAdapter(seasonAdapter);
                    seasonAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                FilmScheduleActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode){
            case CODE_REQUEST_DIALOG:
                getFilmPlan();
                break;
        }
    }
}
