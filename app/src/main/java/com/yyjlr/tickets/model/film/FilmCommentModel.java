package com.yyjlr.tickets.model.film;

import java.util.List;

/**
 * Created by Elvira on 2017/5/9.
 */

public class FilmCommentModel {
    private int hasMore;//是否还有卖品信息：1：有；0：所有卖品信息已经全部加载完成
    private String pagable;//分页参数
    private List<FilmComment> commentList;//评论信息列表

    public FilmCommentModel() {
    }

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }

    public List<FilmComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<FilmComment> commentList) {
        this.commentList = commentList;
    }
}
