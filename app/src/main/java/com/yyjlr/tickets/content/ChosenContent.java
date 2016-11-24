package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.Toast;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.CinemaDetailsActivity;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.ChosenAdapter;
import com.yyjlr.tickets.model.ChosenFilmEntity;
import com.yyjlr.tickets.viewutils.chosen.CarouselLayoutManager;
import com.yyjlr.tickets.viewutils.chosen.CarouselZoomPostLayoutListener;
import com.yyjlr.tickets.viewutils.chosen.CenterScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 精选页面
 */
public class ChosenContent extends LinearLayout implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener {
    private View view;

    private int cardWidth;
    private int cardHeight;

    private List<ChosenFilmEntity> chosenFilmEntityList;

    private RecyclerView listView;
    private RecyclerView choseFling;
    private CinemaAdapter cinemaAdapter;
    private ChosenAdapter chosenAdapter;
    private List<String> typeDate;

    private ImageView enterCinema;//进入影院
    private TextView title;
    private TextView address;

    public ChosenContent(Context context) {
        this(context, null);
    }

    public ChosenContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_chosen, this);
        initView(view);

    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.base_toolbar__text);
        enterCinema = (ImageView) view.findViewById(R.id.base_toolbar__right);
        title.setText(getResources().getText(R.string.text_cinema_name));
        enterCinema.setImageResource(R.mipmap.enter_cinema);
        enterCinema.setAlpha(1.0f);
        enterCinema.setOnClickListener(this);
        address = (TextView) findViewById(R.id.fragment_chosen__address);
        address.setOnClickListener(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        cardWidth = (int) (dm.widthPixels - (2 * 18 * density));
        cardHeight = (int) (dm.heightPixels - (338 * density));

        choseFling = (RecyclerView) view.findViewById(R.id.fragment_chosen__fling);
        initChosenView(choseFling, new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true));

        listView = (RecyclerView) view.findViewById(R.id.fragment_chosen__listview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        getDate();
        cinemaAdapter = new CinemaAdapter(getContext(), typeDate);
        listView.setAdapter(cinemaAdapter);
        listView.setOnClickListener(this);
    }

    private void getDate() {
        typeDate = new ArrayList<String>();
        typeDate.add("3D眼镜");
        typeDate.add("儿童票");
        typeDate.add("停车场");
        typeDate.add("IMAX");
        typeDate.add("4D");
        typeDate.add("4K");
        typeDate.add("ATMOS");
    }

    private void initChosenView(RecyclerView recyclerView, final CarouselLayoutManager layoutManager) {

        chosenFilmEntityList = Application.getiDataService().getChosenMovieList(5);
        chosenAdapter = new ChosenAdapter(chosenFilmEntityList);
        chosenAdapter.setImageSize(cardWidth, cardHeight);

        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        layoutManager.setMaxVisibleItems(2);

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(chosenAdapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new CenterScrollListener());
        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //5.0以下的手机会不会主动刷新到界面,需要调用此方法
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                            chosenAdapter.notifyDataSetChanged();
                        }
                    }
                }, 50);
            }
        });
        chosenAdapter.setOnRecyclerViewItemChildClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__right:
            case R.id.fragment_chosen__address:
            case R.id.fragment_chosen__listview:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), CinemaDetailsActivity.class));
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {

        Application.getInstance().getCurrentActivity().startActivity(
                new Intent(Application.getInstance().getCurrentActivity(), EventActivity.class));

//        if(layoutManager.getOrientation()==CarouselLayoutManager.VERTICAL){
//            if ((int) view.getY() > 0 && view.getHeight() / 2 > (int) view.getY()) {
//                Toast.makeText(context, "" + String.valueOf(position), Toast.LENGTH_SHORT).show();
//            }
//        }else if(layoutManager.getOrientation()==CarouselLayoutManager.HORIZONTAL){
//            if ((int) view.getX() > 0 && view.getWidth() / 2 > (int) view.getX()) {
//                Toast.makeText(context, "" + String.valueOf(position), Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    //adapter
    private class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder> {

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
            if (list != null && list.size() > 0)
                return list.size();
            else
                return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView type;

            public ViewHolder(View itemView) {
                super(itemView);
                type = (TextView) itemView.findViewById(R.id.item_cinema__type);
            }
        }
    }


}
