package com.yiowoc.community.controller;

import com.yiowoc.community.model.User;
import com.yiowoc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getIndex(HttpServletRequest request) {
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
        return "index";
    }
}
