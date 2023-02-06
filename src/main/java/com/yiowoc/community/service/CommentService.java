package com.yiowoc.community.service;

import com.yiowoc.community.dto.CommentDTO;
import com.yiowoc.community.enums.CommentTypeEnum;
import com.yiowoc.community.enums.NotificationTypeEnum;
import com.yiowoc.community.exception.CustomizeErrorCode;
import com.yiowoc.community.exception.CustomizeException;
import com.yiowoc.community.mapper.*;
import com.yiowoc.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    UserMapper userMapper;
    @Autowired
    NotificationMapper notificationMapper;

    @Transactional
    public void insertComment(Comment comment, User commentator) {
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
            // 判断问题是否存在-->注意由于只有二级评论，所以父评论的parentId一定是问题id
            Question question = questionMapper.selectByPrimaryKey(fathorComment.getParentId());
            if(question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 插入评论并发送通知
            commentMapper.insertSelective(comment);
            insertNotification(fathorComment.getCommentator(), commentator.getName(), NotificationTypeEnum.REPLY_COMMENT, question, comment.getCommentator());

            // 增加评论数
            fathorComment.setCommentCount(1);
            commentExtMapper.updateCommentCount(fathorComment);
        } else { // 评论的是问题
            // 判断问题是否存在
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 插入评论并发送通知
            commentMapper.insertSelective(comment);
            insertNotification(question.getCreator(), commentator.getName(), NotificationTypeEnum.REPLY_QUESTION, question, comment.getCommentator());
            // 添加评论数
            question.setCommentCount(1);
            questionExtMapper.updateCommentCount(question);
        }
    }
    private void insertNotification(Integer receiver, String notifierName, NotificationTypeEnum notificationType, Question question, Integer notifier) {
        if(receiver == notifier) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setNotifier(notifier);
        notification.setReceiver(receiver);
        notification.setType(notificationType.getType());
        notification.setOuterId(question.getId());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(question.getTitle());
        notificationMapper.insertSelective(notification);
    }

    public List<CommentDTO> selectCommentByParentId(Integer id, CommentTypeEnum commentType) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                        .andParentIdEqualTo(id)
                                .andTypeEqualTo(commentType.getType());
        commentExample.setOrderByClause("gmt_modified desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评价人id --> 节省空间
        Set<Integer> commentatorSet = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> commentatorList = new ArrayList<>();
        commentatorList.addAll(commentatorSet);

        // 获取评价人信息并转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(commentatorList);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 转换comment为commentDTO
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOs;
    }
}
