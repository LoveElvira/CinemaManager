package com.yyjlr.tickets.adapter;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.PointsEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/9/23.
 */

public class PointsAdapter extends BaseAdapter<PointsEntity> {

    public PointsAdapter(List<PointsEntity> data) {
        super(R.layout.item_points, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PointsEntity item, int position) {
        if (item.getPointsFlag().equals("1")) {
            helper.setText(R.id.item_points__points,item.getPointsNum())
                    .setText(R.id.item_points__points_content,item.getPointsContent())
                    .setText(R.id.item_points__time,item.getPointsTime())
                    .setBackgroundRes(R.id.item_points__image,R.mipmap.zhi);
        }else if(item.getPointsFlag().equals("2")) {
            helper.setText(R.id.item_points__points,item.getPointsNum())
                    .setText(R.id.item_points__points_content,item.getPointsContent())
                    .setText(R.id.item_points__time,item.getPointsTime())
                    .setBackgroundRes(R.id.item_points__image,R.mipmap.shou);
        }
    }
}
