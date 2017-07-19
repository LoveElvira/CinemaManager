package com.yyjlr.tickets.adapter;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.coupon.VoucherModle;

import java.util.List;

/**
 * Created by Elvira on 2017/7/12.
 */

public class VoucherAdapter extends BaseAdapter<VoucherModle> {

    public VoucherAdapter(List<VoucherModle> data) {
        super(R.layout.item_voucher_, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoucherModle item, int position) {
        helper.setText(R.id.item_voucher__num, item.getCouponNumber())
                .setVisible(R.id.item_voucher__time, false)
                .setVisible(R.id.item_voucher__select, false)
                .setOnClickListener(R.id.item_voucher__layout, new OnItemChildClickListener());
        if (item.isChecked()) {
            helper.setVisible(R.id.item_voucher__select, true);
        }

        if (item.getOverTime() != null && !"".equals(item.getOverTime())) {
            helper.setVisible(R.id.item_voucher__time, false)
                    .setText(R.id.item_voucher__time, item.getOverTime());
        }

    }

}
