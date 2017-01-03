package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.model.SaleEntity;
import com.yyjlr.tickets.model.sale.GoodInfo;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 卖品Adapter
 */
public class SaleAdapter extends BaseAdapter<GoodInfo> {
    private int first = -1;
    private int first1 = -1;
    private int last = -1;

    public SaleAdapter(List<GoodInfo> data) {
        super(R.layout.item_sale, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item, int position) {
        helper.setText(R.id.item_sale__package, item.getGoodsName())
                .setText(R.id.item_sale__package_content, item.getGoodsDesc())
                .setText(R.id.item_sale__original_price, ChangeUtils.save2Decimal(item.getPrice()))
                .setText(R.id.item_sale__app_price, ChangeUtils.save2Decimal(item.getAppPrice()))
                .setText(R.id.item_sale__time, ChangeUtils.changeTimeDate(item.getStartTime())+"~"+ChangeUtils.changeTimeDate(item.getEndTime()))
                .setOnClickListener(R.id.item_sale__shopping_cart, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_sale__cardview, new OnItemChildClickListener());
        Picasso.with(helper.getConvertView().getContext())
                .load(item.getGoodsImg())
                .into((ImageView) helper.getView(R.id.item_sale__image));

        LinearLayout priceLayout = helper.getView(R.id.item_sale__price_layout);
        priceLayout.measure(0, 0);
        int width = priceLayout.getMeasuredWidth();
        helper.getView(R.id.item_sale__line).getLayoutParams().width = width;

        boolean f = false;

        if (position == first) {
            helper.getView(R.id.item_sale__cardview).setAlpha((float) 0.8);
            if (position == first1) {
                helper.getView(R.id.item_sale__cardview).setAlpha(1);
            }
            f = true;
        } else {
            helper.getView(R.id.item_sale__cardview).setAlpha(1);
        }
        if (!f) {
            if (position == last) {
                helper.getView(R.id.item_sale__cardview).setAlpha((float) 0.7);
            } else {
                helper.getView(R.id.item_sale__cardview).setAlpha(1);
            }
        }

    }

    public void changeBgFristAndLast(int first, int last, int first1) {
        this.first = first;
        this.first1 = first1;
        this.last = last;
        notifyDataSetChanged();
    }

}
