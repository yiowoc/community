package com.yiowoc.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Integer parentId;
    private Integer type;
    private String content;
}
