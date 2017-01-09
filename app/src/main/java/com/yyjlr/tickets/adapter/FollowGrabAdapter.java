package com.yyjlr.tickets.adapter;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.content.collect.FollowEventContent;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.event.FollowEventInfo;
import com.yyjlr.tickets.viewutils.SlidingButtonView;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 关注抢票Adapter
 */
public class FollowGrabAdapter extends BaseAdapter<FollowEventInfo> implements SlidingButtonView.SlidingButtonListener {

    private SlidingButtonView mMenu = null;
    private SlidingViewClickListener mIDeleteBtnClickListener;

    List<FollowEventInfo> data;

    public FollowGrabAdapter(List<FollowEventInfo> data, FollowEventContent context) {
        this(data);
        this.data = data;
        mIDeleteBtnClickListener = (SlidingViewClickListener) context;
    }

    public FollowGrabAdapter(List<FollowEventInfo> data) {
        super(R.layout.item_follow_grab, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, FollowEventInfo item, int position) {
        AppManager.getInstance().initWidthHeight(helper.getConvertView().getContext());
        helper.setText(R.id.item_follow_grab__name, item.getActivityName())
                .setText(R.id.item_follow_grab__time, ChangeUtils.changeTimeDate(item.getStartTime()) + "~" + ChangeUtils.changeTimeDate(item.getEndTime()))
                .setText(R.id.item_follow_grab__address, item.getAddress())
                .setText(R.id.item_follow_grab__price, item.getPrice())
                .setOnClickListener(R.id.item_follow_grab__enter, new OnItemChildClickListener());

        if (item.getActivityImg() != null && !"".equals(item.getActivityImg())) {
            Picasso.with(helper.getConvertView().getContext())
                    .load(item.getActivityImg())
                    .into((ImageView) helper.getView(R.id.item_follow_grab__image));
        }

        helper.getView(R.id.item_follow_grab__rl_layout).getLayoutParams().width = AppManager.getInstance().getWidth();

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        helper.getView(R.id.item_follow_grab__ll_layout).measure(w, h);
        int height = helper.getView(R.id.item_follow_grab__ll_layout).getMeasuredHeight();
        helper.getView(R.id.item_order__delete).getLayoutParams().height = height;

        helper.setOnClickListener(R.id.item_follow_grab__ll_layout, new View.OnClickListener() {
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

        SlidingButtonView slidingButtonView = helper.getView(R.id.item_follow_grab__button_view);
        slidingButtonView.setSlidingButtonListener(FollowGrabAdapter.this);


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
