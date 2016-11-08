/*
 * Copyright (c) 2016 wuzhen. All rights reserved.
 */

package com.yyjlr.tickets.viewutils.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.SpinnerAdapter;

import com.yyjlr.tickets.R;

/**
 * 可以实现滑动时平缓缩放已经透明度渐变效果的 Gallery。
 *
 * @author wuzhen
 * @version Version 1.0, 2016-09-05
 */
public class WGallery extends EcoGallery {

    public static final int SCALE_PIVOT_CENTER = 0;
    public static final int SCALE_PIVOT_TOP = 1;
    public static final int SCALE_PIVOT_BOTTOM = 2;

    private int mScalePivot;
    private float mSelectedScale;
    private float mUnselectedAlpha;
    private IWGalleryAdapter mWGalleryAdapter;

    public WGallery(Context context) {
        super(context);
        init(context, null, 0);
    }

    public WGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WGallery, defStyle, 0);
        mScalePivot = a.getInteger(R.styleable.WGallery_wGallery_scalePivot, SCALE_PIVOT_CENTER);
        mSelectedScale = a.getFloat(R.styleable.WGallery_wGallery_selectedScale, 1.f);
        mUnselectedAlpha = a.getFloat(R.styleable.WGallery_wGallery_unselectedAlpha, 1.f);
        a.recycle();
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof IWGalleryAdapter) {
            mWGalleryAdapter = (IWGalleryAdapter) adapter;
        } else {
            mWGalleryAdapter = null;
        }
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        return true;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (mSelectedScale != 1.f || mUnselectedAlpha != 1.f) {
            int childWidth = child.getWidth();
            int childHeight = child.getHeight();

            final int center = getCenterOfGallery();
            final int childCenter = child.getLeft() + childWidth / 2;
            final int offsetCenter = Math.abs(center - childCenter);
            final float offsetScale = (childWidth - offsetCenter) * 1.f / childWidth;
            if (offsetCenter < childWidth) {
                if (mSelectedScale != 1.f) {
                    float pivotY = 0;
                    if (mScalePivot == SCALE_PIVOT_CENTER) {
                        pivotY = (childHeight + child.getPaddingTop() - child.getPaddingBottom()) / 2;
                    } else if (mScalePivot == SCALE_PIVOT_TOP) {
                        pivotY = child.getPaddingTop();
                    } else if (mScalePivot == SCALE_PIVOT_BOTTOM) {
                        pivotY = childHeight - child.getPaddingBottom();
                    }
                    float scale = 1 + (mSelectedScale - 1) * offsetScale;
                    child.setPivotX(childWidth / 2.f);
                    child.setPivotY(pivotY);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                }
                if (mUnselectedAlpha != 1.f) {
                    float alpha = (1.f - mUnselectedAlpha) * offsetScale + mUnselectedAlpha;
                    if (mWGalleryAdapter != null) {
                        int alphaViewId = mWGalleryAdapter.getChangeAlphaViewId();
                        if (alphaViewId > 0) {
                            View alphaView = child.findViewById(alphaViewId);
                            if (alphaView != null)
                                alphaView.setAlpha(alpha);
                        }
                    } else {
                        child.setAlpha(alpha);
                    }
                }
            } else {
                if (mSelectedScale != 1.f) {
                    child.setScaleX(1.f);
                    child.setScaleY(1.f);
                }
                if (mUnselectedAlpha != 1.f) {
                    if (mWGalleryAdapter != null) {
                        int alphaViewId = mWGalleryAdapter.getChangeAlphaViewId();
                        if (alphaViewId > 0) {
                            View alphaView = child.findViewById(alphaViewId);
                            if (alphaView != null)
                                alphaView.setAlpha(mUnselectedAlpha);
                        }
                    } else {
                        child.setAlpha(mUnselectedAlpha);
                    }
                }
            }
        }

        return super.drawChild(canvas, child, drawingTime);
    }

    /**
     * 设置选中的 Item 缩放的比例。
     *
     * @param scale 缩放的比例
     */
    public void setSelectedScale(float scale) {
        this.mSelectedScale = scale;
        invalidate();
    }

    /**
     * 设置未选中的 Item 的透明度。
     *
     * @param alpha 透明度
     */
    public void setUnSelectedAlpha(float alpha) {
        this.mUnselectedAlpha = alpha;
        invalidate();
    }

    /**
     * 设置缩放的中心点位置。
     *
     * @param pivot 缩放的中心点位置
     * @see WGallery#SCALE_PIVOT_CENTER
     * @see WGallery#SCALE_PIVOT_TOP
     * @see WGallery#SCALE_PIVOT_BOTTOM
     */
    public void setScalePivot(int pivot) {
        if (pivot != SCALE_PIVOT_BOTTOM && pivot != SCALE_PIVOT_CENTER && pivot != SCALE_PIVOT_TOP) {
            throw new RuntimeException("The scale pivot must be one of SCALE_PIVOT_BOTTOM、" +
                    "SCALE_PIVOT_CENTER or SCALE_PIVOT_TOP");
        }

        this.mScalePivot = pivot;
        invalidate();
    }
}
