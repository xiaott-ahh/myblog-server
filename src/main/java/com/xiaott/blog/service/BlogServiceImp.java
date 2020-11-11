package com.xiaott.blog.service;

import com.xiaott.blog.entity.Blog;
import com.xiaott.blog.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class BlogServiceImp implements BlogService{

    @Autowired
    BlogMapper blogMapper;

    @Override
    public List<Blog> list() {
        try {
            return blogMapper.selectAll();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Blog getById(int id) {
        try {
            return blogMapper.selectById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Blog> getByPage(int startIndex) {
        try {
            return blogMapper.selectFromStartIndex(startIndex);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Blog> getTopFive() {
        try {
            return blogMapper.selectAll()
                             .stream()
                             .sorted(Comparator.comparing(Blog::getCreatedAt).reversed())
                             .limit(5)
                             .collect(Collectors.toList());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Blog> getByKeywords(String keywords) {
        try {
            return blogMapper.selectByKeywords(keywords);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Blog> getByTag(String tag) {
        try {
            return blogMapper.selectByTag(tag);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void add(Blog blog) {
        try {
            blogMapper.insert(blog);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("添加文章失败.");
        }
    }

    @Override
    public void update(Blog blog) {
        try {
            blogMapper.update(blog);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新文章失败.");
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            blogMapper.deleteById(id);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除文章失败.");
        }
    }


}
