package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.adapter.BaseAdapter;
import com.yyjlr.tickets.adapter.MessageAdapter;
import com.yyjlr.tickets.model.MessageEntity;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.model.message.MyMessageBean;
import com.yyjlr.tickets.model.message.MyMessageInfo;
import com.yyjlr.tickets.requestdata.DeleteRequest;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.requestdata.PagableRequest;
import com.yyjlr.tickets.requestdata.RequestNull;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Elvira on 2016/8/3.
 * 我的消息
 */
public class SettingMessageActivity extends AbstractActivity implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener, View.OnClickListener {

    private RecyclerView listView;
    private SuperSwipeRefreshLayout refresh;//刷新
    private TextView title;
    private ImageView leftArrow;
    private ImageView rightDelete;
    private TextView rightCancel;
    private MessageAdapter adapter;
    private List<MessageEntity> messageEntityList;

    private ImageView headerImage;
    private ProgressBar headerProgressBar;
    private TextView headerSta/*, headerTime*/;

    private List<MyMessageInfo> messageInfoList;
    private List<MyMessageInfo> messageInfoLists;//总条数
    private boolean hasMore = false;
    private String pagable = "0";
    private boolean isRead;
    private LinearLayout deleteLayout;//删除界面
    private TextView selectAll;//全选
    private TextView delete;//删除
    private boolean isDelete;//是否为删除模式
    private List<Long> messageIdList;//删除的ID
    private int selectNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysetting_message);
        isDelete = false;
        isRead = false;
        selectNum = 0;
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        isRead = false;
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("我的消息");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        rightDelete = (ImageView) findViewById(R.id.base_toolbar__right);
        rightDelete.setAlpha(1.0f);
        rightDelete.setImageResource(R.mipmap.delete);
        rightDelete.setOnClickListener(this);
        rightCancel = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightCancel.setText("取消");
        rightCancel.setVisibility(View.GONE);
        rightCancel.setOnClickListener(this);

        deleteLayout = (LinearLayout) findViewById(R.id.content_setting_message__delete_layout);
        selectAll = (TextView) findViewById(R.id.content_setting_message__selete_all);
        delete = (TextView) findViewById(R.id.content_setting_message__delete);
        selectAll.setOnClickListener(this);
        delete.setOnClickListener(this);

        listView = (RecyclerView) findViewById(R.id.content_setting_message__listview);
//        messageEntityList = Application.getiDataService().getMessageList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
        listView.setLayoutManager(linearLayoutManager);
//        TOTAL_COUNTER = 40;
        refresh = (SuperSwipeRefreshLayout) findViewById(R.id.content_setting_message__refresh);
        refresh.setHeaderView(createHeaderView());// add headerView
        refresh.setTargetScrollWithLayout(true);
        refresh.setOnPullRefreshListener(this);
        messageInfoLists = new ArrayList<>();
//        initAdapter();
        getMessage(pagable);
    }

    //我的消息
    private void getMessage(final String pagables) {
        PagableRequest pagableRequest = new PagableRequest();
        pagableRequest.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.GET_MESSAGE, new OkHttpClientManager.ResultCallback<MyMessageBean>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(MyMessageBean response) {
                messageInfoList = response.getMessages();
                if (messageInfoList != null && messageInfoList.size() > 0) {
                    if ("0".equals(pagables)) {//第一页
                        messageInfoLists.clear();
                        messageInfoLists.addAll(messageInfoList);
                        Log.i("ee", messageInfoLists.size() + "----" + messageInfoList.size());
                        adapter = new MessageAdapter(messageInfoList);
                        adapter.openLoadAnimation();
                        listView.setAdapter(adapter);
                        adapter.openLoadMore(messageInfoList.size(), true);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                        } else {
                            hasMore = false;
                        }
                        pagable = response.getPagable();
                    } else {
                        messageInfoLists.addAll(messageInfoList);
                        if (response.getHasMore() == 1) {
                            hasMore = true;
                            pagable = response.getPagable();
                            adapter.notifyDataChangedAfterLoadMore(messageInfoList, true);
                        } else {
                            adapter.notifyDataChangedAfterLoadMore(messageInfoList, true);
                            hasMore = false;
                            pagable = "";
                        }
                    }
                    adapter.setOnLoadMoreListener(SettingMessageActivity.this);
                    adapter.setOnRecyclerViewItemChildClickListener(SettingMessageActivity.this);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, pagableRequest, MyMessageBean.class, Application.getInstance().getCurrentActivity());
    }

    @Override
    public void onLoadMoreRequested() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    adapter.notifyDataChangedAfterLoadMore(false);
//                    if (notLoadingView == null) {
//                        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) listView.getParent(), false);
//                    }
//                    filmAdapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getMessage(pagable);
                        }
                    }, delayMillis);
                }
            }

        });
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(getBaseContext())
                .inflate(R.layout.header_loading, null);
        headerProgressBar = (ProgressBar) headerView.findViewById(R.id.header_loading_progress);
        headerSta = (TextView) headerView.findViewById(R.id.header_loading_text);
//        headerTime = (TextView) headerView.findViewById(R.id.header_loading_time);
        headerSta.setText("下拉刷新");
        headerImage = (ImageView) headerView.findViewById(R.id.header_loading_image);
        headerImage.setVisibility(View.VISIBLE);
        headerProgressBar.setVisibility(View.GONE);
        return headerView;
    }

    @Override
    public void onRefresh() {
        headerSta.setText("正在刷新数据中");
        headerImage.setVisibility(View.GONE);
        headerProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pagable = "0";
                getMessage(pagable);
                refresh.setRefreshing(false);
                headerProgressBar.setVisibility(View.GONE);
            }
        }, delayMillis);
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {
        headerSta.setText(enable ? "松开立即刷新" : "下拉刷新");
        headerImage.setVisibility(View.VISIBLE);
        headerImage.setRotation(enable ? 180 : 0);
    }


    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        if (isDelete) {

            if (messageInfoLists.get(position).getIsSelect() == 0) {
                messageInfoLists.get(position).setIsSelect(1);
                selectNum = selectNum + 1;
                delete.setText("删除（" + selectNum + "）");
            } else {
                messageInfoLists.get(position).setIsSelect(0);
                selectNum = selectNum - 1;
                delete.setText("删除（" + selectNum + "）");
            }
            adapter.notifyItemChanged(position);
        } else {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Intent intent = new Intent(SettingMessageActivity.this, SettingMessageDetailsActivity.class);
//        intent.putExtra("messageInfo", messageInfoLists.get(position));
                intent.putExtra("position", position);
                intent.putExtra("messageId", messageInfoLists.get(position).getMessageId());
                startActivityForResult(intent, CODE_REQUEST_ONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                setResult(CODE_RESULT, new Intent().putExtra("isRead", isRead));
                SettingMessageActivity.this.finish();
                break;
            case R.id.base_toolbar__right://图标删除
                if (messageInfoLists.size() > 0) {
                    isDelete = true;
                    selectNum = 0;
                    delete.setText("删除（" + selectNum + "）");
                    deleteLayout.setVisibility(View.VISIBLE);
                    rightCancel.setVisibility(View.VISIBLE);
                    rightDelete.setVisibility(View.GONE);
                    for (int i = 0; i < messageInfoLists.size(); i++) {
                        messageInfoLists.get(i).setDelete(true);
                        messageInfoLists.get(i).setIsSelect(0);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showShortToast("没有消息可删除");
                }
                break;
            case R.id.base_toolbar__right_text://取消删除文字
                isDelete = false;
                selectNum = 0;
                delete.setText("删除（" + selectNum + "）");
                deleteLayout.setVisibility(View.GONE);
                rightCancel.setVisibility(View.GONE);
                rightDelete.setVisibility(View.VISIBLE);
                for (int i = 0; i < messageInfoLists.size(); i++) {
                    messageInfoLists.get(i).setDelete(false);
                    messageInfoLists.get(i).setIsSelect(0);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.content_setting_message__selete_all://全选
                for (int i = 0; i < messageInfoLists.size(); i++) {
                    messageInfoLists.get(i).setIsSelect(1);
                }
                selectNum = messageInfoLists.size();
                delete.setText("删除（" + selectNum + "）");
                adapter.notifyDataSetChanged();
                break;
            case R.id.content_setting_message__delete://删除 确定
                messageIdList = new ArrayList<>();
                for (int i = 0; i < messageInfoLists.size(); i++) {
                    if (messageInfoLists.get(i).getIsSelect() == 1) {
                        messageIdList.add(messageInfoLists.get(i).getMessageId());
                    }
                }
                if (messageIdList.size() > 0) {
                    deleteMessage();
                } else {
                    showShortToast("没有可删除的消息");
                    isDelete = false;
                    selectNum = 0;
                    delete.setText("删除（" + selectNum + "）");
                    deleteLayout.setVisibility(View.GONE);
                    rightCancel.setVisibility(View.GONE);
                    rightDelete.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //删除消息
    private void deleteMessage() {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setIds(messageIdList);
        OkHttpClientManager.postAsyn(Config.DELETE_MESSAGE, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ResponeNull response) {
                isRead = true;
                Log.i("ee", "aa----" + messageInfoLists.size());
                for (int i = 0; i < messageIdList.size(); i++) {
                    Log.i("ee", "bb----" + messageIdList.get(i));
                    for (int j = 0; j < messageInfoLists.size(); j++) {
                        Log.i("ee", "----" + messageInfoLists.get(j).getMessageId());
                        if (messageInfoLists.get(j).getMessageId() == messageIdList.get(i)) {
                            messageInfoLists.remove(j);
                            Log.i("ee", "size----" + messageInfoLists.size());
                            break;
                        }
                    }
                }
                messageIdList.clear();
                isDelete = false;
                selectNum = 0;
                delete.setText("删除（" + selectNum + "）");
                deleteLayout.setVisibility(View.GONE);
                rightCancel.setVisibility(View.GONE);
                rightDelete.setVisibility(View.VISIBLE);
                for (int i = 0; i < messageInfoLists.size(); i++) {
                    messageInfoLists.get(i).setDelete(false);
                    messageInfoLists.get(i).setIsSelect(0);
                }
                adapter = new MessageAdapter(messageInfoLists);
                adapter.openLoadAnimation();
                listView.setAdapter(adapter);
                adapter.openLoadMore(messageInfoLists.size(), true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, deleteRequest, ResponeNull.class, SettingMessageActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;

        if (requestCode == CODE_REQUEST_ONE) {
            int position = data.getIntExtra("position", -1);
            if (!"1".equals(messageInfoLists.get(position).getIsRead())) {
                messageInfoLists.get(position).setIsRead("1");
                adapter.notifyItemChanged(position);
                isRead = true;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(CODE_RESULT, new Intent().putExtra("isRead", isRead));
            SettingMessageActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
