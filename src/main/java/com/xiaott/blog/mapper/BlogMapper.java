package com.xiaott.blog.mapper;

import com.xiaott.blog.entity.Blog;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.BadLocationException;
import java.util.List;


public interface BlogMapper {

    @Select("select * from blogs where id = #{id}")
    @Results(id="result",value = {
        @Result(column = "blog_html",property = "blogHtml"),
        @Result(column = "blog_md",property = "blogMd"),
        @Result(column = "created_at",property = "createdAt")
    })
    Blog selectById(@Param("id") int id);

    @ResultMap(value="result")
    @Select("select * from blogs")
    List<Blog> selectAll();

    @ResultMap(value="result")
    @Select("select * from blogs where tags like concat('%',#{tag},'%')")
    List<Blog> selectByTag(@Param("tag") String tag);

    @ResultMap(value = "result")
    @Select("select * from blogs where tags like concat('%',#{keywords},'%') or title like concat('%',#{keywords},'%') or abs like concat('%',#{keywords},'%')")
    List<Blog> selectByKeywords(@Param("keywords") String keywords);

    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    @Insert("insert into blogs (title,abs,cover,blog_html,blog_md,created_at,like_num,visited_num,tags) values (#{blog.title},#{blog.abs},#{blog.cover},#{blog.blogHtml},#{blog.blogMd},#{blog.createdAt},#{blog.likeNum},#{blog.visitedNum},#{blog.tags})")
    void insert(@Param("blog") Blog blog);

    @Update("update blogs set title=#{blog.title},abs=#{blog.abs},cover=#{blog.cover},blog_html=#{blog.blogHtml},blog_md=#{blog.blogMd},created_at=#{blog.createdAt},like_num=#{blog.likeNum},visited_num=#{blog.visitedNum},tags=#{blog.tags} where id = #{blog.id}")
    void update(@Param("blog") Blog blog);

    @Delete("delete from blogs where id = #{id}")
    void deleteById(@Param("id") int id);
}
