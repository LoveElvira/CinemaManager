package com.yyjlr.tickets.adapter;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.pay.MemberCard;

import java.util.List;

/**
 * Created by Elvira on 2017/7/11.
 * 支付  会员卡列表
 */

public class PayCardAdapter extends BaseAdapter<MemberCard> {

    public PayCardAdapter(List<MemberCard> data) {
        super(R.layout.item_pay_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberCard item, int position) {
        helper.setText(R.id.item_pay_card__vip_num, "No." + item.getCardNo())
                .setText(R.id.item_pay_card__vip_price, "剩余 ¥" + ChangeUtils.save2Decimal(item.getBalance()) + "，点击")
                .setImageResource(R.id.item_pay_card__vip_select_or_default, R.mipmap.sale_no_select)
                .setVisible(R.id.item_pay_card__vip_msg, false)
                .setOnClickListener(R.id.item_pay_card__vip_recharge, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_pay_card__layout, new OnItemChildClickListener());
        if (item.getChecked() == 1) {
            helper.setImageResource(R.id.item_pay_card__vip_select_or_default, R.mipmap.sale_select);
        }

        if (item.getState() == 0) {
//            helper.setText(R.id.item_pay_card__vip_price, "剩余 ¥" + ChangeUtils.save2Decimal(item.getBalance()) + "，" + item.getMsg() + "，点击");
            helper.setText(R.id.item_pay_card__vip_msg, item.getMsg())
                    .setVisible(R.id.item_pay_card__vip_msg, true);
        }

    }

}
