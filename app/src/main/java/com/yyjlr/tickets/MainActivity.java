package com.yyjlr.tickets;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.CinemaDetailsActivity;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.ChosenContent;
import com.yyjlr.tickets.content.FilmContent;
import com.yyjlr.tickets.content.GrabTicketContent;
import com.yyjlr.tickets.content.MySettingContent;
import com.yyjlr.tickets.content.SaleContent;
import com.yyjlr.tickets.service.DownLoadReceive;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 主页
 */
public class MainActivity extends AbstractActivity implements View.OnClickListener {

    private ContentAdapter adapter;
    private LockableViewPager viewPager;

    private ChosenContent chosenContent;
    private FilmContent filmContent;
    private MySettingContent mySettingContent;
    private GrabTicketContent grabTicketContent;
    private SaleContent saleContent;

    private View view;

    private LinearLayout chosenLayout, filmLayout, grabLayout, saleLayout, myLayout;
    private ImageView chosenImage, filmImage, grabImage, saleImage, myImage;

    private DownloadManager downloadManager;
    private DownLoadReceive receiver;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.content_main_view);
        dealStatusBar(view);

        initView();
        //检查更新
        checkUpdate();
//        initPagerContent();
//        List<View> list = new ArrayList<View>();
//        list.add(chosenContent);
//        list.add(filmContent);
//        list.add(grabTicketContent);
//        list.add(saleContent);
//        list.add(mySettingContent);
//        adapter = new ContentAdapter(list, null);
//        viewPager.setSwipeable(false);
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
    }

    private void initView() {
//        viewPager = (LockableViewPager) findViewById(R.id.content_main_viewpager);
        chosenContent = (ChosenContent) findViewById(R.id.content_main__chosen);
        filmContent = (FilmContent) findViewById(R.id.content_main__film);
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

        chosenLayout.setOnClickListener(this);
        filmLayout.setOnClickListener(this);
        grabLayout.setOnClickListener(this);
        saleLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);

        initVisibility();
        chosenContent.setVisibility(View.VISIBLE);
        chosenContent.initView();
    }

    private void initVisibility() {
        chosenContent.setVisibility(View.GONE);
        filmContent.setVisibility(View.GONE);
        grabTicketContent.setVisibility(View.GONE);
        saleContent.setVisibility(View.GONE);
        mySettingContent.setVisibility(View.GONE);
    }


    private void initPagerContent() {
//        Context context = getBaseContext();
//        chosenContent = new ChosenContent(context);
//        mySettingContent = new MySettingContent(context);
//        filmContent = new FilmContent(context);
//        grabTicketContent = new GrabTicketContent(context);
//        saleContent = new SaleContent(context);
    }


    @Override
    public void onClick(View v) {
        setBottomView();
        initVisibility();
        switch (v.getId()) {
            case R.id.bottom_button__chosen://精选
                chosenImage.setImageResource(R.mipmap.jingxuan_select);
                chosenContent.setVisibility(View.VISIBLE);
                chosenContent.initView();
//                viewPager.setCurrentItem(0);
                break;
            case R.id.bottom_button__film://影片
                filmImage.setImageResource(R.mipmap.yingpian_select);
                filmContent.setVisibility(View.VISIBLE);
//                filmContent.initView();
//                viewPager.setCurrentItem(1);
                break;
            case R.id.bottom_button__grab://抢票
                grabImage.setImageResource(R.mipmap.qiangpiao_select);
                grabTicketContent.setVisibility(View.VISIBLE);
                grabTicketContent.initView();
//                viewPager.setCurrentItem(2);
//                grabTicketContent.adapter.set();
                break;
            case R.id.bottom_button__sale://卖品
                saleImage.setImageResource(R.mipmap.maipin_select);
                saleContent.setVisibility(View.VISIBLE);

//                viewPager.setCurrentItem(3);
                break;
            case R.id.bottom_button__my://我的
                myImage.setImageResource(R.mipmap.wode_select);
                mySettingContent.setVisibility(View.VISIBLE);
//                viewPager.setCurrentItem(4);
                break;
        }
    }

    /**
     * 初始底部View样式
     */
    private void setBottomView() {
        chosenImage.setImageResource(R.mipmap.jingxuan);
        filmImage.setImageResource(R.mipmap.yingpian);
        grabImage.setImageResource(R.mipmap.qiangpiao);
        saleImage.setImageResource(R.mipmap.maipin);
        myImage.setImageResource(R.mipmap.wode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode) {
            case CODE_REQUEST_ONE://更新个人信息
                mySettingContent.getMyInfo();
                break;
        }
    }

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
}
