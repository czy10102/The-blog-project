package com.czy.controller;

import com.czy.common.GetProperties;
import com.czy.dto.AccessTokenDTO;
import com.czy.entity.GitHubReqeustParam;
import com.czy.provider.GithubProvider;
import com.czy.dto.GithubUser;
import com.czy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// ctrl + v 快速创建对象
// shift 回车 光标移动下一行最前面

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    GithubProvider githubProvider;
    @Autowired
    UserService userService;
    
    // 用户点击登录按钮授权后 回调该方法并且携带code和state参数
    @GetMapping("/collback")
    public String collback(GitHubReqeustParam gitHubReqeustParam,
                           HttpServletRequest request,
                           HttpServletResponse response){
        gitHubReqeustParam.setRequest(request);
        gitHubReqeustParam.setResponse(response);
        githubProvider.getUser(gitHubReqeustParam);
        log.error("callback get github error,{}",request);
        return "redirect:/";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        userService.logout(request,response);
        return "redirect:/";
    }
}
