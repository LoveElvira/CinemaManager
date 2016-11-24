package com.yyjlr.tickets.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;

/**
 * Created by Elvira on 2016/9/23.
 */

public class PackageDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private LinearLayout collectPackage;
    private LinearLayout sharePackage;
    private ImageView collectImage;
    private TextView collectText;
    private TextView buyPackage;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        initView();

    }
    private void initView(){
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("活动套餐");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        collectPackage = (LinearLayout) findViewById(R.id.content_package_details__collect);
        sharePackage = (LinearLayout) findViewById(R.id.content_package_details__share);
        collectImage = (ImageView) findViewById(R.id.content_package_details__collect_image);
        collectText = (TextView) findViewById(R.id.content_package_details__collect_text);
        buyPackage = (TextView) findViewById(R.id.content_package_details__buy);
        collectPackage.setOnClickListener(this);
        sharePackage.setOnClickListener(this);
        buyPackage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int num = 0;
        if (saleNum != null) {
            num = Integer.parseInt(saleNum.getText().toString());
        }
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                PackageDetailsActivity.this.finish();
                break;
            case R.id.content_package_details__collect:
//                Drawable drawable = null;
                if (flag) {
//                    drawable = getResources().getDrawable(R.mipmap.collect_select);
                    collectImage.setImageResource(R.mipmap.collect_select);
                    collectText.setText("已收藏");
                }else {
//                    drawable = getResources().getDrawable(R.mipmap.collect);
                    collectImage.setImageResource(R.mipmap.collect);
                    collectText.setText("收藏");
                }
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                collectPackage.setCompoundDrawables(null, drawable, null, null);
                flag = !flag;
                break;
            case R.id.content_package_details__share:
                sharePopupWindow();
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

    //弹出分享选择
    private void sharePopupWindow() {

        View parent = View
                .inflate(PackageDetailsActivity.this, R.layout.activity_event, null);
        View view = View
                .inflate(PackageDetailsActivity.this, R.layout.popupwindows_share, null);
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
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
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
