package com.yiowoc.community.controller;

import com.yiowoc.community.dto.QuestionDTO;
import com.yiowoc.community.model.Question;
import com.yiowoc.community.model.User;
import com.yiowoc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String getPublish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String postPublish(@RequestParam(name = "title") String title,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "tag") String tag,
                              @RequestParam(name = "id", required = false) Integer id,
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
            User user = (User) request.getSession().getAttribute("user");
            if(user == null) {
                model.addAttribute("error", "用户未登录");
            } else {
                Question question = new Question();
                question.setId(id);
                question.setTitle(title);
                question.setDescription(description);
                question.setTag(tag);
                question.setCreator(user.getId());
                questionService.insertOrUpdateQuestion(question);
                return "redirect:/";
            }
        }
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String updatePublish(@PathVariable(name = "id") Integer id,
                                Model model) {
        QuestionDTO questionDTO = questionService.selectQuestionById(id);
        if(questionDTO != null) {
            model.addAttribute("title", questionDTO.getTitle());
            model.addAttribute("description", questionDTO.getDescription());
            model.addAttribute("tag", questionDTO.getTag());
        }
        return "publish";
    }
}
