package com.yyjlr.tickets.content;

import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.GrabTicketAdapter;
import com.yyjlr.tickets.model.ticket.GrabTicketModel;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;
import com.yyjlr.tickets.viewutils.countdown.CountdownView;
import com.yyjlr.tickets.viewutils.grabticket.TicketFrameLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Elvira on 2016/7/28.
 * 抢票页面  废弃
 */
public class GrabTicketContent extends BaseLinearLayout {
    private TicketFrameLayout ticketFrameLayout;
    public GrabTicketAdapter adapter = null;

    private TextView title;
    private RelativeLayout parentView;
    private LinearLayout noDate;


    public GrabTicketContent(Context context) {
        this(context, null);
    }

    public GrabTicketContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_grabticket, this);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_grab_title));
        noDate = (LinearLayout) findViewById(R.id.fragment_grab__no_date);
        View bottomView = inflate(getContext(), R.layout.item_ticket, null);
        parentView = (RelativeLayout) bottomView.findViewById(R.id.item_ticket__parent_layout);
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
        ImageView bottomImage = (ImageView) bottomView.findViewById(R.id.item_ticket__no_date);
        bottomImage.setImageResource(R.mipmap.ticket_no_date);

        ticketFrameLayout = (TicketFrameLayout) findViewById(R.id.fragment_grab__layout);
    }

    public void updateView() {
        noDate.setVisibility(GONE);
        ticketFrameLayout.setVisibility(VISIBLE);
        getTicket();
    }

    public void updateAdapter() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

//    public void hideInput() {
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
////            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//            imm.hideSoftInputFromWindow(title.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }

    //获取首页抢票
    private void getTicket() {
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_TICKET, new OkHttpClientManager.ResultCallback<GrabTicketModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo().toString());
            }

            @Override
            public void onResponse(GrabTicketModel response) {
                Log.i("ee", "--" + new Gson().toJson(response));
                if (response != null) {
                    if (response.getActivityList() != null && response.getActivityList().size() > 0) {
                        ticketFrameLayout.removeAllViews();
                        adapter = new GrabTicketAdapter(response.getActivityList());
                        ticketFrameLayout.setAdapter(adapter);
                        ticketFrameLayout.addBottomContent(parentView);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = null;
                        noDate.setVisibility(VISIBLE);
                        ticketFrameLayout.setVisibility(GONE);
                        ticketFrameLayout.removeAllViews();
                    }
                } else {
                    adapter = null;
                    noDate.setVisibility(VISIBLE);
                    ticketFrameLayout.setVisibility(GONE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                Toast.makeText(getContext(), exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }, requestNull, GrabTicketModel.class, Application.getInstance().getCurrentActivity());
    }

}
