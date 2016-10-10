package com.yyjlr.tickets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmSaleEntity;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Elvira on 2016/8/10.
 * 套餐Adapter
 */
public class FilmSaleAdapter extends BaseAdapter<FilmSaleEntity> {

    public FilmSaleAdapter(List<FilmSaleEntity> data) {
        super(R.layout.item_film_sale_package, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilmSaleEntity item, int position) {
        helper.getView(R.id.item_film_sale__line).setVisibility(View.VISIBLE);
        if (position == 0){
            helper.getView(R.id.item_film_sale__line).setVisibility(View.GONE);
        }
        helper.setText(R.id.item_film_sale__package, item.getSalePackage())
                .setText(R.id.item_film_sale__price, "￥"+item.getSalePrice())
                .setText(R.id.item_film_sale__package_content, item.getSalePackageContent())
                .setText(R.id.item_film_sale__time, item.getSaleTime())
                .setImageResource(R.id.item_film_sale__image, R.mipmap.mihua)
                .setOnClickListener(R.id.item_film_sale__layout, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_film_sale__select, new OnItemChildClickListener());
        helper.setImageResource(R.id.item_film_sale__select, R.mipmap.sale_no_select);
        if (item.isSaleSelect()) {
            helper.setImageResource(R.id.item_film_sale__select, R.mipmap.sale_select);
        }
    }
}
