package com.xiaott.blog.service;

import com.xiaott.blog.entity.Blog;
import com.xiaott.blog.entity.BlogIdAndTitle;

import java.util.List;

public interface BlogService {

    List<Blog> list();

    Blog getById(int id);

    List<Blog> getByPage(int startIndex);

    List<BlogIdAndTitle> getTopFive();

    List<Blog> getByKeywords(String keywords);

    List<Blog> getByTag(String tag);

    List<BlogIdAndTitle> getAllBlogsInfo();
    Integer getNums();

    void add(Blog blog);

    void update(Blog blog);

    void deleteById(int id);
}
