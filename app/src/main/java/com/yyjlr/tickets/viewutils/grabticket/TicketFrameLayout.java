package com.yyjlr.tickets.viewutils.grabticket;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.ticket.TicketModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/9/13.
 * 抢票列表
 */
public class TicketFrameLayout extends FrameLayout {
    private int animationTime = 400;
    private float resistance = 0.8f;
    private int minVelocity = 350;

    private boolean isInitFinish;

    private int touchSlop;
    private int mWidth, mHeight;
    private int childTotalHeight;
    private int childExpandedHeight;
    private int childNormalHeight;

    private Adapter mAdapter;
    private final GestureDetector mGestureDetector;
    private final Scroller mScroller;
    private final RelativeLayout layBottomContainer;

    private List<OnScrollChangedListener> scrollCallbacks = new ArrayList<>();

    private boolean isAnimMoving;
    private boolean isTouching;
    private boolean isFling;
    private float interceptDownY;
    private float touchDownY;
    private int touchDownScrollY;
    private int touchUpScrollY;

    private int realIndex;
    private float realPercent;
    private int dataSize = 0;

    public TicketFrameLayout(Context context) {
        this(context, null);
    }

    public TicketFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        layBottomContainer = new RelativeLayout(context);
        mScroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetectorWithOnUp(context, new GestureListenerWithOnUp());
    }

    public void addBottomContent(View vBottomContent) {
        layBottomContainer.removeAllViews();
        layBottomContainer.addView(vBottomContent);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;//获取屏幕的宽度
        mHeight = h;//获取屏幕的高度

        //初始化item  高 最大childExpandedHeight  最小childNormalHeight
//        childExpandedHeight = (int) (mWidth / 588f * 340);
//        childNormalHeight = (int) (mWidth / 588f * 244);
        childExpandedHeight = (int) (mHeight / 2f);
        childNormalHeight = (int) (mHeight / 4f);


        if (!isInitFinish) {
            dataSetChanged();
        }
    }

    private void registerScrollChangedCallback(OnScrollChangedListener callback) {
        if (!scrollCallbacks.contains(callback)) {
            scrollCallbacks.add(callback);
        }
    }

    public interface OnScrollChangedListener {
        void onScroll(int currIndex, float percent);
    }

    void dataSetChanged() {
        if (mWidth * mHeight < 1) {
            isInitFinish = false;
            return;
        }
        isInitFinish = true;

        if (mAdapter == null)
            return;
        dataSize = mAdapter.getCount();
        childTotalHeight = (dataSize < 1 ? 0 : childNormalHeight * (dataSize - 1));

        scrollCallbacks.clear();
        removeAllViewsInLayout();
        for (int i = 0; i < dataSize; i++) {
            RelativeLayout retailMeNotView = new RelativeLayout(getContext());
            View itemView = mAdapter.getView(i, retailMeNotView, childExpandedHeight, childNormalHeight);

            if (!(itemView instanceof OnScrollChangedListener))
                throw new IllegalArgumentException("adapter getView return child -> must impl RetailMeNotLayout.OnScrollChangedListener");
            else
                registerScrollChangedCallback((OnScrollChangedListener) itemView);

            retailMeNotView.addView(itemView);
            retailMeNotView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, childExpandedHeight));
            retailMeNotView.setTranslationY(i * childNormalHeight);
            addView(retailMeNotView);
        }
//        layBottomContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, childTotalHeight));
        layBottomContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight - (dataSize < 1 ? 0 : childExpandedHeight)));
        TicketModel ticketModel = null;
//        layBottomContainer.setData(dataSize, ticketModel, mHeight - (dataSize < 1 ? 0 : childExpandedHeight), mHeight - (dataSize < 1 ? 0 : childExpandedHeight));
        View view = layBottomContainer.getChildAt(0);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        view.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        int bottomTranslateY;
        if (dataSize < 1) {
            bottomTranslateY = 0;
        } else if (dataSize == 1) {
            bottomTranslateY = childExpandedHeight;
        } else {
            bottomTranslateY = childExpandedHeight + childNormalHeight * (dataSize - 1);
        }
        layBottomContainer.setTranslationY(bottomTranslateY);
        //layBottomContainer.setBackgroundColor(getResources().getColor(R.color.white));
//        if (!(layBottomContainer instanceof OnScrollChangedListener))
//            throw new IllegalArgumentException("adapter getView return child -> must impl RetailMeNotLayout.OnScrollChangedListener");
//        else
//            registerScrollChangedCallback(layBottomContainer);
        addView(layBottomContainer);

    }

    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mAdapter = adapter;
            mAdapter.mHolder = this;
        }
    }

    public static abstract class Adapter {
        private TicketFrameLayout mHolder;

        public abstract int getCount();

        public void notifyDataSetChanged() {
            mHolder.dataSetChanged();
        }

        public abstract View getView(int position, ViewGroup parent, int expendedHeight, int normalHeight);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                interceptDownY = ev.getRawY();
                if (isAnimMoving) {
                    isAnimMoving = false;
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getRawY() - interceptDownY) > touchSlop) {
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        return intercept;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (isFling && mScroller.getCurrVelocity() < minVelocity) {
                isFling = false;
                mScroller.forceFinished(true);
                int currIndex = realIndex + (realPercent > 0.5f ? 1 : 0);
                int flingAbortScrollY = mScroller.getCurrY();
                Log.i("ee", "----------------------aaa");
                scrollerStartScroll(flingAbortScrollY, -flingAbortScrollY + currIndex * childNormalHeight, animationTime);
                return;
            }

            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            float scrollResult = mScroller.getCurrY();
            float percent = scrollResult / childNormalHeight;
            realIndex = (int) (percent);
            realPercent = percent % 1;
            if (realIndex < 0) realIndex = 0;
            if (realPercent < 0) realPercent = 0;

            for (OnScrollChangedListener c : scrollCallbacks) {
                c.onScroll(realIndex, realPercent);
            }
            postInvalidate();
            isAnimMoving = true;
        } else {
            isAnimMoving = false;
        }
        super.computeScroll();
    }

    public void scrollerStartScroll(int startY, int dy, int duration) {
        isAnimMoving = true;
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }

    private class GestureDetectorWithOnUp extends GestureDetector {
        private final GestureListenerWithOnUp mListener;

        public GestureDetectorWithOnUp(Context context, GestureListenerWithOnUp listener) {
            super(context, listener);
            mListener = listener;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            boolean handled = super.onTouchEvent(ev);
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                    Log.i("ee", "up");
                    mListener.onUp(ev);
                    break;
            }
            return handled;
        }
    }

    private class GestureListenerWithOnUp extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isTouching) {
                isTouching = true;
                isFling = false;
                touchDownScrollY = getScrollY();
                touchDownY = e2.getRawY();
                mScroller.forceFinished(true);
            } else {
                float offset = (e2.getRawY() - touchDownY) * resistance;
                float scrollResult = touchDownScrollY - offset;

                int touchMoveScrollY = getScrollY();
                if (touchMoveScrollY < 0) {
                    scrollTo(0, (int) scrollResult / 2);
                } else if (touchMoveScrollY > childTotalHeight) {
                    scrollTo(0, childTotalHeight + (int) (scrollResult - childTotalHeight) / 2);
                } else {
                    scrollTo(0, (int) scrollResult);
                }

                float percent = scrollResult / childNormalHeight;
                realIndex = (int) (percent);
                realPercent = percent % 1;
                if (realIndex < 0) realIndex = 0;
                if (realPercent < 0) realPercent = 0;
                for (OnScrollChangedListener c : scrollCallbacks) {
                    c.onScroll(realIndex, realPercent);
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            isFling = true;
            touchUpScrollY = getScrollY();
            if (touchUpScrollY < 0) {
                Log.i("ee", "----------------------bbb");
                scrollerStartScroll(touchUpScrollY, 0 - touchUpScrollY, animationTime);
            } else if (touchUpScrollY > childTotalHeight) {
                Log.i("ee", "----------------------ccc");
                if (dataSize == 1) {
                    isFling = false;
                }
                scrollerStartScroll(touchUpScrollY, childTotalHeight - touchUpScrollY, animationTime);
            } else {
                if (Math.abs(velocityY) > minVelocity) {
                    isAnimMoving = true;
                    Log.i("ee", "----------------------eee");
                    mScroller.fling(0, touchUpScrollY, 0, (int) -velocityY, 0, 0, 0, childTotalHeight);
                    computeScroll();
                } else {
                    int currIndex = realIndex + (realPercent > 0.5f ? 1 : 0);
                    Log.i("ee", "----------------------ddd");
                    scrollerStartScroll(touchUpScrollY, -touchUpScrollY + currIndex * childNormalHeight, animationTime);
                }
            }
            return true;
        }

        public boolean onUp(MotionEvent e) {
            isTouching = false;
            touchUpScrollY = getScrollY();
            if (!isFling) {
                if (touchUpScrollY < 0) {
                    Log.i("ee", "----------------------fff");
                    scrollerStartScroll(touchUpScrollY, 0 - touchUpScrollY, animationTime);
                } else if (touchUpScrollY > childTotalHeight) {
                    Log.i("ee", "----------------------ggg");
                    if (dataSize == 1) {
                        isFling = false;
                    }
                    scrollerStartScroll(touchUpScrollY, childTotalHeight - touchUpScrollY, animationTime);
                } else {
                    int currIndex = realIndex + (realPercent > 0.5f ? 1 : 0);
                    Log.i("ee", "----------------------hhh");
                    scrollerStartScroll(touchUpScrollY, -touchUpScrollY + currIndex * childNormalHeight, animationTime);
                }
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            isTouching = true;
            isFling = false;
            touchDownScrollY = getScrollY();
            touchDownY = e.getRawY();
            mScroller.forceFinished(true);
            return true;
        }
    }
}
