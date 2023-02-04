package com.yiowoc.community.controller;

import com.yiowoc.community.dto.CommentCreateDTO;
import com.yiowoc.community.dto.ResultDTO;
import com.yiowoc.community.exception.CustomizeErrorCode;
import com.yiowoc.community.model.Comment;
import com.yiowoc.community.model.User;
import com.yiowoc.community.service.CommentService;
import com.yiowoc.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @ResponseBody
    @PostMapping("/comment")
    public ResultDTO insertComment(@RequestBody CommentCreateDTO commentCreateDTO,
                                   HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        // 用json传值的方式传输错误信息而不是直接跳转到error页面，会更加温和，同时这类信息可以用ResultDTO来表示
        if(user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        commentService.insertComment(comment);
        return ResultDTO.okOf();
    }
}
