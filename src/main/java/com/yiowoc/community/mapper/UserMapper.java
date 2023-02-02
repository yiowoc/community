package com.yiowoc.community.mapper;

import com.yiowoc.community.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    public int insert(User user);
}
