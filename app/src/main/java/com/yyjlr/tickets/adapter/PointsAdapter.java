package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.PointsEntity;
import com.yyjlr.tickets.model.point.PointDetail;

import java.util.List;

/**
 * Created by Elvira on 2016/9/23.
 */

public class PointsAdapter extends BaseAdapter<PointDetail> {

    public PointsAdapter(List<PointDetail> data) {
        super(R.layout.item_points, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PointDetail item, int position) {
        helper.setText(R.id.item_points__points, item.getPoints() + "积分")
                .setText(R.id.item_points__points_content, item.getDesc())
                .setText(R.id.item_points__time, ChangeUtils.changeYearDot(item.getTime()));
//        if (item.getInOrOut()==0) {//支出
//            helper.setBackgroundRes(R.id.item_points__image,R.mipmap.zhi);
//        }else if(item.getInOrOut()==1) {//收入
//            helper.setBackgroundRes(R.id.item_points__image,R.mipmap.shou);
//        }

        if (item.getImage() != null && !"".equals(item.getImage())) {
            Picasso.with(helper.getConvertView().getContext())
                    .load(item.getImage())
                    .into((ImageView) helper.getView(R.id.item_points__image));
        }

    }
}
