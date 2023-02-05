package com.yiowoc.community.advice;

import com.alibaba.fastjson.JSON;
import com.yiowoc.community.dto.ResultDTO;
import com.yiowoc.community.exception.CustomizeErrorCode;
import com.yiowoc.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    String handleExceptions(Model model, Throwable e,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        String contentType = request.getContentType();
        if(contentType!= null && contentType.equals("application/json")) {
            ResultDTO resultDTO = null;
            if(e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf(((CustomizeException) e).getCode(),e.getMessage());
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ex) {
            }
            return null;
        } else {
            if(e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return "error";
        }
    }
}
