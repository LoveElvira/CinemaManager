package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.home.GoodsInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/4/6.
 */

public class HomeSaleAdapter extends BaseAdapter<GoodsInfo> {

    public HomeSaleAdapter(List<GoodsInfo> data) {
        super(R.layout.item_home_sale, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo item, int position) {
        helper.setText(R.id.item_home_sale__name, item.getName())
                .setText(R.id.item_home_sale__price, "¥ " + ChangeUtils.save2Decimal(item.getPrice()))
                .setOnClickListener(R.id.item_home_sale__layout, new OnItemChildClickListener());

        Picasso.with(helper.getConvertView().getContext())
                .load(item.getImageUrl())
                .error(R.mipmap.bg)
                .into((ImageView) helper.getView(R.id.item_home_sale__image));
    }

}
