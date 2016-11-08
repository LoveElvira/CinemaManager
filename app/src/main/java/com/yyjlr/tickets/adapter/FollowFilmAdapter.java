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

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.FilmEntity;
import com.yyjlr.tickets.model.OrderEntity;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;
import com.yyjlr.tickets.viewutils.SlidingButtonView;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 关注影片Adapter
 */
public class FollowFilmAdapter extends BaseAdapter<FilmEntity> implements SlidingButtonView.SlidingButtonListener {

    private SlidingButtonView mMenu = null;
    private SlidingViewClickListener mIDeleteBtnClickListener;

    List<FilmEntity> data;

    public FollowFilmAdapter(List<FilmEntity> data, Context context) {
        this(data);
        this.data = data;
        mIDeleteBtnClickListener = (SlidingViewClickListener) context;
    }

    public FollowFilmAdapter(List<FilmEntity> data) {
        super(R.layout.item_follow_film, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, FilmEntity item, int position) {
        AppManager.getInstance().initWidthHeight(helper.getConvertView().getContext());
        helper.setImageResource(R.id.item_follow_film__image, R.mipmap.bird)
                .setText(R.id.item_follow_film__name, item.getFilmName())
                .setText(R.id.item_follow_film__director, item.getFilmDirector())
                .setText(R.id.item_follow_film__star, item.getFilmStar())
                .setText(R.id.item_follow_film__showtime, item.getFilmShowTime())
                .setOnClickListener(R.id.item_follow_film__buy_ticket, new OnItemChildClickListener());


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
                }else {
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
