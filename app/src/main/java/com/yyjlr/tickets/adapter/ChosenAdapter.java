package com.yyjlr.tickets.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.model.ChosenFilmEntity;

import java.util.List;

/*
 * Created by Elvira on 2016/7/31.
 * 精选Adapter
*/


public class ChosenAdapter extends BaseAdapter<ChosenFilmEntity> {

    private int cardWidth,cardHeight;
    private Context context;

    public ChosenAdapter(List<ChosenFilmEntity> data) {
        super(R.layout.item_chosen_film,data);
    }

    public void setImageSize(Context context, int cardWidth, int cardHeight) {
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChosenFilmEntity item, int position) {
//        helper.getConvertView().getLayoutParams().width = cardWidth;
        helper.setText(R.id.item_chosen__name,item.getChosenFilmName())
                .setText(R.id.item_chosen__showtime, item.getChosenFilmShowTime())
                .setText(R.id.item_chosen__price,item.getChosenFilmPrice())
                .setOnClickListener(R.id.item_chosen__enter,new OnItemChildClickListener());
        Picasso.with(context)
                .load(item.getChosenFilmImage())
//                .resize(cardWidth, cardHeight)
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView)helper.getView(R.id.item_chosen__image));
    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ChosenFilmEntity chosenFilmEntity = getItem(position);
//        if (convertView == null)
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_chosen_film, parent, false);
//        ViewHolder holder = new ViewHolder();
//        convertView.setTag(holder);
//        convertView.getLayoutParams().width = cardWidth;
//        holder.fileIamge = (SimpleDraweeView) convertView.findViewById(R.id.item_chosen__image);
//        holder.fileIamge.getLayoutParams().height = cardHeight;
//        holder.fileName = (TextView) convertView.findViewById(R.id.item_chosen__name);
//        holder.fileShowTime = (TextView) convertView.findViewById(R.id.item_chosen__showtime);
//        holder.filePirce = (TextView) convertView.findViewById(R.id.item_chosen__price);
//        holder.fileEnter = (TextView) convertView.findViewById(R.id.item_chosen__enter);
//
//        holder.fileIamge.setImageURI(Uri.parse(chosenFilmEntity.getChosenFilmImage()));
//        holder.fileName.setText(chosenFilmEntity.getChosenFilmName());
//        holder.fileShowTime.setText(chosenFilmEntity.getChosenFilmShowTime());
//        holder.filePirce.setText(chosenFilmEntity.getChosenFilmPrice());
//
//        holder.fileEnter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Application.getInstance().getCurrentActivity().startActivity(new Intent(context, EventActivity.class));
//            }
//        });
//
//        return convertView;
//    }
//
//    public static class ViewHolder {
//        SimpleDraweeView fileIamge;
//        TextView fileName;
//        TextView fileShowTime;
//        TextView filePirce;
//        TextView fileEnter;
//    }
}
