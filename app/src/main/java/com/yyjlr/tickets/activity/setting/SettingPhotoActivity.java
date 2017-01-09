package com.yyjlr.tickets.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.BasePhotoActivity;
import com.yyjlr.tickets.utils.BitmapUtils;
import com.yyjlr.tickets.utils.ImageFileUtils;
import com.yyjlr.tickets.viewutils.cilpphoto.ClipImageLayout;

import java.io.IOException;

/**
 * Created by Elvira on 2016/8/17.
 * 设置头像
 */
public class SettingPhotoActivity extends BasePhotoActivity implements View.OnClickListener {

    private ClipImageLayout showPhoto;
    private ImageView back;
    private ImageView selectPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_account_setting_photo);
        showPhoto = (ClipImageLayout) findViewById(R.id.content_setting_photo__show);

        back = (ImageView) findViewById(R.id.back);
        selectPhoto = (ImageView) findViewById(R.id.select_photo);
        back.setOnClickListener(this);
        selectPhoto.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPhoto.mClipImageView.COLOR_BORDER = R.color.white_alpha_0;
        showPhoto.mClipImageView.COLOR_BACKGROUND = R.color.white_alpha_0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                SettingPhotoActivity.this.finish();
                break;
            case R.id.select_photo:
                View parent = LayoutInflater.from(SettingPhotoActivity.this).inflate(R.layout.content_account_setting_photo,null);
                new PopupWindows(SettingPhotoActivity.this,parent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (!mIsKitKat) {//低于4.4的版本
            switch (requestCode) {
                case CODE_CAMERA_REQUEST_TWO://调用相机返回
                    if (resultCode == RESULT_OK) {
                        ClipPhotoActivity.startActivity(SettingPhotoActivity.this, mPublishPhotoPath, CODE_CLIP_REQUEST);
//                        updateImage(mPublishPhotoPath);
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
                case CODE_GALLERY_REQUEST_TWO://调用相册返回

                    if (resultCode == RESULT_CANCELED) {
                        showShortToast("取消了选择图片");
                    } else if (resultCode == RESULT_OK) {
                        if (data != null && data.getData() != null) {
                            String path = getPhotoName(data.getData(), true, "");
                            ClipPhotoActivity.startActivity(SettingPhotoActivity.this, path, CODE_CLIP_REQUEST);
//                            updateImage(path);
                        }
                    }
                    break;

                case CODE_CLIP_REQUEST:
                    updateImage(data.getStringExtra("path"));
                    break;
            }
        } else {//高于4.4
            switch (requestCode) {
                case CODE_CAMERA_REQUEST_ONE:
                    if (resultCode == RESULT_OK) {
                        ClipPhotoActivity.startActivity(SettingPhotoActivity.this, mPublishPhotoPath, CODE_CLIP_REQUEST);
//                        updateImage(mPublishPhotoPath);
                    } else {
                        showShortToast("取消了拍照");
                    }
                    break;
                case CODE_GALLERY_REQUEST_ONE:

                    if (resultCode == RESULT_CANCELED) {
                        showShortToast("取消了选择图片");
                    } else if (resultCode == RESULT_OK) {
                        if (data != null && data.getData() != null) {
                            String path = ImageFileUtils.getPath(this, data.getData());
                            Log.i("ee",path);
                            ClipPhotoActivity.startActivity(SettingPhotoActivity.this, path, CODE_CLIP_REQUEST);
//                            updateImage(path);
                        }
                    }
                    break;
                case CODE_CLIP_REQUEST:
                    updateImage(data.getStringExtra("path"));
                    break;
            }
        }
    }

    //更新头像
    private void updateImage(String path) {
        try {
            showPhoto.setImageBitmap(BitmapUtils.revitionImageSize(path));
//            SettingAccountActivity.headImage.setImageBitmap(BitmapUtils.revitionImageSize(path));
//            upLoadImage(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
