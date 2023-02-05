package com.yiowoc.community.service;

import com.mysql.cj.util.StringUtils;
import com.yiowoc.community.dto.PaginationDTO;
import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.exception.CustomizeErrorCode;
import com.yiowoc.community.exception.CustomizeException;
import com.yiowoc.community.mapper.QuestionExtMapper;
import com.yiowoc.community.mapper.QuestionMapper;
import com.yiowoc.community.mapper.UserMapper;
import com.yiowoc.community.model.Question;
import com.yiowoc.community.model.QuestionExample;
import com.yiowoc.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

    public int insertQuestion(Question question) {
        return questionMapper.insertSelective(question);
    }

    public PaginationDTO selectQuestionsWithUser(Integer curPage, Integer size) {
        Integer totalCount = (int) questionMapper.countByExample(null);
//        Integer totalCount = questionMapper.selectAllCount();
        Integer totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(curPage < 1) {
            curPage = 1;
        } else if(curPage > totalPage) {
            curPage = totalPage;
        }
        PaginationDTO paginationDTO = new PaginationDTO(totalPage, curPage);
        if(totalPage == 0) return paginationDTO;
        Integer offset = (curPage - 1) * size;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria();
        questionExample.setOrderByClause("gmt_modified desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
//        List<Question> questions = questionMapper.selectQuestionsPage(offset, size);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        if(questions != null && questions.size() != 0) {
            for(Question question: questions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                User user = userMapper.selectByPrimaryKey(question.getCreator());
//                User user = userMapper.selectById(question.getCreator());
                questionDTO.setUser(user);
                questionDTOs.add(questionDTO);
            }
        }
        paginationDTO.setQuestionDTOs(questionDTOs);
        return paginationDTO;
    }

    public PaginationDTO selectQuestionsWithUserByUserId(Integer userId, Integer curPage, Integer size) {
        QuestionExample countExample = new QuestionExample();
        countExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(countExample);
//        Integer totalCount = questionMapper.selectCountByUserId(userId);
        Integer totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(curPage < 1) {
            curPage = 1;
        } else if(curPage > totalPage) {
            curPage = totalPage;
        }
        PaginationDTO paginationDTO = new PaginationDTO(totalPage, curPage);
        if(totalPage == 0) return paginationDTO;
        Integer offset = (curPage - 1) * size;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                        .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
//        List<Question> questions = questionMapper.selectQuestionsPageByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        if(questions != null && questions.size() != 0) {
            for(Question question: questions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                User user = userMapper.selectByPrimaryKey(question.getCreator());
//                User user = userMapper.selectById(question.getCreator());
                questionDTO.setUser(user);
                questionDTOs.add(questionDTO);
            }
        }
        paginationDTO.setQuestionDTOs(questionDTOs);
        return paginationDTO;
    }
//    打开详情页面
    public QuestionDTO selectQuestionById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(questionDTO.getCreator());
        questionDTO.setUser(user);
//        questionDTO.setUser(userMapper.selectById(questionDTO.getCreator()));
        return questionDTO;
    }

    public void insertOrUpdateQuestion(Question question) {
        if(question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        } else {
            question.setGmtModified(System.currentTimeMillis());
            int updated = questionMapper.updateByPrimaryKeySelective(question);
            if(updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void updateQuestionViewCount(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.updateViewCount(question);
    }

    public void updateQuestionCommentCount(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setCommentCount(1);
        questionExtMapper.updateCommentCount(question);
    }

    public List<Question> selectRelatedQuestionByTag(QuestionDTO questionDTO) {
        String regexp = questionDTO.getTag().replace(',', '|');
        Question question = new Question();
        question.setTag(regexp);
        question.setId(questionDTO.getId());
        List<Question> questions = questionExtMapper.selectRelated(question);
        return questions;
    }
}
