package com.yyjlr.tickets.adapter;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.content.collect.FollowFilmContent;
import com.yyjlr.tickets.model.film.MovieInfo;
import com.yyjlr.tickets.viewutils.SlidingButtonView;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 关注影片Adapter
 */
public class FollowFilmAdapter extends BaseAdapter<MovieInfo> implements SlidingButtonView.SlidingButtonListener {

    private SlidingButtonView mMenu = null;
    private SlidingViewClickListener mIDeleteBtnClickListener;

    List<MovieInfo> data;

    public FollowFilmAdapter(List<MovieInfo> data, FollowFilmContent context) {
        this(data);
        this.data = data;
        mIDeleteBtnClickListener = (SlidingViewClickListener) context;
    }

    public FollowFilmAdapter(List<MovieInfo> data) {
        super(R.layout.item_follow_film, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MovieInfo item, int position) {
        AppManager.getInstance().initWidthHeight(helper.getConvertView().getContext());
        helper.setText(R.id.item_follow_film__name, item.getMovieName())
                .setText(R.id.item_follow_film__director, item.getDirector())
                .setText(R.id.item_follow_film__star, item.getActors())
                .setText(R.id.item_follow_film__showtime, item.getShowTime())
                .setOnClickListener(R.id.item_follow_film__buy_ticket, new OnItemChildClickListener());

        if (item.getMovieImage() != null && !"".equals(item.getMovieImage())) {
            Picasso.with(helper.getConvertView().getContext())
                    .load(item.getMovieImage())
                    .into((ImageView) helper.getView(R.id.item_follow_film__image));
        }

        helper.getView(R.id.item_follow_film__rl_layout).getLayoutParams().width = AppManager.getInstance().getWidth();

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        helper.getView(R.id.item_follow_film__ll_layout).measure(w, h);
        int height = helper.getView(R.id.item_follow_film__ll_layout).getMeasuredHeight();
        helper.getView(R.id.item_order__delete).getLayoutParams().height = height;

        helper.setOnClickListener(R.id.item_follow_film__ll_layout, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = helper.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(view, n);
                }
            }
        });


        helper.setOnClickListener(R.id.item_order__delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = helper.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(view, n);
            }
        });

        SlidingButtonView slidingButtonView = helper.getView(R.id.item_follow_film__button_view);
        slidingButtonView.setSlidingButtonListener(FollowFilmAdapter.this);


    }

    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }

    public interface SlidingViewClickListener {
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }

}
