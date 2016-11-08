package com.yyjlr.tickets.adapter;

import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.yyjlr.tickets.model.DataImportUtils;
import com.yyjlr.tickets.model.DataInfo;
import com.yyjlr.tickets.viewutils.grabticket.ItemTicketLayout;
import com.yyjlr.tickets.viewutils.grabticket.TicketFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Elvira on 2016/9/13.
 * 抢票adapter
 */
public class GrabTicketAdapter extends TicketFrameLayout.Adapter{
    List<DataInfo> list= new ArrayList<>();
    private SparseArray<ItemTicketLayout> mCountdownVHList;
    private boolean flag = true;
    private Handler mHandler = new Handler();
    private Timer mTimer;
    private boolean isCancel = true;

    public void set() {
        list.clear();
        mCountdownVHList = new SparseArray<>();
        List<DataInfo> l1 = DataImportUtils.init();

        list.addAll(l1);

        // 校对倒计时
        long curTime = System.currentTimeMillis();
        for (DataInfo itemInfo : list) {
            itemInfo.setEndTime(curTime + itemInfo.countDown);
        }
        startRefreshTime();
        notifyDataSetChanged();
    }

    public void startRefreshTime() {
        if (!isCancel) return;

        if (null != mTimer) {
            mTimer.cancel();
        }

        isCancel = false;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mRefreshTimeRunnable);
            }
        }, 0, 10);
    }

    public void cancelRefreshTime() {
        isCancel = true;
        if (null != mTimer) {
            mTimer.cancel();
        }
        mHandler.removeCallbacks(mRefreshTimeRunnable);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, ViewGroup parent, int expendedHeight, int normalHeight) {
        ItemTicketLayout item = new ItemTicketLayout(parent.getContext());
        item.setData(position, list.get(position), expendedHeight, normalHeight);

        // 处理倒计时
        if (list.get(position).countDown > 0) {
            synchronized (mCountdownVHList) {
                mCountdownVHList.put(Integer.parseInt(list.get(position).id), item);
            }
        }
        return item;
    }

    private Runnable mRefreshTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCountdownVHList.size() == 0) return;

            synchronized (mCountdownVHList) {
                long currentTime = System.currentTimeMillis();
                int key;
                for (int i = 0; i < mCountdownVHList.size(); i++) {
                    key = mCountdownVHList.keyAt(i);
                    ItemTicketLayout curMyViewHolder = mCountdownVHList.get(key);
                    if (currentTime >= curMyViewHolder.getBean().endTime) {
                        // 倒计时结束
                        curMyViewHolder.getBean().setCountDown(0);
                        curMyViewHolder.endTime(0);
                        mCountdownVHList.remove(key);
                    } else {
                        curMyViewHolder.refreshTime(currentTime);
                    }
                }
            }
        }
    };
}
