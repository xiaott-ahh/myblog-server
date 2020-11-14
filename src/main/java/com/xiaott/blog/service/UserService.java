package com.xiaott.blog.service;

import com.xiaott.blog.entity.User;
import com.xiaott.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User getUserByName(String username) {
        try {
            return userMapper.getUserByName(username);
        }catch (Exception e) {
            return null;
        }
    }
}
