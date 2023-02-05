package com.yiowoc.community.controller;

import com.yiowoc.community.dto.CommentDTO;
import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.enums.CommentTypeEnum;
import com.yiowoc.community.model.Question;
import com.yiowoc.community.service.CommentService;
import com.yiowoc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String getQuestion(@PathVariable(name = "id") Integer id,
                              Model model) {
        questionService.updateQuestionViewCount(id);
        QuestionDTO questionDTO = questionService.selectQuestionById(id);
        List<CommentDTO> commentDTOs = commentService.selectCommentByParentId(id, CommentTypeEnum.QUESTION);
        List<Question> relatedQuestions = questionService.selectRelatedQuestionByTag(questionDTO);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentDTOs", commentDTOs);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
