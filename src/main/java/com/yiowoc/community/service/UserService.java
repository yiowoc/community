package com.yiowoc.community.service;

import com.yiowoc.community.mapper.UserMapper;
import com.yiowoc.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int insertUser(User user) {
        return userMapper.insert(user);
    }

    public User selectUserByToken(String token) {
        User user = userMapper.selectByToken(token);
        return user;
    }

    public void insertOrUpdateUser(User newUser) {
        User user = userMapper.selectByAccountId(newUser.getAccountId());
        if(user == null) {
            newUser.setGmtCreate(System.currentTimeMillis());
            newUser.setGmtModified(newUser.getGmtCreate());
            userMapper.insert(newUser);
        } else {
            user.setName(newUser.getName());
            user.setAvatarUrl(newUser.getAvatarUrl());
            user.setToken(newUser.getToken());
            user.setGmtModified(System.currentTimeMillis());
            userMapper.update(user);
        }
    }
}
