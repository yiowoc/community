package com.yiowoc.community.mapper;

import com.yiowoc.community.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionMapper {
    public int insert(Question question);
}
