package com.yyjlr.tickets.activity;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.BaseLinearLayout;
import com.yyjlr.tickets.helputils.NetWorkUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.AppConfigEntity;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.snakebar.Prompt;
import com.yyjlr.tickets.viewutils.snakebar.TSnackbar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Elvira on 2016/7/28.
 */
public class AbstractActivity extends AppCompatActivity {

    protected static final int MIN_CLICK_DELAY_TIME = 1000;
    protected long lastClickTime = 0;


    protected static final int CODE_REQUEST_DIALOG = 0x05;
    protected static final int CODE_REQUEST_ONE = 0x06;
    protected static final int CODE_REQUEST_TWO = 0x07;
    protected static final int CODE_REQUEST_THREE = 0x08;
    protected static final int CODE_REQUEST_FOUR = 0x09;

    public static final int CODE_RESULT = 0x10;


    protected int TOTAL_COUNTER = 20;

    protected static final int PAGE_SIZE = 10;

    protected int delayMillis = 1000;

    protected int mCurrentCounter = 0;

    protected View notLoadingView;

    protected InputMethodManager imm;

    protected boolean flag = true;
    //    public static AbstractActivity abstractActivity;
    protected CustomDialog customDialog;

    /**
     * 网络类型
     */
    private int netMobile;
    private TSnackbar snackBar = null;
    private int APP_DOWN = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;

    public static AppConfigEntity appConfig = null;
    protected ImageView bgTitle;
    protected boolean isUpdateAppConfig = false;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // 添加到循环关闭集合
//        Application.getInstance().addActivity(this);
        Application.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Application.getInstance().setCurrentActivity(this);
        // 1. 沉浸式状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        setWindowStatusBarColor();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        abstractActivity = this;
        lastClickTime = 0;
        Application.getInstance().addActivity(this);
    }

    protected void initBgTitle(ImageView bg) {
        if (appConfig != null) {
            Picasso.with(getBaseContext())
                    .load(appConfig.getStyleImage() != null ? appConfig.getStyleImage() : "https://")
                    .error(R.mipmap.bg_title)
                    .into(bg);
        }
    }

    //设置状态栏的颜色
    public void setWindowStatusBarColor() {
        if (appConfig == null)
            appConfig = (AppConfigEntity) SharePrefUtil.get(Constant.FILE_NAME, "appConfig", AbstractActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            if (appConfig != null) {
                if (appConfig.getFontColor() != null) {
                    getWindow().setStatusBarColor(Color.parseColor("#" + appConfig.getFontColor()));
                }
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 短暂显示Toast提示(来自String)
     */
    protected void showShortToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 跳转页面
     */
    protected void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    /**
     * 跳转页面
     */
    protected void startActivity(Class activity, String name, Bundle bundle) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(name, bundle);
        startActivity(intent);
    }

    /**
     * 跳转页面
     */
    protected void startActivity(Class activity, String name, String value) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(name, value);
        startActivity(intent);
    }

    /**
     * 调整沉浸式菜单的title
     */
    protected void dealStatusBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = statusBarHeight;
            view.setLayoutParams(lp);
        }
    }

    protected int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    protected boolean isMobileNum(String mobile) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }


    //获取当前网络环境
    protected String GetNetworkType() {
        String strNetworkType = "";

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }
        return strNetworkType;
    }

    /**
     * 初始化时判断有没有网络
     */

    protected boolean inspectNet() {
        this.netMobile = NetWorkUtils.getNetWorkState(AbstractActivity.this);
        return isNetConnect();
    }

    protected boolean isNetConnect() {
        if (netMobile == 1) {//连接wifi
            return true;
        } else if (netMobile == 0) {//连接移动数据
            return true;
        } else if (netMobile == -1) {//当前没有网络
            return false;

        }
        return false;
    }

    //显示无网络数据的toast
    protected void showTopToast() {
        final ViewGroup viewGroup = (ViewGroup) this.getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
        snackBar = TSnackbar.make(viewGroup, "网络不给力", TSnackbar.LENGTH_SHORT, APP_DOWN);
        snackBar.addIcon(-1, 0, 0);
//        snackBar.setAction("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        snackBar.setPromptThemBackground(Prompt.ERROR);
        snackBar.show();
    }

    //获取APP设置信息
    protected void getAppConfig() {
        final RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_APP_CONFIG, new OkHttpClientManager.ResultCallback<AppConfigEntity>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(AppConfigEntity response) {
                appConfig = response;
                BaseLinearLayout.appConfig = response;
                SharePrefUtil.save(Constant.FILE_NAME, "appConfig", response, AbstractActivity.this);
                setWindowStatusBarColor();
                setResult(CODE_RESULT, new Intent().putExtra("isUpdate", true));
                SelectCinemaActivity.activity.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, requestNull, AppConfigEntity.class, AbstractActivity.this);
    }


}
