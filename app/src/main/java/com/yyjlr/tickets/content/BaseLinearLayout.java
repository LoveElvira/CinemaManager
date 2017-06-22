package com.yyjlr.tickets.content;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.helputils.NetWorkUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.AppConfigEntity;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;
import com.yyjlr.tickets.viewutils.snakebar.Prompt;
import com.yyjlr.tickets.viewutils.snakebar.TSnackbar;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Elvira on 2017/5/3.
 */

public class BaseLinearLayout extends LinearLayout {

    protected final int MIN_CLICK_DELAY_TIME = 1000;
    protected long lastClickTime = 0;

    protected int delayMillis = 1000;
    protected static InputMethodManager imm;

    protected View view;
    protected CustomDialog customDialog;
    protected View notLoadingView;
    protected LinearLayout noWifiLayout;//无网络
    protected boolean isOne = false;//第一次请求网络
    /**
     * 网络类型
     */
    private int netMobile;
    private TSnackbar snackBar = null;
    private int APP_DOWN = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;

    protected ImageView bgTitle;
    public static AppConfigEntity appConfig;
    protected boolean isFirst = true;//第一次运行次界面的标记
    protected MainActivity mainActivity = null;


    public BaseLinearLayout(Context context) {
        super(context);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        lastClickTime = 0;
        imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
    }

    protected void initBgTitle(ImageView bg) {
        if (appConfig == null)
            appConfig = (AppConfigEntity) SharePrefUtil.get(Constant.FILE_NAME, "appConfig", Application.getInstance().getCurrentActivity());
        if (appConfig != null) {
            Log.i("ee", "----" + appConfig.getStyleImage());

            Picasso.with(getContext())
                    .load(appConfig.getStyleImage() != null ? appConfig.getStyleImage() : "https://")
                    .error(R.mipmap.bg_title)
                    .into(bg);
        }
    }

    //设置状态栏的颜色
    public void setWindowStatusBarColor() {
        if (appConfig == null)
            appConfig = (AppConfigEntity) SharePrefUtil.get(Constant.FILE_NAME, "appConfig", Application.getInstance().getCurrentActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = Application.getInstance().getCurrentActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            if (appConfig != null) {
                if (appConfig.getFontColor() != null) {
                    Application.getInstance().getCurrentActivity().getWindow().setStatusBarColor(Color.parseColor("#" + appConfig.getFontColor()));
                }
            } else {
                Application.getInstance().getCurrentActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
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
                AbstractActivity.appConfig = response;
                SharePrefUtil.save(Constant.FILE_NAME, "appConfig", response, Application.getInstance().getCurrentActivity());
                setWindowStatusBarColor();
                if (mainActivity != null) {
                    mainActivity.initVisibility(0);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, requestNull, AppConfigEntity.class, Application.getInstance().getCurrentActivity());
    }

    /**
     * 短暂显示Toast提示(来自String)
     */
    protected void showShortToast(String msg) {
        Toast toast = Toast.makeText(Application.getInstance().getCurrentActivity(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //显示无网络数据的toast
    protected void showTopToast() {
        final ViewGroup viewGroup = (ViewGroup) Application.getInstance().getCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content).getRootView();
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

    /**
     * 初始化时判断有没有网络
     */

    protected boolean inspectNet() {
        this.netMobile = NetWorkUtils.getNetWorkState(Application.getInstance().getCurrentActivity());
        return isNetConnect();
    }

    protected boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }
}
