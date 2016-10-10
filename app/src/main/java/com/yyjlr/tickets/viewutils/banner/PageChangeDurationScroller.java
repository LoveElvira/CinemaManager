package com.yyjlr.tickets.viewutils.banner;

import android.content.Context;
import android.widget.Scroller;

/*
* Created by Elvira on 2016/5/10.
* */
public class PageChangeDurationScroller extends Scroller {
    private int mDuration = 2000;

    public PageChangeDurationScroller(Context context) {
        super(context);
    }

    public PageChangeDurationScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}