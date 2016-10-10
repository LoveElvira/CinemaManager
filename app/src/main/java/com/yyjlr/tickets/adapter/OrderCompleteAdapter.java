package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.OrderEntity;
import com.yyjlr.tickets.viewutils.SlidingButtonView;

import java.util.List;

/**
 * Created by Elvira on 2016/8/11.
 * 已完成的订单
 */
public class OrderCompleteAdapter extends BaseAdapter<OrderEntity> implements SlidingButtonView.SlidingButtonListener {

    private SlidingButtonView mMenu = null;
    private SlidingViewClickListener mIDeleteBtnClickListener;

    List<OrderEntity> data;

    public OrderCompleteAdapter(List<OrderEntity> data, Context context) {
        this(data);
        this.data = data;
        mIDeleteBtnClickListener = (SlidingViewClickListener) context;
    }

    public OrderCompleteAdapter(List<OrderEntity> data) {
        super(R.layout.item_order_complete, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, OrderEntity item, int position) {
        AppManager.getInstance().initWidthHeight(helper.getConvertView().getContext());
        helper.setText(R.id.item_order_complete__order_num, item.getOrderNum())
                .setText(R.id.item_order_complete__order_film, item.getOrderFilmName())
                .setText(R.id.item_order_complete__package, item.getOrderPackage())
                .setOnClickListener(R.id.item_order_complete__rl_layout, new OnItemChildClickListener());
        helper.getView(R.id.item_order_complete__rl_layout).getLayoutParams().width = AppManager.getInstance().getWidth();

        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        helper.getView(R.id.item_order_complete__ll_layout).measure(w, h);
        int height = helper.getView(R.id.item_order_complete__ll_layout).getMeasuredHeight();
        helper.getView(R.id.item_order__delete).getLayoutParams().height = height;

        helper.setOnClickListener(R.id.item_order_complete__ll_layout, new View.OnClickListener() {
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

        SlidingButtonView slidingButtonView = helper.getView(R.id.item_order_complete__button_view);
        slidingButtonView.setSlidingButtonListener(OrderCompleteAdapter.this);
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
