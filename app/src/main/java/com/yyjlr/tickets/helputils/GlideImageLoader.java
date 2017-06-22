package com.yyjlr.tickets.helputils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;
import com.yyjlr.tickets.R;

/**
 * Created by Elvira on 17/4/6.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Picasso.with(context)
                .load(path.toString())
                .placeholder(R.mipmap.bg)
                .error(R.mipmap.bg)
                .into(imageView);
    }
}
