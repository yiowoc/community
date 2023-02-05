package com.yiowoc.community.mapper;

import com.yiowoc.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    void updateViewCount(Question question);

    void updateCommentCount(Question question);

    List<Question> selectRelated(Question question);
}
