package com.yiowoc.community.mapper;

import com.yiowoc.community.model.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper {
    public int insert(Question question);
    public List<Question> selectAllQuestions();
    public List<Question> selectQuestionsPage(Integer offset, Integer size);
    public Integer selectAllCount();
    public List<Question> selectQuestionsPageByUserId(Integer userId, Integer offset, Integer size);
    public Integer selectCountByUserId(Integer userId);
}
