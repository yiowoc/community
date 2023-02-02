package com.yiowoc.community.service;

import com.yiowoc.community.mapper.QuestionMapper;
import com.yiowoc.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    public int insertQuestion(Question question) {
        return questionMapper.insert(question);
    }
}
