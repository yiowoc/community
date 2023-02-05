package com.yiowoc.community.enums;


public enum NotificationTypeEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");
    private Integer type;
    private String notification;

    public Integer getType() {
        return type;
    }

    public String getNotification() {
        return notification;
    }

    NotificationTypeEnum(Integer type, String notification) {
        this.type = type;
        this.notification = notification;
    }

    public static String notificationOfType(Integer type) {
        for (NotificationTypeEnum typeEnum : NotificationTypeEnum.values()) {
            if(typeEnum.getType() == type) {
                return typeEnum.getNotification();
            }
        }
        return "";
    }
}
