package com.yiowoc.community.dto;

import lombok.Data;

@Data
public class QuestionSearchDTO {
    private String search;
    private Integer page;
    private Integer size;
}
