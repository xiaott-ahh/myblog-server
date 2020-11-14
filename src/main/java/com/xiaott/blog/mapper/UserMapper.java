package com.xiaott.blog.mapper;

import com.xiaott.blog.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User getUserByName(@Param("username") String username);

}
