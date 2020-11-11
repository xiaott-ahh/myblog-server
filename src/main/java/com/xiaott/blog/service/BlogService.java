package com.xiaott.blog.service;

import com.xiaott.blog.entity.Blog;

import java.util.List;

public interface BlogService {

    List<Blog> list();

    Blog getById(int id);

    List<Blog> getByPage(int startIndex);

    List<Blog> getTopFive();

    List<Blog> getByKeywords(String keywords);

    List<Blog> getByTag(String tag);

    void add(Blog blog);

    void update(Blog blog);

    void deleteById(int id);
}
