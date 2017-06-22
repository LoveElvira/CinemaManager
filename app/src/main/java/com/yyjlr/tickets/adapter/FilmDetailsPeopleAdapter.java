package com.yyjlr.tickets.adapter;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.film.FilmWorker;

import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影片详情人物介绍Adapter
 */
public class FilmDetailsPeopleAdapter extends BaseAdapter<FilmWorker> {

    public FilmDetailsPeopleAdapter(List<FilmWorker> data) {
        super(R.layout.item_film_details_people, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FilmWorker item, int position) {
        helper.setText(R.id.item_film_details__people_name, item.getItem())
                .setText(R.id.item_film_details__people_position, item.getType());
    }
}
