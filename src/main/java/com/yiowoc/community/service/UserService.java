package com.yiowoc.community.service;

import com.yiowoc.community.mapper.UserMapper;
import com.yiowoc.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public int insertUser(User user) {
        return userMapper.insert(user);
    }
}
