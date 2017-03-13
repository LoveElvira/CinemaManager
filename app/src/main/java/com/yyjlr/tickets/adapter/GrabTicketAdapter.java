package com.yyjlr.tickets.adapter;

import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.yyjlr.tickets.model.DataImportUtils;
import com.yyjlr.tickets.model.DataInfo;
import com.yyjlr.tickets.model.ticket.TicketModel;
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
//    List<DataInfo> list= new ArrayList<>();
//    private SparseArray<ItemTicketLayout> mCountdownVHList;
//    private boolean flag = true;
//    private Handler mHandler = new Handler();
//    private Timer mTimer;
//    private boolean isCancel = true;

    private List<TicketModel> ticketModelList;

    public GrabTicketAdapter(List<TicketModel> ticketModelList) {
        this.ticketModelList = ticketModelList;
//        mCountdownVHList = new SparseArray<>();
//        List<DataInfo> l1 = DataImportUtils.init();
//        this.ticketModelList = ticketModelList;

//        list.addAll(l1);

        // 校对倒计时
//        long curTime = System.currentTimeMillis();
//        for (TicketModel itemInfo : ticketModelList) {
//            itemInfo.setEndTime(curTime + (itemInfo.getEndTime()-itemInfo.getStartTime())/*itemInfo.countDown*/);
//        }
//        startRefreshTime();
    }


//    public void startRefreshTime() {
//        if (!isCancel) return;
//
//        if (null != mTimer) {
//            mTimer.cancel();
//        }
//
//        isCancel = false;
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.post(mRefreshTimeRunnable);
//            }
//        }, 0, 10);
//    }

//    public void cancelRefreshTime() {
//        isCancel = true;
//        if (null != mTimer) {
//            mTimer.cancel();
//        }
//        mHandler.removeCallbacks(mRefreshTimeRunnable);
//    }

    @Override
    public int getCount() {
        return ticketModelList.size();
    }

    @Override
    public View getView(int position, ViewGroup parent, int expendedHeight, int normalHeight) {
        ItemTicketLayout item = new ItemTicketLayout(parent.getContext());
        item.setData(position, ticketModelList.get(position), expendedHeight, normalHeight);

        // 处理倒计时
//        if ((ticketModelList.get(position).getEndTime()-ticketModelList.get(position).getStartTime()) > 0) {
//            synchronized (mCountdownVHList) {
//                mCountdownVHList.put(Integer.parseInt(ticketModelList.get(position).getActivityId()+""), item);
//            }
//        }
        return item;
    }

//    private Runnable mRefreshTimeRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (mCountdownVHList.size() == 0) return;
//
//            synchronized (mCountdownVHList) {
//                long currentTime = System.currentTimeMillis();
//                int key;
//                for (int i = 0; i < mCountdownVHList.size(); i++) {
//                    key = mCountdownVHList.keyAt(i);
//                    ItemTicketLayout curMyViewHolder = mCountdownVHList.get(key);
//                    if (currentTime >= curMyViewHolder.getBean().getEndTime()) {
//                        // 倒计时结束
////                        curMyViewHolder.getBean().setEndTime(0);
//                        curMyViewHolder.endTime(0);
//                        mCountdownVHList.remove(key);
//                    } else {
//                        curMyViewHolder.refreshTime(currentTime);
//                    }
//                }
//            }
//        }
//    };
}
