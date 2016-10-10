package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmSeatEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/9/1.
 */
public class SeatTypeAdapter extends BaseAdapter<FilmSeatEntity.Response.SeatType> {

    public SeatTypeAdapter(List<FilmSeatEntity.Response.SeatType> data) {
        super(R.layout.item_seat_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilmSeatEntity.Response.SeatType item, int position) {
        helper.setText(R.id.item_seat_type__text, item.getName());
        Picasso.with(helper.getConvertView().getContext()).load(item.getIcon()).into((ImageView) helper.getView(R.id.item_seat_type__iamge));
    }
}
