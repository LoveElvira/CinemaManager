package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.CinemaDetailsActivity;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.activity.SelectCinemaActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.CinemaAdapter;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.film.HotFilmContent;
import com.yyjlr.tickets.content.film.NextFilmContent;
import com.yyjlr.tickets.helputils.GlideImageLoader;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.cinemainfo.CinemaListInfo;
import com.yyjlr.tickets.model.cinemainfo.CinemaModel;
import com.yyjlr.tickets.model.home.NewHomeInfoModel;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 改版首页
 */
public class HomeNewContent extends BaseLinearLayout implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener, BaseAdapter.RequestLoadMoreListener {


    private ImageView enterCinema;//进入影院
    private ImageView selectAddress;//选择影院
    private TextView title;

    private CollapsingToolbarLayout collapsingToolbar;
    private ContentAdapter adapter;
    private LockableViewPager viewPager;
    private HotFilmContent hotFilmContent;
    private NextFilmContent nextFilmContent;
    private final String[] titles = {"正在热映", "即将上映"};


    private Banner banner;
    private List<CinemaListInfo> cinemaList;
    private List<CinemaListInfo> cinemaLists;//总条数
    private boolean hasMore = false;
    private String pagable = "0";
    private RecyclerView listView;
    private CinemaAdapter cinemaAdapter;
    private int cinemaPosition = -1;
    private int position;

    private boolean isUpdateCinema;
    private View hotLine;
    private View nextLine;
    private TextView hotFilmText;
    private TextView nextFilmText;
    private boolean isHot;//当前显示是否是正在热映的影片
    private int isUpdateNum = 0;

    public HomeNewContent(Context context) {
        this(context, null);
    }

    public HomeNewContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_new_home_, this);
        initView();
    }

    public void setMainActivity(MainActivity main) {
        mainActivity = main;
    }

    public boolean getIsUpdateCinema() {
        return isUpdateCinema;
    }

    private void initView() {

//        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.fragment_home__collapsing_toolbar);
//
//        AppBarLayout app_bar = (AppBarLayout) findViewById(R.id.fragment_home__appbar);
//        app_bar.addOnOffsetChangedListener(this);
        AppManager.getInstance().initWidthHeight(getContext());
        isUpdateCinema = false;
        isFirst = true;
        isHot = true;
        lastClickTime = 0;
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        selectAddress = (ImageView) findViewById(R.id.base_toolbar__left);
        selectAddress.setImageResource(R.mipmap.down_arrow_white);
        selectAddress.setAlpha(1.0f);
        selectAddress.setOnClickListener(this);
        enterCinema = (ImageView) findViewById(R.id.base_toolbar__right);
        enterCinema.setImageResource(R.mipmap.enter_cinema);
        enterCinema.setAlpha(1.0f);
        enterCinema.setOnClickListener(this);

        banner = (Banner) findViewById(R.id.fragment_home__banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        banner.setIndicatorGravity(BannerConfig.CENTER);

        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.width = AppManager.getInstance().getWidth();
        params.height = AppManager.getInstance().getHeight() / 4;
        banner.setLayoutParams(params);


        hotFilmText = (TextView) findViewById(R.id.fragment_home__hot_film_text);
        nextFilmText = (TextView) findViewById(R.id.fragment_home__next_film_text);
        hotLine = findViewById(R.id.fragment_home__hot_film_line);
        nextLine = findViewById(R.id.fragment_home__next_film_line);

        hotFilmText.setOnClickListener(this);
        nextFilmText.setOnClickListener(this);
        viewPager = (LockableViewPager) findViewById(R.id.content_my_home_pager__viewpager);
        initPagerContent();
        List<View> list = new ArrayList<View>();
        list.add(hotFilmContent);
        list.add(nextFilmContent);
        adapter = new ContentAdapter(list, titles);
        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initTablayout();
                if (position == 0) {
                    hotFilmText.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    hotLine.setVisibility(VISIBLE);
                } else if (position == 1) {
                    if (isUpdateNum == 1) {
                        isUpdateNum = 2;
                        nextFilmContent.updateView(true);
                    }
                    nextFilmText.setTextColor(getResources().getColor(R.color.orange_ff7a0f));
                    nextLine.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewPager.setCurrentItem(0);
//        updateView(false);

    }

    public void updateView(boolean isUpdate) {
        if (isUpdate) {
            isFirst = isUpdate;
        }

        if (isFirst) {
            isFirst = false;
            initBgTitle(bgTitle);
            getHomeInfo();
            viewPager.setCurrentItem(0);
            isUpdateNum = 1;
            hotFilmContent.updateView(true);
        }

        boolean isFirstAction = SharePrefUtil.getBoolean(Constant.FILE_NAME, "isFirstAction", true, Application.getInstance().getCurrentActivity());
        if (isFirstAction) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (hasWindowFocus()) {
            showSelectCinemaPopupWindow(false, "");
            SharePrefUtil.putBoolean(Constant.FILE_NAME, "isFirstAction", false, Application.getInstance().getCurrentActivity());
//                    }
//                }
//            }, 1000);
        }
    }

    private void initPagerContent() {
        hotFilmContent = new HotFilmContent(getContext());
        nextFilmContent = new NextFilmContent(getContext());
    }

    private void initTablayout() {
        hotFilmText.setTextColor(getResources().getColor(R.color.black_363636));
        nextFilmText.setTextColor(getResources().getColor(R.color.black_363636));
        hotLine.setVisibility(GONE);
        nextLine.setVisibility(GONE);
    }

    PopupWindow mPopupWindow;

    public void showSelectCinemaPopupWindow(final boolean isForce, final String msg) {
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.popupwindows_select_cinema, null);

        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in));

        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(AppManager.getInstance().getWidth());
        mPopupWindow.setHeight(AppManager.getInstance().getHeight());
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setContentView(view);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = Application.getInstance().getCurrentActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);

        mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });

        TextView confirm = (TextView) view.findViewById(R.id.content_select_cinema__confirm);
        ImageView cancel = (ImageView) view.findViewById(R.id.content_select_cinema__cancel);
        listView = (RecyclerView) view.findViewById(R.id.content_select_cinema__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
        if (isForce) {
            cancel.setVisibility(GONE);
        }
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cinemaPosition == -1) {
                    if (isForce) {
                        showShortToast(msg);
                    } else {
                        mPopupWindow.dismiss();
                        return;
                    }
                }
                if (!Constant.AppDomain.equals(cinemaLists.get(cinemaPosition).getId() + "")) {
                    if ("0".equals(cinemaLists.get(position).getState())) {
                        SharePrefUtil.putString(Constant.FILE_NAME, "appDomain", cinemaLists.get(position).getId() + "", Application.getInstance().getCurrentActivity());
                        Constant.AppDomain = cinemaLists.get(cinemaPosition).getId() + "";
                        isUpdateCinema = true;
                        getAppConfig();
                        mPopupWindow.dismiss();
                        mainActivity.updateVisibility();
                    } else {
                        showTip(cinemaLists.get(position).getMessage());
                    }
//                    updateView(isUpdateCinema);
                }

            }
        });
        pagable = "0";
        cinemaLists = new ArrayList<>();
        getCinemaList(pagable);
    }

    //我的影城列表
    private void getCinemaList(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.GET_CINEMA_LIST, new OkHttpClientManager.ResultCallback<CinemaModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(CinemaModel response) {
                cinemaList = response.getCinemaList();
                if (cinemaList != null && cinemaList.size() > 0) {
                    if ("0".equals(pagables)) {//第一页
                        cinemaLists.clear();
                        cinemaLists.addAll(cinemaList);
                        Log.i("ee", cinemaLists.size() + "----" + cinemaList.size());
                        cinemaAdapter = new CinemaAdapter(cinemaList);
                        cinemaAdapter.openLoadAnimation();
                        listView.setAdapter(cinemaAdapter);
                        cinemaAdapter.openLoadMore(cinemaList.size(), true);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                        } else {
                            hasMore = false;
                        }
                        pagable = response.getPagable();
                    } else {
                        cinemaLists.addAll(cinemaList);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                            pagable = response.getPagable();
                            cinemaAdapter.notifyDataChangedAfterLoadMore(cinemaList, true);
                        } else {
                            cinemaAdapter.notifyDataChangedAfterLoadMore(cinemaList, true);
                            hasMore = false;
                            pagable = "";
                        }
                    }
                    cinemaAdapter.setOnLoadMoreListener(HomeNewContent.this);
                    cinemaAdapter.setOnRecyclerViewItemChildClickListener(HomeNewContent.this);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, pagableRequest, CinemaModel.class, Application.getInstance().getCurrentActivity());
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    cinemaAdapter.notifyDataChangedAfterLoadMore(false);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCinemaList(pagable);
                        }
                    }, 3000);
                }
            }

        });
    }

    //获取首页推荐
    private void getHomeInfo() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_NEW_HOME, new OkHttpClientManager.ResultCallback<NewHomeInfoModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(final NewHomeInfoModel response) {
                Log.i("ee", new Gson().toJson(response));
                if (response != null) {
                    title.setText(response.getCinemaInfo().getName());
                    if (response.getBannerList() != null && response.getBannerList().size() > 0) {
                        List<String> bannerList = new ArrayList<String>();
                        for (int i = 0; i < response.getBannerList().size(); i++) {
                            bannerList.add(response.getBannerList().get(i).getImageUrl());
                        }
                        banner.setImages(bannerList);
                        banner.setOnBannerClickListener(new OnBannerClickListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                if (response.getBannerList().get(position - 1).getType() == 1) {
                                    Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), CinemaDetailsActivity.class));
                                } else if (response.getBannerList().get(position - 1).getType() == 2) {
                                    String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
                                    if (!isLogin.equals("1")) {
                                        Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class));
                                    } else {
                                        Application.getInstance().getCurrentActivity().startActivity(
                                                new Intent(Application.getInstance().getCurrentActivity(), EventActivity.class)
                                                        .putExtra("eventId", response.getBannerList().get(position - 1).getId()));
                                    }
                                }
                            }
                        });
                        banner.start();
                    } else {
                        banner.setVisibility(GONE);
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, requestNull, NewHomeInfoModel.class, Application.getInstance().getCurrentActivity());
    }

    /**
     * show Dialog 提示 门店正在装修，敬请期待~
     */
    private void showTip(String msg) {
        LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        View layout = inflater.inflate(R.layout.alert_dialog_, null);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog__title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog__message);
        ImageView tipImage = (ImageView) layout.findViewById(R.id.alert_dialog__image);
        TextView confirm = (TextView) layout.findViewById(R.id.alert_dialog__submit);
        confirm.setText("确定");
        tipImage.setImageResource(R.mipmap.error);
        tipImage.setVisibility(View.VISIBLE);
//        title.setText("提示");
        message.setText(msg);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__right:
            case R.id.fragment_chosen__address:
            case R.id.fragment_chosen__listview:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), CinemaDetailsActivity.class));
                break;
            case R.id.base_toolbar__left://进入选择影院的地方
                Application.getInstance().getCurrentActivity().startActivityForResult(new Intent(Application.getInstance().getCurrentActivity(), SelectCinemaActivity.class), 0X07);
                break;
            case R.id.fragment_home__hot_film_text:
                viewPager.setCurrentItem(0);
                break;
            case R.id.fragment_home__next_film_text:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_cinema__layout:
                this.cinemaPosition = position;
                for (int i = 0; i < cinemaLists.size(); i++) {
                    cinemaLists.get(i).setChecked(0);
                }
                cinemaLists.get(position).setChecked(1);
                cinemaAdapter.notifyDataSetChanged();
                break;
        }
    }

//    private CollapsingToolbarLayoutState state;
//
//    @Override
//    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//        if (verticalOffset == 0) {
//            if (state != HerHomePagerActivity.CollapsingToolbarLayoutState.EXPANDED) {
//                state = HerHomePagerActivity.CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
//                collapsingToolbar.setTitle("EXPANDED");//设置title为EXPANDED
//            }
//        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
//            if (state != HerHomePagerActivity.CollapsingToolbarLayoutState.COLLAPSED) {
//                collapsingToolbar.setTitle("");//设置title不显示
////                        playButton.setVisibility(View.VISIBLE);//隐藏播放按钮
//                state = HerHomePagerActivity.CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
//            }
//        } else {
//            if (state != HerHomePagerActivity.CollapsingToolbarLayoutState.INTERNEDIATE) {
//                if (state == HerHomePagerActivity.CollapsingToolbarLayoutState.COLLAPSED) {
////                            playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
//                }
//                collapsingToolbar.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
//                state = HerHomePagerActivity.CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
//            }
//        }
//    }

//    private enum CollapsingToolbarLayoutState {
//        EXPANDED,
//        COLLAPSED,
//        INTERNEDIATE
//    }
}
