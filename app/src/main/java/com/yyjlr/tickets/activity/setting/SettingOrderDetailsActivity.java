package com.yyjlr.tickets.activity.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;

/**
 * Created by Elvira on 2016/8/11.
 * 订单详情
 */
public class SettingOrderDetailsActivity extends AbstractActivity implements View.OnClickListener {
    private TextView title;
    private LinearLayout saleLayout;
    private LinearLayout moreLayout;
    private ImageView moreImage;
    private TextView moreText;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("订单详情");

        saleLayout = (LinearLayout) findViewById(R.id.content_order_details__package_layout);
        moreLayout = (LinearLayout) findViewById(R.id.item_order_sale_details__more_layout);
        moreImage = (ImageView) findViewById(R.id.item_order_sale_details_more__down);
        moreText = (TextView) findViewById(R.id.item_order_sale_details_more__text);

        moreLayout.setOnClickListener(this);


        initSaleList(4,flag);

    }


    //卖品列表
    private void initSaleList(int num,boolean flag){
        saleLayout.removeAllViews();
        if (!flag && num>2) {
            num = 2;
        }
        for (int i=0;i<num;i++) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_order_sale_details, null);
            TextView salePackageName = (TextView) view.findViewById(R.id.item_order_sale_details__package_name);
            TextView salePackageContent = (TextView) view.findViewById(R.id.item_order_sale_details__package_content);
            ImageView salePackageImage = (ImageView) view.findViewById(R.id.item_order_sale_details__package_image);
            saleLayout.addView(view);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SettingOrderDetailsActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_order_sale_details__more_layout:
                if (!flag){
                    moreImage.setImageResource(R.mipmap.more_up);
                    moreText.setText("隐藏部分卖品");
                    flag = true;
                }else if (flag){
                    moreImage.setImageResource(R.mipmap.more_down);
                    moreText.setText("显示更多卖品");
                    flag = false;
                }

                initSaleList(4,flag);

                break;
        }
    }
}
