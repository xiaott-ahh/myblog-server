package com.xiaott.blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Blog {

    private int id;
    //标题
    private String title;
    //摘要
    private String abs;
    //封面
    private String cover;

    //md渲染后的html
    private String blogHtml;
    //md源码
    private String blogMd;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;
    //点赞数
    private int likeNum;
    //浏览数
    private long visitedNum;

    //标签
    private String tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBlogHtml() {
        return blogHtml;
    }

    public void setBlogHtml(String blogHtml) {
        this.blogHtml = blogHtml;
    }

    public String getBlogMd() {
        return blogMd;
    }

    public void setBlogMd(String blogMd) {
        this.blogMd = blogMd;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public long getVisitedNum() {
        return visitedNum;
    }

    public void setVisitedNum(long visitedNum) {
        this.visitedNum = visitedNum;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
