package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 关注影片Adapter
 */
public class FollowFilmAdapter extends BaseAdapter<FilmEntity> {


    public FollowFilmAdapter(List<FilmEntity> data) {
        super(R.layout.item_follow_film, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilmEntity item, int position) {
        helper.setImageResource(R.id.item_follow_film__image, R.mipmap.bird)
                .setText(R.id.item_follow_film__name, item.getFilmName())
                .setText(R.id.item_follow_film__director, item.getFilmDirector())
                .setText(R.id.item_follow_film__star, item.getFilmStar())
                .setText(R.id.item_follow_film__showtime, item.getFilmShowTime())
                .setOnClickListener(R.id.item_follow_film__buy_ticket, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_follow_film__layout, new OnItemChildClickListener());
    }
}
