package com.yyjlr.tickets.content;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.adapter.ContentAdapter;
import com.yyjlr.tickets.content.film.HotFilmContent;
import com.yyjlr.tickets.content.film.NextFilmContent;
import com.yyjlr.tickets.viewutils.LockableViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/7/28.
 * 影片页面  废弃
 */
public class FilmContent extends BaseLinearLayout implements View.OnClickListener {

    private LockableViewPager viewPager;
    private ContentAdapter adapter;

    private TextView hotText;
    private TextView nextText;

    private HotFilmContent hotFilmContent;
    private NextFilmContent nextFilmContent;

    public FilmContent(Context context) {
        this(context, null);
    }

    public FilmContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_sale, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initView(boolean isHot) {

        hotText = (TextView) findViewById(R.id.base_toolbar__left);
        nextText = (TextView) findViewById(R.id.base_toolbar__right);
        hotText.setText("正在热映");
        nextText.setText("即将上映");
        viewPager = (LockableViewPager) findViewById(R.id.content_sale__viewpager);

        hotFilmContent = new HotFilmContent(getContext());
        nextFilmContent = new NextFilmContent(getContext());

        List<View> list = new ArrayList<View>();
        list.add(hotFilmContent);
        list.add(nextFilmContent);

        adapter = new ContentAdapter(list, null);
        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int position) {
                initFirstView();
                if (position == 1) {
                    nextText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    nextText.setBackground(getResources().getDrawable(R.drawable.bg_right_white));
//                    packageImage.setImageResource(R.mipmap.sale_package_select);
                } else if (position == 0) {
                    hotText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    hotText.setBackground(getResources().getDrawable(R.drawable.bg_left_white));
//                    saleImage.setImageResource(R.mipmap.sale_sale_select);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        nextText.setOnClickListener(this);
        hotText.setOnClickListener(this);
        initFirstView();
        hotText.setTextColor(getResources().getColor(R.color.colorPrimary));
        hotText.setBackground(getResources().getDrawable(R.drawable.bg_left_white));
        if (isHot) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(1);
        }

    }

    public void setCurrentView(boolean isHot) {
        if (isHot) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initFirstView() {

        hotText.setTextColor(getResources().getColor(R.color.white));
        nextText.setTextColor(getResources().getColor(R.color.white));
        hotText.setBackground(getResources().getDrawable(R.drawable.bg_border_left_white));
        nextText.setBackground(getResources().getDrawable(R.drawable.bg_border_right_white));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_toolbar__right:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.base_toolbar__left:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                break;
        }
    }
}
