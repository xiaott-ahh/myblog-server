package com.xiaott.blog.service;

import com.xiaott.blog.entity.Blog;

import java.util.List;

public interface BlogService {

    public List<Blog> list();

    public Blog getById(int id);

    public List<Blog> getTopFive();

    public List<Blog> getByKeywords(String keywords);

    public List<Blog> getByTag(String tag);

    public void add(Blog blog);

    public void update(Blog blog);

    public void deleteById(int id);
}
