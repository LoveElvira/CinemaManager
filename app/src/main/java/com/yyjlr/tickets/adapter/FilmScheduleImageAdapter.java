package com.yyjlr.tickets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yyjlr.tickets.R;

import java.util.List;

/**
 * Created by Elvira on 2016/10/31.
 */

public class FilmScheduleImageAdapter extends android.widget.BaseAdapter{

    private List<Integer> list;

    public FilmScheduleImageAdapter(List<Integer> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_film_schedule_image,
                    parent, false);
        }

        ImageView iv = (ImageView) convertView.findViewById(R.id.item_film_schedule__image);
        iv.setImageResource(list.get(position));
        return convertView;
    }
}
//        extends BaseAdapter<Integer>{
//
//    public FilmScheduleImageAdapter(List<Integer> data) {
//        super(R.layout.item_film_schedule_image, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, Integer item, int position) {
//        helper.setImageResource(R.id.item_film_schedule__image,item);
//    }
//}
// extends PagerAdapter implements CardAdapter {
//
//    private List<CardView> mViews;
//    public Context mContext;
//
//    public List<Integer> mData;
//    private float mBaseElevation;
//    public FilmScheduleImageAdapter(Context context, List<Integer> list) {
//        mContext = context;
//        mViews = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            mViews.add(null);
//        }
//        this.mData = list;
//    }
//
//    public float getBaseElevation() {
//        return mBaseElevation;
//    }
//
//    @Override
//    public CardView getCardViewAt(int position) {
//        return mViews.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return mData.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public Object instantiateItem(final ViewGroup container, final int position) {
//        View view = LayoutInflater.from(container.getContext())
//                .inflate(R.layout.item_film_schedule_image, container, false);
//        container.addView(view);
//        CardView cardView = (CardView) view.findViewById(R.id.item_film_schedule__cardView);
//        ImageView mIcon=(ImageView)view.findViewById(R.id.item_film_schedule__image);
//        if (mBaseElevation == 0) {
//            mBaseElevation = cardView.getCardElevation();
//        }
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mOnItemClickListener!=null)mOnItemClickListener.onClick(position);
//            }
//        });
//        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
//        mViews.set(position, cardView);
//
//        mIcon.setImageResource(mData.get(position));
//        return view;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//        mViews.set(position, null);
//    }
//    OnItemClickListener mOnItemClickListener;
//    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
//        this.mOnItemClickListener=mOnItemClickListener;
//    }
//    public interface OnItemClickListener{
//        void onClick(int position);
//    }
//}
