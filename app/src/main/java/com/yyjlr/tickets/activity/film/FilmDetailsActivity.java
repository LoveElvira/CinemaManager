package com.yyjlr.tickets.activity.film;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FilmCommentAdapter;
import com.yyjlr.tickets.adapter.FilmDetailsPeopleAdapter;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.FilmPeopleEntity;
import com.yyjlr.tickets.model.ResponeCollect;
import com.yyjlr.tickets.model.film.FilmComment;
import com.yyjlr.tickets.model.film.FilmCommentModel;
import com.yyjlr.tickets.model.film.FilmDetailsModel;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.AutoRecyclerView;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.Star;
import com.yyjlr.tickets.viewutils.WordWrapView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.yyjlr.tickets.Application.getInstance;

/**
 * Created by Elvira on 2016/8/1.
 * 影片详情
 */
public class FilmDetailsActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private ScrollView scrollView;
    //    private LinearLayout collectLayout;//收藏
    private LinearLayout commentContentLayout;//评论内容
    private LinearLayout commentLayout;//写评论
    private LinearLayout shareLayout;//分享
    //    private ImageView collectImage;//收藏
//    private TextView collectText;//
    private TextView selectSeat;//购票选座
    private ImageView bgImage;
    private ImageView image;
    private ImageView upOrDown;
    private TextView filmName;//电影名
    private TextView filmEnglishName;//电影名
    private TextView introduce;//影片介绍
    private RecyclerView listView;
    private AutoRecyclerView commentListView;
    private FilmCommentAdapter commentAdapter;
    private Star score;//评分
    private FilmDetailsPeopleAdapter adapter;
    private TextView title;
    private ImageView rightCollect;
    private ImageView leftArrow;
    private final int FILM_CONTENT_DESC_MAX_LINE = 3;
    private final int SHRINK_UP_STATE = 1;
    private final int SPREAD_STATE = 2;
    private int mState = SHRINK_UP_STATE;
    private FilmDetailsModel filmDetailsModel;
    private WordWrapView wordWrapView;
    private boolean isUpdateCollect = false;
    private int position = -1;
    private int isCollect = 0;//0 未收藏  1 已收藏
    private String presell;//是否预售 0 否  1  是
    private String filmId = "";
    private List<FilmComment> filmCommentList;
    private List<FilmComment> filmCommentLists = new ArrayList<>();//总条数
    private boolean hasMore = false;
    private String pagable = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);
        position = getIntent().getIntExtra("position", -1);
        presell = getIntent().getStringExtra("isHot");
        filmId = getIntent().getStringExtra("filmId");
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        rightCollect = (ImageView) findViewById(R.id.base_toolbar__right);
        rightCollect.setAlpha(1.0f);
        rightCollect.setImageResource(R.mipmap.collect_white);
        rightCollect.setOnClickListener(this);

//        scrollView = (ScrollView) findViewById(R.id.content_film_details__scrollview);
//        scrollView.smoothScrollTo(0, 0);

        commentContentLayout = (LinearLayout) findViewById(R.id.content_film_details__comment_content_layout);
        commentLayout = (LinearLayout) findViewById(R.id.content_film_details__comment);
        shareLayout = (LinearLayout) findViewById(R.id.content_film_details__share);
//        collectImage = (ImageView) findViewById(R.id.content_film_details__collect_image);
//        collectText = (TextView) findViewById(R.id.content_film_details__collect_text);
        selectSeat = (TextView) findViewById(R.id.content_film_details__select_seat);
        bgImage = (ImageView) findViewById(R.id.content_film_details__bgimage);
        image = (ImageView) findViewById(R.id.content_film_details__image);
        upOrDown = (ImageView) findViewById(R.id.content_film_details__down);
        filmName = (TextView) findViewById(R.id.content_film_details__name);
        filmEnglishName = (TextView) findViewById(R.id.content_film_details__english_name);
        introduce = (TextView) findViewById(R.id.content_film_details__introduce);
        listView = (RecyclerView) findViewById(R.id.content_film_details__listview);
        score = (Star) findViewById(R.id.content_film_details__star);
        wordWrapView = (WordWrapView) findViewById(R.id.content_film_details__tags);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);

        //设置布局管理器
        commentListView = (AutoRecyclerView) findViewById(R.id.content_film_details__comment_listview);
        LinearLayoutManager commentLinearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        commentLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentListView.setLayoutManager(commentLinearLayoutManager);

        commentLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        upOrDown.setOnClickListener(this);

        if ("0".equals(presell)) {
            selectSeat.setBackgroundColor(getResources().getColor(R.color.gray_c7c7c7));
        } else {
            selectSeat.setOnClickListener(this);
            selectSeat.setBackgroundColor(getResources().getColor(R.color.orange_ff7b0f));
        }

        getFilmInfo();
        pagable = "0";
        getComment(pagable);
    }

    //获取影片详情
    private void getFilmInfo() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setMovieId(filmId);
        OkHttpClientManager.postAsyn(Config.GET_FILM_INFO, new OkHttpClientManager.ResultCallback<FilmDetailsModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(FilmDetailsModel response) {
                filmDetailsModel = response;
                if (filmDetailsModel != null) {
                    title.setText(response.getMovieName());
                    filmName.setText(response.getMovieName());
                    filmEnglishName.setText(response.getMovieAlas());
                    introduce.setText(response.getMovieDesc());
//                    introduce.setText(Html.fromHtml(response.getMovieDesc()));
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
                    isCollect = response.getIsFavority();
                    rightCollect.setImageResource(R.mipmap.collect_white);
//                    collectImage.setImageResource(R.mipmap.collect);
//                    collectText.setText("收藏");
                    if (isCollect == 1) {
                        rightCollect.setImageResource(R.mipmap.collect_select);
//                        collectImage.setImageResource(R.mipmap.collect_select);
//                        collectText.setText("已收藏");
                    }
                    score.setMark(response.getScore() / 2.0f);
                    if (response.getWorker() != null && response.getWorker().size() > 0) {
                        adapter = new FilmDetailsPeopleAdapter(response.getWorker());
                        listView.setAdapter(adapter);
                    }

                    if (response.getTags() != null && response.getTags().size() > 0) {
                        for (int i = 0; i < response.getTags().size(); i++) {
                            if (response.getTags().get(i) != null) {
                                wordWrapView.addView(initTag(response.getTags().get(i)));
                            }
                        }
                    }
                }
                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
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

    //获取评论内容
    private void getComment(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        pagableRequest.setMovieId(filmId);
        OkHttpClientManager.postAsyn(Config.GET_FILM_COMMENT, new OkHttpClientManager.ResultCallback<FilmCommentModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(FilmCommentModel response) {
                if (response != null) {
                    filmCommentList = response.getCommentList();
                    if (filmCommentList != null && filmCommentList.size() > 0) {
                        if ("0".equals(pagables)) {//第一页
                            commentContentLayout.setVisibility(View.VISIBLE);
                            filmCommentLists.clear();
                            filmCommentLists.addAll(filmCommentList);
                            Log.i("ee", filmCommentLists.size() + "----" + filmCommentList.size());
                            commentAdapter = new FilmCommentAdapter(filmCommentList);
                            commentAdapter.openLoadAnimation();
                            commentListView.setAdapter(commentAdapter);
                            commentAdapter.openLoadMore(filmCommentList.size(), true);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                            } else {
                                hasMore = false;
                            }
                            pagable = response.getPagable();
                        } else {
                            filmCommentLists.addAll(filmCommentList);
                            if (response.getHasMore() == 1) {
                                hasMore = true;
                                pagable = response.getPagable();
                                commentAdapter.notifyDataChangedAfterLoadMore(filmCommentList, true);
                            } else {
                                commentAdapter.notifyDataChangedAfterLoadMore(filmCommentList, true);
                                hasMore = false;
                                pagable = "";
                            }
                        }
                        commentAdapter.setOnLoadMoreListener(FilmDetailsActivity.this);
                        commentAdapter.setOnRecyclerViewItemChildClickListener(FilmDetailsActivity.this);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, pagableRequest, FilmCommentModel.class, FilmDetailsActivity.this);
    }


    //关注影片或取消关注
    private void collectFilm(final String isCollect) {
        customDialog = new CustomDialog(Application.getInstance().getCurrentActivity(), "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setId(getIntent().getStringExtra("filmId"));
        idRequest.setType("1");
        idRequest.setIsInterest(isCollect);
        OkHttpClientManager.postAsyn(Config.GO_COLLECT, new OkHttpClientManager.ResultCallback<ResponeCollect>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo().toString());
                showShortToast(info.getInfo().toString());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ResponeCollect response) {
                if ("1".equals(isCollect)) {
                    FilmDetailsActivity.this.isCollect = 1;
                    isUpdateCollect = false;
                    rightCollect.setImageResource(R.mipmap.collect_select);
                } else {
                    FilmDetailsActivity.this.isCollect = 0;
                    isUpdateCollect = true;
                    rightCollect.setImageResource(R.mipmap.collect_white);
                }
//                if ("收藏".equals(collectText.getText().toString().trim())) {
//                    isUpdateCollect = false;
//                    collectImage.setImageResource(R.mipmap.collect_select);
//                    collectText.setText("已收藏");
//                } else {
//                    isUpdateCollect = true;
//                    collectImage.setImageResource(R.mipmap.collect);
//                    collectText.setText("收藏");
//                }
                customDialog.dismiss();

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, ResponeCollect.class, FilmDetailsActivity.this);
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    commentAdapter.notifyDataChangedAfterLoadMore(false);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getComment(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.item_film__buy_ticket:
                    break;
            }
            getInstance().getCurrentActivity().startActivity(intent);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.base_toolbar__left) {
            setResult(CODE_RESULT, new Intent()
                    .putExtra("isUpdate", isUpdateCollect)
                    .putExtra("position", position));
            FilmDetailsActivity.this.finish();
            return;
        } else if (view.getId() == R.id.content_film_details__down) {//显示全部介绍
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
        }
        String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", FilmDetailsActivity.this);
        switch (view.getId()) {
            case R.id.base_toolbar__right://收藏
                if (!isLogin.equals("1")) {
                    startActivity(new Intent(FilmDetailsActivity.this, LoginActivity.class)
                            .putExtra("pager", "FilmDetailsActivity"));
                } else {
//                    if ("收藏".equals(collectText.getText().toString().trim())) {
//                        collectFilm("1");
//                    } else {
//                        collectFilm("0");
//                    }
                    if (isCollect == 0) {
                        collectFilm("1");
                    } else {
                        collectFilm("0");
                    }
                }
                break;
            case R.id.content_film_details__comment://写评论
                if (!isLogin.equals("1")) {
                    startActivity(new Intent(FilmDetailsActivity.this, LoginActivity.class)
                            .putExtra("pager", "FilmDetailsActivity"));
                } else {
                    startActivityForResult(new Intent(FilmDetailsActivity.this, CommentActivity.class)
                                    .putExtra("filmName", filmDetailsModel.getMovieName())
                                    .putExtra("filmId", filmDetailsModel.getMovieId() + "")
                            , CODE_REQUEST_ONE);
                }
                break;
            case R.id.content_film_details__share://分享
                showShortToast("此功能暂未开放");
//                sharePopupWindow();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE:
                if (data.getBooleanExtra("isUpdate", false)) {
                    pagable = "0";
                    getComment(pagable);
                }
                break;
            case CODE_REQUEST_DIALOG:
                pagable = "0";
                getComment(pagable);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(CODE_RESULT, new Intent()
                    .putExtra("isUpdate", isUpdateCollect)
                    .putExtra("position", position));
            FilmDetailsActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
