package com.czy.advice;

import com.alibaba.fastjson.JSON;
import com.czy.entity.ResponseResult;
import com.czy.exception.CommonErrorCode;
import com.czy.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, Throwable  ex, Model model, HttpServletResponse response){
        System.out.println(ex.getMessage());
        if("text/html;charset=UTF-8".equals(request.getContentType())){
            if(ex instanceof CustomizeException){
                model.addAttribute("message",ex.getMessage());
            }else{
                model.addAttribute("message",CommonErrorCode.SYSTE_ERROR.getMessage());
            }
            return  new ModelAndView("error");
        }else {
            if (ex instanceof CustomizeException) {
                ResponseResult responseResult = ResponseResult.setResult((CustomizeException) ex);
                try {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(responseResult));
                    writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            } else {
                return ResponseResult.setResult(CommonErrorCode.SYSTE_ERROR);
            }
        }
    }
}
