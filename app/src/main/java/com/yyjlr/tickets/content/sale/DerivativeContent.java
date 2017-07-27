package com.yyjlr.tickets.content.sale;

import android.content.Context;
import android.util.AttributeSet;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.content.BaseLinearLayout;

/**
 * Created by Elvira on 2017/6/14.
 * 衍生品
 */

public class DerivativeContent extends BaseLinearLayout {

    public DerivativeContent(Context context) {
        super(context);
    }

    public DerivativeContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_derivative, this);

    }
}
