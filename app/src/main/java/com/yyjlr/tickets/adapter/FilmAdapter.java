package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 影片Adapter
 */
public class FilmAdapter extends BaseAdapter<FilmEntity> {

    private int first = -1;
    private int first1 = -1;
    private int last = -1;

    public FilmAdapter(List<FilmEntity> data) {
        super(R.layout.item_film,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilmEntity item, int position) {
        helper.setText(R.id.item_film__name,item.getFilmName())
                .setBackgroundRes(R.id.item_film__image,R.mipmap.bird)
                .setText(R.id.item_film__director,item.getFilmDirector())
                .setText(R.id.item_film__star,item.getFilmStar())
                .setText(R.id.item_film__showtime,item.getFilmShowTime())
                .setOnClickListener(R.id.item_film__cardview,new OnItemChildClickListener())
                .setOnClickListener(R.id.item_film__buy_ticket,new OnItemChildClickListener())
                .setOnClickListener(R.id.item_film__image,new OnItemChildClickListener());
        boolean f = false;

        if (position == first) {
            helper.getView(R.id.item_film__cardview).setAlpha((float) 0.8);
            if(position == first1){
                helper.getView(R.id.item_film__cardview).setAlpha(1);
            }
            f = true;
        } else {
            helper.getView(R.id.item_film__cardview).setAlpha(1);
        }
        if (!f){
            if (position == last) {
                helper.getView(R.id.item_film__cardview).setAlpha((float) 0.7);
            } else {
                helper.getView(R.id.item_film__cardview).setAlpha(1);
            }
        }

    }

    public void changeBgFristAndLast(int first, int last,int first1) {
        this.first = first;
        this.first1 = first1;
        this.last = last;
        notifyDataSetChanged();
    }

}
