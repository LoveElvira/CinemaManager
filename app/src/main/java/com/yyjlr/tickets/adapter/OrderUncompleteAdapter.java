package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.view.View;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.content.order.UnCompleteOrderContent;
import com.yyjlr.tickets.model.order.MyOrderInfo;
import com.yyjlr.tickets.viewutils.SlidingButtonView;

import java.util.List;

/**
 * Created by Elvira on 2016/8/11.
 * 未完成的订单
 */
public class OrderUncompleteAdapter extends BaseAdapter<MyOrderInfo> implements SlidingButtonView.SlidingButtonListener {

    private SlidingButtonView mMenu = null;
    private SlidingViewClickListener mIDeleteBtnClickListener;

    List<MyOrderInfo> data;

    public OrderUncompleteAdapter(List<MyOrderInfo> data, UnCompleteOrderContent context) {
        this(data);
        this.data = data;
        mIDeleteBtnClickListener = (SlidingViewClickListener) context;
    }

    public OrderUncompleteAdapter(List<MyOrderInfo> data) {
        super(R.layout.item_order_nocomplete, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MyOrderInfo item, int position) {
        AppManager.getInstance().initWidthHeight(helper.getConvertView().getContext());

        helper.setText(R.id.item_order_nocomplete__order_num, item.getOrderNo())
                .setVisible(R.id.item_order_nocomplete__order_film_layout, false)
                .setVisible(R.id.item_order_nocomplete__package_layout, false)
                .setVisible(R.id.item_order_nocomplete__pay, true)
                .setVisible(R.id.item_order_nocomplete__lost, true)
                .setVisible(R.id.item_order_nocomplete__cancel, true)
                .setOnClickListener(R.id.item_order_nocomplete__cancel, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_order_nocomplete__pay, new OnItemChildClickListener());

        if (item.getMovieName() != null) {
            helper.setVisible(R.id.item_order_nocomplete__order_film_layout, true)
                    .setText(R.id.item_order_nocomplete__order_film, item.getMovieName());
        }

        String goodName = "";
        if (item.getGoodsName() != null) {
            for (int i = 0; i < item.getGoodsName().size(); i++) {
                if (i == item.getGoodsName().size() - 1) {
                    goodName = goodName + item.getGoodsName().get(i);
                } else {
                    goodName = goodName + item.getGoodsName().get(i) + "，";
                }
            }
        }
        if (!"".equals(goodName)) {
            helper.setVisible(R.id.item_order_nocomplete__package_layout, true)
                    .setText(R.id.item_order_nocomplete__package, goodName);
        }

        // 订单状态，1：待支付；2：待出票；3：已完成；4：用户取消；5：待退款；6：已退款；7：购买卖品失败；8：出票失败；9：超时失效
        if (item.getOrderStatus() == 1) {
            helper.setVisible(R.id.item_order_nocomplete__lost, false);
//            if (item.getOrderType() == 2 || item.getOrderType() == 3) {
//                helper.setVisible(R.id.item_order_nocomplete__cancel, false);
//            }
        } else if (item.getOrderStatus() == 2) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
            helper.setText(R.id.item_order_nocomplete__lost, "待出票");
        } else if (item.getOrderStatus() == 4) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
            helper.setText(R.id.item_order_nocomplete__lost, "已取消");
        } else if (item.getOrderStatus() == 5) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
            helper.setText(R.id.item_order_nocomplete__lost, "待退款");
        } else if (item.getOrderStatus() == 6) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
            helper.setText(R.id.item_order_nocomplete__lost, "已退款");
        } else if (item.getOrderStatus() == 7) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
            helper.setText(R.id.item_order_nocomplete__lost, "购买失败");
        } else if (item.getOrderStatus() == 8) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
            helper.setText(R.id.item_order_nocomplete__lost, "出票失败");
        } else if (item.getOrderStatus() == 9) {
            helper.setVisible(R.id.item_order_nocomplete__pay, false)
                    .setVisible(R.id.item_order_nocomplete__cancel, false);
            helper.setText(R.id.item_order_nocomplete__lost, "超时失效");
        }

        helper.getView(R.id.item_order_nocomplete__rl_layout).getLayoutParams().width = AppManager.getInstance().getWidth();

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        helper.getView(R.id.item_order_nocomplete__ll_layout).measure(w, h);
        int height = helper.getView(R.id.item_order_nocomplete__ll_layout).getMeasuredHeight();
        helper.getView(R.id.item_order__delete).getLayoutParams().height = height;

        helper.setOnClickListener(R.id.item_order_nocomplete__ll_layout,    new View.OnClickListener() {
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
