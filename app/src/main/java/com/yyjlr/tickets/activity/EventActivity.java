package com.yyjlr.tickets.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.setting.AccountNameActivity;
import com.yyjlr.tickets.activity.setting.SettingAccountActivity;
import com.yyjlr.tickets.content.MySettingContent;

/**
 * Created by Elvira on 2016/8/3.
 * 活动 明星见面会
 */
public class EventActivity extends AbstractActivity implements View.OnClickListener/* implements View.OnClickListener*/ {

    private TextView title;
    private ImageView leftArrow;
    private ImageView rightImage;
    private TextView rightPhoto;

    private NestedScrollView nestedScrollView;
    private View mContentView;
    private CardView mCardView;

    private LinearLayout shareLayout;
    private LinearLayout collectLayout;
    private ImageView collectImage;
    private TextView collectText;
    private TextView join;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initView();
    }

    private void initView(){

//        AppManager.getInstance().initWidthHeight(this);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        rightImage = (ImageView) findViewById(R.id.base_toolbar__right);
        rightImage.setVisibility(View.GONE);
        rightPhoto = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightPhoto.setVisibility(View.VISIBLE);
        rightPhoto.setText("相册");
        rightPhoto.setOnClickListener(this);
//        initWidget();
        title.setText("明星见面会");
        collectLayout = (LinearLayout) findViewById(R.id.content_event__collect);
        shareLayout = (LinearLayout) findViewById(R.id.content_event__share);
        collectImage = (ImageView) findViewById(R.id.content_event__collect_image);
        collectText = (TextView) findViewById(R.id.content_event__collect_text);
        join = (TextView) findViewById(R.id.content_event__join);
        collectLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        join.setOnClickListener(this);
    }


    private void initWidget() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.content_event__nested_scroll_view);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                        , RelativeLayout.LayoutParams.MATCH_PARENT);
        nestedScrollView.setLayoutParams(params);
        mContentView = findViewById(R.id.content_event__relayout);
        mContentView.setPadding(0, AppManager.getInstance().getHeight() / 3, 0, mContentView.getPaddingBottom() * 2);
        mCardView = (CardView) findViewById(R.id.content_event__cardview);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                        , AppManager.getInstance().getWidth() * 105 / 100);
        layoutParams.setMargins(AppManager.getInstance().getWidth() * 5 / 100, AppManager.getInstance().getHeight() / 4
                , AppManager.getInstance().getWidth() * 5 / 100, 0);
        mCardView.setLayoutParams(layoutParams);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                EventActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text://展示背景图片信息
                break;
            case R.id.content_event__collect:
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
//                collect.setCompoundDrawables(null, drawable, null, null);
                flag = !flag;
                break;
            case R.id.content_event__share:
                sharePopupWindow();
                break;
            case R.id.content_event__join:
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

    PopupWindow mPopupWindow;

    //弹出分享选择
    private void sharePopupWindow() {

        View parent = View
                .inflate(EventActivity.this, R.layout.activity_event, null);
        View view = View
                .inflate(EventActivity.this, R.layout.popupwindows_share, null);
        view.startAnimation(AnimationUtils.loadAnimation(EventActivity.this,
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
