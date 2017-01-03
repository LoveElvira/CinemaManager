package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.model.FilmPeopleEntity;
import com.yyjlr.tickets.model.film.FilmWorker;

import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影片详情人物介绍Adapter
 */
public class FilmDetailsPeopleAdapter extends BaseAdapter<FilmWorker>{

    public FilmDetailsPeopleAdapter(List<FilmWorker> data) {
        super(R.layout.item_film_details_people, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FilmWorker item, int position) {
        helper.setText(R.id.item_film_details__people_name,item.getItem());
        helper.setText(R.id.item_film_details__people_position,item.getType());
    }
}
