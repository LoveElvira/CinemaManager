package com.yyjlr.tickets;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.EventContent;
import com.yyjlr.tickets.content.FilmContent;
import com.yyjlr.tickets.content.GrabTicketContent;
import com.yyjlr.tickets.content.HomeContent;
import com.yyjlr.tickets.content.HomeNewContent;
import com.yyjlr.tickets.content.MySettingContent;
import com.yyjlr.tickets.content.SaleContent;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.AppConfigEntity;
import com.yyjlr.tickets.model.CinemaStatusEntity;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.DownLoadReceive;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.LockableViewPager;

/**
 * Created by Elvira on 2016/7/28.
 * 主页
 */
public class MainActivity extends AbstractActivity implements View.OnClickListener {

    private long mExitTime; //退出时间
    private FilmContent filmContent;
    private EventContent eventContent;
    public static MySettingContent mySettingContent;
    private GrabTicketContent grabTicketContent;
    private SaleContent saleContent;
    private HomeNewContent homeContent;

    private View view;

    private LinearLayout chosenLayout, filmLayout, grabLayout, saleLayout, myLayout;
    private ImageView chosenImage;
    private ImageView filmImage;
    private ImageView grabImage;
    private ImageView saleImage;
    private ImageView myImage;
    private TextView chosenText;
    private TextView filmText;
    private TextView grabText;
    private TextView saleText;
    private TextView myText;

    private DownloadManager downloadManager;
    private DownLoadReceive receiver;
    private boolean isUpdateCinema;
    private boolean isUpdateFilm;
    private boolean isUpdateSale;
    private boolean isUpdateMy;
    private boolean isUpdateEvent;
    private boolean isFirstFilm;
    private boolean isFirstTicket;
    private boolean isFirstSale;
    private boolean isHot = true;
    private String isError = "https://";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customDialog = new CustomDialog(MainActivity.this, "切换中...");
        isUpdateCinema = false;
        isUpdateFilm = true;
        isUpdateSale = true;
        isUpdateEvent = true;
        isUpdateMy = true;
        view = findViewById(R.id.content_main_view);
        dealStatusBar(view);

        initView();
        //检查更新
        checkUpdate();
    }

    private void initView() {
        homeContent = (HomeNewContent) findViewById(R.id.content_main__home);
        homeContent.setMainActivity(this);
        filmContent = (FilmContent) findViewById(R.id.content_main__film);
        eventContent = (EventContent) findViewById(R.id.content_main__event);
        grabTicketContent = (GrabTicketContent) findViewById(R.id.content_main__ticket);
        saleContent = (SaleContent) findViewById(R.id.content_main__sale);
        mySettingContent = (MySettingContent) findViewById(R.id.content_main__my);

        chosenLayout = (LinearLayout) findViewById(R.id.bottom_button__chosen);
        filmLayout = (LinearLayout) findViewById(R.id.bottom_button__film);
        grabLayout = (LinearLayout) findViewById(R.id.bottom_button__grab);
        saleLayout = (LinearLayout) findViewById(R.id.bottom_button__sale);
        myLayout = (LinearLayout) findViewById(R.id.bottom_button__my);
        chosenImage = (ImageView) findViewById(R.id.bottom_button__chosen_image);
        filmImage = (ImageView) findViewById(R.id.bottom_button__film_image);
        grabImage = (ImageView) findViewById(R.id.bottom_button__grab_image);
        saleImage = (ImageView) findViewById(R.id.bottom_button__sale_image);
        myImage = (ImageView) findViewById(R.id.bottom_button__my_image);
        chosenText = (TextView) findViewById(R.id.bottom_button__chosen_text);
        filmText = (TextView) findViewById(R.id.bottom_button__film_text);
        grabText = (TextView) findViewById(R.id.bottom_button__grab_text);
        saleText = (TextView) findViewById(R.id.bottom_button__sale_text);
        myText = (TextView) findViewById(R.id.bottom_button__my_text);

        chosenLayout.setOnClickListener(this);
        filmLayout.setOnClickListener(this);
        grabLayout.setOnClickListener(this);
        saleLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);
        getCinemaStatus();
//        homeContent.updateView(isUpdateFilm);
    }

    public void initVisibility(int type) {
        homeContent.setVisibility(View.GONE);
//        filmContent.setVisibility(View.GONE);
        eventContent.setVisibility(View.GONE);
//        grabTicketContent.setVisibility(View.GONE);
        saleContent.setVisibility(View.GONE);
        mySettingContent.setVisibility(View.GONE);

        chosenText.setTextColor(getResources().getColor(R.color.black_343434));
        filmText.setTextColor(getResources().getColor(R.color.black_343434));
        saleText.setTextColor(getResources().getColor(R.color.black_343434));
        grabText.setTextColor(getResources().getColor(R.color.black_343434));
        myText.setTextColor(getResources().getColor(R.color.black_343434));

//        chosenImage.setImageResource(R.mipmap.bottom_film_default);
        Picasso.with(getBaseContext())
                .load((appConfig != null && appConfig.getMovieIconImage() != null) ? appConfig.getMovieIconImage() : isError)
                .error(R.mipmap.bottom_film_default)
                .into(filmImage);
        Picasso.with(getBaseContext())
                .load((appConfig != null && appConfig.getActivityIconImage() != null) ? appConfig.getActivityIconImage() : isError)
                .error(R.mipmap.bottom_event_default)
                .into(grabImage);
        Picasso.with(getBaseContext())
                .load((appConfig != null && appConfig.getStoreIconImage() != null) ? appConfig.getStoreIconImage() : isError)
                .error(R.mipmap.bottom_mall_default)
                .into(saleImage);
        Picasso.with(getBaseContext())
                .load((appConfig != null && appConfig.getMyIconImage() != null) ? appConfig.getMyIconImage() : isError)
                .error(R.mipmap.bottom_mine_default)
                .into(myImage);
        switch (type) {
            case 0://影片
                hideInput();
                Picasso.with(getBaseContext())
                        .load((appConfig != null && appConfig.getMovieIconoptImage() != null) ? appConfig.getMovieIconoptImage() : isError)
                        .error(R.mipmap.bottom_film_select)
                        .into(filmImage);
                filmText.setTextColor((appConfig != null && appConfig.getFontColor() != null) ? Color.parseColor("#" + appConfig.getFontColor()) : getResources().getColor(R.color.blue_2AABE2));
//                filmImage.setImageResource(R.mipmap.bottom_film_select);
                homeContent.setVisibility(View.VISIBLE);
                homeContent.updateView((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateFilm);
                isUpdateFilm = false;
                break;
            case 1://活动
                hideInput();
                Picasso.with(getBaseContext())
                        .load((appConfig != null && appConfig.getActivityIconoptImage() != null) ? appConfig.getActivityIconoptImage() : isError)
                        .error(R.mipmap.bottom_event_select)
                        .into(grabImage);
                grabText.setTextColor((appConfig != null && appConfig.getFontColor() != null) ? Color.parseColor("#" + appConfig.getFontColor()) : getResources().getColor(R.color.blue_2AABE2));
//                filmImage.setImageResource(R.mipmap.bottom_film_select);
                eventContent.setVisibility(View.VISIBLE);
                eventContent.updateView((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateEvent);
                isUpdateEvent = false;
                break;
            case 2://商城
                hideInput();
                Picasso.with(getBaseContext())
                        .load((appConfig != null && appConfig.getStoreIconoptImage() != null) ? appConfig.getStoreIconoptImage() : isError)
                        .error(R.mipmap.bottom_mall_select)
                        .into(saleImage);
                saleText.setTextColor((appConfig != null && appConfig.getFontColor() != null) ? Color.parseColor("#" + appConfig.getFontColor()) : getResources().getColor(R.color.blue_2AABE2));
//                filmImage.setImageResource(R.mipmap.bottom_film_select);
                saleContent.setVisibility(View.VISIBLE);
                saleContent.updateView((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateSale);
                isUpdateSale = false;
                break;
            case 3://我的
                hideInput();
                Picasso.with(getBaseContext())
                        .load((appConfig != null && appConfig.getMyIconoptImage() != null) ? appConfig.getMyIconoptImage() : isError)
                        .error(R.mipmap.bottom_mine_select)
                        .into(myImage);
                myText.setTextColor((appConfig != null && appConfig.getFontColor() != null) ? Color.parseColor("#" + appConfig.getFontColor()) : getResources().getColor(R.color.blue_2AABE2));
//                filmImage.setImageResource(R.mipmap.bottom_film_select);
                mySettingContent.setVisibility(View.VISIBLE);
                mySettingContent.updateView((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateMy);
                isUpdateMy = false;
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_button__chosen://精选
                hideInput();
                chosenImage.setImageResource(R.mipmap.jingxuan_select);
                chosenText.setTextColor(getResources().getColor(R.color.blue_2AABE2));
                homeContent.setVisibility(View.VISIBLE);
//                homeContent.hideInput();
                break;
            case R.id.bottom_button__film://影片
                if (homeContent.getVisibility() == View.GONE) {
                    initVisibility(0);
                }

//                hideInput();
//                filmText.setTextColor(getResources().getColor(R.color.blue_2AABE2));
//                filmImage.setImageResource(R.mipmap.bottom_film_select);
//                homeContent.setVisibility(View.VISIBLE);
//
//                if (isFirstFilm) {
//                    filmContent.initView(isHot);
//                    isFirstFilm = false;
//                    return;
//                }
//                if ((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateFilm) {
//                    filmContent.initView(isHot);
//                    isUpdateFilm = false;
//                    return;
//                }
//                if (isUpdateFilm && isHot) {
//                    filmContent.initView(isHot);
//                    isUpdateFilm = false;
//                    return;
//                }
                break;
            case R.id.bottom_button__grab://抢票
                if (grabTicketContent.getVisibility() == View.GONE) {
                    initVisibility(1);
                }

//                hideInput();
//                grabText.setTextColor(getResources().getColor(R.color.blue_2AABE2));
//                grabImage.setImageResource(R.mipmap.bottom_event_select);
//                eventContent.setVisibility(View.VISIBLE);
//                if (isFirstTicket) {
//                    eventContent.updateView();
//                    isFirstTicket = false;
//                }
//                if ((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateTicket) {
////                    grabTicketContent.updateView();
//                    eventContent.updateView();
//                    isUpdateTicket = false;
//                }
////                grabTicketContent.updateAdapter();
//                eventContent.updateAdapter();
                break;
            case R.id.bottom_button__sale://卖品
                if (saleContent.getVisibility() == View.GONE) {
                    initVisibility(2);
                }

//                saleText.setTextColor(getResources().getColor(R.color.blue_2AABE2));
//                saleImage.setImageResource(R.mipmap.bottom_mall_select);
//                saleContent.setVisibility(View.VISIBLE);
//                if (isFirstSale) {
////                    saleContent.initView();
//                    isFirstSale = false;
//                }
//                if ((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateSale) {
////                    saleContent.initView();
//                    isUpdateSale = false;
//                }
                break;
            case R.id.bottom_button__my://我的
                if (mySettingContent.getVisibility() == View.GONE) {
                    initVisibility(3);
                }

//                hideInput();
//                myText.setTextColor(getResources().getColor(R.color.blue_2AABE2));
//                myImage.setImageResource(R.mipmap.bottom_mine_select);
//                mySettingContent.setVisibility(View.VISIBLE);
////                mySettingContent.hideInput();
//                mySettingContent.updateView();
                break;
        }
    }

    //隐藏虚拟键盘
    public void hideInput() {
        if (saleContent.getEditText() != null) {
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(saleContent.getEditText().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE://更新个人信息
                if (data.getBooleanExtra("isExit", false)) {
                    //退出登录
                    mySettingContent.updateTopView(false, "", "", "");
                } else {
                    String headImage = data.getStringExtra("headImage");
                    String sex = data.getStringExtra("sex");
                    String name = data.getStringExtra("name");
                    mySettingContent.updateTopView(true, headImage, sex, name);
                }
                break;
            case CODE_REQUEST_TWO://更新cinema ID
                if (data.getBooleanExtra("isUpdate", false)) {
//                    customDialog.show();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(2000);
//                                customDialog.dismiss();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
                    isUpdateCinema = true;
                    isUpdateFilm = true;
                    isUpdateSale = true;
                    isUpdateEvent = true;
                    isUpdateMy = true;
                    initVisibility(0);
                }
                break;
            case CODE_REQUEST_THREE:
                if (data.getBooleanExtra("isRead", false)) {
                    mySettingContent.getMyMessageNum();
                }
                break;
            case CODE_REQUEST_FOUR:
//                if (data.getBooleanExtra("isUpdate", false)) {
                int isHaveCollect = data.getIntExtra("isHaveCollect", -1);
                if (isHaveCollect != 0 && isHaveCollect != -1) {
                    int position = data.getIntExtra("position", -1);
                    eventContent.updateAdapter(position, isHaveCollect);
                }
//                }
                break;

        }
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    public static void startContent(View view, boolean isFilmHot) {
//        setBottomView();
//        initVisibility();
//        switch (view.getId()) {
//            case R.id.fragment_home__film_more_layout:
//                filmImage.setImageResource(R.mipmap.bottom_film_select);
//                filmContent.setVisibility(View.VISIBLE);
//                isHot = isFilmHot;
//                if (isFirstFilm) {
//                    filmContent.initView(isHot);
//                    isFirstFilm = false;
//                } else if ((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateFilm) {
//                    filmContent.initView(isHot);
//                    isUpdateFilm = false;
//                } else {
//                    filmContent.setCurrentView(isHot);
//                }
//                break;
//            case R.id.fragment_home__sale_more_layout:
//                saleImage.setImageResource(R.mipmap.bottom_mall_select);
//                saleContent.setVisibility(View.VISIBLE);
//                if (isFirstSale) {
//                    saleContent.initView();
//                    isFirstSale = false;
//                } else if ((homeContent.getIsUpdateCinema() || isUpdateCinema) && isUpdateSale) {
//                    saleContent.initView();
//                    isUpdateSale = false;
//                } else {
////                    saleContent.setCurrentView();
//                }
//
//                break;
//        }
//    }


    //检查更新
    private void checkUpdate() {
//        RequestNull requestNull = new RequestNull();
//        OkHttpClientManager.postAsyn(Config.GET_APP_VERSION, new OkHttpClientManager.ResultCallback<AppVersion>() {
//            @Override
//            public void onError(Request request, Error info) {
//                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
//            }
//
//            @Override
//            public void onResponse(AppVersion response) {
//                appVersion = response;
//                if (appVersion == null)
//                    return;
//                PackageInfo pi = null;
//                PackageManager pm = getPackageManager();
//                try {
//                    pi = pm.getPackageInfo(getPackageName(), 0);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//                String versionName = appVersion.getAndroid().getVersion().replace(".", "");
//                String versionNameLocal = pi.versionName.replace(".", "");
//                if (Float.parseFloat(versionName) > Float.parseFloat((versionNameLocal))) {
//                    SharePrefUtil.putString(Constant.FILE_NAME, "app_update", "true", MainActivity.this);
//
//                    //提示更新
//                    showUpdataDialog();
//                }
//            }
//
//            @Override
//            public void onOtherError(Request request, Exception exception) {
//                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//            }
//        }, requestNull, AppVersion.class);
    }

    //提示更新的dailog
    private void showUpdataDialog() {
//        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//        View layout = inflater.inflate(R.layout.alert_dialog, null);
//        final AlertDialog builder = new AlertDialog.Builder(MainActivity.this).create();
//        builder.setView(layout);
//        builder.setCancelable(false);
//        builder.show();
//        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
//        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
//        TextView submit = (TextView) layout.findViewById(R.id.alert_dialog__submit);
//        title.setText(appVersion.getAndroid().getTitle());
//        submit.setText("确定");
//        message.setText(appVersion.getAndroid().getDesc().toString());
//        layout.findViewById(R.id.alert_dialog__cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.dismiss();
//            }
//        });
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String netType = GetNetworkType();
//                if ("wifi".equalsIgnoreCase(netType)) {
//                    downLoadApk();
//                    builder.dismiss();
//                } else {
//                    builder.dismiss();
//                    showNetWorkDialog();
//                }
//            }
//        });
//        if (appVersion.getAndroid().getForcedUpdate() == 0) {
//            //当点取消按钮时进行登录
//            layout.findViewById(R.id.alert_dialog__cancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    builder.dismiss();
//                }
//            });
//        }
//    }
//
//    //提示更新的dailog 非wifi
//    private void showNetWorkDialog() {
//        final AlertDialog.Builder builer = new AlertDialog.Builder(this);
//        builer.setTitle("更新提示");
//
//        builer.setMessage("当前使用的是移动网络(非wifi),是否确认下载?");
//        //当点确定按钮时从服务器上下载 新的apk 然后安装
//        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                downLoadApk();
//                dialog.dismiss();
//            }
//        });
//
//        //当点取消按钮时进行登录
//        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                showUpdataDialog();
//            }
//        });
//
//        AlertDialog dialog = builer.create();
//        dialog.setCancelable(false);
//        dialog.show();
    }

    //下载apk
    private void downLoadApk() {
        //下载apk
        new Thread() {
            @Override
            public void run() {
                try {
//                    String apkUrl = appVersion.getAndroid().getUpdateUrl();
                    String apkUrl = "";
                    // String apkUrl = "http://dsapp.asc-wines.com/downloadapp/ds-app.apk";
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
                    request.setDestinationInExternalPublicDir("download", "sfc.apk");
                    request.setDescription(getResources().getString(R.string.app_name) + "新版本下载");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setMimeType("application/vnd.android.package-archive");
                    // 设置为可被媒体扫描器找到
                    request.allowScanningByMediaScanner();
                    // 设置为可见和可管理
                    request.setVisibleInDownloadsUi(true);
                    long refernece = downloadManager.enqueue(request);
                    SharedPreferences spf = MainActivity.this.getSharedPreferences("download", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putLong("download_id", refernece);//保存下载ID
                    editor.commit();
                    receiver = new DownLoadReceive();
                    MainActivity.this.registerReceiver(receiver, new IntentFilter(
                            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    //  installApk(refernece);
                    //    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

    //获取影院是否在维护中的信息
    protected void getCinemaStatus() {
        final RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_CINEMA_STATUS, new OkHttpClientManager.ResultCallback<CinemaStatusEntity>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(CinemaStatusEntity response) {
                if (response != null) {
                    if ("0".equals(response.getState())) {//门店正常
                        initVisibility(0);
                    } else {//门店维护中
                        showShortToast(response.getMessage());
                        SharePrefUtil.putBoolean(Constant.FILE_NAME, "isFirstAction", false, Application.getInstance().getCurrentActivity());
                        homeContent.showSelectCinemaPopupWindow(true,response.getMessage());
                    }
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, requestNull, CinemaStatusEntity.class, MainActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                showShortToast("再按一次退出应用程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
