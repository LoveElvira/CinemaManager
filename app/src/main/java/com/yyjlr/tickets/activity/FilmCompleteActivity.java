package com.yyjlr.tickets.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.MainActivity;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.FilmSaleAdapter;
import com.yyjlr.tickets.model.FilmSaleEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/8/10.
 * 完成选座
 */
public class FilmCompleteActivity extends AbstractActivity implements BaseAdapter.OnRecyclerViewItemChildClickListener, View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private TextView payPrice;//应付金额
    private ImageView addPackage;//添加套餐
    private LinearLayout addPackageLayout;
    private EditText phone;
    private TextView confirmOrder;
    private ImageView deletePhone;//清除号码

    private List<FilmSaleEntity> allDate;
    private List<FilmSaleEntity> partDate = new ArrayList<FilmSaleEntity>();
    private FilmSaleAdapter filmSaleAdapter;

    private RelativeLayout addLayout;
    private LinearLayout discountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_complete_seat);
        AppManager.getInstance().initWidthHeight(getBaseContext());
        initView();
    }

    private void initView() {

        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("完成选座");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        payPrice = (TextView) findViewById(R.id.content_film_complete_seat__pay_price);
        addPackage = (ImageView) findViewById(R.id.content_film_complete_seat__add);
        addPackageLayout = (LinearLayout) findViewById(R.id.content_film_complete_seat__sale_layout);
        phone = (EditText) findViewById(R.id.content_sale_bill__phone);
        deletePhone = (ImageView) findViewById(R.id.content_sale_bill__delete_phone);
        confirmOrder = (TextView) findViewById(R.id.content_sale_bill__confirm_order);
        addLayout = (RelativeLayout) findViewById(R.id.content_film_complete_seat__add_layout);
        discountLayout = (LinearLayout) findViewById(R.id.content_film_complete_seat__discount_layout);
        deletePhone.setOnClickListener(this);
        phone.addTextChangedListener(textWatcher);
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = phone.getText().toString().trim();
                if (isMobileNum(num)){
                    startActivity(PaySelectActivity.class);
                }else {
                    showShortToast("手机号码不对");
                }
            }
        });

        addPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAllDatePopupWindow();
            }
        });

        allDate = Application.getiDataService().getFileSaleList();
        partDate.add(allDate.get(0));
        partDate.add(allDate.get(1));


        if (flag) {
            discountLayout.setVisibility(View.VISIBLE);
            addLayout.setVisibility(View.GONE);
            initAddDiscountPackage();
        } else {
            discountLayout.setVisibility(View.GONE);
            addLayout.setVisibility(View.VISIBLE);
            initAddPackage(0);
        }
        flag = !flag;
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().equals("") || editable.toString() == null) {
                deletePhone.setVisibility(View.GONE);
            } else {
                deletePhone.setVisibility(View.VISIBLE);
            }
        }
    };

    //添加优惠活动产品
    private void initAddDiscountPackage() {
        View view = LayoutInflater.from(FilmCompleteActivity.this).inflate(R.layout.item_film_sale_package_, null, false);
        final ImageView selectImage = (ImageView) view.findViewById(R.id.item_film_sale__select);
        ImageView saleImage = (ImageView) view.findViewById(R.id.item_film_sale__image);
        TextView salePackage = (TextView) view.findViewById(R.id.item_film_sale__package);
        TextView salePackagePrice = (TextView) view.findViewById(R.id.item_film_sale__price);
        TextView salePackageContent = (TextView) view.findViewById(R.id.item_film_sale__package_content);
        TextView saleDiscount = (TextView) view.findViewById(R.id.item_film_sale__discount);
        LinearLayout saleParent = (LinearLayout) view.findViewById(R.id.item_film_sale__layout);

        saleImage.setImageResource(R.mipmap.mihua);
        salePackage.setText(partDate.get(0).getSalePackage());
        salePackagePrice.setText("￥" + partDate.get(0).getSalePrice());
        salePackageContent.setText(partDate.get(0).getSalePackageContent());
//        saleDiscount.setText("");
        selectImage.setImageResource(R.mipmap.sale_no_select);
        saleParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(0).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(0).setSaleSelect(false);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(0).setSaleSelect(true);
                }
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(0).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(0).setSaleSelect(false);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(0).setSaleSelect(true);
                }
            }
        });

        addPackageLayout.addView(view);
    }


    //添加套餐
    private void initAddPackage(int i) {
        if (i >= partDate.size()) {
            return;
        }
        View view = LayoutInflater.from(FilmCompleteActivity.this).inflate(R.layout.item_film_sale_package, null, false);
        final ImageView selectImage = (ImageView) view.findViewById(R.id.item_film_sale__select);
        ImageView saleImage = (ImageView) view.findViewById(R.id.item_film_sale__image);
        TextView salePackage = (TextView) view.findViewById(R.id.item_film_sale__package);
        TextView salePackagePrice = (TextView) view.findViewById(R.id.item_film_sale__price);
        TextView salePackageContent = (TextView) view.findViewById(R.id.item_film_sale__package_content);
        TextView saleTime = (TextView) view.findViewById(R.id.item_film_sale__time);
        LinearLayout saleLayout = (LinearLayout) view.findViewById(R.id.item_film_sale__layout);
        final LinearLayout addLayout = (LinearLayout) view.findViewById(R.id.item_film_sale__add_layout);
        TextView saleLost = (TextView) view.findViewById(R.id.item_film_sale__lost);
        final TextView saleNum = (TextView) view.findViewById(R.id.item_film_sale__num);
        TextView saleAdd = (TextView) view.findViewById(R.id.item_film_sale__add);

        saleImage.setImageResource(R.mipmap.mihua);
        salePackage.setText(partDate.get(i).getSalePackage());
        salePackagePrice.setText("￥" + partDate.get(i).getSalePrice());
        salePackageContent.setText(partDate.get(i).getSalePackageContent());
        saleTime.setText(partDate.get(i).getSaleTime());

        selectImage.setImageResource(R.mipmap.sale_no_select);
        addLayout.setVisibility(View.GONE);
        if (partDate.get(i).isSaleSelect()) {
            selectImage.setImageResource(R.mipmap.sale_select);
            addLayout.setVisibility(View.VISIBLE);
            saleNum.setText("1");
        }
        saleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(saleNum.getText().toString()) + 1;
                saleNum.setText(num + "");
            }
        });
        saleLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(saleNum.getText().toString());
                if (num != 0)
                    num = num - 1;
                saleNum.setText(num + "");
            }
        });
        final int finalI = i;
        saleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(finalI).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(finalI).setSaleSelect(false);
                    addLayout.setVisibility(View.GONE);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(finalI).setSaleSelect(true);
                    addLayout.setVisibility(View.VISIBLE);
                    saleNum.setText("1");
                }
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (partDate.get(finalI).isSaleSelect()) {
                    selectImage.setImageResource(R.mipmap.sale_no_select);
                    partDate.get(finalI).setSaleSelect(false);
                    addLayout.setVisibility(View.GONE);
                } else {
                    selectImage.setImageResource(R.mipmap.sale_select);
                    partDate.get(finalI).setSaleSelect(true);
                    addLayout.setVisibility(View.VISIBLE);
                    saleNum.setText("1");
                }
            }
        });

        addPackageLayout.addView(view);
        i++;
        initAddPackage(i);
    }

    private void showAllDatePopupWindow() {

        View parent = LayoutInflater.from(FilmCompleteActivity.this).inflate(R.layout.content_film_complete_seat, null, false);
        View view = LayoutInflater.from(FilmCompleteActivity.this).inflate(
                R.layout.popupwindow_film_sale, null, false);

        view.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                R.anim.fade_in));
//        RelativeLayout ll_popup = (RelativeLayout) view
//                .findViewById(R.id.ll_popup);
//        ll_popup.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
//                R.anim.fade_out));

        final PopupWindow mPopupWindow = new PopupWindow(view);
        mPopupWindow.setWidth(AppManager.getInstance().getWidth() - AppManager.getInstance().getWidth() / 6);
        mPopupWindow.setHeight(AppManager.getInstance().getHeight() - AppManager.getInstance().getHeight() / 4);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setContentView(view);

        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        mPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                restAddPackage();
                lp.alpha = 1.0f;
                Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            }
        });


        ImageView cancel = (ImageView) view.findViewById(R.id.content_film_sale__cancel);
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.content_film_sale__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FilmCompleteActivity.this);
        listView.setLayoutManager(linearLayoutManager);
        initAdapter(listView);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
                restAddPackage();
                mPopupWindow.dismiss();
            }
        });

    }

    private void initAdapter(RecyclerView listView) {
        filmSaleAdapter = new FilmSaleAdapter(allDate);
        filmSaleAdapter.openLoadAnimation();
        listView.setAdapter(filmSaleAdapter);
        mCurrentCounter = filmSaleAdapter.getData().size();
//        filmSaleAdapter.setOnLoadMoreListener(this);
        filmSaleAdapter.openLoadMore(PAGE_SIZE, true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        filmSaleAdapter.setOnRecyclerViewItemChildClickListener(this);
    }

    private void restAddPackage() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < partDate.size(); i++) {
            list.add(partDate.get(i).getSaleId());
        }
        for (int i = 0; i < allDate.size(); i++) {
            for (int j = 0; j < partDate.size(); j++) {
                if (allDate.get(i).getSaleId().equals(partDate.get(j).getSaleId()) && allDate.get(i).isSaleSelect()) {
                    partDate.get(j).setSaleSelect(true);
                    break;
                } else if (allDate.get(i).isSaleSelect() && !list.contains(allDate.get(i).getSaleId())) {
                    partDate.add(allDate.get(i));
                    break;
                }
            }
        }
        addPackageLayout.removeAllViews();
        Log.i("ee", partDate.size() + "----------------" + addPackageLayout.getChildCount());
        initAddPackage(0);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        if (allDate.get(position).isSaleSelect()) {
            allDate.get(position).setSaleSelect(false);
        } else {
            allDate.get(position).setSaleSelect(true);
        }
        filmSaleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left:
                FilmCompleteActivity.this.finish();
                break;
            case R.id.content_sale_bill__delete_phone:
                phone.setText("");
                break;
        }
    }
}
