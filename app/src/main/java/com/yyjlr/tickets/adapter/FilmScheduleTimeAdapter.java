package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmTimeEntity;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;

import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/8/2.
 */
public class FilmScheduleTimeAdapter extends BaseAdapter<Map<String, String>> {

    public FilmScheduleTimeAdapter(List<Map<String, String>> data) {
        super(R.layout.item_film_schedule_time, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, String> item, int position) {
        helper.setText(R.id.item_schedule__time, item.get("time"))
                .setText(R.id.item_schedule__date, item.get("week"))
                .setBackgroundRes(R.id.item_film_schedule__layout, R.drawable.circle_border_gray)
                .setVisible(R.id.item_film_schedule__confirm, false)
                .setOnClickListener(R.id.item_film_schedule__layout_parent, new OnItemChildClickListener());
        if ("0".equals(item.get("show"))) {
            helper.setBackgroundRes(R.id.item_film_schedule__layout, R.drawable.circle_border_orange)
                    .setVisible(R.id.item_film_schedule__confirm, true);
        }
    }
}
