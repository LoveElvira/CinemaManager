package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.CinemaDetailsActivity;
import com.yyjlr.tickets.activity.FilmDetailsActivity;
import com.yyjlr.tickets.adapter.ChosenAdapter;
import com.yyjlr.tickets.model.ChosenFilmEntity;
import com.yyjlr.tickets.viewutils.chosen.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 精选页面
 */
public class ChosenContent extends LinearLayout implements SwipeFlingAdapterView.onFlingListener, View.OnClickListener {
    private View view;

    private int cardWidth;
    private int cardHeight;

    private SwipeFlingAdapterView swipeView;
    private ChosenAdapter adapter;
    private List<ChosenFilmEntity> chosenFilmEntityList;

    private RecyclerView listView;
    private CinemaAdapter cinemaAdapter;
    private List<String> typeDate;

    private ImageView enterCinema;//进入影院
    private TextView title;

    public ChosenContent(Context context) {
        this(context,null);
    }

    public ChosenContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_chosen, this);
        title = (TextView) view.findViewById(R.id.base_toolbar__text);
        enterCinema = (ImageView) findViewById(R.id.base_toolbar__right);
        title.setText(getResources().getText(R.string.text_cinema_name));
        enterCinema.setBackgroundResource(R.mipmap.enter_cinema);
        enterCinema.setVisibility(View.VISIBLE);
        enterCinema.setOnClickListener(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));

        swipeView = (SwipeFlingAdapterView) findViewById(R.id.fragment_chosen__fling);
        swipeView.setFlingListener(this);
//        swipeView.setOnItemClickListener(this);点击事件
        adapter = new ChosenAdapter(Application.getInstance().getCurrentActivity(),cardWidth,cardHeight);
        chosenFilmEntityList = Application.getiDataService().getChosenMovieList(4);
        adapter.addAll(chosenFilmEntityList);
        swipeView.setAdapter(adapter);


        listView = (RecyclerView) findViewById(R.id.fragment_chosen__listview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        getDate();
        cinemaAdapter = new CinemaAdapter(context, typeDate);
        listView.setAdapter(cinemaAdapter);

    }

    private void getDate(){
        typeDate = new ArrayList<String>();
        typeDate.add("3D眼镜");
        typeDate.add("儿童票");
        typeDate.add("停车场");
        typeDate.add("IMAX");
        typeDate.add("4D");
        typeDate.add("4K");
        typeDate.add("ATMOS");
    }

    @Override
    public void removeFirstObjectInAdapter() {
        adapter.remove(0);
    }

    @Override
    public void onLeftCardExit(Object dataObject) {}

    @Override
    public void onRightCardExit(Object dataObject) {}

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        if (itemsInAdapter == chosenFilmEntityList.size()-1) {
            chosenFilmEntityList = Application.getiDataService().getChosenMovieList(4);
            adapter.addAll(chosenFilmEntityList);
        }
    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_toolbar__right:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(),CinemaDetailsActivity.class));
                break;
        }
    }

    //adapter
    private class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder>{

        private List<String> list;
        private Context context;

        public CinemaAdapter(Context context, List<String> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chose_, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.type.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            if (list != null && list.size()>0)
                return list.size();
            else
                return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView type;
            public ViewHolder(View itemView) {
                super(itemView);
                type = (TextView) itemView.findViewById(R.id.item_cinema__type);
            }
        }
    }


}
