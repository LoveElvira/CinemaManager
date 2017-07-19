package com.yyjlr.tickets.activity.film;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.activity.OldPaySelectActivity;
import com.yyjlr.tickets.adapter.SeatTypeAdapter;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.ResponseId;
import com.yyjlr.tickets.model.order.AddMovieOrderBean;
import com.yyjlr.tickets.model.order.ConfirmOrderBean;
import com.yyjlr.tickets.model.seat.SeatBean;
import com.yyjlr.tickets.model.seat.SeatInfo;
import com.yyjlr.tickets.model.seat.SeatTypeInfo;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.LockSeatRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.SeatTableView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/8/10.
 * 选择座位
 */
public class FilmSelectSeatActivity extends AbstractActivity implements View.OnClickListener {

    private TextView confirmSeat;
    private TextView title;
    private ImageView leftArrow;
    private TextView seatTitle;
    private TextView seatTime;
    private TextView seatDate;
    private TextView seatHall;
    private TextView seatHallType;
    private TextView language;
    private TextView filmType;
    private SeatTableView seatTableView;
    private LinearLayout addSelectSeatLayout;
    private List<SeatInfo> oldSeatList = new ArrayList<>();
    private List<SeatInfo> seatSelectList = new ArrayList<>();
    private List<SeatInfo> seatSelectList_1 = null;
    private List<SeatTypeInfo> seatTypeList = new ArrayList<>();

    private RecyclerView seatTypeListView;
    private SeatTypeAdapter seatTypeAdapter;
    protected LinearLayout showSeatRecommendLayout;//显示推荐座位 1人座 2人座 3人座 4人座
    private TextView seatOne, seatTwo, seatThree, seatFour;
    private SeatBean seatBean;
    private TextView totalPrice;
    private TextView seatPrice;
    private String planId = "";
    public static Activity activity;
    private boolean isFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_select_seat);
        activity = this;
        planId = getIntent().getStringExtra("planId");
        isFirst = getIntent().getBooleanExtra("isFirst", false);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        title.setText("选座");
        //为了加载出选座内容 图片
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        seatTypeListView = (RecyclerView) findViewById(R.id.content_film_seat__type_listview);
        seatTableView = (SeatTableView) findViewById(R.id.content_film_seat__show_seat);
        addSelectSeatLayout = (LinearLayout) findViewById(R.id.content_film_seat__show_select_seat_layout);
        confirmSeat = (TextView) findViewById(R.id.content_film_seat__confirm_seat);
        seatTitle = (TextView) findViewById(R.id.content_film_seat__seat_title);
        seatDate = (TextView) findViewById(R.id.content_film_seat__date);
        seatTime = (TextView) findViewById(R.id.content_film_seat__time);
        seatHall = (TextView) findViewById(R.id.content_film_seat__hall);
        seatHallType = (TextView) findViewById(R.id.content_film_seat__hall_type);
        language = (TextView) findViewById(R.id.content_film_seat__film_language);
        filmType = (TextView) findViewById(R.id.content_film_seat__film_type);
        showSeatRecommendLayout = (LinearLayout) findViewById(R.id.content_film_seat__seat_recommend_layout);
        seatOne = (TextView) findViewById(R.id.content_film_seat__seat_recommend_one);
        seatTwo = (TextView) findViewById(R.id.content_film_seat__seat_recommend_two);
        seatThree = (TextView) findViewById(R.id.content_film_seat__seat_recommend_three);
        seatFour = (TextView) findViewById(R.id.content_film_seat__seat_recommend_four);
        totalPrice = (TextView) findViewById(R.id.content_film_seat__price);
        seatPrice = (TextView) findViewById(R.id.content_film_seat__price_);

        seatTitle.setText("快速选座");

        AssetManager assetManager = getBaseContext().getAssets();
        Typeface font = Typeface.createFromAsset(assetManager, "fonts/Digital2.ttf");
        seatTime.setTypeface(font);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        seatTypeListView.setLayoutManager(linearLayoutManager);

        seatTableView.setMaxSelected(4);//设置最多选中
        seatTableView.setSeatChecker(seatChecker);
        confirmSeat.setOnClickListener(this);
        if (isFirst) {
            CheckNoPayOrder();
        } else {
            customDialog = new CustomDialog(this, "加载中...");
            customDialog.show();
            getSeatPlan();
        }
    }

    //获取影片（抢票）座位信息接口
    private void getSeatPlan() {
        IdRequest idRequest = new IdRequest();
        idRequest.setPlanId(planId);
        OkHttpClientManager.postAsyn(Config.GET_FILM_SEAT, new OkHttpClientManager.ResultCallback<SeatBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(SeatBean response) {
                seatBean = response;
//                title.setText(seatBean.getMovieName());
                seatDate.setText(ChangeUtils.changeTimeDate(seatBean.getPlayStartTime()));
                seatTime.setText(ChangeUtils.changeTimeTime(seatBean.getPlayStartTime()));
                seatHall.setText(seatBean.getHallName());
                if (seatBean.getHallType() != null) {
                    seatHallType.setVisibility(View.VISIBLE);
                    seatHallType.setText(seatBean.getHallType());
                } else {
                    seatHallType.setVisibility(View.GONE);
                }
                language.setText(seatBean.getLanguage());
                filmType.setText(seatBean.getMovieType());
                seatTableView.setScreenName(seatBean.getHallName());//设置屏幕名称
                if (seatBean.getSeatList() != null && seatBean.getSeatList().size() > 0) {
                    oldSeatList = seatTableView.setData(seatBean.getSeatList(), seatBean.getSeatType());
                    seatOne.setOnClickListener(FilmSelectSeatActivity.this);
                    seatTwo.setOnClickListener(FilmSelectSeatActivity.this);
                    seatThree.setOnClickListener(FilmSelectSeatActivity.this);
                    seatFour.setOnClickListener(FilmSelectSeatActivity.this);
                }
                if (seatBean.getSeatType() != null && seatBean.getSeatType().size() > 0) {
                    for (int i = 0; i < seatBean.getSeatType().size(); i++) {
                        if (seatBean.getSeatType().get(i).getIsShow().equals("1")) {
                            seatTypeList.add(seatBean.getSeatType().get(i));
                        }
                    }
                    seatTypeAdapter = new SeatTypeAdapter(seatTypeList);
                    seatTypeListView.setAdapter(seatTypeAdapter);
                }

                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, SeatBean.class, FilmSelectSeatActivity.this);
    }


    //影片（抢票）锁座下单接口
    private void lockSeat(List<String> seatIdList) {
        customDialog = new CustomDialog(this, "下单中...");
        customDialog.show();
        LockSeatRequest lockSeatRequest = new LockSeatRequest();
        lockSeatRequest.setPlanId(planId);
        lockSeatRequest.setSeatIds(seatIdList);
        OkHttpClientManager.postAsyn(Config.LOCK_FILM_SEAT, new OkHttpClientManager.ResultCallback<AddMovieOrderBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(AddMovieOrderBean response) {
                customDialog.dismiss();
                AddMovieOrderBean orderBean = response;
                if (orderBean != null) {
                    Intent intent = new Intent(FilmSelectSeatActivity.this, FilmCompleteActivity.class);
                    intent.putExtra("movieOrderBean", orderBean);
                    intent.putExtra("planId", planId);
                    intent.putExtra("isNoPay", false);
                    FilmSelectSeatActivity.this.startActivityForResult(intent, CODE_REQUEST_ONE);
//                    FilmSelectSeatActivity.this.finish();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, lockSeatRequest, AddMovieOrderBean.class, FilmSelectSeatActivity.this);
    }

    //检查同场次未支付订单
    private void CheckNoPayOrder() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
//        idRequest.setPlanId(getIntent().getStringExtra("planId"));
        OkHttpClientManager.postAsyn(Config.CHECK_NO_PAY_ORDER, new OkHttpClientManager.ResultCallback<ResponseId>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponseId response) {

                Log.i("ee", response.getOrderId() + "--------------------------------");

                if (response != null && response.getOrderId() != 0) {
                    customDialog.dismiss();
                    showCancelOrder(response);
                } else {
                    getSeatPlan();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, ResponseId.class, FilmSelectSeatActivity.this);
    }

    //选座
    SeatTableView.SeatChecker seatChecker = new SeatTableView.SeatChecker() {
        @Override
        public boolean isValidSeat(int row, int column, String flag) {
            if (flag.equals("1")) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isSold(int row, int column, String type) {
            if (type.equals("0-2") ||
                    type.equals("1-2") ||
                    type.equals("2-2") ||
                    type.equals("3-2") ||
                    type.equals("4-2")) {
                return true;
            }
            return false;
        }

        @Override
        public void checked(SeatInfo seatInfo) {
            seatSelectList.add(seatInfo);
            addSelectSeatText();
            seatTableView.selectSeat(seatInfo);
            seatSelectList_1 = seatSelectList;
        }

        @Override
        public void unCheck(SeatInfo seatInfo) {
            for (int i = 0; i < seatSelectList.size(); i++) {
                if (seatSelectList.get(i).getRow().equals(seatInfo.getRow())
                        && seatSelectList.get(i).getCol().equals(seatInfo.getCol())) {
                    seatSelectList.remove(i);
                }
            }
            addSelectSeatText();
            seatTableView.cancelSeat(seatInfo);
            seatSelectList_1 = seatSelectList;
        }

        @Override
        public String[] checkedSeatTxt(int row, int column) {
            return null;
        }
    };

    //添加已经选定的座位
    private void addSelectSeatText() {
        totalPrice.setText("0");
        this.seatPrice.setText("");
        addSelectSeatLayout.removeAllViews();
        if (seatSelectList.size() <= 0) {
            seatTitle.setText("快速选座");
            showSeatRecommendLayout.setVisibility(View.VISIBLE);
        } else {
            seatTitle.setText("已选座位");
            showSeatRecommendLayout.setVisibility(View.GONE);
        }
        long seatPrice = 0;
        String seatPriceStr = "(";
        for (int i = 0; i < seatSelectList.size(); i++) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_seat_select_text, null);
            TextView seat = (TextView) view.findViewById(R.id.item_seat_select__text);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.item_seat_select__layout);
            final String row = seatSelectList.get(i).getRow();
            final String col = seatSelectList.get(i).getCol();
            seat.setText(row + "排" + col + "座");
            final int finalI = i;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(7, 0, 7, 0);
            layout.setLayoutParams(layoutParams);

            seatPrice += seatSelectList.get(i).getPrice();
            if (i == seatSelectList.size() - 1) {
                seatPriceStr = seatPriceStr + ChangeUtils.save2Decimal(seatSelectList.get(i).getPrice());
            } else {
                seatPriceStr = seatPriceStr + ChangeUtils.save2Decimal(seatSelectList.get(i).getPrice()) + "+";
            }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("ee", "---------------------------" + finalI + "-----" + seatSelectList_1.get(finalI).getType());
                    if (seatSelectList_1.get(finalI).getType().equals("1-1")) {//情侣首座
                        if ((finalI + 1) < seatSelectList_1.size() && seatSelectList_1.get(finalI + 1).getType().equals("2-1")) {
                            //正常的 首座的在前 次座的在后
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI));
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI + 1));
                            seatSelectList.remove(finalI);
                            seatSelectList.remove(finalI);
                        } else if ((finalI - 1) >= 0 && seatSelectList_1.get(finalI - 1).getType().equals("2-1")) {//非正常的 首座的在后 次座的在前
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI));
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI - 1));
                            seatSelectList.remove(finalI - 1);
                            seatSelectList.remove(finalI - 1);
                        }
                    } else if (seatSelectList_1.get(finalI).getType().equals("2-1")) {//情侣次座
                        if ((finalI - 1) >= 0 && seatSelectList_1.get(finalI - 1).getType().equals("1-1")) {
                            //正常的 首座的在前 次座的在后
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI));
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI - 1));
                            seatSelectList.remove(finalI - 1);
                            seatSelectList.remove(finalI - 1);
                        } else if ((finalI + 1) < seatSelectList_1.size() && seatSelectList_1.get(finalI + 1).getType().equals("1-1")) {//非正常的 首座的在后 次座的在前
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI));
                            seatTableView.cancelSeat(seatSelectList_1.get(finalI + 1));
                            seatSelectList.remove(finalI);
                            seatSelectList.remove(finalI);
                        }
                    } else {
                        seatTableView.cancelSeat(seatSelectList_1.get(finalI));
                        seatSelectList.remove(finalI);
                    }
                    addSelectSeatText();
                }
            });
            totalPrice.setText(ChangeUtils.save2Decimal(seatPrice));
            this.seatPrice.setText(seatPriceStr + ")");
            addSelectSeatLayout.addView(view);
        }
    }

    //取消订单
    private void cancelOrder(int orderId) {
        customDialog = new CustomDialog(this, "请稍后...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setOrderId(orderId + "");
        OkHttpClientManager.postAsyn(Config.CANCEL_ORDER, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
                customDialog.dismiss();
                getSeatPlan();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, FilmSelectSeatActivity.this);
    }

    /**
     * show Dialog 是否确定 取消订单
     */
    private void showCancelOrder(final ResponseId responseId) {
        final int orderId = responseId.getOrderId();
        LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        TextView cancel = (TextView) layout.findViewById(R.id.alert_dialog__cancel);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
        confirm.setText("确定");
        cancel.setText("查看");
        title.setText("提示");
        message.setText("有未支付订单，是否取消此订单?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                if (responseId.getOrderType() == 1) {
                    AddMovieOrderBean addMovieOrderBean = null;
                    startActivity(new Intent(getBaseContext(), FilmCompleteActivity.class)
                            .putExtra("orderId", orderId + "")
                            .putExtra("movieOrderBean", addMovieOrderBean)
                            .putExtra("isNoPay", true));
                } else {
                    ConfirmOrderBean confirmOrderBean = null;
                    startActivity(new Intent(getBaseContext(), OldPaySelectActivity.class)
                            .putExtra("orderId", orderId + "")
                            .putExtra("position", 0)
                            .putExtra("orderBean", confirmOrderBean));
                }
                FilmSelectSeatActivity.this.finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消订单
                builder.dismiss();
                cancelOrder(orderId);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                FilmSelectSeatActivity.this
                        .finish();
                break;
            case R.id.content_film_seat__seat_recommend_one://1人座
                seatRecommend(1);
                break;
            case R.id.content_film_seat__seat_recommend_two://2人座
                seatRecommend(2);
                break;
            case R.id.content_film_seat__seat_recommend_three://3人座
                seatRecommend(3);
                break;
            case R.id.content_film_seat__seat_recommend_four://4人座
                seatRecommend(4);
                break;
            case R.id.content_film_seat__confirm_seat://确定选座
                String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
                if (!isLogin.equals("1")) {
                    startActivity(new Intent(FilmSelectSeatActivity.this, LoginActivity.class)
                            .putExtra("pager", "FilmSelectSeatActivity"));
                } else {

                    if (seatSelectList.size() > 0) {
//                        List<Boolean> list = new ArrayList<Boolean>();
//                        for (int i = 0; i < seatSelectList.size(); i++) {
//                            int row = seatSelectList.get(i).getgRow() - 1;
//                            int column = seatSelectList.get(i).getgCol() - 1;
//                            list.add(seatTableView.isSelect(row, column));
//                        }
//                        Log.i("ee----", list.toString() + "-------------------");
//
//                        if (list.contains(false)) {
//                            showShortToast("旁边座位不要留空");
//                            return;
//                        } else {
                        sortList();
//                        }
                    } else {
                        showShortToast("请先选择座位");
                    }
                }
                break;
        }
    }

    //排序 方便判断是否空一个位置 或者是否为连续座位  符合条件 确认选座
    private void sortList() {
        List<SeatInfo> sortList = seatSelectList;
        for (int i = 0; i < sortList.size() - 1; i++) {
            for (int j = 0; j < sortList.size() - i - 1; j++) {
                if (sortList.get(j).getgCol() > sortList.get(j + 1).getgCol()
                        && sortList.get(j).getgRow() == sortList.get(j + 1).getgRow()) {// 这里是从大到小排序，如果是从小到大排序，只需将“<”换成“>”
                    SeatInfo seatInfo;
                    seatInfo = sortList.get(j);
                    sortList.set(j, sortList.get(j + 1));
                    sortList.set(j + 1, seatInfo);
                }
            }
        }
        if (seatTableView.isSelectSeat(sortList, true)) {//方便判断是否空一个位置 或者是否为连续座位  符合条件 确认选座
            confirmLockSeat(sortList);
        }
    }

    //确定锁座
    private void confirmLockSeat(List<SeatInfo> sortList) {
        List<String> seatIdList = new ArrayList<>();
        for (int i = 0; i < sortList.size(); i++) {
            seatIdList.add(sortList.get(i).getId());
        }
        lockSeat(seatIdList);
    }

    private void seatRecommend(int num) {
        boolean isSelect = seatTableView.selectSeatRecommend(num);
        if (!isSelect) {
            showShortToast("没有适合的连坐选择");
        } else {
            seatTitle.setText("已选座位");
            showSeatRecommendLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE:
                if (data.getBooleanExtra("isPay", false)) {
                    FilmSelectSeatActivity.this.finish();
                } else {
                    for (int i = 0; i < seatSelectList_1.size(); i++) {
                        seatTableView.cancelSeat(seatSelectList_1.get(i));
                    }
                    seatSelectList = new ArrayList<>();
                    seatSelectList_1 = new ArrayList<>();
                    addSelectSeatText();
                }
                break;
            case CODE_REQUEST_DIALOG:
                if (isFirst) {
                    CheckNoPayOrder();
                } else {
                    customDialog = new CustomDialog(this, "加载中...");
                    customDialog.show();
                    getSeatPlan();
                }
                break;
        }
    }
}
