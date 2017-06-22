package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.FilmSeasonEntity;
import com.yyjlr.tickets.model.FilmTimeEntity;
import com.yyjlr.tickets.model.film.FilmPlanContent;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;

import java.io.File;
import java.util.List;

/**
 * Created by Elvira on 2016/8/2.
 * 影院场次
 */
public class FilmScheduleSeasonAdapter extends BaseAdapter<FilmPlanContent> {

    public FilmScheduleSeasonAdapter(List<FilmPlanContent> data) {
        super(R.layout.item_film_schedule_season, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilmPlanContent item, int position) {
        AssetManager assetManager = helper.getView(R.id.item_schedule__start_time).getContext().getAssets();
        Typeface font = Typeface.createFromAsset(assetManager, "fonts/Digital2.ttf");
        helper.setImageResource(R.id.item_schedule__sun_or_moon, R.mipmap.sun)
                .setText(R.id.item_schedule__start_time, item.getStartTime())
                .setTypeface(R.id.item_schedule__start_time, font)
                .setText(R.id.item_schedule__end_time, item.getEndTime() + "散场")
                .setText(R.id.item_schedule__language, item.getLanguage())
                .setText(R.id.item_schedule__type, item.getMovieType())
                .setText(R.id.item_schedule__hall, item.getHallName())
                .setText(R.id.item_schedule__hall_type, item.getHallType())
//                .setVisible(R.id.item_schedule__hall_type, true)
                .setText(R.id.item_schedule__original_price, "￥" + ChangeUtils.save2Decimal(Long.parseLong(item.getOrgPrice())))
                .setFlags(R.id.item_schedule__original_price, Paint.STRIKE_THRU_TEXT_FLAG)
                .setText(R.id.item_schedule__app_price, "￥" + ChangeUtils.save2Decimal(Long.parseLong(item.getAppPrice())))
                .setOnClickListener(R.id.item_schedule__buy_ticket, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_schedule__parent, new OnItemChildClickListener());
//        if (item.getHallType() == null) {
//            helper.setVisible(R.id.item_schedule__hall_type, false);
//        }

        if (item.getPlayTimeIconType() == 0) {
            helper.setImageResource(R.id.item_schedule__sun_or_moon, R.mipmap.moon);
        }
    }

}
