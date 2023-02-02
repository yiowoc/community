package com.yiowoc.community.controller;

import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.model.User;
import com.yiowoc.community.service.QuestionService;
import com.yiowoc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String getIndex(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0) {
            for(Cookie cookie: cookies) {
                if(cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userService.selectUserByToken(token);
                    if(user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        List<QuestionDTO> questionDTOs = questionService.selectQuestionWithUserList();
        model.addAttribute("questions", questionDTOs);
        return "index";
    }
}
