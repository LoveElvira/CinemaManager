package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.pay.SelectPay;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 选择支付方式
 */
public class PayAdapter extends BaseAdapter<SelectPay> {

    public PayAdapter(List<SelectPay> data) {
        super(R.layout.item_select_pay, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectPay item, int position) {
        helper.setText(R.id.item_select_pay__text, item.getName())
                .setImageResource(R.id.item_select_pay__select_image, R.mipmap.sale_no_select)
                .setOnClickListener(R.id.item_select_pay__layout, new OnItemChildClickListener());
        if (item.getChecked() == 1) {
            helper.setImageResource(R.id.item_select_pay__select_image, R.mipmap.sale_select);
        }

    }

}
