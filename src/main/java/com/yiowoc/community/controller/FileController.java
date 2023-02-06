package com.yiowoc.community.controller;

import com.yiowoc.community.dto.FileDTO;
import com.yiowoc.community.provider.AliyunProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
public class FileController {
    @Autowired
    AliyunProvider aliyunProvider;
    @PostMapping("/file/upload")
    @ResponseBody
    public FileDTO fileUpload(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String fileUrl = aliyunProvider.uploadFile(file.getOriginalFilename(), file.getInputStream());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setMessage("成功！");
        fileDTO.setUrl(fileUrl);
        return fileDTO;
    }
}
