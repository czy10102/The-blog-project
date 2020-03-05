package com.czy.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    NOT_LOGIIN("1001","请先登录哦"),
    QUESTION_ID_NOT_FOUND("1002","问题id为空"),
    QUESTION_NOT_FOUND("1003","问题不存在"),
    TARGET_NOT_FOUND("1004","未选中任何问题或评论!"),
    TARGET_NOT_WARING("1005","评论错误"),
    PHOTO_UPLOADFALE("1005","评论错误"),
    USER_ID_ISNOT("99999","用户id为空");
    private String message;
    private String code;
    CustomizeErrorCode(String code,String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }
}
