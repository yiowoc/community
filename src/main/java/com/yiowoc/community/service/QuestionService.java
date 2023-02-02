package com.yiowoc.community.service;

import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.mapper.QuestionMapper;
import com.yiowoc.community.mapper.UserMapper;
import com.yiowoc.community.model.Question;
import com.yiowoc.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public int insertQuestion(Question question) {
        return questionMapper.insert(question);
    }

    public List<QuestionDTO> selectQuestionWithUserList() {
        List<Question> questions = questionMapper.selectAllQuestions();
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        if(questions != null && questions.size() != 0) {
            for(Question question: questions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                User user = userMapper.selectById(question.getCreator());
                questionDTO.setUser(user);
                questionDTOs.add(questionDTO);
            }
        }
        return questionDTOs;
    }
}
