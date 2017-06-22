package com.yyjlr.tickets.adapter;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.film.FilmComment;
import com.yyjlr.tickets.viewutils.CircleImageView;

import java.util.List;

/**
 * Created by Elvira on 2017/5/9.
 */

public class FilmCommentAdapter extends BaseAdapter<FilmComment> {

    public FilmCommentAdapter(List<FilmComment> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilmComment item, int position) {
        helper.setText(R.id.item_comment__user_name, item.getNickname())
                .setText(R.id.item_comment__user_word, item.getContent())
                .setText(R.id.item_comment__send_time, ChangeUtils.changeTimeFormat(item.getComTime()));
        if (!"".equals(item.getHeadImg()) && item.getHeadImg() != null) {
            Picasso.with(helper.getConvertView().getContext())
                    .load(item.getHeadImg())
                    .into((CircleImageView) helper.getView(R.id.item_comment__head_image));
        }
    }
}
