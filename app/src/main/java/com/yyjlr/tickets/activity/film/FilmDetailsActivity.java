package com.yyjlr.tickets.activity.film;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.FilmDetailsPeopleAdapter;
import com.yyjlr.tickets.model.FilmPeopleEntity;
import com.yyjlr.tickets.model.film.FilmDetailsModel;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.WordWrapView;

import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影片详情
 */
public class FilmDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private LinearLayout collectLayout;//收藏
    private LinearLayout shareLayout;//分享
    private ImageView collectImage;//收藏
    private TextView collectText;//
    private TextView selectSeat;//购票选座
    private ImageView bgImage;
    private ImageView image;
    private ImageView upOrDown;
    private TextView filmName;//电影名
    private TextView filmEnglishName;//电影名
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
    private FilmDetailsModel filmDetailsModel;
    private WordWrapView wordWrapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        collectLayout = (LinearLayout) findViewById(R.id.content_film_details__collect);
        shareLayout = (LinearLayout) findViewById(R.id.content_film_details__share);
        collectImage = (ImageView) findViewById(R.id.content_film_details__collect_image);
        collectText = (TextView) findViewById(R.id.content_film_details__collect_text);
        selectSeat = (TextView) findViewById(R.id.content_film_details__select_seat);
        bgImage = (ImageView) findViewById(R.id.content_film_details__bgimage);
        image = (ImageView) findViewById(R.id.content_film_details__image);
        upOrDown = (ImageView) findViewById(R.id.content_film_details__down);
        filmName = (TextView) findViewById(R.id.content_film_details__name);
        filmEnglishName = (TextView) findViewById(R.id.content_film_details__english_name);
        introduce = (TextView) findViewById(R.id.content_film_details__introduce);
        listView = (RecyclerView) findViewById(R.id.content_film_details__listview);
        score = (RatingBar) findViewById(R.id.content_film_details__star);
        wordWrapView = (WordWrapView) findViewById(R.id.content_film_details__tags);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);

        collectLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        selectSeat.setOnClickListener(this);
        upOrDown.setOnClickListener(this);
        getFilmInfo();
    }

    //获取影片详情
    private void getFilmInfo() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setMovieId(getIntent().getStringExtra("filmId"));
        OkHttpClientManager.postAsyn(Config.GET_FILM_INFO, new OkHttpClientManager.ResultCallback<FilmDetailsModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(FilmDetailsModel response) {
                filmDetailsModel = response;
                title.setText(response.getMovieName());
                filmName.setText(response.getMovieName());
                filmEnglishName.setText(response.getMovieAlas());
                introduce.setText(response.getMovieDesc());
                if (response.getMovieBanner() != null) {
                    Picasso.with(getBaseContext())
                            .load(response.getMovieBanner())
                            .into(bgImage);
                }
                if (response.getMoviePortrait() != null) {
                    Picasso.with(getBaseContext())
                            .load(response.getMoviePortrait())
                            .into(image);
                }

                collectImage.setImageResource(R.mipmap.collect);
                if (response.getIsFavority() == 1) {
                    collectImage.setImageResource(R.mipmap.collect_select);
                }
                score.setRating(response.getScore() / 2.0f);
                if (response.getWorker() != null) {
//                    filmPeopleEntityList = Application.getiDataService().getFilmPeopleList();
                    adapter = new FilmDetailsPeopleAdapter(response.getWorker());
                    listView.setAdapter(adapter);
                }

                if (response.getTags() != null) {
                    for (int i = 0; i < response.getTags().size(); i++) {
                        wordWrapView.addView(initTag(response.getTags().get(i)));
                    }
                }
                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, FilmDetailsModel.class, FilmDetailsActivity.this);
    }

    //动态创建标签
    private View initTag(String tag) {
        View view = View.inflate(getBaseContext(), R.layout.item_film_details_tag, null);
        TextView tagText = (TextView) view.findViewById(R.id.item_film_details__tag);
        tagText.setText(tag);
        return view;
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
                if (flag) {
                    collectImage.setImageResource(R.mipmap.collect_select);
                    collectText.setText("已收藏");
                } else {
                    collectImage.setImageResource(R.mipmap.collect);
                    collectText.setText("收藏");
                }
                flag = !flag;
                break;
            case R.id.content_film_details__share://分享
                sharePopupWindow();
                break;
            case R.id.content_film_details__select_seat://选座购票
                startActivity(new Intent(getBaseContext(), FilmScheduleActivity.class).putExtra("filmId", filmDetailsModel.getMovieId() + ""));
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
