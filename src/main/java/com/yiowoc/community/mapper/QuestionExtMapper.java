package com.yiowoc.community.mapper;

import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.dto.QuestionSearchDTO;
import com.yiowoc.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    void updateViewCount(Question question);

    void updateCommentCount(Question question);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionSearchDTO questionSearchDTO);

    List<Question> selectBySearch(QuestionSearchDTO questionSearchDTO);
}
