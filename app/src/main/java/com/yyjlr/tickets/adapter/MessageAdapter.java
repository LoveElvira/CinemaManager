package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.model.MessageEntity;
import com.yyjlr.tickets.model.message.MyMessageInfo;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Elvira on 2016/8/3.
 * 消息Adapter
 */
public class MessageAdapter extends BaseAdapter<MyMessageInfo> {

    public MessageAdapter(List<MyMessageInfo> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMessageInfo item, int position) {
        helper.setText(R.id.item_message__title, item.getTitle())
                .setVisible(R.id.item_message__no_read, false)
                .setVisible(R.id.item_message__select, false)
                .setVisible(R.id.item_message__time_layout, true)
                .setText(R.id.item_message__time, ChangeUtils.changeTime(item.getSendDate()))
                .setOnClickListener(R.id.item_message__layout, new OnItemChildClickListener());
        if (item.getIsRead().equals("0")) {
            helper.setVisible(R.id.item_message__no_read, true);
        }

        if (item.isDelete() == true) {//TRUE 显示按钮
            helper.setVisible(R.id.item_message__select, true)
                    .setVisible(R.id.item_message__time_layout, false)
                    .setImageResource(R.id.item_message__select, R.mipmap.sale_no_select);

            if (item.getIsSelect() == 1) {
                helper.setImageResource(R.id.item_message__select, R.mipmap.sale_select);
            }
        }
    }

}
