package com.yyjlr.tickets.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.sale.SaleActivity;
import com.yyjlr.tickets.model.cinemainfo.CinemaInfoModel;
import com.yyjlr.tickets.model.cinemainfo.HallType;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.CustomDialog;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影院详情
 */
public class CinemaDetailsActivity extends AbstractActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private RecyclerView listView;
    private CinemaAdapter adapter;
    private List<String> typeDate;
    //    private ImageView leftArrow;
    private LinearLayout telPhoneLayout;
    private LinearLayout addShopLayout;
    private TextView title;
    private ImageView leftArrowBg;
    private ImageView leftArrow;

    private View view;
    private LinearLayout cinemaLayout;//透明背景
    private CinemaInfoModel cinemaInfoModel = null;
    private TextView cinemaName, cinemaIntro;//影院名称 影院介绍
    private ImageView cinemaImage;//影院图片
    private ImageView cinemaAddressImage;//地址图标
    private TextView cinemaAddress;
    private LinearLayout timeAndTelLayout, trafficLayout, featureLayout, addressLayout; // 营业时间和服务电话 影院交通 影院服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_details);
        initView();
        getCinemaInfo();
    }

    private void initView() {

        AppManager.getInstance().initWidthHeight(CinemaDetailsActivity.this);

        view = findViewById(R.id.content_cinema__view);
        dealStatusBar(view);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.content_cinema__toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.base_toolbar__text);
        leftArrowBg = (ImageView) findViewById(R.id.base_toolbar__left_bg);
        leftArrowBg.setAlpha(1.0f);
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout) findViewById(R.id.content_cinema__collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.content_cinema__appbar);
        appBarLayout.addOnOffsetChangedListener(this);


        cinemaLayout = (LinearLayout) findViewById(R.id.content_cinema__layout);
        cinemaLayout.getLayoutParams().width = AppManager.getInstance().getWidth() / 5 * 2;
//        leftArrow = (ImageView) findViewById(R.id.content_cinema__left);
        cinemaName = (TextView) findViewById(R.id.content_cinema__name);
        cinemaIntro = (TextView) findViewById(R.id.content_cinema__introduction);
        cinemaImage = (ImageView) findViewById(R.id.content_cinema__image);
        cinemaAddress = (TextView) findViewById(R.id.content_cinema__address);
        cinemaAddressImage = (ImageView) findViewById(R.id.content_cinema__address_image);

        listView = (RecyclerView) findViewById(R.id.content_cinema__listview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);

        timeAndTelLayout = (LinearLayout) findViewById(R.id.content_cinema__time_and_tel_layout);
        trafficLayout = (LinearLayout) findViewById(R.id.content_cinema__traffic_layout);
        featureLayout = (LinearLayout) findViewById(R.id.content_cinema__feature_layout);
        addressLayout = (LinearLayout) findViewById(R.id.content_cinema__address_layout);


//        telPhoneLayout = (LinearLayout) findViewById(R.id.content_cinema__tel_phone);
//        addShopLayout = (LinearLayout) findViewById(R.id.content_cinema_add_shopping);
//        leftArrow.setOnClickListener(this);
//        telPhoneLayout.setOnClickListener(this);
//        addShopLayout.setOnClickListener(this);
    }

    //获取影院信息
    private void getCinemaInfo() {
        customDialog = new CustomDialog(this, "加载中...");
        customDialog.show();
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.GET_CINEMA_INFO, new OkHttpClientManager.ResultCallback<CinemaInfoModel>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
                customDialog.dismiss();
            }

            @Override
            public void onResponse(CinemaInfoModel response) {
                Log.i("ee", new Gson().toJson(response));
                cinemaInfoModel = response;

                if (cinemaInfoModel != null) {
                    cinemaName.setText(cinemaInfoModel.getCinemaName());
                    cinemaIntro.setText(cinemaInfoModel.getCinemaDesc());
                    if (cinemaInfoModel.getCinemaImg() != null && !"".equals(cinemaInfoModel.getCinemaImg())) {
                        Picasso.with(getBaseContext())
                                .load(cinemaInfoModel.getCinemaImg())
                                .into(cinemaImage);
                    }

                    if (cinemaInfoModel.getAddress() != null && !"".equals(cinemaInfoModel.getAddress())) {
                        cinemaAddress.setText(cinemaInfoModel.getAddress());
                        addressLayout.setVisibility(View.VISIBLE);
                    }
                    typeDate = cinemaInfoModel.getHallType();
                    if (typeDate != null && typeDate.size() > 0) {
                        adapter = new CinemaAdapter(CinemaDetailsActivity.this, typeDate);
                        listView.setAdapter(adapter);
                    }
                    if (cinemaInfoModel.getAddressIcon() != null) {
                        Picasso.with(getBaseContext())
                                .load(cinemaInfoModel.getAddressIcon())
                                .into(cinemaAddressImage);
                    }

                    if (cinemaInfoModel.getTimeAndTel() != null && cinemaInfoModel.getTimeAndTel().size() > 0) {
                        timeAndTelLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < cinemaInfoModel.getTimeAndTel().size(); i++) {
                            if (cinemaInfoModel.getTimeAndTel().get(i).getMemo() != null) {
                                timeAndTelLayout.addView(initCinemaType(cinemaInfoModel.getTimeAndTel().get(i)));
                            }
                        }
                    }
                    if (cinemaInfoModel.getTraffic() != null && cinemaInfoModel.getTraffic().size() > 0) {
                        trafficLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < cinemaInfoModel.getTraffic().size(); i++) {
                            if (cinemaInfoModel.getTraffic().get(i).getMemo() != null) {
                                trafficLayout.addView(initCinemaType(cinemaInfoModel.getTraffic().get(i)));
                            }
                        }
                    }
                    if (cinemaInfoModel.getFeature() != null && cinemaInfoModel.getFeature().size() > 0) {
                        featureLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < cinemaInfoModel.getFeature().size(); i++) {
                            if (cinemaInfoModel.getFeature().get(i).getMemo() != null) {
                                featureLayout.addView(initCinemaType(cinemaInfoModel.getFeature().get(i)));
                            }
                        }
                    }
                }
                customDialog.dismiss();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
                customDialog.dismiss();
            }
        }, requestNull, CinemaInfoModel.class, CinemaDetailsActivity.this);
    }


    //动态添加
    private View initCinemaType(final HallType type) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_cinema_type, null, false);
        LinearLayout parent = (LinearLayout) view.findViewById(R.id.item_cinema_type__layout);
        ImageView image = (ImageView) view.findViewById(R.id.item_cinema_type__image);
        TextView text = (TextView) view.findViewById(R.id.item_cinema_type__text);
        ImageView rightImage = (ImageView) view.findViewById(R.id.item_cinema_type__right_image);
        text.setText(type.getMemo());
        rightImage.setVisibility(View.GONE);
        if (type.getDesc() != null) {
            if (type.getDesc().contains("热线电话")) {
                text.setText(type.getDesc() + "：" + type.getMemo());
                parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;
                            showPhoneService(type.getMemo());
                        }
                    }
                });
            } else if (type.getDesc().contains("营业时间")) {
                text.setText(type.getDesc() + "：" + type.getMemo());
            } else if (type.getDesc().contains("卖品")) {
                parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;
                            CinemaDetailsActivity.this.startActivity(SaleActivity.class);
                        }
                    }
                });
            }
        }

        if (!"".equals(type.getIcon())) {
            Picasso.with(getBaseContext())
                    .load(type.getIcon())
                    .into(image);
        }

        if (!"".equals(type.getActionIcon())) {
            rightImage.setVisibility(View.VISIBLE);
            Picasso.with(getBaseContext())
                    .load(type.getActionIcon())
                    .into(rightImage);
        }
        return view;
    }

//    private void getDate() {
//        typeDate = new ArrayList<String>();
//        typeDate.add("3D眼镜");
//        typeDate.add("儿童票");
//        typeDate.add("停车场");
//        typeDate.add("IMAX");
//        typeDate.add("4D");
//        typeDate.add("4K");
//        typeDate.add("ATMOS");
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                CinemaDetailsActivity.this.finish();
                break;
//            case R.id.content_cinema__tel_phone:
//                showPhoneService();
//                break;
//            case R.id.content_cinema_add_shopping:
//                startActivity(SaleActivity.class);
//                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (state != CollapsingToolbarLayoutState.EXPANDED) {
                state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                title.setText("");
                leftArrowBg.setAlpha(1.0f);
            }
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                if (cinemaInfoModel != null)
                    title.setText(cinemaInfoModel.getCinemaName());


                leftArrowBg.setAlpha(0.0f);
                state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
            }
        } else {
            if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                title.setText("");
                leftArrowBg.setAlpha(1.0f);
                state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
            }
        }
    }

    //adapter
    private class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder> {

        private List<String> list;
        private Context context;

        public CinemaAdapter(Context context, List<String> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cinema, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.type.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            if (list != null && list.size() > 0)
                return list.size();
            else
                return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView type;

            public ViewHolder(View itemView) {
                super(itemView);
                type = (TextView) itemView.findViewById(R.id.item_cinema__type);
            }
        }
    }

    /**
     * show Dialog 呼叫服务电话
     */

    private void showPhoneService(String phone) {
        final String phoneNumber = phone;
        LayoutInflater inflater = LayoutInflater.from(CinemaDetailsActivity.this);
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(CinemaDetailsActivity.this).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        TextView message = (TextView) layout.findViewById(R.id.alert_dialog_message);
        title.setText("联系服务人员");
        message.setText("拨打   " + phoneNumber);
        layout.findViewById(R.id.alert_dialog__cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        layout.findViewById(R.id.alert_dialog__submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拨打电话
                if (!"".equals(phoneNumber)) {
                    TelephonyManager mTelephonyManager = (TelephonyManager) Application.getInstance().getCurrentActivity().getSystemService(Service.TELEPHONY_SERVICE);
                    int absent = mTelephonyManager.getSimState();
                    if (absent == TelephonyManager.SIM_STATE_ABSENT) {
                        Toast.makeText(CinemaDetailsActivity.this, "请确认sim卡是否插入或者sim卡暂时不可用！",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent phoneIntent = new Intent(
                                "android.intent.action.CALL", Uri.parse("tel:"
                                + phoneNumber));
                        CinemaDetailsActivity.this.startActivity(phoneIntent);
                    }
                    builder.dismiss();
                } else {
                    Toast.makeText(CinemaDetailsActivity.this, "电话为空",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
}
