package com.yyjlr.tickets.content.sale;

import android.content.Context;
import android.util.AttributeSet;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.content.BaseLinearLayout;

/**
 * Created by Elvira on 2017/6/14.
 * 会员卡
 */

public class VipCardContent extends BaseLinearLayout {

    public VipCardContent(Context context) {
        super(context);
    }

    public VipCardContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_vip_card, this);
    }
}
