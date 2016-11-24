package com.yyjlr.tickets.content;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.GrabTicketAdapter;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;
import com.yyjlr.tickets.viewutils.countdown.CountdownView;
import com.yyjlr.tickets.viewutils.grabticket.TicketFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Elvira on 2016/7/28.
 * 抢票页面
 */
public class GrabTicketContent extends LinearLayout implements SuperSwipeRefreshLayout.OnPullRefreshListener {
    private View view;
    private TicketFrameLayout ticketFrameLayout;
    public static GrabTicketAdapter adapter;

    private TextView title;

    private SuperSwipeRefreshLayout refresh;//刷新
    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    public GrabTicketContent(Context context) {
        this(context,null);
    }
    public GrabTicketContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_grabticket, this);

        title = (TextView) view.findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_grab_title));

//        refresh = (SuperSwipeRefreshLayout) view.findViewById(R.id.fragment_grab__refresh);
//        refresh.setHeaderView(createHeaderView());// add headerView
//        refresh.setTargetScrollWithLayout(true);
//        refresh.setOnPullRefreshListener(this);
//        refresh.setTargetScrollWithLayout(false);

        ticketFrameLayout = (TicketFrameLayout) view.findViewById(R.id.fragment_grab__layout);
        View bottomView = inflate(getContext(),R.layout.item_ticket,null);
        RelativeLayout parentView = (RelativeLayout) bottomView.findViewById(R.id.item_ticket__parent_layout);
        TextView bottomTitle = (TextView) bottomView.findViewById(R.id.item_ticket__title);
        TextView bottomDate = (TextView) bottomView.findViewById(R.id.item_ticket__date);
        TextView bottomPrice = (TextView) bottomView.findViewById(R.id.item_ticket__price);
        bottomDate.setVisibility(GONE);
        bottomPrice.setVisibility(GONE);
        bottomTitle.setText("更多产品，敬请期待");
        CountdownView bottomTime = (CountdownView) bottomView.findViewById(R.id.item_ticket__time);
        bottomTime.setVisibility(GONE);
        ImageView bottomImageShadow = (ImageView) bottomView.findViewById(R.id.item_ticket__shadow);
        bottomImageShadow.setAlpha(0.5f);
        ImageView bottomImage = (ImageView) bottomView.findViewById(R.id.item_ticket__background);
        bottomImage.setImageResource(R.mipmap.bg);
//        TextView tMoreInfo = new TextView(context);
//        tMoreInfo.setWidth(MATCH_PARENT);
//        tMoreInfo.setHeight(MATCH_PARENT);
//        tMoreInfo.setBackgroundResource(R.mipmap.bg);
//        tMoreInfo.setGravity(View.TEXT_ALIGNMENT_CENTER);
//        tMoreInfo.setTextColor(getResources().getColor(R.color.white));
//        tMoreInfo.setTextSize(18f);
//        tMoreInfo.setText("更多产品，敬请期待...");
        ticketFrameLayout.addBottomContent(parentView);
        ticketFrameLayout.setAdapter(adapter = new GrabTicketAdapter());
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.header_loading, null);
        headerProgressBar = (ProgressBar) headerView.findViewById(R.id.header_loading_progress);
        headerSta = (TextView) headerView.findViewById(R.id.header_loading_text);
//        headerTime = (TextView) headerView.findViewById(R.id.header_loading_time);
        headerSta.setText("下拉刷新");
        headerImage = (ImageView) headerView.findViewById(R.id.header_loading_image);
        headerImage.setVisibility(View.VISIBLE);
        headerProgressBar.setVisibility(View.GONE);
        return headerView;
    }

    @Override
    public void onRefresh() {
        headerSta.setText("正在刷新数据中");
        headerImage.setVisibility(View.GONE);
        headerProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.set();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                Date curDate = new Date(System.currentTimeMillis());
//                headerTime.setText("最后更新：今天"+formatter.format(curDate));
                refresh.setRefreshing(false);
                headerProgressBar.setVisibility(View.GONE);
            }
        }, 1000);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {
        headerSta.setText(enable ? "松开立即刷新" : "下拉刷新");
        headerImage.setVisibility(View.VISIBLE);
        headerImage.setRotation(enable ? 180 : 0);
    }
}
