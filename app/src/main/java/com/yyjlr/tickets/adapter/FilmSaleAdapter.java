package com.yyjlr.tickets.adapter;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.sale.RecommendGoodsInfo;

import java.util.List;

/**
 * Created by Elvira on 2016/8/10.
 * 套餐Adapter
 */
public class FilmSaleAdapter extends BaseAdapter<RecommendGoodsInfo> {

    public FilmSaleAdapter(List<RecommendGoodsInfo> data) {
        super(R.layout.item_film_sale_package, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendGoodsInfo item, int position) {
        helper.getView(R.id.item_film_sale__line).setVisibility(View.VISIBLE);
        if (position == 0) {
            helper.getView(R.id.item_film_sale__line).setVisibility(View.GONE);
        }
        helper.setText(R.id.item_film_sale__package, item.getGoodsName())
                .setText(R.id.item_film_sale__price, "￥ " + ChangeUtils.save2Decimal(item.getPrice()))
                .setText(R.id.item_film_sale__package_content, item.getGoodsDetail())
                .setImageResource(R.id.item_film_sale__select, R.mipmap.sale_no_select)
                .setOnClickListener(R.id.item_film_sale__layout, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_film_sale__select, new OnItemChildClickListener());

        if (item.getStartTime() != 0 && item.getEndTime() != 0) {
            helper.setText(R.id.item_film_sale__time, ChangeUtils.changeTimeYear(item.getStartTime()) + "~" + ChangeUtils.changeTimeYear(item.getEndTime()));
        } else if (item.getStartTime() != 0 && item.getEndTime() == 0) {
            helper.setText(R.id.item_film_sale__time, ChangeUtils.changeTimeYear(item.getStartTime()));
        }

        if (item.getGoodsImg() != null && !"".equals(item.getGoodsImg())) {
            Picasso.with(helper.getConvertView().getContext())
                    .load(item.getGoodsImg())
                    .into((ImageView) helper.getView(R.id.item_film_sale__image));
        }
        if (item.getSelected() == 1) {
            helper.setImageResource(R.id.item_film_sale__select, R.mipmap.sale_select);
        }
    }
}
