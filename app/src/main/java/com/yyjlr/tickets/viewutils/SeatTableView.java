package com.yyjlr.tickets.viewutils;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmSeatEntity;
import com.yyjlr.tickets.model.seat.SeatInfo;
import com.yyjlr.tickets.model.seat.SeatTypeInfo;
import com.yyjlr.tickets.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvira on 2016/9/1.
 */
public class SeatTableView extends View {

    /**
     * 过道
     */
    private final static int SEAT_TYPE_ROAD = 0x00;

    /**
     * 普通座位
     */
    private final static int SEAT_TYPE_ORDINARY = 0x01;
    /**
     * 普通座位 已选
     */
    private final static int SEAT_TYPE_ORDINARY_1 = 0x02;
    /**
     * 普通座位 已售
     */
    private final static int SEAT_TYPE_ORDINARY_2 = 0x03;
    /**
     * 普通座位 维修
     */
    private final static int SEAT_TYPE_ORDINARY_3 = 0x04;

    /**
     * 情侣首座
     */
    private final static int SEAT_TYPE_LOVERS_LEFT = 0x05;
    /**
     * 情侣首座 已选
     */
    private final static int SEAT_TYPE_LOVERS_LEFT_1 = 0x06;
    /**
     * 情侣首座 已售
     */
    private final static int SEAT_TYPE_LOVERS_LEFT_2 = 0x07;
    /**
     * 情侣首座 维修
     */
    private final static int SEAT_TYPE_LOVERS_LEFT_3 = 0x08;


    /**
     * 情侣次座
     */
    private final static int SEAT_TYPE_LOVERS_RIGHT = 0x09;
    /**
     * 情侣次座 已选
     */
    private final static int SEAT_TYPE_LOVERS_RIGHT_1 = 0x10;
    /**
     * 情侣次座 已售
     */
    private final static int SEAT_TYPE_LOVERS_RIGHT_2 = 0x11;
    /**
     * 情侣次座 维修
     */
    private final static int SEAT_TYPE_LOVERS_RIGHT_3 = 0x12;

    /**
     * 特殊人群
     */
    private final static int SEAT_TYPE_DISABLED = 0x13;
    /**
     * 特殊人群 已选
     */
    private final static int SEAT_TYPE_DISABLED_1 = 0x14;
    /**
     * 特殊人群 已售
     */
    private final static int SEAT_TYPE_DISABLED_2 = 0x15;
    /**
     * 特殊人群 维修
     */
    private final static int SEAT_TYPE_DISABLED_3 = 0x16;


    /**
     * VIP专座
     */
    private final static int SEAT_TYPE_VIP = 0x17;
    /**
     * VIP专座 已选
     */
    private final static int SEAT_TYPE_VIP_1 = 0x18;
    /**
     * VIP专座 已售
     */
    private final static int SEAT_TYPE_VIP_2 = 0x19;
    /**
     * VIP专座 维修
     */
    private final static int SEAT_TYPE_VIP_3 = 0x20;


    private final String TAG = this.getClass().getSimpleName();
    private final boolean DBG = false;
    Paint paint = new Paint();
    Paint overviewPaint = new Paint();
    Paint lineNumberPaint;
    float lineNumberTxtHeight;
    /**
     * 用来保存所有行号
     */
    ArrayList<String> lineNumbers = new ArrayList<>();
    Paint.FontMetrics lineNumberPaintFontMetrics;
    Matrix matrix = new Matrix();
    /**
     * 座位水平间距
     */
    int spacing;
    /**
     * 座位垂直间距
     */
    int verSpacing;
    /**
     * 行号宽度
     */
    int numberWidth;
    /**
     * 行数
     */
    int row;
    /**
     * 列数
     */
    int column;
    /**
     * 可选时座位的图片
     *//*
    Bitmap seatBitmap;
    *//**
     * 选中时座位的图片
     *//*
    Bitmap checkedSeatBitmap;
    *//**
     * 座位已经售出时的图片
     *//*
    Bitmap seatSoldBitmap;
    */
    /**
     * 情侣座  左右
     *//*
    Bitmap seatLoversLeftBitmap;
    Bitmap seatLoversRightBitmap;*/
    Bitmap overviewBitmap;
    int lastX;
    int lastY;
    /**
     * 整个座位图的宽度
     */
    int seatBitmapWidth;
    /**
     * 整个座位图的高度
     */
    int seatBitmapHeight;
    /**
     * 标识是否需要绘制座位图
     */
    boolean isNeedDrawSeatBitmap = true;
    /**
     * 概览图白色方块高度
     */
    float rectHeight;
    /**
     * 概览图白色方块的宽度
     */
    float rectWidth;
    /**
     * 概览图上方块的水平间距
     */
    float overviewSpacing;
    /**
     * 概览图上方块的垂直间距
     */
    float overviewVerSpacing;
    /**
     * 概览图的比例
     */
    float overviewScale = 4.8f;
    /**
     * 荧幕高度
     */
    float screenHeight;
    /**
     * 荧幕默认宽度与座位图的比例
     */
    float screenWidthScale = 0.5f;
    /**
     * 荧幕最小宽度
     */
    int defaultScreenWidth;
    /**
     * 标识是否正在缩放
     */
    boolean isScaling;
    /**
     * 缩放的中心点
     */
    float scaleX, scaleY;
    /**
     * 是否是第一次缩放
     */
    boolean firstScale = true;
    /**
     * 最多可以选择的座位数量
     */
    int maxSelected = Integer.MAX_VALUE;
    /**
     * 整个概览图的宽度
     */
    float rectW;
    /**
     * 整个概览图的高度
     */
    float rectH;
    /**
     * 是否第一次执行onDraw
     */
    boolean isFirstDraw = true;
    /**
     * 标识是否需要绘制概览图
     */
    boolean isDrawOverview = false;
    /**
     * 标识是否需要更新概览图
     */
    boolean isDrawOverviewBitmap = true;
    int overview_checked;
    int overview_sold;
    int txt_color;

    //普通座位
    Bitmap seatTypeOrdinaryBitmap;
    Bitmap seatTypeOrdinaryBitmap_1;
    Bitmap seatTypeOrdinaryBitmap_2;
    Bitmap seatTypeOrdinaryBitmap_3;
    //情侣首座
    Bitmap seatTypeLoversLeftBitmap;
    Bitmap seatTypeLoversLeftBitmap_1;
    Bitmap seatTypeLoversLeftBitmap_2;
    Bitmap seatTypeLoversLeftBitmap_3;
    //情侣次座
    Bitmap seatTypeLoversRightBitmap;
    Bitmap seatTypeLoversRightBitmap_1;
    Bitmap seatTypeLoversRightBitmap_2;
    Bitmap seatTypeLoversRightBitmap_3;
    //特殊人群
    Bitmap seatTypeDisabledBitmap;
    Bitmap seatTypeDisabledBitmap_1;
    Bitmap seatTypeDisabledBitmap_2;
    Bitmap seatTypeDisabledBitmap_3;
    //VIP专座
    Bitmap seatTypeVipBitmap;
    Bitmap seatTypeVipBitmap_1;
    Bitmap seatTypeVipBitmap_2;
    Bitmap seatTypeVipBitmap_3;

    //    int seatCheckedResID;
//    int seatSoldResID;
//    int seatLoversLeftResID;
//    int seatLoversRightResID;
//    int seatAvailableResID;
    boolean isOnClick;
    Paint pathPaint;
    RectF rectF;
    /**
     * 头部下面横线的高度
     */
//    int borderHeight = 1;
    Paint redBorderPaint;
    float xScale1 = 1;
    float yScale1 = 1;
    int screenWidthPx;
    int screenHeightPx;
    int screenX;
    Matrix tempMatrix = new Matrix();
    int bacColor = Color.parseColor("#7e000000");
    Handler handler = new Handler();
    ArrayList<Integer> selects = new ArrayList<>();
    private List<SeatInfo> seatList = new ArrayList<>();
    private List<SeatInfo> newSeatList = new ArrayList<>();
    private List<SeatInfo> oldSeatList = new ArrayList<>();
    private List<SeatTypeInfo> seatType = new ArrayList<>();
    float[] m = new float[9];
    /**
     * 监听手势进行缩放
     */
    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            isScaling = true;
            float scaleFactor = detector.getScaleFactor();
            Log.i("ee", "---------------scaleFactor:" + scaleFactor);
            if (getMatrixScaleY() * scaleFactor > 3) {
                scaleFactor = 3 / getMatrixScaleY();
                Log.i("ee", "1---------------scaleFactor:" + scaleFactor);
            }

            if (firstScale) {
                //进行缩放的中心点
                scaleX = detector.getCurrentSpanX();
                scaleY = detector.getCurrentSpanY();
                firstScale = false;
            }

            if (getMatrixScaleY() * scaleFactor < 0.5) {
                scaleFactor = 0.5f / getMatrixScaleY();
                Log.i("ee", "2---------------scaleFactor:" + scaleFactor + "--------scaleX:" + scaleX + "-----------scaleY:" + scaleY);
            }


            matrix.postScale(scaleFactor, scaleFactor, scaleX, scaleY);
            invalidate();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            isScaling = false;
            firstScale = true;
        }
    });
    private SeatChecker seatChecker;
    /**
     * 荧幕名称
     */
    private String screenName = "";
    private int downX, downY;
    private boolean pointer;
    /**
     * 默认的座位图宽度,如果使用的自己的座位图片比这个尺寸大或者小,会缩放到这个大小
     */
    private float defaultImgW = 45;
    /**
     * 默认的座位图高度
     */
    private float defaultImgH = 39;
    /**
     * 座位图片的宽度
     */
    private int seatWidth;
    /**
     * 座位图片的高度
     */
    private int seatHeight;
    /**
     * 影厅与座位之间的距离
     */
//    private int screenAndSeatHeight;
    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isOnClick = true;
            int x = (int) e.getX();
            int y = (int) e.getY();

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {

                    int tempX = (int) ((j * seatWidth) * getMatrixScaleX() + getTranslateX());
                    int maxTemX = (int) (tempX + seatWidth * getMatrixScaleX());

                    int tempY = (int) ((i * seatHeight) * getMatrixScaleY() + getTranslateY());
                    int maxTempY = (int) (tempY + seatHeight * getMatrixScaleY());

                    if (seatChecker != null && seatChecker.isValidSeat(i, j, newSeatList.get(i * column + j).getFlag()) && !seatChecker.isSold(i, j, newSeatList.get(i * column + j).getType())) {
                        Log.i("ee", "i=" + i + "--j=" + j + "---------selects--3-------------:" + selects.toString());
                        int id = getID(i, j);
                        int index = isHave(id);
                        if (x >= tempX && x <= maxTemX && y >= tempY && y <= maxTempY) {
                            if (index >= 0) {
                                remove(index);
                                if (seatChecker != null) {
                                    seatChecker.unCheck(newSeatList.get(id));
                                    if (newSeatList.get(id).getType().equals("1")) {
                                        remove(isHave(getID(i, j + 1)));
                                        seatChecker.unCheck(newSeatList.get(getID(i, j + 1)));
                                    } else if (newSeatList.get(id).getType().equals("2")) {
                                        remove(isHave(getID(i, j - 1)));
                                        seatChecker.unCheck(newSeatList.get(getID(i, j - 1)));
                                    }
                                }
                            } else {
                                Toast toast = Toast.makeText(getContext(), "最多只能选择" + maxSelected + "个", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                if (selects.size() >= maxSelected) {
                                    toast.show();
                                    return super.onSingleTapConfirmed(e);
                                } else {
                                    addChooseSeat(i, j);
                                    if (newSeatList.get(id).getType().equals("1")) {
                                        addChooseSeat(i, j + 1);
                                        if (seatChecker != null) {
                                            if (selects.size() > maxSelected) {
                                                remove(isHave(getID(i, j)));
                                                remove(isHave(getID(i, j + 1)));
                                                toast.show();
                                                return super.onSingleTapConfirmed(e);
                                            }
                                            seatChecker.checked(newSeatList.get(id));
                                            seatChecker.checked(newSeatList.get(i * column + j + 1));
                                        }
                                    } else if (newSeatList.get(i * column + j).getType().equals("2")) {
                                        addChooseSeat(i, j - 1);
                                        if (seatChecker != null) {
                                            if (selects.size() > maxSelected) {
                                                remove(isHave(getID(i, j)));
                                                remove(isHave(getID(i, j - 1)));
                                                toast.show();
                                                return super.onSingleTapConfirmed(e);
                                            }
                                            seatChecker.checked(newSeatList.get(id));
                                            seatChecker.checked(newSeatList.get(i * column + j - 1));
                                        }
                                    } else {
                                        if (seatChecker != null) {
                                            seatChecker.checked(newSeatList.get(id));
                                        }
                                    }


                                }
                            }
                            isNeedDrawSeatBitmap = true;
                            isDrawOverviewBitmap = true;
                            float currentScaleY = getMatrixScaleY();

                            if (currentScaleY < 1.7) {
                                scaleX = x;
                                scaleY = y;
                                zoomAnimate(currentScaleY, 1.9f);
                            }

                            invalidate();
                            break;
                        }
                    }
                }
            }

            return super.onSingleTapConfirmed(e);
        }
    });
    private Runnable hideOverviewRunnable = new Runnable() {
        @Override
        public void run() {
            isDrawOverview = false;
            invalidate();
        }
    };
    private float zoom;

    public SeatTableView(Context context) {
        super(context);
    }


    public SeatTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SeatTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 设置行号 默认显示 1,2,3....数字
     *
     * @param lineNumbers
     */
    public void setLineNumbers(ArrayList<String> lineNumbers) {
        this.lineNumbers = lineNumbers;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SeatTableView);
        overview_checked = typedArray.getColor(R.styleable.SeatTableView_overview_checked, Color.parseColor("#5A9E64"));
        overview_sold = typedArray.getColor(R.styleable.SeatTableView_overview_sold, Color.RED);
        txt_color = typedArray.getColor(R.styleable.SeatTableView_txt_color, Color.WHITE);

        /*seatCheckedResID = typedArray.getResourceId(R.styleable.SeatTableView_seat_checked, R.mipmap.seat_select);
        seatSoldResID = typedArray.getResourceId(R.styleable.SeatTableView_overview_sold, R.mipmap.seat_locked);
        seatLoversLeftResID = typedArray.getResourceId(R.styleable.SeatTableView_overview_sold, R.mipmap.seat_lovers_left);
        seatLoversRightResID = typedArray.getResourceId(R.styleable.SeatTableView_overview_sold, R.mipmap.seat_lovers_right);
        seatAvailableResID = typedArray.getResourceId(R.styleable.SeatTableView_seat_available, R.mipmap.seat_no_select);*/
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {

        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();

        screenWidthPx = dm.widthPixels; // 屏幕宽（dip，如：320dip）
        screenHeightPx = dm.heightPixels; // 屏幕宽（dip，如：533dip）
        Log.i("ee", screenWidthPx + "-------------" + screenHeightPx);

//        screenAndSeatHeight = (int) dip2Px(20);
        spacing = (int) dip2Px(5);
        verSpacing = (int) dip2Px(5);
        defaultScreenWidth = (int) dip2Px(80);

//        seatBitmap = BitmapFactory.decodeResource(getResources(), seatAvailableResID);

        float scaleX = defaultImgW / seatTypeOrdinaryBitmap.getWidth();
        float scaleY = defaultImgH / seatTypeOrdinaryBitmap.getHeight();
        xScale1 = scaleX;
        yScale1 = scaleY;

        seatHeight = (int) (seatTypeOrdinaryBitmap.getHeight() * yScale1);
        seatWidth = (int) (seatTypeOrdinaryBitmap.getWidth() * xScale1);

//        checkedSeatBitmap = BitmapFactory.decodeResource(getResources(), seatCheckedResID);
//        seatSoldBitmap = BitmapFactory.decodeResource(getResources(), seatSoldResID);
//        seatLoversLeftBitmap = BitmapFactory.decodeResource(getResources(), seatLoversLeftResID);
//        seatLoversRightBitmap = BitmapFactory.decodeResource(getResources(), seatLoversRightResID);

        seatBitmapWidth = (int) ((column + 1) * seatTypeOrdinaryBitmap.getWidth() * xScale1);
        seatBitmapHeight = (int) ((row) * seatTypeOrdinaryBitmap.getHeight() * yScale1);
        paint.setColor(Color.RED);
        numberWidth = (int) dip2Px(22);

        screenHeight = dip2Px(20);

        screenX = (screenWidthPx - seatBitmapWidth) / 2;

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(Color.parseColor("#e2e2e2"));

        redBorderPaint = new Paint();
        redBorderPaint.setAntiAlias(true);
        redBorderPaint.setColor(Color.RED);
        redBorderPaint.setStyle(Paint.Style.STROKE);
        redBorderPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 1);

        rectF = new RectF();

        rectHeight = seatHeight / overviewScale;
        rectWidth = seatWidth / overviewScale;
        overviewSpacing = spacing / overviewScale;
        overviewVerSpacing = verSpacing / overviewScale;

        rectW = (column) * rectWidth + (column) * overviewSpacing + overviewSpacing * 2;
        rectH = (row) * rectHeight + (row - 1) * overviewVerSpacing + overviewVerSpacing * 2 + 20;
        overviewBitmap = Bitmap.createBitmap((int) rectW, (int) rectH, Bitmap.Config.ARGB_4444);

        lineNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lineNumberPaint.setColor(bacColor);
        lineNumberPaint.setTextSize(numberWidth * getMatrixScaleX() / 2/*getResources().getDisplayMetrics().density * 14*/);
        lineNumberTxtHeight = lineNumberPaint.measureText("4");
        lineNumberPaintFontMetrics = lineNumberPaint.getFontMetrics();
        lineNumberPaint.setTextAlign(Paint.Align.CENTER);

        if (lineNumbers == null) {
            lineNumbers = new ArrayList<>();
        } else if (lineNumbers.size() <= 0) {
            for (int i = 0; i < row; i++) {
                lineNumbers.add((i + 1) + "");
            }
        }


        matrix.postTranslate(screenX, screenHeight);

    }

    //给座位赋值图片 Bitmap
    private void initSeatBitmap() {
        for (int i = 0; i < seatType.size(); i++) {
            final String type = seatType.get(i).getType();
            final Bitmap[] bitmap = {null};
            final int finalI = i;
            //Picasso.with(getContext()).load(seatType.get(finalI).getIcon());
//            bitmap[0] = BitmapUtils.returnBitmap(seatType.get(finalI).getIcon());
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
            bitmap[0] = BitmapUtils.returnBitmap(seatType.get(finalI).getIcon());
            Log.i("ee", "bitmap------------" + bitmap[0]);

            if (type.equals("0")) {//普通座位
                seatTypeOrdinaryBitmap = bitmap[0];
                Log.i("ee", "seatTypeOrdinaryBitmap:-----" + seatTypeOrdinaryBitmap);
            } else if (type.equals("0-1")) {//普通座位 已选
                seatTypeOrdinaryBitmap_1 = bitmap[0];
                Log.i("ee", "seatTypeOrdinaryBitmap_1:-----" + seatTypeOrdinaryBitmap_1);
            } else if (type.equals("0-2")) {//普通座位  已售
                seatTypeOrdinaryBitmap_2 = bitmap[0];
                Log.i("ee", "seatTypeOrdinaryBitmap_2:-----" + seatTypeOrdinaryBitmap_2);
            } else if (type.equals("0-3")) {//普通座位  维修
                seatTypeOrdinaryBitmap_3 = bitmap[0];
                Log.i("ee", "seatTypeOrdinaryBitmap_3:-----" + seatTypeOrdinaryBitmap_3);
            } else if (type.equals("1")) {//情侣首座
                seatTypeLoversLeftBitmap = bitmap[0];
                Log.i("ee", "seatTypeLoversLeftBitmap:-----" + seatTypeLoversLeftBitmap);
            } else if (type.equals("1-1")) {//情侣首座  已选
                seatTypeLoversLeftBitmap_1 = bitmap[0];
                Log.i("ee", "seatTypeLoversLeftBitmap_1:-----" + seatTypeLoversLeftBitmap_1);
            } else if (type.equals("1-2")) {//情侣首座  已售
                seatTypeLoversLeftBitmap_2 = bitmap[0];
                Log.i("ee", "seatTypeLoversLeftBitmap_2:-----" + seatTypeLoversLeftBitmap_2);
            } else if (type.equals("1-3")) {//情侣首座  维修
                seatTypeLoversLeftBitmap_3 = bitmap[0];
                Log.i("ee", "seatTypeLoversLeftBitmap_3:-----" + seatTypeLoversLeftBitmap_3);
            } else if (type.equals("2")) {//情侣次座
                seatTypeLoversRightBitmap = bitmap[0];
                Log.i("ee", "seatTypeLoversRightBitmap:-----" + seatTypeLoversRightBitmap);
            } else if (type.equals("2-1")) {//情侣次座  已选
                seatTypeLoversRightBitmap_1 = bitmap[0];
                Log.i("ee", "seatTypeLoversRightBitmap_1:-----" + seatTypeLoversRightBitmap_1);
            } else if (type.equals("2-2")) {//情侣次座  已售
                seatTypeLoversRightBitmap_2 = bitmap[0];
                Log.i("ee", "seatTypeLoversRightBitmap_2:-----" + seatTypeLoversRightBitmap_2);
            } else if (type.equals("2-3")) {//情侣次座  维修
                seatTypeLoversRightBitmap_3 = bitmap[0];
                Log.i("ee", "seatTypeLoversRightBitmap_3:-----" + seatTypeLoversRightBitmap_3);
            } else if (type.equals("3")) {//特殊人群
                seatTypeDisabledBitmap = bitmap[0];
                Log.i("ee", "seatTypeDisabledBitmap:-----" + seatTypeDisabledBitmap);
            } else if (type.equals("3-1")) {//特殊人群  已选
                seatTypeDisabledBitmap_1 = bitmap[0];
                Log.i("ee", "seatTypeDisabledBitmap_1:-----" + seatTypeDisabledBitmap_1);
            } else if (type.equals("3-2")) {//特殊人群  已售
                seatTypeDisabledBitmap_2 = bitmap[0];
                Log.i("ee", "seatTypeDisabledBitmap_2:-----" + seatTypeDisabledBitmap_2);
            } else if (type.equals("3-3")) {//特殊人群  维修
                seatTypeDisabledBitmap_3 = bitmap[0];
                Log.i("ee", "seatTypeDisabledBitmap_3:-----" + seatTypeDisabledBitmap_3);
            } else if (type.equals("4")) {//VIP专座
                seatTypeVipBitmap = bitmap[0];
                Log.i("ee", "seatTypeVipBitmap:-----" + seatTypeVipBitmap);
            } else if (type.equals("4-1")) {//VIP专座  已选
                seatTypeVipBitmap_1 = bitmap[0];
                Log.i("ee", "seatTypeVipBitmap_1:-----" + seatTypeVipBitmap_1);
            } else if (type.equals("4-2")) {//VIP专座  已售
                seatTypeVipBitmap_2 = bitmap[0];
                Log.i("ee", "seatTypeVipBitmap_2:-----" + seatTypeVipBitmap_2);
            } else if (type.equals("4-3")) {//VIP专座  维修
                seatTypeVipBitmap_3 = bitmap[0];
                Log.i("ee", "seatTypeVipBitmap_3:-----" + seatTypeVipBitmap_3);
            }

//                }
//            }).start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long startTime = System.currentTimeMillis();
        if (row <= 0 || column == 0) {
            return;
        }

        drawSeat(canvas);
        drawNumber(canvas);

        drawScreen(canvas);

        if (isDrawOverview) {
            long s = System.currentTimeMillis();
            if (isDrawOverviewBitmap) {
                drawOverview();
            }
            canvas.drawBitmap(overviewBitmap, 0, 0, null);
            drawOverview(canvas);
            Log.d("drawTime", "OverviewDrawTime:" + (System.currentTimeMillis() - s));
        }

        if (DBG) {
            long drawTime = System.currentTimeMillis() - startTime;
            Log.d("drawTime", "totalDrawTime:" + drawTime);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        super.onTouchEvent(event);

        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        int pointerCount = event.getPointerCount();

        if (pointerCount > 1) {
            pointer = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointer = false;
                downX = x;
                downY = y;
                isDrawOverview = true;
                handler.removeCallbacks(hideOverviewRunnable);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScaling && !isOnClick) {
                    int downDX = Math.abs(x - downX);
                    int downDY = Math.abs(y - downY);
                    if ((downDX > 10 || downDY > 10) && !pointer) {
                        int dx = x - lastX;
                        int dy = y - lastY;
                        matrix.postTranslate(dx, dy);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.postDelayed(hideOverviewRunnable, 1500);

                autoScale();
                int downDX = Math.abs(x - downX);
                int downDY = Math.abs(y - downY);
                if ((downDX > 10 || downDY > 10) && !pointer) {
                    autoScroll();
                }

                break;
        }
        isOnClick = false;
        lastY = y;
        lastX = x;

        return true;
    }

    /**
     * 绘制中间屏幕
     */
    void drawScreen(Canvas canvas) {

        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setColor(Color.parseColor("#e2e2e2"));
        float startY = 0;

        float centerX = seatBitmapWidth * getMatrixScaleX() / 2 + getTranslateX();
//        float centerX = screenWidthPx * getMatrixScaleX() / 2;
        float screenWidth = seatBitmapWidth * screenWidthScale * getMatrixScaleX();
        if (screenWidth < defaultScreenWidth) {
            screenWidth = defaultScreenWidth;
        }

        Path path = new Path();
        path.moveTo(centerX, startY);
        path.lineTo(centerX - screenWidth / 2, startY);
        path.lineTo(centerX - screenWidth / 2 + 20 * getMatrixScaleX(), screenHeight * getMatrixScaleY() + startY);
        path.lineTo(centerX + screenWidth / 2 - 20 * getMatrixScaleX(), screenHeight * getMatrixScaleY() + startY);
        path.lineTo(centerX + screenWidth / 2, startY);

        canvas.drawPath(path, pathPaint);

        pathPaint.setColor(Color.BLACK);
        pathPaint.setTextSize(20 * getMatrixScaleX());

        canvas.drawText(screenName, centerX - pathPaint.measureText(screenName) / 2, getBaseLine(pathPaint, startY, startY + screenHeight * getMatrixScaleY()), pathPaint);
    }

    void drawSeat(Canvas canvas) {

        zoom = getMatrixScaleX();
        long startTime = System.currentTimeMillis();
        float translateX = getTranslateX();
        float translateY = getTranslateY();
        float scaleX = zoom;
        float scaleY = zoom;

        for (int i = 0; i < row; i++) {
            float top = i * seatTypeOrdinaryBitmap.getHeight() * yScale1 * scaleY + translateY/* + screenAndSeatHeight * scaleY*/;

            float bottom = top + seatTypeOrdinaryBitmap.getHeight() * yScale1 * scaleY;
            if (bottom < 0 || top > getHeight()) {
                continue;
            }

            for (int j = 0; j < column; j++) {
                float left = 0;
//                if (((i) * column + j) < newSeatList.size() && newSeatList.get((i) * column + j).getType().equals("2")) {
//                    left = j * seatBitmap.getWidth() * xScale1 * scaleX + translateX;
//                } else {
                left = j * seatTypeOrdinaryBitmap.getWidth() * xScale1 * scaleX + translateX;
//                }

                float right = (left + seatTypeOrdinaryBitmap.getWidth() * xScale1 * scaleY);
                if (right < 0 || left > getWidth()) {
                    continue;
                }
//                Log.i("ee", "-------------top:" + top + "-----------bottom:" + bottom + "----------------left:" + left + "-----------------right:" + right);
                int seatType = getSeatType(i, j);
                tempMatrix.setTranslate(left, top);
                tempMatrix.postScale(xScale1, yScale1, left, top);
                tempMatrix.postScale(scaleX, scaleY, left, top);

                switch (seatType) {
                    case SEAT_TYPE_ROAD://过道
//                        canvas.drawBitmap(seatBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_ORDINARY://普通座位
                        canvas.drawBitmap(seatTypeOrdinaryBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_ORDINARY_1://普通座位 已选
                        canvas.drawBitmap(seatTypeOrdinaryBitmap_1, tempMatrix, paint);
//                        drawText(canvas, i, j, top, left);
                        break;
                    case SEAT_TYPE_ORDINARY_2://普通座位 已售
                        canvas.drawBitmap(seatTypeOrdinaryBitmap_2, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_ORDINARY_3://普通座位 维修
                        canvas.drawBitmap(seatTypeOrdinaryBitmap_3, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_LEFT://情侣座首座
                        canvas.drawBitmap(seatTypeLoversLeftBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_LEFT_1://情侣座首座 已选
                        canvas.drawBitmap(seatTypeLoversLeftBitmap_1, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_LEFT_2://情侣座首座 已售
                        canvas.drawBitmap(seatTypeLoversLeftBitmap_2, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_LEFT_3://情侣座首座 维修
                        canvas.drawBitmap(seatTypeLoversLeftBitmap_3, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_RIGHT://情侣座次座
                        canvas.drawBitmap(seatTypeLoversRightBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_RIGHT_1://情侣座次座 已选
                        canvas.drawBitmap(seatTypeLoversRightBitmap_1, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_RIGHT_2://情侣座次座 已售
                        canvas.drawBitmap(seatTypeLoversRightBitmap_2, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_LOVERS_RIGHT_3://情侣座次座 维修
                        canvas.drawBitmap(seatTypeLoversRightBitmap_3, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_DISABLED://特殊人群
                        canvas.drawBitmap(seatTypeDisabledBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_DISABLED_1://特殊人群 已选
                        canvas.drawBitmap(seatTypeDisabledBitmap_1, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_DISABLED_2://特殊人群 已售
                        canvas.drawBitmap(seatTypeDisabledBitmap_2, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_DISABLED_3://特殊人群 维修
                        canvas.drawBitmap(seatTypeDisabledBitmap_3, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_VIP://VIP专座
                        canvas.drawBitmap(seatTypeVipBitmap, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_VIP_1://VIP专座 已选
                        canvas.drawBitmap(seatTypeVipBitmap_1, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_VIP_2://VIP专座 已售
                        canvas.drawBitmap(seatTypeVipBitmap_2, tempMatrix, paint);
                        break;
                    case SEAT_TYPE_VIP_3://VIP专座 维修
                        canvas.drawBitmap(seatTypeVipBitmap_3, tempMatrix, paint);
                        break;


                }

            }
        }

        if (DBG) {
            long drawTime = System.currentTimeMillis() - startTime;
            Log.d("drawTime", "seatDrawTime:" + drawTime);
        }
    }

    private int getSeatType(int row, int column) {

        if ((row * (this.column) + column) < newSeatList.size()) {
            String type = newSeatList.get(row * (this.column) + column).getType();
            if (isHave(getID(row, column)) >= 0) {
                if (type.equals("0-1")) {//普通座位 已选
                    return SEAT_TYPE_ORDINARY_1;
                } else if (type.equals("1-1")) {//情侣座首座 已选
                    return SEAT_TYPE_LOVERS_LEFT_1;
                } else if (type.equals("2-1")) {//情侣座次座 已选
                    return SEAT_TYPE_LOVERS_RIGHT_1;
                } else if (type.equals("4-1")) {//VIP专座 已选
                    return SEAT_TYPE_VIP_1;
                }
            }
            if (seatChecker != null) {
                if ((newSeatList.get(row * (this.column) + column).getgRow() == row + 1
                        && newSeatList.get(row * (this.column) + column).getgCol() == column + 1)) {
                    if (type.equals("0")) {//普通座位
                        return SEAT_TYPE_ORDINARY;
                    } else if (type.equals("0-1")) {//普通座位 已选
                        return SEAT_TYPE_ORDINARY_1;
                    } else if (type.equals("0-2")) {//普通座位 已售
                        return SEAT_TYPE_ORDINARY_2;
                    } else if (type.equals("0-3")) {//普通座位 维修
                        return SEAT_TYPE_ORDINARY_3;
                    } else if (type.equals("1")) {//情侣座首座
                        return SEAT_TYPE_LOVERS_LEFT;
                    } else if (type.equals("1-1")) {//情侣座首座 已选
                        return SEAT_TYPE_LOVERS_LEFT_1;
                    } else if (type.equals("1-2")) {//情侣座首座 已售
                        return SEAT_TYPE_LOVERS_LEFT_2;
                    } else if (type.equals("1-3")) {//情侣座首座 维修
                        return SEAT_TYPE_LOVERS_LEFT_3;
                    } else if (type.equals("2")) {//情侣座次座
                        return SEAT_TYPE_LOVERS_RIGHT;
                    } else if (type.equals("2-1")) {//情侣座次座 已选
                        return SEAT_TYPE_LOVERS_RIGHT_1;
                    } else if (type.equals("2-2")) {//情侣座次座 已售
                        return SEAT_TYPE_LOVERS_RIGHT_2;
                    } else if (type.equals("2-3")) {//情侣座次座 维修
                        return SEAT_TYPE_LOVERS_RIGHT_3;
                    } else if (type.equals("3")) {//特殊人群
                        return SEAT_TYPE_DISABLED;
                    } else if (type.equals("3-1")) {//特殊人群 已选
                        return SEAT_TYPE_DISABLED_1;
                    } else if (type.equals("3-2")) {//特殊人群 已售
                        return SEAT_TYPE_DISABLED_2;
                    } else if (type.equals("3-3")) {//特殊人群 维修
                        return SEAT_TYPE_DISABLED_3;
                    } else if (type.equals("4")) {//VIP专座
                        return SEAT_TYPE_VIP;
                    } else if (type.equals("4-1")) {//VIP专座 已选
                        return SEAT_TYPE_VIP_1;
                    } else if (type.equals("4-2")) {//VIP专座 已售
                        return SEAT_TYPE_VIP_2;
                    } else if (type.equals("4-3")) {//VIP专座 维修
                        return SEAT_TYPE_VIP_3;
                    } else if (type.equals("-1")) {//过道
                        return SEAT_TYPE_ROAD;
                    }

                }
            }
        }
        return SEAT_TYPE_ROAD; //空白 过道  没有座位
    }

    private int getID(int row, int column) {
        return row * this.column + (column);
    }

    /**
     * 绘制选中座位的行号列号
     *
     * @param row
     * @param column
     */
    private void drawText(Canvas canvas, int row, int column, float top, float left) {

        String txt = (row + 1) + "排";
        String txt1 = (column + 1) + "座";

        if (seatChecker != null) {
            String[] strings = seatChecker.checkedSeatTxt(row, column);
            if (strings != null && strings.length > 0) {
                if (strings.length >= 2) {
                    txt = strings[0];
                    txt1 = strings[1];
                } else {
                    txt = strings[0];
                    txt1 = null;
                }
            }
        }

        TextPaint txtPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        txtPaint.setColor(txt_color);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float seatHeight = this.seatHeight * getMatrixScaleX();
        float seatWidth = this.seatWidth * getMatrixScaleX();
        txtPaint.setTextSize(seatHeight / 3);

        //获取中间线
        float center = seatHeight / 2;
        float txtWidth = txtPaint.measureText(txt);
        float startX = left + seatWidth / 2 - txtWidth / 2;

        //只绘制一行文字
        if (txt1 == null) {
            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + seatHeight), txtPaint);
        } else {
            canvas.drawText(txt, startX, getBaseLine(txtPaint, top, top + center), txtPaint);
            canvas.drawText(txt1, startX, getBaseLine(txtPaint, top + center, top + center + seatHeight / 2), txtPaint);
        }

        if (DBG) {
            Log.d("drawTest:", "top:" + top);
        }
    }

    /**
     * 绘制行号
     */
    void drawNumber(Canvas canvas) {
        long startTime = System.currentTimeMillis();
        lineNumberPaint.setColor(bacColor);
        int translateY = (int) getTranslateY();
        float scaleY = getMatrixScaleY();

        rectF.top = translateY - lineNumberTxtHeight / 2;
        rectF.bottom = translateY + ((seatBitmapHeight) * scaleY) + lineNumberTxtHeight / 2;
        rectF.left = 0 + 10;
        rectF.right = numberWidth - 10;
        canvas.drawRoundRect(rectF, numberWidth / 2, numberWidth / 2, lineNumberPaint);

        lineNumberPaint.setColor(Color.WHITE);

        for (int i = 0; i < lineNumbers.size(); i++) {

            float top = (i * seatHeight) * scaleY + translateY;
            float bottom = (i * seatHeight + seatHeight) * scaleY + translateY;
            float baseline = (bottom + top - lineNumberPaintFontMetrics.bottom - lineNumberPaintFontMetrics.top) / 2;

            canvas.drawText(lineNumbers.get(i), numberWidth / 2, baseline, lineNumberPaint);
        }

        if (DBG) {
            long drawTime = System.currentTimeMillis() - startTime;
            Log.d("drawTime", "drawNumberTime:" + drawTime);
        }
    }

    /**
     * 绘制概览图
     */
    void drawOverview(Canvas canvas) {

//        rectW = (column + 1) * rectWidth + (column) * overviewSpacing + overviewSpacing * 2 + 20;
//        rectH = (row) * rectHeight + (row - 1) * overviewVerSpacing + overviewVerSpacing * 2 + 20;

        //绘制红色框
        int left = (int) -getTranslateX();
        if (left < 0) {
            left = 0;
        }
        left /= (overviewScale - 1.35);
        left /= getMatrixScaleX();

        int currentWidth = (int) (getTranslateX() + ((column) * seatWidth) * getMatrixScaleX());
        if (currentWidth > getWidth()) {
            currentWidth = currentWidth - getWidth();
        } else {
            currentWidth = 0;
        }
        int right = (int) (rectW - currentWidth / (overviewScale - 1.35) / getMatrixScaleX());

        float top = -getTranslateY();
        if (top < 0) {
            top = 0;
        }
        top /= (overviewScale - 1.35);
        top /= getMatrixScaleY();
        if (top > 0) {
            top += overviewVerSpacing;
        }

        int currentHeight = (int) (getTranslateY() + ((row) * seatHeight) * getMatrixScaleY());
        if (currentHeight > getHeight()) {
            currentHeight = currentHeight - getHeight();
        } else {
            currentHeight = 0;
        }
        int bottom = (int) (rectH - currentHeight / (overviewScale - 1.35) / getMatrixScaleY());

        canvas.drawRect(left, top, right, bottom, redBorderPaint);
    }

    Bitmap drawOverview() {
        isDrawOverviewBitmap = false;

        int bac = Color.parseColor("#7e000000");
        overviewPaint.setColor(bac);
        overviewPaint.setAntiAlias(true);
        overviewPaint.setStyle(Paint.Style.FILL);
        overviewBitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(overviewBitmap);

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(40, 0);
        path.lineTo(60, 10);
        path.lineTo(rectW - 60, 10);
        path.lineTo(rectW - 40, 0);
        path.lineTo(rectW, 0);
        path.lineTo(rectW, rectH);
        path.lineTo(0, rectH);

        canvas.drawPath(path, overviewPaint);

        //绘制透明灰色背景
//        canvas.drawRect(0, 0, rectW, rectH, overviewPaint);

        overviewPaint.setColor(Color.WHITE);
        for (int i = 0; i < row; i++) {
            float top = i * rectHeight + i * overviewVerSpacing + overviewVerSpacing + 10;
            for (int j = 0; j < column; j++) {

                int seatType = getSeatType(i, j);
                switch (seatType) {
                    case SEAT_TYPE_ORDINARY:
                    case SEAT_TYPE_LOVERS_LEFT:
                    case SEAT_TYPE_LOVERS_RIGHT:
                    case SEAT_TYPE_DISABLED:
                    case SEAT_TYPE_VIP:
                        overviewPaint.setColor(Color.WHITE);
                        break;
                    case SEAT_TYPE_ROAD:
                        continue;
                    case SEAT_TYPE_ORDINARY_1:
                    case SEAT_TYPE_LOVERS_LEFT_1:
                    case SEAT_TYPE_LOVERS_RIGHT_1:
                    case SEAT_TYPE_DISABLED_1:
                    case SEAT_TYPE_VIP_1:
                        overviewPaint.setColor(overview_checked);
                        break;
                    case SEAT_TYPE_ORDINARY_2:
                    case SEAT_TYPE_LOVERS_LEFT_2:
                    case SEAT_TYPE_LOVERS_RIGHT_2:
                    case SEAT_TYPE_DISABLED_2:
                    case SEAT_TYPE_VIP_2:
                        overviewPaint.setColor(overview_sold);
                        break;
                }

                float left;

                left = j * rectWidth + j * overviewSpacing + overviewSpacing;
                canvas.drawRect(left, top, left + rectWidth, top + rectHeight, overviewPaint);
            }
        }

        return overviewBitmap;
    }

    /**
     * 自动回弹
     * 整个大小不超过控件大小的时候:
     * 往左边滑动,自动回弹到行号右边
     * 往右边滑动,自动回弹到右边
     * 往上,下滑动,自动回弹到顶部
     * <p/>
     * 整个大小超过控件大小的时候:
     * 往左侧滑动,回弹到最右边,往右侧滑回弹到最左边
     * 往上滑动,回弹到底部,往下滑动回弹到顶部
     */
    private void autoScroll() {
        float currentSeatBitmapWidth = (seatBitmapWidth) * getMatrixScaleX();
        float currentSeatBitmapHeight = (seatBitmapHeight/* + screenAndSeatHeight*/) * getMatrixScaleY();
        float moveYLength = 0;
        float moveXLength = 0;

        // 当前所有座位宽度小于控件的宽度
        if (currentSeatBitmapWidth < getWidth()) {
            if (getTranslateX() < 0 || getMatrixScaleX() < spacing) {
                moveXLength = screenX - seatWidth * getMatrixScaleX() - getTranslateX();
            }
        } else {
            if (getTranslateX() >= 0 || getTranslateX() + currentSeatBitmapWidth <= getWidth()) {
                // 往左侧滑动
                if (getTranslateX() + currentSeatBitmapWidth < getWidth()) {
                    moveXLength = getWidth() - (getTranslateX() + currentSeatBitmapWidth);
                    Log.i(TAG, "autoScroll: 向左侧滑动， moveXLength = " + moveXLength);
                } else {    //右侧滑动
                    moveXLength = -getTranslateX();
                    Log.i(TAG, "autoScroll: 向右侧滑动， moveXLength = " + moveXLength);
                }
            }
        }
        float startYPosition = screenHeight * getMatrixScaleY()/* + verSpacing * getMatrixScaleY()*/;
        // 当前所有座位的高度小于控件的高度
        if (currentSeatBitmapHeight < getHeight()) {
            moveYLength = startYPosition - getTranslateY();
        } else {
            if (getTranslateY() >= 0 || getTranslateY() + currentSeatBitmapHeight <= getHeight()) {
                //往上滑动
                if (getTranslateY() + currentSeatBitmapHeight < getHeight()) {
                    moveYLength = getHeight() - (getTranslateY() + currentSeatBitmapHeight);
                    Log.i(TAG, "autoScroll: 向上滑动， moveYLength = " + moveYLength);
                } else {
                    moveYLength = -(getTranslateY() - (startYPosition));
                    Log.i(TAG, "autoScroll: 向下滑动， moveYLength = " + moveYLength);
                }
            }
        }

        Point start = new Point();
        start.x = (int) getTranslateX();
        start.y = (int) getTranslateY();

        Point end = new Point();
        end.x = (int) (start.x + moveXLength /*+ spacing * getMatrixScaleX() + seatWidth * getMatrixScaleX()*/);
        end.y = (int) (start.y + moveYLength);
        moveAnimate(start, end);
    }

    private void autoScale() {

       /* float minScale = (float) getMeasuredHeight() / (float) (seatBitmapWidth) *0.9f;
        if (getMatrixScaleX() > 2.2f) {
            zoomAnimate(getMatrixScaleX(), 2.0f);
        } else if (getMatrixScaleX() < minScale) {
            zoomAnimate(getMatrixScaleX(), minScale);
        }*/

        if (getMatrixScaleX() > 2.2) {
            zoomAnimate(getMatrixScaleX(), 2.0f);
        } else if (getMatrixScaleX() < 0.98) {
            zoomAnimate(getMatrixScaleX(), 1.0f);
        }
    }

    public ArrayList<String> getSelectedSeat() {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                if (isHave(getID(i, j)) >= 0) {
                    results.add(i + "," + j);
                }
            }
        }
        return results;
    }

    private int isHave(Integer seat) {
        return Collections.binarySearch(selects, seat);
    }

    private void remove(int index) {
        selects.remove(index);
    }

    // 表示x轴上平移的值
    private float getTranslateX() {
        matrix.getValues(m);
        return m[2];
    }

    //表示的是y轴上的平移的值
    private float getTranslateY() {
        matrix.getValues(m);
        return m[5];
    }

    //表示的y轴上的缩放比例
    private float getMatrixScaleY() {
        matrix.getValues(m);
        return m[4];
    }

    //表示的是缩放的x值
    private float getMatrixScaleX() {
        matrix.getValues(m);
        return m[Matrix.MSCALE_X];
    }

    private float dip2Px(float value) {
        return getResources().getDisplayMetrics().density * value;
    }

    private float getBaseLine(Paint p, float top, float bottom) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int baseline = (int) ((bottom + top - fontMetrics.bottom - fontMetrics.top) / 2);
        return baseline;
    }

    private void moveAnimate(Point start, Point end) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MoveEvaluator(), start, end);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        MoveAnimation moveAnimation = new MoveAnimation();
        valueAnimator.addUpdateListener(moveAnimation);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private void zoomAnimate(float cur, float tar) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(cur, tar);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        ZoomAnimation zoomAnim = new ZoomAnimation();
        valueAnimator.addUpdateListener(zoomAnim);
        valueAnimator.addListener(zoomAnim);
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private void zoom(float zoom) {
        float z = zoom / getMatrixScaleX();
        matrix.postScale(z, z, scaleX, scaleY);
        invalidate();
    }

    private void move(Point p) {
        float x = p.x - getTranslateX();
        float y = p.y - getTranslateY();
        matrix.postTranslate(x, y);
        invalidate();
    }

    public void setData(List<SeatInfo> seatList, List<SeatTypeInfo> seatType) {

        this.seatList = seatList;
        this.seatType = seatType;
        newSeatList = new ArrayList<>();
        oldSeatList = new ArrayList<>();
        ArrayList<String> lineList = new ArrayList<>();
        this.row = 0;
        this.column = 0;
        for (int i = 0; i < seatList.size(); i++) {
            if (!lineList.toString().contains(seatList.get(i).getRow())) {
                lineList.add(seatList.get(i).getRow());
            }
            if (seatList.get(i).getgRow() > this.row) {
                this.row = seatList.get(i).getgRow();
            }
            if (seatList.get(i).getgCol() > this.column) {
                this.column = seatList.get(i).getgCol();
            }
        }

        Log.i("ee", "--------------" + lineList.toString());

        setLineNumbers(lineList);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {

                if (returnSeat(i, j) != null) {
                    newSeatList.add(returnSeat(i, j));
                    oldSeatList.add(returnSeat(i, j));
                } else {
                    SeatInfo seat = new SeatInfo();
                    seat.setId("");
                    seat.setCol("");
                    seat.setRow("");
                    seat.setFlag("1");
                    seat.setgCol(j + 1);
                    seat.setgRow(i + 1);
                    seat.setType("-1");
                    newSeatList.add(seat);
                    oldSeatList.add(seat);
                }
            }
        }
        initSeatBitmap();
        init();
        invalidate();
    }

    private SeatInfo returnSeat(int i, int j) {
        for (int k = 0; k < seatList.size(); k++) {
            if ((seatList.get(k).getgRow() == i + 1
                    && seatList.get(k).getgCol() == j + 1)) {
                return seatList.get(k);
            }
        }
        return null;
    }

    private void addChooseSeat(int row, int column) {
        int id = getID(row, column);
        for (int i = 0; i < selects.size(); i++) {
            int item = selects.get(i);
            if (id < item) {
                selects.add(i, id);
                Log.i("ee", "selects--2:-----------" + selects.toString());
                return;
            }
        }

        selects.add(id);
        Log.i("ee", "selects--1:-----------" + selects.toString());
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setMaxSelected(int maxSelected) {
        this.maxSelected = maxSelected;
    }

    public void setSeatChecker(SeatChecker seatChecker) {
        this.seatChecker = seatChecker;
        invalidate();
    }

    private int getRowNumber(int row) {
        int result = row;
        if (seatChecker == null) {
            return -1;
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (seatChecker.isValidSeat(i, j, newSeatList.get(i * this.column + j).getFlag())) {
                    break;
                }

                if (j == column - 1) {
                    if (i == row) {
                        return -1;
                    }
                    result--;
                }
            }
        }
        return result;
    }

    private int getColumnNumber(int row, int column) {
        int result = column;
        if (seatChecker == null) {
            return -1;
        }

        for (int i = row; i <= row; i++) {
            for (int j = 0; j < column; j++) {

                if (!seatChecker.isValidSeat(i, j, newSeatList.get(i * this.column + j).getFlag())) {
                    if (j == column) {
                        return -1;
                    }
                    result--;
                }
            }
        }
        return result;
    }

    public interface SeatChecker {
        /**
         * 是否可用座位
         *
         * @param row
         * @param column
         * @param flag   0可选 1不可选
         * @return
         */
        boolean isValidSeat(int row, int column, String flag);

        /**
         * 是否已售
         *
         * @param row
         * @param column
         * @return
         */
        boolean isSold(int row, int column, String type);

        void checked(SeatInfo seatInfo);

        void unCheck(SeatInfo seatInfo);

        /**
         * 获取选中后座位上显示的文字
         *
         * @param row
         * @param column
         * @return 返回2个元素的数组, 第一个元素是第一行的文字, 第二个元素是第二行文字, 如果只返回一个元素则会绘制到座位图的中间位置
         */
        String[] checkedSeatTxt(int row, int column);

    }

    class MoveAnimation implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            Point p = (Point) animation.getAnimatedValue();

            move(p);
        }
    }

    class MoveEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));
            int y = (int) (startPoint.y + fraction * (endPoint.y - startPoint.y));
            return new Point(x, y);
        }
    }

    class ZoomAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            zoom = (Float) animation.getAnimatedValue();
            zoom(zoom);

            if (DBG) {
                Log.d("zoomTest", "zoom:" + zoom);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

    }

    //选择座位
    public void selectSeat(SeatInfo seatInfo) {
        int row = seatInfo.getgRow() - 1;
        int column = seatInfo.getgCol() - 1;
        int id = getID(row, column);
        String type = newSeatList.get(id).getType();
        if (type.equals("0")) {
            newSeatList.get(id).setType("0-1");
        } else if (type.equals("1")) {
            newSeatList.get(id).setType("1-1");
        } else if (type.equals("2")) {
            newSeatList.get(id).setType("2-1");
        } else if (type.equals("3")) {
            newSeatList.get(id).setType("3-1");
        } else if (type.equals("4")) {
            newSeatList.get(id).setType("4-1");
        }

        invalidate();
    }

    //取消座位
    public void cancelSeat(SeatInfo seatInfo) {
        int row = seatInfo.getgRow() - 1;
        int column = seatInfo.getgCol() - 1;
        int id = getID(row, column);
        String type = newSeatList.get(id).getType();
        if (type.equals("0-1")) {
            newSeatList.get(id).setType("0");
        } else if (type.equals("1-1")) {
            newSeatList.get(id).setType("1");
        } else if (type.equals("2-1")) {
            newSeatList.get(id).setType("2");
        } else if (type.equals("3-1")) {
            newSeatList.get(id).setType("3");
        } else if (type.equals("4-1")) {
            newSeatList.get(id).setType("4");
        }
        int index = isHave(id);
        if (index >= 0) {
            remove(index);
        }
        invalidate();
    }

    //专座推荐 0 普通 1 情侣首座 2 情侣次座 3 特殊人群 4 vip专座
    public boolean selectSeatRecommend(int num) {
        boolean isSelect = false;
        List<List<Map<String, Integer>>> seatRecommendListTwo = new ArrayList<>();
        //最小的绝对值
        int minNum = 100;
        int bestNum = 0;
        int centerRow = lineNumbers.size() * 2 / 3;//最佳行数 2/3 得位置
        Log.i("ee", "-最佳-------------------" + centerRow + "----------------" + seatRecommendListTwo.size());
        int centerColumn = column / 2;
        for (int i = centerRow; i < row; i++) {
            List<List<Map<String, Integer>>> seatRecommendListOne = new ArrayList<>();

            //筛选出所有的可能性 包括 不可选的位置
            for (int j = 0; j < column; j++) {

                int n = 0;
                List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
                for (int k = j; k < column && n < num; k++, n++) {
                    Map<String, Integer> map = new HashMap<String, Integer>();
                    map.put("row", i);
                    map.put("col", k);
                    int id = getID(i, k);
                    Log.i("ee", "------newSeatList.get(id).getType()---------" + newSeatList.get(id).getType().toString());

                    if (newSeatList.get(id).getType().equals("0")) {
                        list.add(map);
                    } else if (!newSeatList.get(id).getType().equals("0")) {
                        break;
                    }
                }
                if (list.size() == num) {
                    seatRecommendListOne.add(list);
                }
            }
            Log.i("ee", "------seatRecommendListOne---------" + seatRecommendListOne.toString());
            //最小的绝对值
            minNum = 100;
            bestNum = 0;
            //取完每一行的可能性之后 选出最佳位置
            for (int j = 0; j < seatRecommendListOne.size(); j++) {

                int min = 0;
                for (int k = 0; k < seatRecommendListOne.get(j).size(); k++) {
                    min += Math.abs(centerColumn - seatRecommendListOne.get(j).get(k).get("col"));
                }

                if (min < minNum) {
                    minNum = min;
                    bestNum = j;
                }
            }
            Log.i("ee", "------seatRecommendListTwo---------" + seatRecommendListTwo.toString());

            if (seatRecommendListOne.size() > 0) {
                //存放每一行的最佳位置
                seatRecommendListTwo.add(seatRecommendListOne.get(bestNum));
            }


        }

        if (seatRecommendListTwo.size() <= 0) {
            for (int i = 0; i < centerRow; i++) {//上半部分
                List<List<Map<String, Integer>>> seatRecommendListOne = new ArrayList<List<Map<String, Integer>>>();

                //筛选出所有的可能性 包括 不可选的位置
                for (int j = 0; j < column; j++) {

                    int n = 0;
                    List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
                    for (int k = j; k < column && n < num; k++, n++) {
                        Map<String, Integer> map = new HashMap<String, Integer>();
                        map.put("row", i);
                        map.put("col", k);
                        int id = getID(i, k);
                        Log.i("ee", "------newSeatList.get(id).getType()---------" + newSeatList.get(id).getType().toString());

                        if (newSeatList.get(id).getType().equals("0")) {
                            list.add(map);
                        } else if (!newSeatList.get(id).getType().equals("0")) {
                            break;
                        }
                    }
                    if (list.size() == num) {
                        seatRecommendListOne.add(list);
                    }

                }

                Log.i("ee", "------seatRecommendListOne---------" + seatRecommendListOne.toString());

                //最小的绝对值
                minNum = 100;
                bestNum = 0;
                //取完每一行的可能性之后 选出最佳位置
                for (int j = 0; j < seatRecommendListOne.size(); j++) {

                    int min = 0;
                    for (int k = 0; k < seatRecommendListOne.get(j).size(); k++) {
                        min += Math.abs(centerColumn - seatRecommendListOne.get(j).get(k).get("col"));
                    }

                    if (min < minNum) {
                        minNum = min;
                        bestNum = j;
                    }
                }

                if (seatRecommendListOne.size() > 0) {
                    Log.i("ee", "------seatRecommendListTwo---------" + seatRecommendListTwo.toString());
                    //存放每一行的最佳位置
                    seatRecommendListTwo.add(seatRecommendListOne.get(bestNum));
                }
            }
        }

        //最小的绝对值
        minNum = 100;
        bestNum = 0;
        //取完每一行的可能性之后 选出最佳位置
        for (int j = 0; j < seatRecommendListTwo.size(); j++) {

            int min = 0;
            for (int k = 0; k < seatRecommendListTwo.get(j).size(); k++) {
                min += Math.abs(centerColumn - seatRecommendListTwo.get(j).get(k).get("col"));
            }

            if (min < minNum) {
                minNum = min;
                bestNum = j;
            }
        }
        if (seatRecommendListTwo.size() > 0) {
            for (int i = 0; i < seatRecommendListTwo.get(bestNum).size(); i++) {
                int a = seatRecommendListTwo.get(bestNum).get(i).get("row");
                int b = seatRecommendListTwo.get(bestNum).get(i).get("col");
                int id = getID(a, b);
                addChooseSeat(a, b);
                seatChecker.checked(newSeatList.get(id));
                newSeatList.get(id).setType("0-1");
            }
            isSelect = true;
        }
        invalidate();
        return isSelect;
    }


    //判断是否符合选择规则
    public boolean isSelect(int row, int col) {

        boolean leftOne = isSelectSeat(row, col - 1, 0);
        boolean leftTwo = isSelectSeat(row, col - 2, 0);
        boolean rightOne = isSelectSeat(row, col + 1, 2);
        boolean rightTwo = isSelectSeat(row, col + 2, 2);

        Log.i("ee",leftOne+"--"+leftTwo+"--"+rightOne+"--"+rightTwo);

        if (leftOne && leftTwo && rightOne && !rightTwo) {
            return false;
        }

        if (leftOne && !leftTwo && rightOne && !rightTwo) {
            return false;
        }

        if (leftOne && !leftTwo && rightOne && rightTwo) {
            Log.i("ee", "------------------------");
            return false;
        }

//        //向左查看均存在座位 本身 1 2  向右查看均存在座位 本身 1 2
//        if (isSelectSeat(row, col - 1, 0) && isSelectSeat(row, col - 2, 0)
//                && isSelectSeat(row, col + 1, 2) && isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }
//        //向左查看均存在座位 本身 1 2不可选  向右查看均存在座位 本身 1 2
//        if (isSelectSeat(row, col - 1, 0) && !isSelectSeat(row, col - 2, 0)
//                && isSelectSeat(row, col + 1, 2) && isSelectSeat(row, col + 2, 2)) {
//            return false;
//        }
//
//        //向左查看均存在座位 本身 1 2  向右查看均存在座位 本身 1 2不可选
//        if (isSelectSeat(row, col - 1, 0) && isSelectSeat(row, col - 2, 0)
//                && isSelectSeat(row, col + 1, 2) && !isSelectSeat(row, col + 2, 2)) {
//            return false;
//        }
//
//        //左 2 不可选 1可选 右 1不可选 2可选
//        if (isSelectSeat(row, col - 1, 0) && !isSelectSeat(row, col - 2, 0)
//                && !isSelectSeat(row, col + 1, 2) && isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }
//
//
//        //向左查看 不 均存在座位 本身 1不可选 2不可选  向右查看均存在座位 本身 1 2
//        if (!isSelectSeat(row, col - 1, 0) && !isSelectSeat(row, col - 2, 0)
//                && isSelectSeat(row, col + 1, 2) && isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }
//
//        //向左查看均存在座位 本身 1 2  向右查看 不 均存在座位 本身 1不可选 2不可选
//        if (isSelectSeat(row, col - 1, 0) && isSelectSeat(row, col - 2, 0)
//                && !isSelectSeat(row, col + 1, 2) && !isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }
//
//        //向左查看均存在座位 本身 1不可选 2  向右查看均存在座位 本身 1 2
//        if (!isSelectSeat(row, col - 1, 0) && isSelectSeat(row, col - 2, 0)
//                && isSelectSeat(row, col + 1, 2) && isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }
//
//        //向左查看均存在座位 本身 1 2  向右查看均存在座位 本身 1不可选  2
//        if (isSelectSeat(row, col - 1, 0) && isSelectSeat(row, col - 2, 0)
//                && !isSelectSeat(row, col + 1, 2) && isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }
//
//        //向左查看均存在座位 本身 1 2  向右查看均存在座位 本身 1不可选 2不可选
//        if (isSelectSeat(row, col - 1, 0) && isSelectSeat(row, col - 2, 0)
//                && !isSelectSeat(row, col + 1, 2) && !isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }
//
//        //向左查看均存在座位 本身 1 2不可选 向右查看均存在座位 本身 1 2不可选
//        if (isSelectSeat(row, col - 1, 0) && !isSelectSeat(row, col - 2, 0)
//                && isSelectSeat(row, col + 1, 2) && !isSelectSeat(row, col + 2, 2)) {
//            return true;
//        }


        return true;
    }

    private boolean addIsHave(int col) {
        Log.i("ee", column + "--------------" + col);
        if (col >= column) {
            return false;
        }
        return true;
    }

    private boolean lostIsHave(int col) {
        if (col < 0) {
            return false;
        }
        return true;
    }

    private boolean isSelectSeat(int row, int col, int flag) {
        boolean isTrue = false;
        if (flag == 0) {//真的话  向左
            isTrue = lostIsHave(col);
        } else if (flag == 2) {//假的话 向右
            isTrue = addIsHave(col);
        } else {
            isTrue = false;
        }

        if (isTrue) {//判断那个位置是否可选
            int id = getID(row, col);
            Log.i("ee", "newSeatList.get(id).getType()---" + newSeatList.get(id).getType() +
                    "-----------" + id + "-------type:------" + oldSeatList.get(id).getType() + "----flag---" + flag + "---col---" + col);
            if (( oldSeatList.get(id).getType().equals("0"))
                    ||(oldSeatList.get(id).getType().equals("1"))
                    ||(oldSeatList.get(id).getType().equals("2"))
                    ||(oldSeatList.get(id).getType().equals("3"))
                    ||(oldSeatList.get(id).getType().equals("4"))) {
                isTrue = true;
            } else {
                isTrue = false;
            }
            /*else if (newSeatList.get(id).getType().equals("0-1")) {
                isTrue = true;
            }*/
        }
        return isTrue;
    }

    public void clear(){
        selects.clear();
        seatList.clear();
        oldSeatList.clear();
        seatType.clear();
        requestLayout();
    }

}
