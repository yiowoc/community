package com.yiowoc.community.service;

import com.yiowoc.community.enums.CommentTypeEnum;
import com.yiowoc.community.exception.CustomizeErrorCode;
import com.yiowoc.community.exception.CustomizeException;
import com.yiowoc.community.mapper.CommentExtMapper;
import com.yiowoc.community.mapper.CommentMapper;
import com.yiowoc.community.mapper.QuestionExtMapper;
import com.yiowoc.community.mapper.QuestionMapper;
import com.yiowoc.community.model.Comment;
import com.yiowoc.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;
    @Autowired
    CommentExtMapper commentExtMapper;

    public void insertComment(Comment comment) {
        // 判断post数据是否合法，不合法直接跳转到error页面
        if(comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        // 评论的是某一条评论
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 判断父评论是否存在
            Comment fathorComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(fathorComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            // 增加评论数
            fathorComment.setCommentCount(1);
            commentExtMapper.updateCommentCount(fathorComment);
        } else { // 评论的是问题
            // 判断问题是否存在
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            // 添加评论数
            question.setCommentCount(1);
            questionExtMapper.updateCommentCount(question);
        }

    }
}
