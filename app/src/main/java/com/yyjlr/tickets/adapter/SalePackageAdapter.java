package com.yyjlr.tickets.adapter;

import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.helputils.ChangeUtils;
import com.yyjlr.tickets.model.SaleEntity;
import com.yyjlr.tickets.model.sale.GoodInfo;

import java.util.List;

/**
 * Created by Elvira on 2016/7/31.
 * 卖品套餐Adapter
 */
public class SalePackageAdapter extends BaseAdapter<GoodInfo> {
    private int first = -1;
    private int first1 = -1;
    private int last = -1;

    public SalePackageAdapter(List<GoodInfo> data) {
        super(R.layout.item_sale_package, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodInfo item, int position) {

        String html = "<span style=\"position:relative; color: #C3C3C3; padding:0 2px; font-size: 10px;\">" +
                "原价：<span style=\"color: #929292; font-size: 12px;\">" + item.getPrice() + "</span>元" +
                "<span style=\"position:absolute;left:0; top:40%; display:block; width:100%; height:2px; background:#f00;\">" +
                "</span>" + "</span>";

//        SpannableString styledText = new SpannableString("原价：" + item.getOriginalPrice() + "元");
//        styledText.setSpan(new TextAppearanceSpan(helper.getConvertView().getContext(), R.style.text_gray), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        styledText.setSpan(new TextAppearanceSpan(helper.getConvertView().getContext(), R.style.text_gray), styledText.length() - 1, styledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        styledText.setSpan(new StrikethroughSpan(), 0, styledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        styledText.setSpan(new ForegroundColorSpan(Color.RED), 0, styledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        TextPaint textPaint = new TextPaint();
//        textPaint.setColor(helper.getConvertView().getContext().getResources().getColor(R.color.red));  //设置划线颜色
//        textPaint.setStrikeThruText(true);

//        TextView originalPrice = helper.getView(R.id.item_sale__original_price);
//        originalPrice.setText(Html.fromHtml(html));

        helper.setText(R.id.item_sale__package_name, item.getGoodsName())
                .setText(R.id.item_sale__package_content, item.getGoodsDesc())
                .setText(R.id.item_sale__original_price, ChangeUtils.save2Decimal(item.getPrice()))
                .setText(R.id.item_sale__app_price, ChangeUtils.save2Decimal(item.getAppPrice()))
                .setImageResource(R.id.item_sale_package__image, R.mipmap.bird)
                .setOnClickListener(R.id.item_sale_package__right, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_sale_package__cardview, new OnItemChildClickListener());
        LinearLayout priceLayout = helper.getView(R.id.item_sale__price_layout);
        priceLayout.measure(0, 0);
        int width = priceLayout.getMeasuredWidth();
        helper.getView(R.id.item_sale__line).getLayoutParams().width = width;


        boolean f = false;

        if (position == first) {
            helper.getView(R.id.item_sale_package__cardview).setAlpha((float) 0.8);
            if (position == first1) {
                helper.getView(R.id.item_sale_package__cardview).setAlpha(1);
            }
            f = true;
        } else {
            helper.getView(R.id.item_sale_package__cardview).setAlpha(1);
        }
        if (!f) {
            if (position == last) {
                helper.getView(R.id.item_sale_package__cardview).setAlpha((float) 0.7);
            } else {
                helper.getView(R.id.item_sale_package__cardview).setAlpha(1);
            }
        }
    }

    public void changeBgFristAndLast(int first, int last, int first1) {
        this.first = first;
        this.first1 = first1;
        this.last = last;
        notifyDataSetChanged();
    }

}
