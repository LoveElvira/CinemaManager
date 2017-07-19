package com.yyjlr.tickets.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.CinemaDetailsActivity;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.activity.SelectCinemaActivity;
import com.yyjlr.tickets.activity.film.FilmDetailsActivity;
import com.yyjlr.tickets.activity.sale.SaleCompleteActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.CinemaAdapter;
import com.yyjlr.tickets.adapter.HomeFilmAdapter;
import com.yyjlr.tickets.adapter.HomeSaleAdapter;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.helputils.GlideImageLoader;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.cinemainfo.CinemaListInfo;
import com.yyjlr.tickets.model.cinemainfo.CinemaModel;
import com.yyjlr.tickets.model.home.GoodsInfo;
import com.yyjlr.tickets.model.home.HomeInfoModel;
import com.yyjlr.tickets.model.home.MovieInfo;
import com.yyjlr.tickets.model.sale.GoodInfo;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.yyjlr.tickets.Application.getInstance;

/**
 * Created by Elvira on 2016/7/28.
 * 首页  废弃
 */
public class HomeContent extends BaseLinearLayout implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener, BaseAdapter.RequestLoadMoreListener {

    private ScrollView scrollView;
    private Banner banner;
    private ImageView eventImage;
    private RecyclerView filmListView;
    private RecyclerView saleListView;
    //    private AutoRecyclerView cinemaListView;
//    private HomeCinemaAdapter cinemaAdapter;
    private HomeFilmAdapter filmAdapter;
    private HomeSaleAdapter saleAdapter;
    private LinearLayout filmLayout;
    private LinearLayout saleLayout;

    private ImageView enterCinema;//进入影院
    private ImageView selectAddress;//选择影院
    private TextView title;
    private LinearLayout filmMoreLayout;
    private LinearLayout saleMoreLayout;
    private HomeInfoModel homeInfoModel;
    private long totalPrice = 0;//popupwindow中的价格
    private int position;

    private List<CinemaListInfo> cinemaList;
    private List<CinemaListInfo> cinemaLists;//总条数
    private boolean hasMore = false;
    private String pagable = "0";
    private RecyclerView listView;
    private CinemaAdapter cinemaAdapter;
    private int cinemaPosition = -1;
    private boolean isUpdateCinema;
    private LinearLayout hotFilmLayout;
    private LinearLayout nextFilmLayout;
    private View hotLine;
    private View nextLine;
    private TextView hotFilmText;
    private TextView nextFilmText;
    private boolean isHot;//当前显示是否是正在热映的影片

    public HomeContent(Context context) {
        this(context, null);
    }

    public HomeContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_home, this);
        AppManager.getInstance().initWidthHeight(getContext());
        isUpdateCinema = false;
//        initView();
    }

    public boolean getIsUpdateCinema() {
        return isUpdateCinema;
    }

    public void initView() {
        isHot = true;
        lastClickTime = 0;
        title = (TextView) findViewById(R.id.base_toolbar__text);
        selectAddress = (ImageView) findViewById(R.id.base_toolbar__left);
        selectAddress.setImageResource(R.mipmap.down_arrow_white);
        selectAddress.setAlpha(1.0f);
        selectAddress.setOnClickListener(this);
        enterCinema = (ImageView) findViewById(R.id.base_toolbar__right);
        enterCinema.setImageResource(R.mipmap.enter_cinema);
        enterCinema.setAlpha(1.0f);
        enterCinema.setOnClickListener(this);

        scrollView = (ScrollView) findViewById(R.id.fragment_home__scrollview);
        scrollView.smoothScrollTo(0, 0);
        banner = (Banner) findViewById(R.id.fragment_home__banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideImageLoader());
        banner.setIndicatorGravity(BannerConfig.CENTER);
        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.width = AppManager.getInstance().getWidth();
        params.height = AppManager.getInstance().getHeight() / 4;
        banner.setLayoutParams(params);


        hotFilmLayout = (LinearLayout) findViewById(R.id.fragment_home__hot_film_layout);
        nextFilmLayout = (LinearLayout) findViewById(R.id.fragment_home__next_film_layout);
        hotFilmText = (TextView) findViewById(R.id.fragment_home__hot_film_text);
        nextFilmText = (TextView) findViewById(R.id.fragment_home__next_film_text);
        hotLine = findViewById(R.id.fragment_home__hot_film_line);
        nextLine = findViewById(R.id.fragment_home__next_film_line);
        filmLayout = (LinearLayout) findViewById(R.id.fragment_home__film_layout);
        saleLayout = (LinearLayout) findViewById(R.id.fragment_home__sale_layout);
        filmMoreLayout = (LinearLayout) findViewById(R.id.fragment_home__film_more_layout);
        saleMoreLayout = (LinearLayout) findViewById(R.id.fragment_home__sale_more_layout);

        hotFilmLayout.setOnClickListener(this);
        nextFilmLayout.setOnClickListener(this);
        filmMoreLayout.setOnClickListener(this);
        saleMoreLayout.setOnClickListener(this);


        filmListView = (RecyclerView) findViewById(R.id.fragment_home__film_listview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManagerH = new LinearLayoutManager(getInstance().getCurrentActivity());
        linearLayoutManagerH.setOrientation(LinearLayoutManager.HORIZONTAL);
        filmListView.setLayoutManager(linearLayoutManagerH);

        saleListView = (RecyclerView) findViewById(R.id.fragment_home__sale_listview);
        LinearLayoutManager linearLayoutManagerSale = new LinearLayoutManager(getInstance().getCurrentActivity());
        linearLayoutManagerSale.setOrientation(LinearLayoutManager.HORIZONTAL);
        saleListView.setLayoutManager(linearLayoutManagerSale);

        eventImage = (ImageView) findViewById(R.id.fragment_home__event_image);
        Picasso.with(getContext())
                .load("http://img06.tooopen.com/images/20170316/tooopen_sy_202006455884.jpg")
                .into(eventImage);

//        cinemaListView = (AutoRecyclerView) findViewById(R.id.fragment_home__cinema_listview);
//        //设置布局管理器
//        LinearLayoutManager linearLayoutManagerV = new LinearLayoutManager(getInstance().getCurrentActivity());
//        cinemaListView.setLayoutManager(linearLayoutManagerV);
//        initDate();
        getHomeInfo();
        boolean isFirstAction = SharePrefUtil.getBoolean(Constant.FILE_NAME, "isFirstAction", true, Application.getInstance().getCurrentActivity());
        if (isFirstAction) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (hasWindowFocus()) {
                        showSelectCinemaPopupWindow();
                        SharePrefUtil.putBoolean(Constant.FILE_NAME, "isFirstAction", false, Application.getInstance().getCurrentActivity());
                    }
                }
            }, 1000);
        }

    }

//    public void hideInput() {
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
////            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//            imm.hideSoftInputFromWindow(title.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }


    private void showSelectCinemaPopupWindow() {
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, null);
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.popupwindows_select_cinema, null);

        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in));

        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(AppManager.getInstance().getWidth());
//        mPopupWindow.setHeight(AppManager.getInstance().getHeight() - AppManager.getInstance().getHeight() / 4);
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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cinemaPosition == -1) {
                    mPopupWindow.dismiss();
                    return;
                }
                if (!Constant.AppDomain.equals(cinemaLists.get(cinemaPosition).getId() + "")) {
                    SharePrefUtil.putString(Constant.FILE_NAME, "appDomain", cinemaLists.get(position).getId() + "", Application.getInstance().getCurrentActivity());
                    Constant.AppDomain = cinemaLists.get(cinemaPosition).getId() + "";
                    isUpdateCinema = true;
                    getHomeInfo();
                }
                mPopupWindow.dismiss();
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
                    cinemaAdapter.setOnLoadMoreListener(HomeContent.this);
                    cinemaAdapter.setOnRecyclerViewItemChildClickListener(HomeContent.this);
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

    //跳转界面
//    private void startContent(View view, boolean isHot) {
//        MainActivity.startContent(view, isHot);
//    }

    //获取首页推荐
    private void getHomeInfo() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_HOME, new OkHttpClientManager.ResultCallback<HomeInfoModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(HomeInfoModel response) {
                Log.i("ee", new Gson().toJson(response));
                homeInfoModel = response;
                if (homeInfoModel != null) {
                    title.setText(homeInfoModel.getCinemaInfo().getName());
                    if (homeInfoModel.getBannerList() != null && homeInfoModel.getBannerList().size() > 0) {
                        List<String> bannerList = new ArrayList<String>();
                        for (int i = 0; i < homeInfoModel.getBannerList().size(); i++) {
                            bannerList.add(homeInfoModel.getBannerList().get(i).getImageUrl());
                        }
                        banner.setImages(bannerList);
                        banner.setOnBannerClickListener(new OnBannerClickListener() {
                            @Override
                            public void OnBannerClick(int position) {

                                if (homeInfoModel.getBannerList().get(position - 1).getType() == 1) {
                                    Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), CinemaDetailsActivity.class));
                                } else if (homeInfoModel.getBannerList().get(position - 1).getType() == 2) {
                                    String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
                                    if (!isLogin.equals("1")) {
                                        Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class));
                                    } else {
                                        Application.getInstance().getCurrentActivity().startActivity(
                                                new Intent(Application.getInstance().getCurrentActivity(), EventActivity.class)
                                                        .putExtra("eventId", homeInfoModel.getBannerList().get(position - 1).getId()));
                                    }
                                }
                            }
                        });
                        banner.start();
                    } else {
                        banner.setVisibility(GONE);
                    }

                    if (homeInfoModel.getMovieInfo() != null) {
                        initFilmText(isHot);
//                        if (homeInfoModel.getMovieInfo().getHotMovieList() != null && homeInfoModel.getMovieInfo().getHotMovieList().size() > 0) {

//                            filmLayout.setVisibility(VISIBLE);
//                            filmAdapter = new HomeFilmAdapter(homeInfoModel.getMovieInfo().getHotMovieList());
//                            filmListView.setAdapter(filmAdapter);
//                            filmAdapter.setOnRecyclerViewItemChildClickListener(HomeContent.this);
//                            filmAdapter.notifyDataSetChanged();
//                        }
                    } else {
                        filmLayout.setVisibility(GONE);
                    }

                    if (homeInfoModel.getGoodsList() != null && homeInfoModel.getGoodsList().size() > 0) {
                        saleLayout.setVisibility(VISIBLE);
                        saleAdapter = new HomeSaleAdapter(homeInfoModel.getGoodsList());
                        saleListView.setAdapter(saleAdapter);
                        saleAdapter.setOnRecyclerViewItemChildClickListener(HomeContent.this);
                    } else {
                        saleLayout.setVisibility(GONE);
                    }
                } else {
                    banner.setVisibility(GONE);
                    filmLayout.setVisibility(GONE);
                    saleLayout.setVisibility(GONE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, requestNull, HomeInfoModel.class, Application.getInstance().getCurrentActivity());
    }

    TextView salePrice;
    TextView saleNum;
    PopupWindow mPopupWindow;
    private long goodPrice;

    //弹出popwindow 选择数量
    private void selectPopupWindow(GoodsInfo goodInfo) {

        View parent = View
                .inflate(getContext(), R.layout.fragment_home, null);
        View view = View
                .inflate(getContext(), R.layout.popupwindows_sale, null);
        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in));
        mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = Application.getInstance().getCurrentActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);

        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });

        ImageView saleImage = (ImageView) view.findViewById(R.id.popup_sale__image);
        TextView buy = (TextView) view.findViewById(R.id.popup_sale__buy);
        TextView lost = (TextView) view.findViewById(R.id.popup_sale__lost);
        TextView add = (TextView) view.findViewById(R.id.popup_sale__add);
        TextView salePackage = (TextView) view.findViewById(R.id.popup_sale__package);
        TextView salePackageContent = (TextView) view.findViewById(R.id.popup_sale__package_content);
        saleNum = (TextView) view.findViewById(R.id.popup_sale__num);
        salePrice = (TextView) view.findViewById(R.id.popup_sale_price);

        salePackage.setText(goodInfo.getName());
        salePackageContent.setText(goodInfo.getGoodsDesc());
        if (goodInfo.getImageUrl() != null && !"".equals(goodInfo.getImageUrl())) {
            Picasso.with(getContext())
                    .load(goodInfo.getImageUrl())
                    .into(saleImage);
        }

        salePrice.setText(ChangeUtils.save2Decimal(goodInfo.getPrice()));
        goodPrice = goodInfo.getPrice();

        buy.setOnClickListener(HomeContent.this);
        lost.setOnClickListener(HomeContent.this);
        add.setOnClickListener(HomeContent.this);

    }


    @Override
    public void onClick(View v) {
        int num = 0;
        if (saleNum != null) {
            num = Integer.parseInt(saleNum.getText().toString());
        }
        switch (v.getId()) {
            case R.id.base_toolbar__right:
            case R.id.fragment_chosen__address:
            case R.id.fragment_chosen__listview:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), CinemaDetailsActivity.class));
                break;
            case R.id.base_toolbar__left://进入选择影院的地方
                Application.getInstance().getCurrentActivity().startActivityForResult(new Intent(Application.getInstance().getCurrentActivity(), SelectCinemaActivity.class), 0X07);
                break;
            case R.id.fragment_home__film_more_layout:
            case R.id.fragment_home__sale_more_layout:
//                startContent(v, isHot);
                break;
            case R.id.popup_sale__buy://购买
                Intent intent = new Intent();
                String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
                if (!isLogin.equals("1")) {
                    intent.setClass(getInstance().getCurrentActivity(), LoginActivity.class);
                } else {
                    intent.setClass(getInstance().getCurrentActivity(), SaleCompleteActivity.class);
                    GoodInfo goodInfo = new GoodInfo();
                    goodInfo.setGoodsId(homeInfoModel.getGoodsList().get(this.position).getId());
                    goodInfo.setGoodsName(homeInfoModel.getGoodsList().get(this.position).getName());
                    goodInfo.setStartTime(homeInfoModel.getGoodsList().get(this.position).getStartTime());
                    goodInfo.setEndTime(homeInfoModel.getGoodsList().get(this.position).getEndTime());
                    goodInfo.setGoodsDesc(homeInfoModel.getGoodsList().get(this.position).getGoodsDesc());
                    goodInfo.setPrice(homeInfoModel.getGoodsList().get(this.position).getPrice());
                    goodInfo.setGoodsImg(homeInfoModel.getGoodsList().get(this.position).getImageUrl());
                    goodInfo.setLimitedCount(homeInfoModel.getGoodsList().get(this.position).getLimitedCount());
                    intent.putExtra("goodInfo", goodInfo);
                    intent.putExtra("num", num);
                }
//                Toast.makeText(getContext(), "购买功能正在开放中", Toast.LENGTH_SHORT).show();
                Application.getInstance().getCurrentActivity().startActivity(intent);
                mPopupWindow.dismiss();
                break;
            case R.id.popup_sale__lost://减少数量
                if (num != 1) {
                    num -= 1;
                }
                salePrice.setText(ChangeUtils.save2Decimal(totalPrice * num));
                saleNum.setText(num + "");
                break;
            case R.id.popup_sale__add://增加数量
                if (homeInfoModel.getGoodsList().get(this.position).getLimitedCount() == -1) {
                    saleNum.setText((num + 1) + "");
                    salePrice.setText(ChangeUtils.save2Decimal(totalPrice * (num + 1)));
                } else if (homeInfoModel.getGoodsList().get(this.position).getLimitedCount() > 0) {
                    if (num < homeInfoModel.getGoodsList().get(this.position).getLimitedCount()) {
                        saleNum.setText((num + 1) + "");
                        salePrice.setText(ChangeUtils.save2Decimal(totalPrice * (num + 1)));
                    } else {
                        Toast.makeText(getContext(), "最多可以购买" + homeInfoModel.getGoodsList().get(this.position).getLimitedCount() + "份", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.fragment_home__hot_film_layout:
                isHot = true;
                initFilmText(isHot);
                break;
            case R.id.fragment_home__next_film_layout:
                isHot = false;
                initFilmText(isHot);
                break;
        }
    }

    private void initFilmText(boolean isHot) {
        hotFilmText.setTextColor(getResources().getColor(R.color.gray_e4e4e4));
        nextFilmText.setTextColor(getResources().getColor(R.color.gray_e4e4e4));
        hotLine.setVisibility(GONE);
        nextLine.setVisibility(GONE);
        if (isHot) {
            hotFilmText.setTextColor(getResources().getColor(R.color.white));
            hotLine.setVisibility(VISIBLE);
            if (homeInfoModel.getMovieInfo().getHotMovieList() != null && homeInfoModel.getMovieInfo().getHotMovieList().size() > 0) {
                filmAdapter = new HomeFilmAdapter(homeInfoModel.getMovieInfo().getHotMovieList());
                filmListView.setAdapter(filmAdapter);
                filmAdapter.setOnRecyclerViewItemChildClickListener(HomeContent.this);
                filmAdapter.notifyDataSetChanged();
            } else {
                List<MovieInfo> hotMovieInfoList = new ArrayList<>();
                filmAdapter = new HomeFilmAdapter(hotMovieInfoList);
                filmListView.setAdapter(filmAdapter);
                filmAdapter.notifyDataSetChanged();
            }
        } else {
            nextFilmText.setTextColor(getResources().getColor(R.color.white));
            nextLine.setVisibility(VISIBLE);
            if (homeInfoModel.getMovieInfo().getNextMovieList() != null && homeInfoModel.getMovieInfo().getNextMovieList().size() > 0) {
                filmAdapter = new HomeFilmAdapter(homeInfoModel.getMovieInfo().getNextMovieList());
                filmListView.setAdapter(filmAdapter);
                filmAdapter.setOnRecyclerViewItemChildClickListener(HomeContent.this);
                filmAdapter.notifyDataSetChanged();
            } else {
                List<MovieInfo> nextMovieInfoList = new ArrayList<>();
                filmAdapter = new HomeFilmAdapter(nextMovieInfoList);
                filmListView.setAdapter(filmAdapter);
                filmAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_home_sale__layout:
                totalPrice = homeInfoModel.getGoodsList().get(position).getPrice();
                selectPopupWindow(homeInfoModel.getGoodsList().get(position));
                break;
            case R.id.item_cinema__layout:
                this.cinemaPosition = position;
                for (int i = 0; i < cinemaLists.size(); i++) {
                    cinemaLists.get(i).setChecked(0);
                }
                cinemaLists.get(position).setChecked(1);
                cinemaAdapter.notifyDataSetChanged();
                break;
        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            this.position = position;
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.item_home_film__layout:
                    intent.setClass(getInstance().getCurrentActivity(), FilmDetailsActivity.class);
                    if (isHot) {
                        intent.putExtra("filmId", homeInfoModel.getMovieInfo().getHotMovieList().get(position).getId() + "");
                        intent.putExtra("isHot", homeInfoModel.getMovieInfo().getHotMovieList().get(position).getPresell());
                    } else {
                        intent.putExtra("filmId", homeInfoModel.getMovieInfo().getNextMovieList().get(position).getId() + "");
                        intent.putExtra("isHot", homeInfoModel.getMovieInfo().getNextMovieList().get(position).getPresell());
                    }
                    Application.getInstance()
                            .getCurrentActivity().startActivity(intent);
                    break;
            }
        }
    }

}
