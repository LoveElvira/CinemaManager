package com.yyjlr.tickets.content;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.sale.DerivativeContent;
import com.yyjlr.tickets.content.sale.PackageContent;
import com.yyjlr.tickets.content.sale.VipCardContent;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 卖品页面
 */
public class SaleContent extends BaseLinearLayout implements View.OnClickListener {

    private LockableViewPager viewPager;
    private ContentAdapter adapter;

    private TextView saleText;
    private TextView packageText;
    private TextView derivativeText;
    private TextView vipText;
    private View saleLine;
    private View packageLine;
    private View derivativeLine;
    private View vipLine;

    private com.yyjlr.tickets.content.sale.SaleContent saleContent;
    private PackageContent packageContent;
    private DerivativeContent derivativeContent;
    private VipCardContent vipCardContent;
    private static EditText search = null;//搜索框
    private LinearLayout cancelSearch;//取消搜索
    private TextView goSearch;//搜索
    private int type = 0;//当前处于哪一个模块
    private boolean isSaleUpdate = false;
    private boolean isPackageUpdate = false;
    private boolean isDerivativeUpdate = false;
    private boolean isVipUpdate = false;

    public SaleContent(Context context) {
        this(context, null);
    }

    public SaleContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_sale, this);
        initView();
    }

    public EditText getEditText() {
        return search;
    }

    private void initView() {
        isFirst = true;
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        search = (EditText) findViewById(R.id.content_listview__search);
        cancelSearch = (LinearLayout) findViewById(R.id.content_listview__cancel);
        goSearch = (TextView) findViewById(R.id.content_listview__go_search);
        cancelSearch.setOnClickListener(this);
        goSearch.setOnClickListener(this);
        search.addTextChangedListener(textWatcher);
        saleText = (TextView) findViewById(R.id.fragment_sale__sale_text);
        packageText = (TextView) findViewById(R.id.fragment_sale__package_text);
        derivativeText = (TextView) findViewById(R.id.fragment_sale__derivative_text);
        vipText = (TextView) findViewById(R.id.fragment_sale__vip_text);
        saleLine = findViewById(R.id.fragment_sale__sale_line);
        packageLine = findViewById(R.id.fragment_sale__package_line);
        derivativeLine = findViewById(R.id.fragment_sale__derivative_line);
        vipLine = findViewById(R.id.fragment_sale__vip_line);

//        viewPager = (LockableViewPager) findViewById(R.id.content_sale__viewpager);
        saleContent = (com.yyjlr.tickets.content.sale.SaleContent) findViewById(R.id.content_sale__sale_content);
        packageContent = (PackageContent) findViewById(R.id.content_sale__package_content);
        derivativeContent = (DerivativeContent) findViewById(R.id.content_sale__derivative_content);
        vipCardContent = (VipCardContent) findViewById(R.id.content_sale__vip_content);

        packageText.setOnClickListener(this);
        saleText.setOnClickListener(this);
        derivativeText.setOnClickListener(this);
        vipText.setOnClickListener(this);
//        viewPager.setCurrentItem(1);
        keySearch();

    }

    public void updateView(boolean isUpdate) {
        if (isUpdate) {
            isFirst = isUpdate;
        }

        if (isFirst) {
            isFirst = false;
            isSaleUpdate = true;
            isPackageUpdate = true;
            isDerivativeUpdate = true;
            isVipUpdate = true;
            initBgTitle(bgTitle);
            initVisibility(0, isSaleUpdate);
            isSaleUpdate = false;
        }
    }

    private void keySearch() {
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //跳转页面
                    imm.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    switch (type) {
                        case 0:
                            saleContent.getSale("0", search.getText().toString().trim());
                            break;
                        case 1:
                            packageContent.getPackage("0", search.getText().toString().trim());
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                }
                return false;
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                cancelSearch.setVisibility(VISIBLE);
                goSearch.setVisibility(VISIBLE);
            } else {
                cancelSearch.setVisibility(GONE);
                goSearch.setVisibility(GONE);
                switch (type) {
                    case 0:
                        if (saleContent.getVisibility() == VISIBLE)
                            saleContent.getSale("0", "");
                        break;
                    case 1:
                        if (packageContent.getVisibility() == VISIBLE)
                            packageContent.getPackage("0", "");
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        }
    };

    private void initVisibility(int type, boolean isUpdate) {

        saleText.setTextColor(getResources().getColor(R.color.gray_929292));
        packageText.setTextColor(getResources().getColor(R.color.gray_929292));
        derivativeText.setTextColor(getResources().getColor(R.color.gray_929292));
        vipText.setTextColor(getResources().getColor(R.color.gray_929292));

        saleLine.setVisibility(GONE);
        packageLine.setVisibility(GONE);
        derivativeLine.setVisibility(GONE);
        vipLine.setVisibility(GONE);

        saleContent.setVisibility(GONE);
        packageContent.setVisibility(GONE);
        derivativeContent.setVisibility(GONE);
        vipCardContent.setVisibility(GONE);
        this.type = type;
        switch (type) {
            case 0://卖品
                saleText.setTextColor(getResources().getColor(R.color.orange_ff7b0f));
                saleLine.setVisibility(VISIBLE);
                saleContent.setVisibility(VISIBLE);
//                if (!"".equals(saleContent.getSearchText()))
//                search.setText(saleContent.getSearchText());
                saleContent.updateView(isUpdate);
                break;
            case 1://套餐
                packageText.setTextColor(getResources().getColor(R.color.orange_ff7b0f));
                packageLine.setVisibility(VISIBLE);
                packageContent.setVisibility(VISIBLE);
//                if (!"".equals(packageContent.getSearchText()))
//                search.setText(packageContent.getSearchText());
                packageContent.updateView(isUpdate);
                break;
            case 2://衍生品
                derivativeText.setTextColor(getResources().getColor(R.color.orange_ff7b0f));
                derivativeLine.setVisibility(VISIBLE);
                derivativeContent.setVisibility(VISIBLE);
//                packageContent.updateView(isUpdate);
                break;
            case 3://会员卡
                vipText.setTextColor(getResources().getColor(R.color.orange_ff7b0f));
                vipLine.setVisibility(VISIBLE);
                vipCardContent.setVisibility(VISIBLE);
//                packageContent.updateView(isUpdate);
                break;
        }

    }

    //隐藏虚拟键盘
    public static void hideInput() {
        boolean isOpen = imm.isActive();
        if (isOpen) {
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_sale__sale_text:
                if (saleContent.getVisibility() == GONE) {
                    hideInput();
                    initVisibility(0, isSaleUpdate);
                    isSaleUpdate = false;
                }
                break;
            case R.id.fragment_sale__package_text:
                if (packageContent.getVisibility() == GONE) {
                    hideInput();
                    initVisibility(1, isPackageUpdate);
                    isPackageUpdate = false;
                }
                break;
            case R.id.fragment_sale__derivative_text:
                if (derivativeContent.getVisibility() == GONE) {
                    hideInput();
                    initVisibility(2, isDerivativeUpdate);
                    isDerivativeUpdate = false;
                }
                break;
            case R.id.fragment_sale__vip_text:
                if (vipCardContent.getVisibility() == GONE) {
                    hideInput();
                    initVisibility(3, isVipUpdate);
                    isVipUpdate = false;
                }
                break;
            case R.id.content_listview__cancel:
                search.setText("");
                break;
            case R.id.content_listview__go_search:
                imm.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                switch (type) {
                    case 0:
                        saleContent.getSale("0", search.getText().toString().trim());
                        break;
                    case 1:
                        packageContent.getPackage("0", search.getText().toString().trim());
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                break;
        }
    }
}
