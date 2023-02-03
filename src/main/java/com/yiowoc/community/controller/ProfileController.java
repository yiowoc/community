package com.yiowoc.community.controller;

import com.yiowoc.community.dto.PaginationDTO;
import com.yiowoc.community.model.User;
import com.yiowoc.community.service.QuestionService;
import com.yiowoc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String getProfile(HttpServletRequest request, Model model,
                             @PathVariable(name = "action") String action,
                             @RequestParam(name = "curPage", defaultValue = "1") Integer curPage,
                             @RequestParam(name = "size", defaultValue = "2") Integer size) {
        Cookie[] cookies = request.getCookies();
        User user = null;
        if(cookies != null && cookies.length != 0) {
            for(Cookie cookie: cookies) {
                if(cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userService.selectUserByToken(token);
                    if(user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if(user == null) {
            return "redirect:/";
        }
        model.addAttribute("section", action);
        if(action.equals("questions")) {
            model.addAttribute("sectionName", "我的问题");
        } else if(action.equals("replies")) {
            model.addAttribute("sectionName", "最新回复");
        }
        PaginationDTO paginationDTO = questionService.selectQuestionsWithUserByUserId(user.getId(), curPage, size);
        model.addAttribute("paginationDTO", paginationDTO);
        return "profile";
    }
}
