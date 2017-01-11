package com.yyjlr.tickets.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.viewutils.zoom.PhotoView;
import com.yyjlr.tickets.viewutils.zoom.PhotoViewAttacher;

/**
 * Created by Elvira on 2017/1/11.
 */

public class LookPhotoActivity extends AbstractActivity {

    private PhotoView photoView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_photo);
        path = getIntent().getStringExtra("path");
        photoView = (PhotoView) findViewById(R.id.look_photo__photo);

        if (!"".equals(path)) {
            Picasso.with(getBaseContext())
                    .load(path)
                    .into(photoView);
        }
        initListener();
    }

    private void initListener() {
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                LookPhotoActivity.this.finish();
            }
        });
    }

}
