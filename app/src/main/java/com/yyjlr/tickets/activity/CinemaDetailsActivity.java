package com.yyjlr.tickets.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yyjlr.tickets.AppManager;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影院详情
 */
public class CinemaDetailsActivity extends AbstractActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_details);

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
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.content_cinema__collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.content_cinema__appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

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
                        title.setText("SFC上影");
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
        });
        initView();

    }

    private void initView() {

        cinemaLayout = (LinearLayout) findViewById(R.id.content_cinema__layout);
        cinemaLayout.getLayoutParams().width = AppManager.getInstance().getWidth() / 5 * 2;
//        leftArrow = (ImageView) findViewById(R.id.content_cinema__left);
        listView = (RecyclerView) findViewById(R.id.content_cinema__listview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        getDate();
        adapter = new CinemaAdapter(CinemaDetailsActivity.this, typeDate);
        listView.setAdapter(adapter);
        telPhoneLayout = (LinearLayout) findViewById(R.id.content_cinema__tel_phone);
        addShopLayout = (LinearLayout) findViewById(R.id.content_cinema_add_shopping);
//        leftArrow.setOnClickListener(this);
        telPhoneLayout.setOnClickListener(this);
        addShopLayout.setOnClickListener(this);
    }

    private void getDate() {
        typeDate = new ArrayList<String>();
        typeDate.add("3D眼镜");
        typeDate.add("儿童票");
        typeDate.add("停车场");
        typeDate.add("IMAX");
        typeDate.add("4D");
        typeDate.add("4K");
        typeDate.add("ATMOS");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                CinemaDetailsActivity.this.finish();
                break;
            case R.id.content_cinema__tel_phone:
                showPhoneService();
                break;
            case R.id.content_cinema_add_shopping:
                startActivity(SaleActivity.class);
                break;
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
    private void showPhoneService() {
        final String phoneNumber = "88888888";
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
