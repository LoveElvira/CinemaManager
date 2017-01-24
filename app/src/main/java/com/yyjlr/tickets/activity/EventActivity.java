package com.yyjlr.tickets.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.setting.AccountNameActivity;
import com.yyjlr.tickets.activity.setting.SettingAccountActivity;
import com.yyjlr.tickets.adapter.EventCollectUserAdapter;
import com.yyjlr.tickets.content.MySettingContent;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.cinemainfo.CinemaInfoModel;
import com.yyjlr.tickets.model.event.EventModel;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

/**
 * Created by Elvira on 2016/8/3.
 * 活动 明星见面会
 */
public class EventActivity extends AbstractActivity implements View.OnClickListener/* implements View.OnClickListener*/ {

    private TextView title;
    private ImageView leftArrow;
    private ImageView rightImage;
    private TextView rightPhoto;

    private NestedScrollView nestedScrollView;
    private View mContentView;
    private CardView mCardView;

    private LinearLayout shareLayout;
    private LinearLayout collectLayout;
    private ImageView collectImage;
    private TextView collectText;
    private TextView join;
    private RecyclerView listView;//收藏人
    private TextView startTime;//开始时间
    private TextView address;//地址
    private TextView joinNum;//参加数
    private TextView price;//价格
    private TextView description;//描述
    private TextView collectNum;//收藏用户
    private EventModel eventModel;
    private EventCollectUserAdapter adapter;
    private ImageView imagebg;//背景图片 封面
    private String path = "";//背景图片地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initView();
        getEventInfo();
    }

    private void initView() {

//        AppManager.getInstance().initWidthHeight(this);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        rightImage = (ImageView) findViewById(R.id.base_toolbar__right);
        rightImage.setVisibility(View.GONE);
        rightPhoto = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightPhoto.setVisibility(View.VISIBLE);
        rightPhoto.setText("相册");
        rightPhoto.setOnClickListener(this);
//        initWidget();
//        title.setText("明星见面会");

        collectNum = (TextView) findViewById(R.id.content_event__collect_num);
        startTime = (TextView) findViewById(R.id.content_event__time);
        address = (TextView) findViewById(R.id.content_event__address);
        joinNum = (TextView) findViewById(R.id.content_event__join_number);
        price = (TextView) findViewById(R.id.content_event__price);
        description = (TextView) findViewById(R.id.content_event__description);
        imagebg = (ImageView) findViewById(R.id.content_event__bg_image);

        listView = (RecyclerView) findViewById(R.id.content_event__listview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 5);
        listView.setLayoutManager(gridLayoutManager);


        collectLayout = (LinearLayout) findViewById(R.id.content_event__collect);
        shareLayout = (LinearLayout) findViewById(R.id.content_event__share);
        collectImage = (ImageView) findViewById(R.id.content_event__collect_image);
        collectText = (TextView) findViewById(R.id.content_event__collect_text);
        join = (TextView) findViewById(R.id.content_event__join);
        collectLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        join.setOnClickListener(this);
    }


    //获取活动信息
    private void getEventInfo() {
        IdRequest idRequest = new IdRequest();
        idRequest.setActivityId(getIntent().getLongExtra("eventId", 0) + "");
        OkHttpClientManager.postAsyn(Config.GET_EVENT_INFO, new OkHttpClientManager.ResultCallback<EventModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(EventModel response) {
                Log.i("ee", new Gson().toJson(response));
                eventModel = response;
                title.setText(eventModel.getActivityName());
                startTime.setText(ChangeUtils.changeTimeDot(eventModel.getStartTime()));
                address.setText(eventModel.getAddress());
                joinNum.setText(eventModel.getInterestUsers() + "");
                collectNum.setText(eventModel.getInterestUsers() + "位用户收藏了这个信息");
                price.setText("¥" + eventModel.getPrice());
                description.setText(eventModel.getActivityDesc());

                collectImage.setImageResource(R.mipmap.collect);
                collectText.setText("收藏");
                if (response.getIsInterest() == 1) {
                    collectImage.setImageResource(R.mipmap.collect_select);
                    collectText.setText("已收藏");
                }

                if (eventModel.getInterestUserInfo() != null) {
                    adapter = new EventCollectUserAdapter(eventModel.getInterestUserInfo());
                    listView.setAdapter(adapter);
                }

                path = eventModel.getActivityImg();
                if (!"".equals(path)) {
                    Picasso.with(getBaseContext())
                            .load(path)
                            .into(imagebg);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, idRequest, EventModel.class, EventActivity.this);
    }


    private void initWidget() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.content_event__nested_scroll_view);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                        , RelativeLayout.LayoutParams.MATCH_PARENT);
        nestedScrollView.setLayoutParams(params);
        mContentView = findViewById(R.id.content_event__relayout);
        mContentView.setPadding(0, AppManager.getInstance().getHeight() / 3, 0, mContentView.getPaddingBottom() * 2);
        mCardView = (CardView) findViewById(R.id.content_event__cardview);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                        , AppManager.getInstance().getWidth() * 105 / 100);
        layoutParams.setMargins(AppManager.getInstance().getWidth() * 5 / 100, AppManager.getInstance().getHeight() / 4
                , AppManager.getInstance().getWidth() * 5 / 100, 0);
        mCardView.setLayoutParams(layoutParams);
    }

    //关注影片或取消关注
    private void collectFilm(String isCollect) {
        customDialog = new CustomDialog(Application.getInstance().getCurrentActivity(), "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setActivityId(eventModel.getActivityId() + "");
        idRequest.setIsInterest(isCollect);
        idRequest.setActivityType(eventModel.getActivityType() + "");
        OkHttpClientManager.postAsyn(Config.COLLECT_EVENT, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeNull response) {
                if ("收藏".equals(collectText.getText().toString().trim())) {
                    collectImage.setImageResource(R.mipmap.collect_select);
                    collectText.setText("已收藏");
                } else {
                    collectImage.setImageResource(R.mipmap.collect);
                    collectText.setText("收藏");
                }
                customDialog.dismiss();

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, ResponeNull.class, EventActivity.this);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.base_toolbar__left) {
            EventActivity.this.finish();
            return;
        } else if (v.getId() == R.id.base_toolbar__right_text) {//展示背景图片信息
            if (!"".equals(path)) {
                startActivity(new Intent(getBaseContext(), LookPhotoActivity.class)
                        .putExtra("path", path));
            } else {
                showShortToast("没有图片集锦");
            }
        }

        String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", EventActivity.this);
        if (!isLogin.equals("1")) {
            startActivity(LoginActivity.class);
            return;
        }

        switch (v.getId()) {
            case R.id.content_event__collect:
                if ("收藏".equals(collectText.getText().toString().trim())) {
                    collectFilm("1");
                } else {
                    collectFilm("0");
                }
                break;
            case R.id.content_event__share:
                sharePopupWindow();
                break;
            case R.id.content_event__join:
                showShortToast("参加功能正在开放中");
                break;
            case R.id.popup_share__weixin:
                mPopupWindow.dismiss();
                break;
            case R.id.popup_share__friend_circle:
                mPopupWindow.dismiss();
                break;
            case R.id.popup_share__xinlangweibo:
                mPopupWindow.dismiss();
                break;
            case R.id.popup_share__qq_kongjian:
                mPopupWindow.dismiss();
                break;
            case R.id.popup_share__cancel:
                mPopupWindow.dismiss();
                break;
        }
    }

    PopupWindow mPopupWindow;

    //弹出分享选择
    private void sharePopupWindow() {

        View parent = View
                .inflate(EventActivity.this, R.layout.activity_event, null);
        View view = View
                .inflate(EventActivity.this, R.layout.popupwindows_share, null);
        view.startAnimation(AnimationUtils.loadAnimation(EventActivity.this,
                R.anim.fade_in));
        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });


        LinearLayout weixin = (LinearLayout) view.findViewById(R.id.popup_share__weixin);
        LinearLayout friendCircle = (LinearLayout) view.findViewById(R.id.popup_share__friend_circle);
        LinearLayout xinlangweibo = (LinearLayout) view.findViewById(R.id.popup_share__xinlangweibo);
        LinearLayout qqkongjian = (LinearLayout) view.findViewById(R.id.popup_share__qq_kongjian);
        TextView cancel = (TextView) view.findViewById(R.id.popup_share__cancel);

        weixin.setOnClickListener(this);
        friendCircle.setOnClickListener(this);
        xinlangweibo.setOnClickListener(this);
        qqkongjian.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }
}
