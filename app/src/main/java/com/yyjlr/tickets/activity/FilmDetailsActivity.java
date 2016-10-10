package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.FilmDetailsPeopleAdapter;
import com.yyjlr.tickets.model.FilmPeopleEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影片详情
 */
public class FilmDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private TextView collect;//收藏
    private TextView share;//分享
    private TextView selectSeat;//购票选座
    private ImageView bgImage;
    private ImageView image;
    private ImageView upOrDown;
    private TextView filmName;//电影名
    private TextView filmEnglishName;//电影名
    private TextView type;//类型
    private TextView duration;//时长
    private TextView degree;//2D 3D 4D
    private TextView country;//国家
    private TextView showTime;//上映
    private TextView introduce;//影片介绍
    private RecyclerView listView;
    private RatingBar score;//评分
    private FilmDetailsPeopleAdapter adapter;
    private List<FilmPeopleEntity> filmPeopleEntityList;
    private TextView title;
    private static final int FILM_CONTENT_DESC_MAX_LINE = 3;
    private static final int SHRINK_UP_STATE = 1;
    private static final int SPREAD_STATE = 2;
    private static int mState = SHRINK_UP_STATE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_film_details_title));
        collect = (TextView) findViewById(R.id.content_film_details__collect);
        share = (TextView) findViewById(R.id.content_film_details__share);
        selectSeat = (TextView) findViewById(R.id.content_film_details__select_seat);
        bgImage = (ImageView) findViewById(R.id.content_film_details__bgimage);
        image = (ImageView) findViewById(R.id.content_film_details__image);
        upOrDown = (ImageView) findViewById(R.id.content_film_details__down);
        filmName = (TextView) findViewById(R.id.content_film_details__name);
        filmEnglishName = (TextView) findViewById(R.id.content_film_details__english_name);
        type = (TextView) findViewById(R.id.content_film_details__type);
        duration = (TextView) findViewById(R.id.content_film_details__duration);
        degree = (TextView) findViewById(R.id.content_film_details__degree);
        country = (TextView) findViewById(R.id.content_film_details__country);
        showTime = (TextView) findViewById(R.id.content_film_details__showtime);
        introduce = (TextView) findViewById(R.id.content_film_details__introduce);
        listView = (RecyclerView) findViewById(R.id.content_film_details__listview);
        score = (RatingBar) findViewById(R.id.content_film_details__star);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        filmPeopleEntityList = Application.getiDataService().getFilmPeopleList();
        adapter = new FilmDetailsPeopleAdapter(filmPeopleEntityList);
        listView.setAdapter(adapter);

        collect.setOnClickListener(this);
        share.setOnClickListener(this);
        selectSeat.setOnClickListener(this);
        upOrDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_film_details__down://显示全部介绍
                if (mState == SPREAD_STATE) {//显示全部
                    introduce.setMaxLines(FILM_CONTENT_DESC_MAX_LINE);
                    introduce.requestLayout();
                    upOrDown.setBackgroundResource(R.mipmap.down_arrow);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {//隐藏部分
                    introduce.setMaxLines(Integer.MAX_VALUE);
                    introduce.requestLayout();
                    upOrDown.setBackgroundResource(R.mipmap.up_arrow);
                    mState = SPREAD_STATE;
                }

                break;
            case R.id.content_film_details__collect://收藏
                break;
            case R.id.content_film_details__share://分享
                break;
            case R.id.content_film_details__select_seat://选座购票
                startActivity(FilmScheduleActivity.class);
                break;
        }
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
                FilmDetailsActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
