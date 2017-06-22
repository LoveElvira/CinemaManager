package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.ticket.EventInfoEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 活动Adapter
 */
public class EventAdapter extends BaseAdapter<EventInfoEntity> {

    public EventAdapter(List<EventInfoEntity> data) {
        super(R.layout.item_event, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EventInfoEntity item, int position) {
        helper.setText(R.id.item_event__name, item.getActivityName())
                .setText(R.id.item_event__date, item.getRemnantTime())
                .setText(R.id.item_event__collect_num, item.getFavorite() + "")
                .setOnClickListener(R.id.item_event__layout, new OnItemChildClickListener());
        if (item.getActivityImg() != null) {
            Picasso.with(helper.getConvertView().getContext())
                    .load(item.getActivityImg())
                    .into((ImageView) helper.getView(R.id.item_event__image));
        }

        if (item.getStartTime() != 0 && item.getEndTime() != 0) {
            helper.setText(R.id.item_event__time, ChangeUtils.changeTimeYearLine(item.getStartTime()) + "~" + ChangeUtils.changeTimeYearLine(item.getEndTime()));
        } else if (item.getStartTime() != 0 && item.getEndTime() == 0) {
            helper.setText(R.id.item_event__time, ChangeUtils.changeTimeYearLine(item.getStartTime()));
        }
    }
}
