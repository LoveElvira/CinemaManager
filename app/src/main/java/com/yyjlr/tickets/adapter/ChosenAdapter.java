package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.EventActivity;
import com.yyjlr.tickets.model.ChosenFilmEntity;

import java.util.ArrayList;
import java.util.Collection;

/*
 * Created by Elvira on 2016/7/31.
 * 精选Adapter
*/


public class ChosenAdapter extends BaseAdapter {
    private ArrayList<ChosenFilmEntity> chosenFilmEntityList;
    private int cardWidth,cardHeight;
    private Context context;

    public ChosenAdapter(Context context, int cardWidth, int cardHeight) {
        chosenFilmEntityList = new ArrayList<>();
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.context = context;
    }

    public void addAll(Collection<ChosenFilmEntity> collection) {
        if (isEmpty()) {
            chosenFilmEntityList.addAll(collection);
            notifyDataSetChanged();
        } else {
            chosenFilmEntityList.addAll(collection);
        }
    }

    public void clear() {
        chosenFilmEntityList.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return chosenFilmEntityList.isEmpty();
    }

    public void remove(int index) {
        if (index > -1 && index < chosenFilmEntityList.size()) {
            chosenFilmEntityList.remove(index);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return chosenFilmEntityList.size();
    }

    @Override
    public ChosenFilmEntity getItem(int position) {
        if (chosenFilmEntityList == null || chosenFilmEntityList.size() == 0) return null;
        return chosenFilmEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChosenFilmEntity chosenFilmEntity = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chosen_film, parent, false);
        ViewHolder holder = new ViewHolder();
        convertView.setTag(holder);
        convertView.getLayoutParams().width = cardWidth;
        holder.fileIamge = (SimpleDraweeView) convertView.findViewById(R.id.item_chosen__image);
        holder.fileIamge.getLayoutParams().height = cardHeight;
        holder.fileName = (TextView) convertView.findViewById(R.id.item_chosen__name);
        holder.fileShowTime = (TextView) convertView.findViewById(R.id.item_chosen__showtime);
        holder.filePirce = (TextView) convertView.findViewById(R.id.item_chosen__price);
        holder.fileEnter = (TextView) convertView.findViewById(R.id.item_chosen__enter);

        holder.fileIamge.setImageURI(Uri.parse(chosenFilmEntity.getChosenFilmImage()));
        holder.fileName.setText(chosenFilmEntity.getChosenFilmName());
        holder.fileShowTime.setText(chosenFilmEntity.getChosenFilmShowTime());
        holder.filePirce.setText(chosenFilmEntity.getChosenFilmPrice());

        holder.fileEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Application.getInstance().getCurrentActivity().startActivity(new Intent(context, EventActivity.class));
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        SimpleDraweeView fileIamge;
        TextView fileName;
        TextView fileShowTime;
        TextView filePirce;
        TextView fileEnter;
    }
}
