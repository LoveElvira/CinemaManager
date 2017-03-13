package com.yyjlr.tickets.viewutils.grabticket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Constant;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.activity.LoginActivity;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.helputils.SharePrefUtil;
import com.yyjlr.tickets.model.DataInfo;
import com.yyjlr.tickets.model.ticket.TicketModel;
import com.yyjlr.tickets.viewutils.countdown.CountdownView;

import java.util.Calendar;

import static com.yyjlr.tickets.Application.getInstance;

/**
 * Created by Elvira on 2016/9/12.
 * 抢票item内容
 */
public class ItemTicketLayout extends RelativeLayout implements View.OnClickListener, TicketFrameLayout.OnScrollChangedListener {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    private float currPercent;//透明度
    private int maxHeight;//最大高度
    private int minHeight;//最小高度
    private int pos;//下坐标
    private TicketModel d;//数据

    private ImageView itemBackground;//背景图片
    private ImageView itemShadow;
    private LinearLayout itemLayout;//文字区域的layout
    private TextView itemTitle;//标题
    private CountdownView itemTime;//抢购时间
    private TextView itemDate;//举办日期
    private TextView itemPrice;//优惠后价格

    public ItemTicketLayout(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.item_ticket, this);
        itemBackground = (ImageView) findViewById(R.id.item_ticket__background);
        itemShadow = (ImageView) findViewById(R.id.item_ticket__shadow);
        itemLayout = (LinearLayout) findViewById(R.id.item_ticket__layout);
        itemTime = (CountdownView) findViewById(R.id.item_ticket__time);
        itemTitle = (TextView) findViewById(R.id.item_ticket__title);
        itemDate = (TextView) findViewById(R.id.item_ticket__date);
        itemPrice = (TextView) findViewById(R.id.item_ticket__price);
        //监听点击事件
        setOnClickListener(this);
    }

    public void setData(int position, TicketModel data, int childExpandedHeight, int childNormalHeight) {
        lastClickTime = 0;
        d = data;
        pos = position;
        maxHeight = childExpandedHeight;
        minHeight = childNormalHeight;

        LayoutParams lpContainer = new LayoutParams(LayoutParams.MATCH_PARENT, pos == 0 ? maxHeight : minHeight);
        lpContainer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        setLayoutParams(lpContainer);


        if (pos == 0) {

//            itemTime.setAlpha(1f);
            itemDate.setAlpha(1f);
            itemPrice.setAlpha(1f);
            itemShadow.setAlpha(0f);

        } else {

//            itemTime.setAlpha(0f);
            itemDate.setAlpha(0f);
            itemPrice.setAlpha(0f);
            itemShadow.setAlpha(0.5f);

        }

        itemShadow.getLayoutParams().height = maxHeight;
        itemLayout.getLayoutParams().height = maxHeight;
        itemBackground.getLayoutParams().height = maxHeight;

//        if ((d.getEndTime() - d.getStartTime()) > 0) {
//            refreshTime(System.currentTimeMillis());
//        } else {
////            itemTime.allShowZero();
//            endTime(0);
//        }
        itemTitle.setText(data.getActivityName());
        itemDate.setText(ChangeUtils.changeTimeDate(d.getStartTime()) + "-" + ChangeUtils.changeTimeDate(d.getEndTime()));
        itemPrice.setText("人均" + ChangeUtils.save2Decimal(d.getPrice()) + "元");
        Picasso.with(getContext())
                .load(data.getActivityImg())
                .resize(getWidth(), maxHeight)
                .placeholder(R.mipmap.bg)
                .into(itemBackground);

    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
//        Toast.makeText(v.getContext().getApplicationContext(), "" + (d != null ? d.title : "null"), Toast.LENGTH_SHORT).
//                show();
            String isLogin = SharePrefUtil.getString(Constant.FILE_NAME, "flag", "", Application.getInstance().getCurrentActivity());
            Intent intent = new Intent();
            if (!isLogin.equals("1")) {
                intent.setClass(v.getContext(), LoginActivity.class);
            } else {
                intent.setClass(v.getContext(), EventActivity.class);
                intent.putExtra("eventId", d.getActivityId());
            }
            v.getContext().startActivity(intent);
        }
    }


    @Override
    public void onScroll(int currIndex, float percent) {
        if (pos == currIndex + 1) {
            animate(currPercent = percent);
        } else if (pos > currIndex + 1 && currPercent > 0) {
            animate(currPercent = 0);
        } else if (pos < currIndex + 1 && currPercent < 1) {
            animate(currPercent = 1);
        }
    }

    //动画 修改透明度 显示大小
    private void animate(float percent) {
        Log.i("ee", "-----------percent:" + percent);
        if (percent == 1f) {
            itemShadow.setAlpha(0f);
        } else if (percent == 0f) {
            itemShadow.setAlpha(0.5f);
        } else {
            if (percent > 0.5f)
                itemShadow.setAlpha(1 - percent);
        }
//        itemTime.setAlpha(percent);
        itemPrice.setAlpha(percent);
        itemDate.setAlpha(percent);

        getLayoutParams().height = (int) (minHeight + (maxHeight - minHeight) * percent);
        requestLayout();
    }

//    public void refreshTime(long curTimeMillis) {
//        if (null == d || (d.getEndTime() - d.getStartTime()) <= 0) return;
//        itemTime.updateShow(d.getEndTime() - curTimeMillis);
//    }
//
//    public void endTime(long curTimeMillis) {
//        itemTime.updateShow(curTimeMillis);
//    }

    public TicketModel getBean() {
        return d;
    }
}
