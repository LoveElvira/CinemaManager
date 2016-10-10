package com.yyjlr.tickets.viewutils.cilpphoto;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Elvira on 2016/8/11.
 */
public class AmplifyImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean mOnce;
    /** 初始化时的缩放值 */
    private float mInitScale;
    /** 双击放大到达的值 */
    private float mMidScale;
    /** 放大的极限值 */
    private float mMaxScale;

    private Matrix mScaleMatrix;
    /** 获取多点触控时的缩放比例 */
    private ScaleGestureDetector mScaleGestureDetector;

    // ------------------------自由移动
    /** 记录上一次多点触控的数量 */
    private int mLastPointerCount;

    private float mLastX;
    private float mLastY;
    private int mTouchSlop;
    private boolean isCanDrag;

    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    // ------------------------双击放大和缩小
    private GestureDetector mGestureDetector;

    private boolean isAutoScale;

    public AmplifyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        mScaleGestureDetector = new ScaleGestureDetector(context, this);

        setOnTouchListener(this);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

                        if (isAutoScale)
                            return true;

                        float x = e.getX();
                        float y = e.getY();

                        if (getScale() < mMidScale) {
                            // mScaleMatrix.postScale(mMidScale / getScale(),
                            // mMidScale / getScale(), x, y);
                            // setImageMatrix(mScaleMatrix);
                            postDelayed(new AutoScaleRunnable(mMidScale, x, y),
                                    16);
                            isAutoScale = true;
                        } else {
                            // mScaleMatrix.postScale(mInitScale / getScale(),
                            // mInitScale / getScale(), x, y);
                            // setImageMatrix(mScaleMatrix);
                            postDelayed(
                                    new AutoScaleRunnable(mInitScale, x, y), 16);
                            isAutoScale = true;
                        }

                        return true;
                    }
                });
    }

    /** 缓慢放大和缩小 */
    private class AutoScaleRunnable implements Runnable {
        // 缩放的目标值
        private float mTargetScale;
        // 缩放的中心点
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.93f;

        private float tmpScale;

        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            }

            if (getScale() > mTargetScale) {
                tmpScale = SMALLER;
            }
        }

        @Override
        public void run() {
            // 进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();

            if ((tmpScale > 1.0f && currentScale < mTargetScale)
                    || (tmpScale < 1.0f && currentScale > mTargetScale)) {// 比目标值小
                postDelayed(this, 16);
            } else {// 比目标值大
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);

                isAutoScale = false;
            }
        }

    }

    public AmplifyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AmplifyImageView(Context context) {
        this(context, null);
    }

    /** 当控件添加到屏幕上时执行的方法 */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /** 当控件从屏幕上消失时执行的方法 */
    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 布局加载完成后调用的方法 获取imageview加载的图片
     * */
    @Override
    public void onGlobalLayout() {

        if (!mOnce) {
            // 控件的宽和高
            int width = getWidth();
            int height = getHeight();
            // 得到图片以及图片的宽高
            Drawable d = getDrawable();
            if (d == null) {
                return;
            }
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            float scale = 1.0f;// 默认缩放值

            // 图片比imageview宽，没有imageview高
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;// 避免400/600=0，这里一定要*1.0f
            }
            // 图片比imageview高，没有imageview宽
            if (dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }
            // 图片宽高都大于imageview或都小于imageview
            if ((dh > height && dw > width) || (dh < height && dw < width)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            // 设置初始化时的比例
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;

            // 将图片移动到控件的中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;
            mScaleMatrix.postTranslate(dx, dy);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2,
                    height / 2);
            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }

    }

    /** 获取当前图片的缩放值 */
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        // 获取用户要缩放的大小
        float scaleFator = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;

        // 缩放范围控制：比最大小，并且大于1，或者比最小大，并且小于1，可以放大或者缩小
        if ((scale < mMaxScale && scaleFator > 1.0f)
                || (scale > mInitScale && scaleFator < 1.0f)) {
            if (scale * scaleFator < mInitScale) {// 缩小后，小于最小值,应设置为最小值
                scaleFator = mInitScale / scale;
            }

            if (scale * scaleFator > mMaxScale) {// 放大后，大于最大值,应设置为最大值
                scaleFator = mMaxScale / scale;
            }
            // 缩放
            mScaleMatrix.postScale(scaleFator, scaleFator,
                    detector.getFocusX(), detector.getFocusY());

            checkBorderAndCenterWhenScale();

            setImageMatrix(mScaleMatrix);
        }

        return true;
    }

    /** 获取图片放大缩小后的宽高以及位置 */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        return rectF;
    }

    /** 缩放时边界和中心点的控制 */
    private void checkBorderAndCenterWhenScale() {
        RectF rect = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rect.width() >= width) {// 水平方向控制
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }

        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        // 宽或者高小于控件的宽高则剧终显示
        if (rect.width() < width) {
            deltaX = width / 2.0f - rect.right + rect.width() / 2.0f;
        }
        if (rect.height() < height) {
            deltaY = height / 2.0f - rect.bottom + rect.height() / 2.0f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // 这里必须返回true
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    boolean isMove = false;
    float x1, y1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {// 双击判断在最前，保证双击时无法移动
            return true;
        }

        mScaleGestureDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        // 获得多点触控的数量
        int pointerCount = event.getPointerCount();
        // 循环拿到每个触控点的位置
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        // 计算中心点
        x /= pointerCount;
        y /= pointerCount;

        if (mLastPointerCount != pointerCount) {// 触控点的数量发生变化
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF rect = getMatrixRectF();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 当图片的宽或者高大于控件时，不允许父控件拦截touch事件
                // 获取控件宽高返回的是int值，避免发生错应转换为float
                if (rect.width() > getWidth() + 0.01f
                        || rect.height() > getHeight() + 0.01f) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 当图片的宽或者高大于控件时，不允许父控件拦截touch事件
                // 获取控件宽高返回的是int值，避免发生错应转换为float
                if (rect.width() > getWidth() + 0.01f
                        || rect.height() > getHeight() + 0.01f) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                isMove = true;

                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {
                    RectF rectF = getMatrixRectF();
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // 宽度小于控件宽不能横向移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        // 高度小于控件高不能纵向移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }

                }
                mLastX = x;
                mLastY = y;

                break;
            case MotionEvent.ACTION_UP:
                if (rect.width() > getWidth() + 0.01f
                        || rect.height() > getHeight() + 0.01f) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float f = Math.abs(x1 - event.getX());
                    if (f == 0) {
                        // click
                        mListener.onAmplifyClick();
                    } else {
                        // move
                    }
                } else {
                    if (rect.width() == getWidth() || rect.height() == getHeight()) {
                        if (isMove) {
                            isMove = false;
                            float f = Math.abs(x1 - event.getX());
                            if (f == 0) {
                                // click
                                mListener.onAmplifyClick();
                            } else {
                                // move
                            }
                        }
                    }
                }
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }
        return true;
    }

    /** 移动时的边界检查 */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();

        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /** 判断是否足以移动 */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    private AmplifyDownListener mListener;

    public interface AmplifyDownListener {
        public void onAmplifyClick();
    }

    public void setAmplifyListener(AmplifyDownListener listener) {
        this.mListener = listener;
    }
}
