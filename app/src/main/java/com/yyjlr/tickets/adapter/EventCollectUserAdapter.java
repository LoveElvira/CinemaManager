package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.event.CollectUserInfo;

import java.util.List;

/**
 * Created by Elvira on 2016/12/20.
 */

public class EventCollectUserAdapter extends BaseAdapter<CollectUserInfo> {

    List<CollectUserInfo> data;

    public EventCollectUserAdapter(List<CollectUserInfo> data) {
        super(R.layout.item_event_collect_user, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectUserInfo item, int position) {
        helper.setVisible(R.id.item_event_collect_user__more, false)
                .setVisible(R.id.item_event_collect_user__headimage, true);

        if (position == 4) {
            helper.setText(R.id.item_event_collect_user__more, "+" + data.size())
                    .setVisible(R.id.item_event_collect_user__more, true)
                    .setVisible(R.id.item_event_collect_user__headimage, false);

            return;
        } else if (position > 4) {
            return;
        } else {
            helper.setText(R.id.item_event_collect_user__username, item.getNickName());

            if (item.getImageUrl() != null)
                Picasso.with(helper.getConvertView().getContext())
                        .load(item.getImageUrl())
                        .into((ImageView) helper.getView(R.id.item_event_collect_user__headimage));
        }
    }
}