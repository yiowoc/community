package com.yiowoc.community.controller;

import com.yiowoc.community.dto.PaginationDTO;
import com.yiowoc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String getIndex(@RequestParam(name = "curPage", defaultValue = "1") Integer curPage,
                           @RequestParam(name = "size", defaultValue = "2") Integer size,
                           @RequestParam(name = "search", required = false) String search,
                           Model model) {
        PaginationDTO paginationDTO = questionService.selectQuestionDTOs(curPage, size, search);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("search", search);
        return "index";
    }
}
