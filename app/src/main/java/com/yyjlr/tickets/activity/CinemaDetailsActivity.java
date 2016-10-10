package com.yyjlr.tickets.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvira on 2016/8/1.
 * 影院详情
 */
public class CinemaDetailsActivity extends AbstractActivity {

    private RecyclerView listView;
    private CinemaAdapter adapter;
    private List<String> typeDate;
    private ImageView leftArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cinema_details);
        leftArrow = (ImageView) findViewById(R.id.content_cinema__left);
        listView = (RecyclerView) findViewById(R.id.content_cinema__listview);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listView.setLayoutManager(linearLayoutManager);
        getDate();
        adapter = new CinemaAdapter(CinemaDetailsActivity.this, typeDate);
        listView.setAdapter(adapter);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CinemaDetailsActivity.this.finish();
            }
        });

    }

    private void getDate(){
        typeDate = new ArrayList<String>();
        typeDate.add("3D眼镜");
        typeDate.add("儿童票");
        typeDate.add("停车场");
        typeDate.add("IMAX");
        typeDate.add("4D");
        typeDate.add("4K");
        typeDate.add("ATMOS");
    }

    //adapter
    private class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.ViewHolder>{

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
            if (list != null && list.size()>0)
                return list.size();
            else
                return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView type;
            public ViewHolder(View itemView) {
                super(itemView);
                type = (TextView) itemView.findViewById(R.id.item_cinema__type);
            }
        }
    }
}
