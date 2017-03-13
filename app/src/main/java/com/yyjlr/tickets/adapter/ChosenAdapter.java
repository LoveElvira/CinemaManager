package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.chosen.EventInfo;

import java.util.List;

/*
 * Created by Elvira on 2016/7/31.
 * 精选Adapter
*/


public class ChosenAdapter extends BaseAdapter<EventInfo> {

    private int cardWidth, cardHeight;

    public ChosenAdapter(List<EventInfo> data) {
        super(R.layout.item_chosen_film, data);
    }

    public void setImageSize(int cardWidth, int cardHeight) {
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
    }

    @Override
    protected void convert(BaseViewHolder helper, EventInfo item, int position) {
//        helper.getConvertView().getLayoutParams().width = cardWidth;
        helper.setText(R.id.item_chosen__name, item.getActivityName())
                .setText(R.id.item_chosen__showtime, ChangeUtils.changeTimeYear(item.getStartTime()) + "~" + ChangeUtils.changeTimeYear(item.getEndTime()))
                .setText(R.id.item_chosen__price, ChangeUtils.save2Decimal(item.getPrice()) + "元")
                .setOnClickListener(R.id.item_chosen__parent, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_chosen__enter, new OnItemChildClickListener());
        if (item.getActivityImg() != null) {
            Picasso.with(helper.getConvertView().getContext())
                    .load(item.getActivityImg())
                    .placeholder(R.mipmap.icon_logo)
                    .into((ImageView) helper.getView(R.id.item_chosen__image));
        }
    }
}
