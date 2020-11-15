package com.xiaott.blog.mapper;

import com.xiaott.blog.entity.Blog;
import com.xiaott.blog.entity.BlogIdAndTitle;
import org.apache.ibatis.annotations.*;
import java.util.List;


public interface BlogMapper {

    @Select("select * from blogs where id = #{id}")
    @Results(id="result",value = {
        @Result(column = "blog_html",property = "blogHtml"),
        @Result(column = "blog_md",property = "blogMd"),
        @Result(column = "created_at",property = "createdAt"),
        @Result(column = "visited_num",property = "visitedNum")
    })
    Blog selectById(@Param("id") int id);

    @ResultMap(value="result")
    @Select("select * from blogs")
    List<Blog> selectAll();

    @ResultMap(value = "result")
    @Select("select * from blogs order by visited_num desc limit #{startIndex},9")
    List<Blog> selectFromStartIndex(@Param("startIndex") int startIndex);

    @ResultMap(value="result")
    @Select("select * from blogs where tags like concat('%',#{tag},'%')")
    List<Blog> selectByTag(@Param("tag") String tag);

    @ResultMap(value = "result")
    @Select("select * from blogs where tags like concat('%',#{keywords},'%') or title like concat('%',#{keywords},'%') or abs like concat('%',#{keywords},'%')")
    List<Blog> selectByKeywords(@Param("keywords") String keywords);

    @Results(id="idAndTitle",value = {
            @Result(column = "id",property = "id"),
            @Result(column = "title",property = "title"),
            @Result(column = "created_at",property = "createdAt")
    })
    @Select("select id,title,created_at from blogs")
    List<BlogIdAndTitle> selectBlogIdAndTitle();

    @Select("select count(*) from blogs")
    Integer selectNums();

    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    @Insert("insert into blogs (title,abs,cover,blog_html,blog_md,created_at,like_num,tags) values (#{blog.title},#{blog.abs},#{blog.cover},#{blog.blogHtml},#{blog.blogMd},#{blog.createdAt},#{blog.likeNum},#{blog.tags})")
    void insert(@Param("blog") Blog blog);

    @Update("update blogs set title=#{blog.title},abs=#{blog.abs},cover=#{blog.cover},blog_html=#{blog.blogHtml},blog_md=#{blog.blogMd},created_at=#{blog.createdAt},like_num=#{blog.likeNum},visited_num=#{blog.visitedNum},tags=#{blog.tags} where id = #{blog.id}")
    void update(@Param("blog") Blog blog);

    @Delete("delete from blogs where id = #{id}")
    void deleteById(@Param("id") int id);
}
