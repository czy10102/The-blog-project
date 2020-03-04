package com.czy.entity;

import com.czy.exception.CommonErrorCode;
import com.czy.exception.CustomizeException;
import com.czy.exception.ICustomizeErrorCode;
import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@ToString
public class ResponseResult<T> {
    private String code;
    private String message;
    public ResponseResult(String code,String message){
        this.message = message;
        this.code = code;
    }
    public static ResponseResult setResult(ICustomizeErrorCode iCustomizeErrorCode){
        return new ResponseResult(
                iCustomizeErrorCode.getCode(),
                iCustomizeErrorCode.getMessage()
        );
    }
    public static ResponseResult setResult(CustomizeException cus){
        return new ResponseResult(
                cus.getCode(),
                cus.getMessage()
        );
    }
    public static ResponseResult success(){
        
        return new ResponseResult(
                "200",
                "操作成功!"
        );
    }
    public static ResponseResult fail(){

        return new ResponseResult(
                "400",
                "操作失败!"
        );
    }
}
