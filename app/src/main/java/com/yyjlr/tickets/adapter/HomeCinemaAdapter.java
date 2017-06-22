package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.cinemainfo.CinemaListInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/4/6.
 */

public class HomeCinemaAdapter extends BaseAdapter<String> {

    public HomeCinemaAdapter(List<String> data) {
        super(R.layout.item_home_cinema, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item, int position) {
        helper.setText(R.id.item_home_cinema__name, "宁波影都")
                .setText(R.id.item_home_cinema__address, "浙江省宁波市江东区中兴路268号天伦时代广场")
                .setOnClickListener(R.id.item_home_cinema__layout, new OnItemChildClickListener());
        LinearLayout typeLayout = helper.getView(R.id.item_home_cinema__type_layout);
        typeLayout.removeAllViews();
        for (int i = 0; i < 2; i++) {
            typeLayout.addView(initType(typeLayout.getContext(), "MAX"));
        }

    }

    //动态添加类型
    private View initType(Context context, String filmType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_cinema_type, null, false);
        TextView type = (TextView) view.findViewById(R.id.item_home_cinema__type);
        type.setText(filmType);
        return view;
    }
}
