package com.yyjlr.tickets.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyjlr.tickets.Application;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.BasePhotoActivity;
import com.yyjlr.tickets.helputils.BitmapUtils;
import com.yyjlr.tickets.helputils.ImageFileUtils;
import com.yyjlr.tickets.viewutils.cilpphoto.ClipImageLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Elvira on 2016/8/18.
 * 裁剪图片
 */
public class ClipPhotoActivity extends BasePhotoActivity implements View.OnClickListener {

    private ClipImageLayout mClipImageLayout;
    private TextView title;
    private ImageView leftArrow;

    public static void startActivity(Activity activity, String path, int code) {
        Intent intent = new Intent(activity, ClipPhotoActivity.class);
        intent.putExtra("path", path);
        activity.startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_clip_photo);
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("裁剪图片");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.clip_photo);
        mClipImageLayout.mClipImageView.COLOR_BORDER = R.color.white;
        mClipImageLayout.mClipImageView.COLOR_BACKGROUND = R.color.black_alpha_3;
        String path = getIntent().getStringExtra("path");
        try {
            mClipImageLayout.setImageBitmap(BitmapUtils.revitionImageSize(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        findViewById(R.id.clip_photo_confirm).setOnClickListener(this);
        findViewById(R.id.clip_photo_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.clip_photo_confirm) {

            Bitmap bitmap = mClipImageLayout.clip();

            String rootDir = ImageFileUtils.getRootDir(Application.getInstance().getCurrentActivity());
            // 创建应用缓存目录
            String appCacheDir = rootDir + File.separator + ImageFileUtils.LBTWYYJS;
            File dir = new File(appCacheDir);
            if (!dir.exists())
                dir.mkdirs();
            // 创建应用缓存文件

            String path = appCacheDir + File.separator + UUID.randomUUID() + ".jpg";
            saveBitmap(bitmap, path);

            Intent intent = new Intent();
            intent.putExtra("path", path);
            setResult(RESULT_OK, intent);
        }

        ClipPhotoActivity.this.finish();
    }


    private void saveBitmap(Bitmap bitmap, String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }

        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
