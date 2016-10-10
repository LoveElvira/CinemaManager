package com.yyjlr.tickets.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;

/**
 * Created by Elvira on 2016/9/23.
 */

public class PackageDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private TextView collectPackage;
    private TextView sharePackage;
    private TextView buyPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("《某某某》活动套餐");

        collectPackage = (TextView) findViewById(R.id.content_package_details__collect);
        sharePackage = (TextView) findViewById(R.id.content_package_details__share);
        buyPackage = (TextView) findViewById(R.id.content_package_details__buy);
        collectPackage.setOnClickListener(this);
        sharePackage.setOnClickListener(this);
        buyPackage.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PackageDetailsActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int num = 0;
        if (saleNum != null) {
            num = Integer.parseInt(saleNum.getText().toString());
        }
        switch (v.getId()) {
            case R.id.content_package_details__collect:
                break;
            case R.id.content_package_details__share:
                break;
            case R.id.content_package_details__buy:
                selectPopupWindow();
                break;
            case R.id.popup_sale__buy://购买
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), SaleCompleteActivity.class));
                mPopupWindow.dismiss();
                break;
            case R.id.popup_sale__lost://减少数量
                if (num != 0)
                    num = num - 1;
                saleNum.setText(num + "");
                break;
            case R.id.popup_sale__add://增加数量
                saleNum.setText((num + 1) + "");
                break;
        }
    }

    TextView price;
    TextView saleNum;
    PopupWindow mPopupWindow;

    //弹出popwindow 选择数量
    private void selectPopupWindow() {

        View parent = View
                .inflate(PackageDetailsActivity.this, R.layout.fragment_sale, null);
        View view = View
                .inflate(PackageDetailsActivity.this, R.layout.popupwindows_sale, null);
        view.startAnimation(AnimationUtils.loadAnimation(PackageDetailsActivity.this,
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
        lp.alpha = 0.5f;
        Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);

        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });


        TextView buy = (TextView) view.findViewById(R.id.popup_sale__buy);
        TextView lost = (TextView) view.findViewById(R.id.popup_sale__lost);
        TextView add = (TextView) view.findViewById(R.id.popup_sale__add);
        TextView salePackage = (TextView) view.findViewById(R.id.popup_sale__package);
        TextView salePackageContent = (TextView) view.findViewById(R.id.popup_sale__package_content);
        saleNum = (TextView) view.findViewById(R.id.popup_sale__num);
        price = (TextView) view.findViewById(R.id.popup_sale_price);

        buy.setOnClickListener(this);
        lost.setOnClickListener(this);
        add.setOnClickListener(this);

    }


}
