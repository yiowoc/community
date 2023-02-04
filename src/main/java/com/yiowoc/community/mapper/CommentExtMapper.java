package com.yiowoc.community.mapper;

import com.yiowoc.community.model.Comment;

public interface CommentExtMapper {

    void updateCommentCount(Comment parentComment);
}
