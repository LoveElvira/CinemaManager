package com.yyjlr.tickets.adapter;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.coupon.CouponInfo;

import java.util.List;

/**
 * Created by Elvira on 2017/5/9.
 * 未使用
 */

public class NoUseCouponAdapter extends BaseAdapter<CouponInfo> {

    public NoUseCouponAdapter(List<CouponInfo> data) {
        super(R.layout.item_coupon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponInfo item, int position) {
        helper.setText(R.id.item_coupon__num, item.getCouponNumber())
                .setText(R.id.item_coupon__price, ChangeUtils.saveDecimal(item.getDiscount()))
                .setVisible(R.id.item_coupon__time, true)
                .setVisible(R.id.item_coupon__type, false)
                .setVisible(R.id.item_coupon__type_layout, false)
                .setImageResource(R.id.item_coupon__left_bg, R.mipmap.bg_voucher_blue_left)
                .setImageResource(R.id.item_coupon__right_bg, R.mipmap.bg_voucher_blue_right);

        if (item.getType()==1) {
            helper.setVisible(R.id.item_coupon__type, true);
        } else if (item.getType()==2) {
            helper.setVisible(R.id.item_coupon__type_layout, true)
                    .setText(R.id.item_coupon__price, ChangeUtils.saveDecimal(item.getDiscount()));
        }

        if (item.getOverTime() != null && !"".equals(item.getOverTime())) {
            helper.setText(R.id.item_coupon__time, item.getOverTime());
        } else {
            helper.setVisible(R.id.item_coupon__time, false);
        }

    }
}
