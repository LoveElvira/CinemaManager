package com.yyjlr.tickets.activity.sale;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.content.sale.DerivativeContent;
import com.yyjlr.tickets.content.sale.PackageContent;
import com.yyjlr.tickets.content.sale.VipCardContent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Elvira on 2016/11/10.
 * 从影院详情里面进行跳转的卖品页面
 */

public class SaleActivity extends AbstractActivity implements View.OnClickListener {

    private ImageView leftArrow;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_sale);
        initView();
        updateView();
    }

    private void initView() {
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left_image);
        leftArrow.setVisibility(VISIBLE);
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

        leftArrow.setOnClickListener(this);
        packageText.setOnClickListener(this);
        saleText.setOnClickListener(this);
        derivativeText.setOnClickListener(this);
        vipText.setOnClickListener(this);
//        viewPager.setCurrentItem(1);
        keySearch();

    }

    private void updateView() {
        isSaleUpdate = true;
        isPackageUpdate = true;
        isDerivativeUpdate = true;
        isVipUpdate = true;
        initBgTitle(bgTitle);
        initVisibility(0, isSaleUpdate);
        isSaleUpdate = false;
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
    public void hideInput() {
        boolean isOpen = imm.isActive();
        if (isOpen) {
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__left_image:
                SaleActivity.this.finish();
                break;
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


//    private ImageView back;
//    private LockableViewPager viewPager;
//    private ContentAdapter adapter;
//
//    private TextView saleText;
//    private TextView packageText;
//
//    private com.yyjlr.tickets.content.sale.SaleContent saleContent;
//    private com.yyjlr.tickets.content.sale.PackageContent packageContent;
//    private EditText search;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cinema_sale);
//        initView();
//    }
//
//    private void initView() {
//        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
//        initBgTitle(bgTitle);
//        back = (ImageView) findViewById(R.id.base_toolbar__left_image);
//        back.setOnClickListener(this);
//
//        saleText = (TextView) findViewById(R.id.base_toolbar__left);
//        packageText = (TextView) findViewById(R.id.base_toolbar__right);
//
//        viewPager = (LockableViewPager) findViewById(R.id.content_sale__viewpager);
//
//        saleContent = new com.yyjlr.tickets.content.sale.SaleContent(getBaseContext());
//        packageContent = new PackageContent(getBaseContext());
//
//        List<View> list = new ArrayList<View>();
//        list.add(packageContent);
//        list.add(saleContent);
//
//        adapter = new ContentAdapter(list, null);
//        viewPager.setSwipeable(false);
//        viewPager.setAdapter(adapter);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onPageSelected(int position) {
//                initFirstView();
//                if (position == 0) {
//                    packageText.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    packageText.setBackground(getResources().getDrawable(R.drawable.bg_right_white));
//                    //search = saleContent.getEditText();
//                    //hideInput();
//                } else if (position == 1) {
//                    saleText.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    saleText.setBackground(getResources().getDrawable(R.drawable.bg_left_white));
//                   // search = packageContent.getEditText();
//                   // hideInput();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//        packageText.setOnClickListener(this);
//        saleText.setOnClickListener(this);
//        viewPager.setCurrentItem(1);
//
//    }
//
//    //隐藏虚拟键盘
//    public void hideInput() {
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
////            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//            imm.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private void initFirstView() {
//        saleText.setTextColor(getResources().getColor(R.color.white));
//        packageText.setTextColor(getResources().getColor(R.color.white));
//        saleText.setBackground(getResources().getDrawable(R.drawable.bg_border_left_white));
//        packageText.setBackground(getResources().getDrawable(R.drawable.bg_border_right_white));
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.base_toolbar__right:
//                if (viewPager.getCurrentItem() != 0) {
//                    viewPager.setCurrentItem(0);
//                }
//                break;
//            case R.id.base_toolbar__left:
//                if (viewPager.getCurrentItem() != 1) {
//                    viewPager.setCurrentItem(1);
//                }
//                break;
//            case R.id.base_toolbar__left_image:
//                SaleActivity.this.finish();
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != CODE_RESULT)
//            return;
//        switch (requestCode){
//            case CODE_REQUEST_DIALOG:
//                break;
//        }
//
//    }
}
