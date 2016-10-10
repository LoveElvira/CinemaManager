package com.yyjlr.tickets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.ChosenFilmEntity;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 关注抢票Adapter
 */
public class FollowGrabAdapter extends BaseAdapter<ChosenFilmEntity> {

    public FollowGrabAdapter(List<ChosenFilmEntity> data) {
        super(R.layout.item_follow_grab, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChosenFilmEntity item, int position) {
        helper.setImageResource(R.id.item_follow_grab__image, R.mipmap.bird)
                .setText(R.id.item_follow_grab__name, item.getChosenFilmName())
                .setText(R.id.item_follow_grab__time, item.getChosenFilmShowTime())
                .setText(R.id.item_follow_grab__address, item.getChosenFilmAddress())
                .setText(R.id.item_follow_grab__price, item.getChosenFilmPrice())
                .setOnClickListener(R.id.item_follow_grab__enter, new OnItemChildClickListener());
    }

}
