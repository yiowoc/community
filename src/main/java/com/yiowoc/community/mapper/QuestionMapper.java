package com.yiowoc.community.mapper;

import com.yiowoc.community.model.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper {
    int insert(Question question);
    List<Question> selectAllQuestions();
    List<Question> selectQuestionsPage(Integer offset, Integer size);
    Integer selectAllCount();
    List<Question> selectQuestionsPageByUserId(Integer userId, Integer offset, Integer size);
    Integer selectCountByUserId(Integer userId);
    Question selectQuestionById(Integer id);
}
