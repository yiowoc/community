package com.yiowoc.community.controller;

import com.yiowoc.community.dto.NotificationDTO;
import com.yiowoc.community.enums.NotificationTypeEnum;
import com.yiowoc.community.model.User;
import com.yiowoc.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String readNotification(@PathVariable(name = "id") Integer id,
                                   HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null) {
            return "redirect:/";
        }
        NotificationDTO notificationDTO =  notificationService.read(id, user);
        if(NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
        || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterId();
        }
        return "redirect:/";
    }
}
