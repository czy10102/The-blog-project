package com.czy.enums;

public enum  CommentTypeEnum {
    QUESSION(1),
    COMMENT(2);
    private Integer type;
    CommentTypeEnum(Integer type){
        this.type = type;
    }
    
    public static boolean isExist(Integer type){
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()){
            if(commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }
    public Integer getType(){
        return  type;
    }
}
