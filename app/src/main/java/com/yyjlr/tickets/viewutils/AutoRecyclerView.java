package com.yyjlr.tickets.viewutils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Elvira on 2016/12/16.
 * 可以试用于Scrollview
 */

public class AutoRecyclerView extends RecyclerView {
    public AutoRecyclerView(Context context) {
        super(context);
    }

    public AutoRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoRecyclerView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
