package com.czy.exception;

public enum CommonErrorCode implements ICustomizeErrorCode {
    SUCCESS("200","操作成功"),
    FALIE("400","操作失败"),
    SYSTE_ERROR("9999","系统错误");
    String message;
    String code;
    CommonErrorCode(String code,String message){
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
