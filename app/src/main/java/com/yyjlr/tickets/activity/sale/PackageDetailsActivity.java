package com.yyjlr.tickets.activity.sale;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.sale.PackageDetails;
import com.yyjlr.tickets.model.sale.PackageInfo;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import static com.yyjlr.tickets.Application.getInstance;

/**
 * Created by Elvira on 2016/9/23.
 * 套餐详情
 */

public class PackageDetailsActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;

    private TextView packageName;
    private TextView packageContent;
    private TextView packageDesc;
    private TextView packageUnitPrice;
    private TextView packagePrice;
    private ImageView packageImage;

    private LinearLayout collectPackage;
    private LinearLayout sharePackage;
    private ImageView collectImage;
    private TextView collectText;
    private TextView buyPackage;
    private boolean flag = true;
    private PackageDetails packageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("活动套餐");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);

        packageName = (TextView) findViewById(R.id.content_package_details__name);
        packageContent = (TextView) findViewById(R.id.content_package_details__content);
        packageDesc = (TextView) findViewById(R.id.content_package_details__desc);
        packageUnitPrice = (TextView) findViewById(R.id.content_package_details__original_price);
        packagePrice = (TextView) findViewById(R.id.content_package_details__package_price);
        packageImage = (ImageView) findViewById(R.id.content_package_details__image);

        collectPackage = (LinearLayout) findViewById(R.id.content_package_details__collect);
        sharePackage = (LinearLayout) findViewById(R.id.content_package_details__share);
        collectImage = (ImageView) findViewById(R.id.content_package_details__collect_image);
        collectText = (TextView) findViewById(R.id.content_package_details__collect_text);
        buyPackage = (TextView) findViewById(R.id.content_package_details__buy);

        collectPackage.setOnClickListener(this);
        sharePackage.setOnClickListener(this);
        buyPackage.setOnClickListener(this);
        getPackageDetail();
    }

    //获取活动套餐
    private void getPackageDetail() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        IdRequest idRequest = new IdRequest();
        idRequest.setId(getIntent().getStringExtra("packageId"));
        OkHttpClientManager.postAsyn(Config.GET_SALE_GOOD_INFO, new OkHttpClientManager.ResultCallback<PackageInfo>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(PackageInfo response) {
                customDialog.dismiss();
                packageDetails = response.getGoodsInfo();
                if (packageDetails != null) {
                    packageName.setText(packageDetails.getName());
                    packageContent.setText(packageDetails.getDetail());
                    packageDesc.setText(packageDetails.getDesc());
                    packageUnitPrice.setText("原价：" + ChangeUtils.save2Decimal(packageDetails.getCostPrice()) + " 元");
                    packagePrice.setText(ChangeUtils.save2Decimal(packageDetails.getPrice()));

                    if (packageDetails.getImage() != null && !"".equals(packageDetails.getImage())) {
                        Picasso.with(getBaseContext())
                                .load(packageDetails.getImage())
                                .into(packageImage);
                    }
                }

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, idRequest, PackageInfo.class, PackageDetailsActivity.this);
    }

    @Override
    public void onClick(View v) {
        int num = 0;
        long price = 0;
        if (saleNum != null) {
            num = Integer.parseInt(saleNum.getText().toString());
            int dotIndex = salePrice.getText().toString().trim().indexOf(".");
            price = Long.parseLong(salePrice.getText().toString().trim().substring(0, dotIndex)) * 100;
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
                } else {
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
//                Intent intent = new Intent();
//                String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
//                if (!isLogin.equals("1")) {
//                    intent.setClass(getInstance().getCurrentActivity(), LoginActivity.class);
//                } else {
//                    intent.setClass(getInstance().getCurrentActivity(), SaleCompleteActivity.class);
//                    intent.putExtra("goodInfo", goodInfoLists.get(position));
//                    intent.putExtra("num", num);
//                }
//                Toast.makeText(getContext(), "购买功能正在开放中", Toast.LENGTH_SHORT).show();
//                Application.getInstance().getCurrentActivity().startActivity(intent);
                mPopupWindow.dismiss();
                break;
            case R.id.popup_sale__lost://减少数量
                if (num != 1) {
                    num -= 1;
                    price -= goodPrice;
                }
                saleNum.setText(num + "");
                salePrice.setText(ChangeUtils.save2Decimal(price));
                break;
            case R.id.popup_sale__add://增加数量
                saleNum.setText((num + 1) + "");
                salePrice.setText(ChangeUtils.save2Decimal(price + goodPrice));
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

    TextView salePrice;
    TextView saleNum;
    PopupWindow mPopupWindow;
    private long goodPrice;

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

        ImageView saleImage = (ImageView) view.findViewById(R.id.popup_sale__image);
        TextView buy = (TextView) view.findViewById(R.id.popup_sale__buy);
        TextView lost = (TextView) view.findViewById(R.id.popup_sale__lost);
        TextView add = (TextView) view.findViewById(R.id.popup_sale__add);
        TextView salePackage = (TextView) view.findViewById(R.id.popup_sale__package);
        TextView salePackageContent = (TextView) view.findViewById(R.id.popup_sale__package_content);
        saleNum = (TextView) view.findViewById(R.id.popup_sale__num);
        salePrice = (TextView) view.findViewById(R.id.popup_sale_price);

        salePackage.setText(packageDetails.getName());
        salePackageContent.setText(packageDetails.getDesc());
        if (packageDetails.getImage() != null && !"".equals(packageDetails.getImage())) {
            Picasso.with(getBaseContext())
                    .load(packageDetails.getImage())
                    .into(saleImage);
        }

        salePrice.setText(ChangeUtils.save2Decimal(packageDetails.getPrice()));
        goodPrice = packageDetails.getPrice();


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
