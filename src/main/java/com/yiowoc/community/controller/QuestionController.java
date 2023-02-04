package com.yiowoc.community.controller;

import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String getQuestion(@PathVariable(name = "id") Integer id,
                              Model model) {
        questionService.updateQuestionViewCount(id);
        QuestionDTO questionDTO = questionService.selectQuestionById(id);
        model.addAttribute("questionDTO", questionDTO);
        return "question";
    }
}
