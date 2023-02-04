package com.yiowoc.community.enums;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

    private Integer type;
    CommentTypeEnum(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum typeEnum : CommentTypeEnum.values()) {
            if(typeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

}
