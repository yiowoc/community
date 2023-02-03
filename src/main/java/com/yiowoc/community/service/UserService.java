package com.yiowoc.community.service;

import com.yiowoc.community.mapper.UserMapper;
import com.yiowoc.community.model.User;
import com.yiowoc.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int insertUser(User user) {
        return userMapper.insertSelective(user);
    }

    public User selectUserByToken(String token) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                        .andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(userExample);
        return users.get(0);
    }

    public void insertOrUpdateUser(User newUser) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                        .andAccountIdEqualTo(newUser.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() == 0) {
            newUser.setGmtCreate(System.currentTimeMillis());
            newUser.setGmtModified(newUser.getGmtCreate());
            userMapper.insertSelective(newUser);
        } else {
            User user = users.get(0);
            user.setName(newUser.getName());
            user.setAvatarUrl(newUser.getAvatarUrl());
            user.setToken(newUser.getToken());
            user.setGmtModified(System.currentTimeMillis());
            userMapper.updateByPrimaryKeySelective(user);
        }
    }
}
