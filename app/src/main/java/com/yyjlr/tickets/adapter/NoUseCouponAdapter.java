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
                .setImageResource(R.id.item_coupon__left_bg, R.mipmap.bg_voucher_blue_left)
                .setImageResource(R.id.item_coupon__right_bg, R.mipmap.bg_voucher_blue_right);

        if (item.getOverTime() != 0) {
            helper.setText(R.id.item_coupon__time, "有效期至" + ChangeUtils.changeYearDot(item.getOverTime()));
        } else {
            helper.setVisible(R.id.item_coupon__time, false);
        }

    }
}
