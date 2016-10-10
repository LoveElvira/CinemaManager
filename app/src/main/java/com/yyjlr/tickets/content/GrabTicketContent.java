package com.yyjlr.tickets.content;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.GrabTicketAdapter;
import com.yyjlr.tickets.viewutils.TicketFrameLayout;

/**
 * Created by Elvira on 2016/7/28.
 * 抢票页面
 */
public class GrabTicketContent extends LinearLayout {
    private View view;
    private TicketFrameLayout ticketFrameLayout;
    public static GrabTicketAdapter adapter;

    private TextView title;

    public GrabTicketContent(Context context) {
        this(context,null);
    }
    public GrabTicketContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_grabticket, this);

        title = (TextView) view.findViewById(R.id.base_toolbar__text);
        title.setText(getResources().getText(R.string.text_grab_title));

        ticketFrameLayout = (TicketFrameLayout) view.findViewById(R.id.fragment_grab__layout);
        TextView tMoreInfo = new TextView(context);
        tMoreInfo.setText("no more information..");
        ticketFrameLayout.addBottomContent(tMoreInfo);
        ticketFrameLayout.setAdapter(adapter = new GrabTicketAdapter());
        adapter.set();
    }

}
