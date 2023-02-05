package com.yiowoc.community.controller;

import com.yiowoc.community.dto.PaginationDTO;
import com.yiowoc.community.model.Notification;
import com.yiowoc.community.model.User;
import com.yiowoc.community.service.NotificationService;
import com.yiowoc.community.service.QuestionService;
import com.yiowoc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String getProfile(HttpServletRequest request, Model model,
                             @PathVariable(name = "action") String action,
                             @RequestParam(name = "curPage", defaultValue = "1") Integer curPage,
                             @RequestParam(name = "size", defaultValue = "2") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null) {
            return "redirect:/";
        }
        model.addAttribute("section", action);
        PaginationDTO paginationDTO = null;
        if(action.equals("questions")) {
            model.addAttribute("sectionName", "我的问题");
            paginationDTO = questionService.selectQuestionDTOsByUserId(user.getId(), curPage, size);
        } else if(action.equals("replies")) {
            model.addAttribute("sectionName", "最新回复");
            paginationDTO = notificationService.selectNotificationDTOsByUserId(user.getId(), curPage, size);
        }
        model.addAttribute("paginationDTO", paginationDTO);
        return "profile";
    }
}
