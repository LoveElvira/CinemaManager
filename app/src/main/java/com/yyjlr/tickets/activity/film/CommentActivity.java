package com.yyjlr.tickets.activity.film;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyjlr.tickets.Config;
import com.yyjlr.tickets.R;
import com.yyjlr.tickets.activity.AbstractActivity;
import com.yyjlr.tickets.model.ResponeNull;
import com.yyjlr.tickets.requestdata.IdRequest;
import com.yyjlr.tickets.service.Error;
import com.yyjlr.tickets.service.OkHttpClientManager;
import com.yyjlr.tickets.viewutils.Star;

/**
 * Created by Elvira on 2017/4/27.
 * 电影评论
 */

public class CommentActivity extends AbstractActivity implements View.OnClickListener {

    private TextView title;
    private ImageView leftArrow;
    private TextView rightSend;

    private Star star;
    private TextView starNum;
    private TextView starSmallNum;
    private EditText myWord;
    private TextView filmName;
    private String filmId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
    }

    private void initView() {
        bgTitle = (ImageView) findViewById(R.id.base_toolbar__bg);
        initBgTitle(bgTitle);
        title = (TextView) findViewById(R.id.base_toolbar__text);
        title.setText("评论");
        leftArrow = (ImageView) findViewById(R.id.base_toolbar__left);
        leftArrow.setAlpha(1.0f);
        leftArrow.setOnClickListener(this);
        findViewById(R.id.base_toolbar__right).setVisibility(View.GONE);
        rightSend = (TextView) findViewById(R.id.base_toolbar__right_text);
        rightSend.setVisibility(View.VISIBLE);
        rightSend.setOnClickListener(this);
        rightSend.setText("发送");

        star = (Star) findViewById(R.id.content_comment__star);
        starNum = (TextView) findViewById(R.id.content_comment__num);
        starSmallNum = (TextView) findViewById(R.id.content_comment__dot_num);

        myWord = (EditText) findViewById(R.id.content_comment__my_word);
        filmName = (TextView) findViewById(R.id.content_comment__film_name);
        filmId = getIntent().getStringExtra("filmId");
        starNum.setText("0");
        starSmallNum.setText("0");
        filmName.setText(getIntent().getStringExtra("filmName"));
        star.setStarChangeLister(new Star.OnStarChangeListener() {
            @Override
            public void onStarChange(Float mark) {
                String marks = String.format("%.1f", (mark * 2.0));
                int dotPosition = marks.indexOf(".");
                starNum.setText("" + marks.substring(0, dotPosition));
                starSmallNum.setText(marks.substring(dotPosition + 1, marks.length()));
            }
        });
    }

    //发布评论
    private void sendFilmComment(String word) {
        IdRequest idRequest = new IdRequest();
        idRequest.setMovieId(filmId);
        idRequest.setGrade(starNum.getText().toString().trim() + "." + starSmallNum.getText().toString().trim());
        idRequest.setContent(word);
        OkHttpClientManager.postAsyn(Config.ADD_FILM_COMMENT, new OkHttpClientManager.ResultCallback<ResponeNull>() {

            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                showShortToast(info.getInfo());
            }

            @Override
            public void onResponse(ResponeNull response) {
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isUpdate", true));
                CommentActivity.this.finish();
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
//                showShortToast(exception.getMessage());
            }
        }, idRequest, ResponeNull.class, CommentActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_toolbar__left:
                setResult(CODE_RESULT, new Intent()
                        .putExtra("isUpdate", false));
                CommentActivity.this.finish();
                break;
            case R.id.base_toolbar__right_text://发送
                String word = myWord.getText().toString().trim();
                if ("".equals(word)) {
                    showShortToast("评论内容不能为空");
                } else {
                    sendFilmComment(word);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != CODE_RESULT)
            return;
        switch (requestCode){
            case CODE_REQUEST_DIALOG:

                break;
        }
    }
}
