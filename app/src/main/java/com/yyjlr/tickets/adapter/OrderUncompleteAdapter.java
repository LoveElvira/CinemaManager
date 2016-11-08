package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.OrderEntity;
import com.yyjlr.tickets.service.OnRecyclerViewItemClickListener;
import com.yyjlr.tickets.viewutils.SlidingButtonView;

import java.util.List;

/**
 * Created by Elvira on 2016/8/11.
 * 未完成的订单
 */
public class OrderUncompleteAdapter extends BaseAdapter<OrderEntity> implements SlidingButtonView.SlidingButtonListener {

    private SlidingButtonView mMenu = null;
    private SlidingViewClickListener mIDeleteBtnClickListener;

    List<OrderEntity> data;

    public OrderUncompleteAdapter(List<OrderEntity> data, Context context) {
        this(data);
        this.data = data;
        mIDeleteBtnClickListener = (SlidingViewClickListener) context;
    }

    public OrderUncompleteAdapter(List<OrderEntity> data) {
        super(R.layout.item_order_nocomplete, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, OrderEntity item, int position) {
        helper.setText(R.id.item_order_nocomplete__order_num, item.getOrderNum())
                .setText(R.id.item_order_nocomplete__order_film, item.getOrderFilmName())
                .setText(R.id.item_order_nocomplete__package, item.getOrderPackage())
                .setVisible(R.id.item_order_nocomplete__pay, true)
                .setVisible(R.id.item_order_nocomplete__lost, true)
                .setVisible(R.id.item_order_nocomplete__cancel, true)
                .setOnClickListener(R.id.item_order_nocomplete__cancel, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_order_nocomplete__pay, new OnItemChildClickListener());
        if ("1".equals(item.getOrderComplete())) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
        } else {
            helper.setVisible(R.id.item_order_nocomplete__lost, false);
        }

        helper.getView(R.id.item_order_nocomplete__rl_layout).getLayoutParams().width = AppManager.getInstance().getWidth();

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        helper.getView(R.id.item_order_nocomplete__ll_layout).measure(w, h);
        int height = helper.getView(R.id.item_order_nocomplete__ll_layout).getMeasuredHeight();
        helper.getView(R.id.item_order__delete).getLayoutParams().height = height;

        helper.setOnClickListener(R.id.item_order_nocomplete__ll_layout, new View.OnClickListener() {
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

        SlidingButtonView slidingButtonView = helper.getView(R.id.item_order_nocomplete__button_view);
        slidingButtonView.setSlidingButtonListener(OrderUncompleteAdapter.this);

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
