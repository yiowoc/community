package com.yiowoc.community.mapper;

import com.yiowoc.community.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int insert(User user);

    User selectByToken(String token);

    User selectById(Integer id);

    User selectByAccountId(String accountId);

    void update(User user);
}
