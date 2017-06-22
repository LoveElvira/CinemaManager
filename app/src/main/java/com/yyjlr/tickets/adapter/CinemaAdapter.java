package com.yyjlr.tickets.adapter;

import android.widget.AdapterView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.cinemainfo.CinemaListInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/4/5.
 */

public class CinemaAdapter extends BaseAdapter<CinemaListInfo> {

    public CinemaAdapter(List<CinemaListInfo> data) {
        super(R.layout.item_cinema_, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CinemaListInfo item, int position) {
        helper.setText(R.id.item_cinema__name, item.getName())
                .setText(R.id.item_cinema__address, item.getAddress())
                .setVisible(R.id.item_cinema__select, false)
                .setOnClickListener(R.id.item_cinema__layout, new OnItemChildClickListener());
        if (item.getDistance() != null) {
            helper.setText(R.id.item_cinema__distance, item.getDistance());
        }

        if (item.getChecked() == 1) {
            helper.setVisible(R.id.item_cinema__select, true);
        }

    }
}
