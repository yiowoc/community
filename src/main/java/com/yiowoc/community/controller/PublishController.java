package com.yiowoc.community.controller;

import com.yiowoc.community.model.Question;
import com.yiowoc.community.model.User;
import com.yiowoc.community.service.QuestionService;
import com.yiowoc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/publish")
    public String getPublish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String postPublish(@RequestParam(name = "title") String title,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "tag") String tag,
                              HttpServletRequest request,
                              Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if(title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
        } else if(description == null || description == "") {
            model.addAttribute("error", "问题补充不能为空");
        } else if(tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
        } else {
            User user = null;
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for(Cookie cookie: cookies) {
                    if(cookie.getName().equals("token")) {
                        String token = cookie.getValue();
                        user = userService.selectUserByToken(token);
                        break;
                    }
                }
            }
            if(user == null) {
                model.addAttribute("error", "用户未登录");
                return "publish";
            }
            Question question = new Question();
            question.setTitle(title);
            question.setDescription(description);
            question.setTag(tag);
            question.setCreator(user.getId());
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionService.insertQuestion(question);

            return "redirect:/";
        }
        return "publish";
    }
}
