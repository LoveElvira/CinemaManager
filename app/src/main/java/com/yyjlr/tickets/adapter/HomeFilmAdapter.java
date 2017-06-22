package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.home.MovieInfo;
import com.yyjlr.tickets.viewutils.Star;

import java.util.List;

/**
 * Created by Elvira on 2017/4/6.
 */

public class HomeFilmAdapter extends BaseAdapter<MovieInfo> {

    public HomeFilmAdapter(List<MovieInfo> data) {
        super(R.layout.item_home_film, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MovieInfo item, int position) {
        helper.setText(R.id.item_home_film__name, item.getName())
                .setText(R.id.item_home_film__score, item.getScore() + "")
                .setOnClickListener(R.id.item_home_film__layout, new OnItemChildClickListener());

        Star star = helper.getView(R.id.item_home_film__star);
        star.setMark(item.getScore() / 2.0f);

        Picasso.with(helper.getConvertView().getContext())
                .load(item.getImageUrl())
                .error(R.mipmap.bg)
                .into((ImageView) helper.getView(R.id.item_home_film__image));

    }

}
