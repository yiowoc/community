package com.yiowoc.community.service;

import com.yiowoc.community.dto.PaginationDTO;
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

    public PaginationDTO selectQuestionsWithUser(Integer curPage, Integer size) {
        Integer totalCount = questionMapper.selectAllCount();
        Integer totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(curPage < 1) {
            curPage = 1;
        } else if(curPage > totalPage) {
            curPage = totalPage;
        }
        PaginationDTO paginationDTO = new PaginationDTO(totalPage, curPage);
        if(totalPage == 0) return paginationDTO;
        Integer offset = (curPage - 1) * size;
        List<Question> questions = questionMapper.selectQuestionsPage(offset, size);
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
        paginationDTO.setQuestionDTOs(questionDTOs);
        return paginationDTO;
    }

    public PaginationDTO selectQuestionsWithUserByUserId(Integer userId, Integer curPage, Integer size) {
        Integer totalCount = questionMapper.selectCountByUserId(userId);
        Integer totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(curPage < 1) {
            curPage = 1;
        } else if(curPage > totalPage) {
            curPage = totalPage;
        }
        PaginationDTO paginationDTO = new PaginationDTO(totalPage, curPage);
        if(totalPage == 0) return paginationDTO;
        Integer offset = (curPage - 1) * size;
        List<Question> questions = questionMapper.selectQuestionsPageByUserId(userId, offset, size);
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
        paginationDTO.setQuestionDTOs(questionDTOs);
        return paginationDTO;
    }

    public QuestionDTO selectQuestionById(Integer id) {
        Question question = questionMapper.selectQuestionById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(userMapper.selectById(questionDTO.getCreator()));
        return questionDTO;
    }
}
