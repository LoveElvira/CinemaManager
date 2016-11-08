package com.yyjlr.tickets.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.FilmDetailsPeopleAdapter;
import com.yyjlr.tickets.model.FilmPeopleEntity;

import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影片详情
 */
public class FilmDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private LinearLayout collectLayout;//收藏
    private LinearLayout shareLayout;//分享
    private ImageView collectImage;//收藏
    private TextView selectSeat;//购票选座
    private ImageView bgImage;
    private ImageView image;
    private ImageView upOrDown;
    private TextView filmName;//电影名
    private TextView filmEnglishName;//电影名
    private TextView type;//类型
    private TextView duration;//时长
    private TextView degree;//2D 3D 4D
    private TextView country;//国家
    private TextView showTime;//上映
    private TextView introduce;//影片介绍
    private RecyclerView listView;
    private RatingBar score;//评分
    private FilmDetailsPeopleAdapter adapter;
    private List<FilmPeopleEntity> filmPeopleEntityList;
    private TextView title;
    private ImageView leftArrow;
    private static final int FILM_CONTENT_DESC_MAX_LINE = 3;
    private static final int SHRINK_UP_STATE = 1;
    private static final int SPREAD_STATE = 2;
    private static int mState = SHRINK_UP_STATE;
    private boolean flag = true;

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_film_details_title));
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        collectLayout = (LinearLayout) findViewById(R.id.content_film_details__collect);
        shareLayout = (LinearLayout) findViewById(R.id.content_film_details__share);
        collectImage = (ImageView) findViewById(R.id.content_film_details__collect_image);
        selectSeat = (TextView) findViewById(R.id.content_film_details__select_seat);
        bgImage = (ImageView) findViewById(R.id.content_film_details__bgimage);
        image = (ImageView) findViewById(R.id.content_film_details__image);
        upOrDown = (ImageView) findViewById(R.id.content_film_details__down);
        filmName = (TextView) findViewById(R.id.content_film_details__name);
        filmEnglishName = (TextView) findViewById(R.id.content_film_details__english_name);
        type = (TextView) findViewById(R.id.content_film_details__type);
        duration = (TextView) findViewById(R.id.content_film_details__duration);
        degree = (TextView) findViewById(R.id.content_film_details__degree);
        country = (TextView) findViewById(R.id.content_film_details__country);
        showTime = (TextView) findViewById(R.id.content_film_details__showtime);
        introduce = (TextView) findViewById(R.id.content_film_details__introduce);
        listView = (RecyclerView) findViewById(R.id.content_film_details__listview);
        score = (RatingBar) findViewById(R.id.content_film_details__star);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        filmPeopleEntityList = Application.getiDataService().getFilmPeopleList();
        adapter = new FilmDetailsPeopleAdapter(filmPeopleEntityList);
        listView.setAdapter(adapter);

        collectLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        selectSeat.setOnClickListener(this);
        upOrDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                FilmDetailsActivity.this.finish();
                break;
            case R.id.content_film_details__down://显示全部介绍
                if (mState == SPREAD_STATE) {//显示全部
                    introduce.setMaxLines(FILM_CONTENT_DESC_MAX_LINE);
                    introduce.requestLayout();
                    upOrDown.setBackgroundResource(R.mipmap.down_arrow);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {//隐藏部分
                    introduce.setMaxLines(Integer.MAX_VALUE);
                    introduce.requestLayout();
                    upOrDown.setBackgroundResource(R.mipmap.up_arrow);
                    mState = SPREAD_STATE;
                }

                break;
            case R.id.content_film_details__collect://收藏
//                Drawable drawable = null;
                if (flag) {
//                    drawable = getResources().getDrawable(R.mipmap.collect_select);
                    collectImage.setImageResource(R.mipmap.collect_select);
                } else {
//                    drawable = getResources().getDrawable(R.mipmap.collect);
                    collectImage.setImageResource(R.mipmap.collect);
                }
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                collect.setCompoundDrawables(null, drawable, null, null);
                flag = !flag;
                break;
            case R.id.content_film_details__share://分享
                sharePopupWindow();
                break;
            case R.id.content_film_details__select_seat://选座购票
                startActivity(FilmScheduleActivity.class);
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
                .inflate(FilmDetailsActivity.this, R.layout.activity_film_details, null);
        View view = View
                .inflate(FilmDetailsActivity.this, R.layout.popupwindows_share, null);
        view.startAnimation(AnimationUtils.loadAnimation(FilmDetailsActivity.this,
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
