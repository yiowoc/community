package com.yiowoc.community.service;

import com.yiowoc.community.dto.PaginationDTO;
import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.dto.QuestionSearchDTO;
import com.yiowoc.community.exception.CustomizeErrorCode;
import com.yiowoc.community.exception.CustomizeException;
import com.yiowoc.community.mapper.QuestionExtMapper;
import com.yiowoc.community.mapper.QuestionMapper;
import com.yiowoc.community.mapper.UserMapper;
import com.yiowoc.community.model.Question;
import com.yiowoc.community.model.QuestionExample;
import com.yiowoc.community.model.User;
import org.apache.commons.lang3.StringUtils;
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

    // 列出所有用户的问题数据用于首页
    public PaginationDTO selectQuestionDTOs(Integer curPage, Integer size, String search) {
        if(!StringUtils.isBlank(search)) {
            String regexp = search.replace(' ', '|');
        }
        QuestionSearchDTO questionSearchDTO = new QuestionSearchDTO();
        questionSearchDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionSearchDTO);
        Integer totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(curPage < 1) {
            curPage = 1;
        } else if(curPage > totalPage) {
            curPage = totalPage;
        }
        PaginationDTO paginationDTO = new PaginationDTO(totalPage, curPage);
        if(totalPage == 0) return paginationDTO;
        Integer offset = (curPage - 1) * size;
        //questionSearchDTO.setPage(curPage);
        questionSearchDTO.setPage(offset);
        questionSearchDTO.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionSearchDTO);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        if(questions != null && questions.size() != 0) {
            for(Question question: questions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                questionDTO.setUser(user);
                questionDTOs.add(questionDTO);
            }
        }
        paginationDTO.setData(questionDTOs);
        return paginationDTO;
    }

    // 通过用户id找寻问题数据
    public PaginationDTO selectQuestionDTOsByUserId(Integer userId, Integer curPage, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        Integer totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(curPage < 1) {
            curPage = 1;
        } else if(curPage > totalPage) {
            curPage = totalPage;
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO(totalPage, curPage);
        if(totalPage == 0) return paginationDTO;
        Integer offset = (curPage - 1) * size;
        questionExample.setOrderByClause("gmt_modified desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        if(questions != null && questions.size() != 0) {
            for(Question question: questions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                questionDTO.setUser(user);
                questionDTOs.add(questionDTO);
            }
        }
        paginationDTO.setData(questionDTOs);
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
