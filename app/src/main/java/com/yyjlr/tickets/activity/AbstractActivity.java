package com.yyjlr.tickets.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.yyjlr.tickets.Application;

/**
 * Created by Elvira on 2016/7/28.
 */
public class AbstractActivity extends AppCompatActivity {


    protected int TOTAL_COUNTER = 20;

    protected static final int PAGE_SIZE = 10;

    protected int delayMillis = 1000;

    protected int mCurrentCounter = 0;

    protected View notLoadingView;

    protected InputMethodManager imm;

    protected boolean flag = true;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.getInstance().setCurrentActivity(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Application.getInstance().setCurrentActivity(this);
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
     * */
    protected void startActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    /**
     * 跳转页面
     * */
    protected void startActivity(Class activity,String name,Bundle bundle){
        Intent intent = new Intent(this,activity);
        intent.putExtra(name,bundle);
        startActivity(intent);
    }
    /**
     * 跳转页面
     * */
    protected void startActivity(Class activity,String name,String value){
        Intent intent = new Intent(this,activity);
        intent.putExtra(name,value);
        startActivity(intent);
    }
}
