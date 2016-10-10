package com.yyjlr.tickets.activity;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.SeatTypeAdapter;
import com.yyjlr.tickets.model.FilmSeatEntity;
import com.yyjlr.tickets.viewutils.SeatTableView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/10.
 * 选择座位
 */
public class FilmSelectSeatActivity extends AbstractActivity implements View.OnClickListener {

    private TextView confirmSeat;
    private TextView title;
    private TextView seatTitle;
    private TextView seatTime;
    private SeatTableView seatTableView;
    private LinearLayout addSelectSeatLayout;
    private List<FilmSeatEntity.Response.SeatList> seatSelectList = new ArrayList<FilmSeatEntity.Response.SeatList>();
    private List<FilmSeatEntity.Response.SeatList> seatSelectList_1 = null;
    private List<FilmSeatEntity.Response.SeatType> seatTypeList = new ArrayList<FilmSeatEntity.Response.SeatType>();

    private RecyclerView seatTypeListView;
    private SeatTypeAdapter seatTypeAdapter;
    protected LinearLayout showSeatRecommendLayout;//显示推荐座位 1人座 2人座 3人座 4人座
    private TextView seatOne, seatTwo, seatThree, seatFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_select_seat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("XXX特价观影活动");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        seatTypeListView = (RecyclerView) findViewById(R.id.content_film_seat__type_listview);
        seatTableView = (SeatTableView) findViewById(R.id.content_film_seat__show_seat);
        addSelectSeatLayout = (LinearLayout) findViewById(R.id.content_film_seat__show_select_seat_layout);
        confirmSeat = (TextView) findViewById(R.id.content_film_seat__confirm_seat);
        seatTitle = (TextView) findViewById(R.id.content_film_seat__seat_title);
        seatTime = (TextView) findViewById(R.id.content_film_seat__time);
        showSeatRecommendLayout = (LinearLayout) findViewById(R.id.content_film_seat__seat_recommend_layout);
        seatOne = (TextView) findViewById(R.id.content_film_seat__seat_recommend_one);
        seatTwo = (TextView) findViewById(R.id.content_film_seat__seat_recommend_two);
        seatThree = (TextView) findViewById(R.id.content_film_seat__seat_recommend_three);
        seatFour = (TextView) findViewById(R.id.content_film_seat__seat_recommend_four);
        seatTitle.setText("专座推荐");

        AssetManager assetManager = getBaseContext().getAssets();
        Typeface font = Typeface.createFromAsset(assetManager, "fonts/Digital2.ttf");
        seatTime.setTypeface(font);


        confirmSeat.setOnClickListener(this);
        seatOne.setOnClickListener(this);
        seatTwo.setOnClickListener(this);
        seatThree.setOnClickListener(this);
        seatFour.setOnClickListener(this);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        seatTypeListView.setLayoutManager(linearLayoutManager);

        seatTableView.setMaxSelected(4);//设置最多选中
        seatTableView.setSeatChecker(seatChecker);
        Gson gson = new Gson();
        Log.i("ee", getAssets().toString() + "------" + readFromAsset("seat.json"));
        FilmSeatEntity filmSeatEntity = gson.fromJson(readFromAsset("seat.json"), FilmSeatEntity.class);
        seatTableView.setData(filmSeatEntity.getResponse().getSeatList(), filmSeatEntity.getResponse().getSeatType());
        seatTableView.setScreenName(filmSeatEntity.getResponse().getHallName());//设置屏幕名称
        for (int i = 0; i < filmSeatEntity.getResponse().getSeatType().size(); i++) {
            if (filmSeatEntity.getResponse().getSeatType().get(i).getIsShow().equals("1")) {
                seatTypeList.add(filmSeatEntity.getResponse().getSeatType().get(i));
            }
        }

        seatTypeAdapter = new SeatTypeAdapter(seatTypeList);
        seatTypeListView.setAdapter(seatTypeAdapter);
    }

    public String readFromAsset(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    getAssets().open(fileName), "UTF-8"));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //选座
    SeatTableView.SeatChecker seatChecker = new SeatTableView.SeatChecker() {
        @Override
        public boolean isValidSeat(int row, int column, String flag) {
            if (flag.equals("1")) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isSold(int row, int column, String type) {
            if (type.equals("0-2") ||
                    type.equals("1-2") ||
                    type.equals("2-2") ||
                    type.equals("3-2") ||
                    type.equals("4-2")) {
                return true;
            }
            return false;
        }

        @Override
        public void checked(FilmSeatEntity.Response.SeatList seatList) {
            seatSelectList.add(seatList);
            addSelectSeatText();
            seatTableView.selectSeat(seatList);
            seatSelectList_1 = seatSelectList;
        }

        @Override
        public void unCheck(FilmSeatEntity.Response.SeatList seatList) {
            for (int i = 0; i < seatSelectList.size(); i++) {
                if (seatSelectList.get(i).getRow().equals(seatList.getRow())
                        && seatSelectList.get(i).getCol().equals(seatList.getCol())) {
                    seatSelectList.remove(i);
                }
            }
            addSelectSeatText();
            seatTableView.cancelSeat(seatList);
            seatSelectList_1 = seatSelectList;
        }

        @Override
        public String[] checkedSeatTxt(int row, int column) {
            return null;
        }
    };

    //添加已经选定的座位
    private void addSelectSeatText() {

        addSelectSeatLayout.removeAllViews();
        if (seatSelectList.size()<=0){
            seatTitle.setText("专座推荐");
            showSeatRecommendLayout.setVisibility(View.VISIBLE);
        }else {
            seatTitle.setText("已选座位");
            showSeatRecommendLayout.setVisibility(View.GONE);
        }
        for (int i = 0; i < seatSelectList.size(); i++) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_seat_select_text, null);
            TextView seat = (TextView) view.findViewById(R.id.item_seat_select__text);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.item_seat_select__layout);
            final String row = seatSelectList.get(i).getRow();
            final String col = seatSelectList.get(i).getCol();
            seat.setText(row + "排" + col + "座");
            final int finalI = i;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(7, 0, 7, 0);
            layout.setLayoutParams(layoutParams);

//            layoutParams.leftMargin = 7;
//            layoutParams.rightMargin = 7;
//            layoutParams.topMargin = 0;
//            layoutParams.bottomMargin = 0;

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    seatTableView.cancelSeat(seatSelectList_1.get(finalI));
                    seatSelectList.remove(finalI);
//                    for (int i = 0; i < seatSelectList.size(); i++) {
//                        if (seatSelectList.get(i).getRow().equals(row)
//                                && seatSelectList.get(i).getCol().equals(col)) {
//                            if (seatSelectList.get(i).getType().equals("1")){
//                                seatTableView.cancelSeat(seatSelectList_1.get(finalI+1));
//                                seatSelectList.remove(i);//先移除  i   i+1 就变成 i
//                                seatSelectList.remove(i);//i+1
//                            }else if(seatSelectList.get(i).getType().equals("2")){
//                                seatTableView.cancelSeat(seatSelectList_1.get(finalI-1));
//                                seatSelectList.remove(i);
//                                seatSelectList.remove(i-1);
//                            }else {
//                                seatSelectList.remove(i);
//                            }
//                        }
//                    }
                    addSelectSeatText();
                }
            });
            addSelectSeatLayout.addView(view);
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
                FilmSelectSeatActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.content_film_seat__seat_recommend_one://1人座
                seatRecommend(1);
                break;
            case R.id.content_film_seat__seat_recommend_two://2人座
                seatRecommend(2);
                break;
            case R.id.content_film_seat__seat_recommend_three://3人座
                seatRecommend(3);
                break;
            case R.id.content_film_seat__seat_recommend_four://4人座
                seatRecommend(4);
                break;
            case R.id.content_film_seat__confirm_seat:
                startActivity(FilmCompleteActivity.class);
                break;
        }
    }

    private void seatRecommend(int num){
        seatTitle.setText("已选座位");
        showSeatRecommendLayout.setVisibility(View.GONE);
        seatTableView.selectSeatRecommend(num);
    }
}
