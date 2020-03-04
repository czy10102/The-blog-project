package com.czy.service;

import com.czy.entity.User;
import com.czy.mapper.IUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    IUserMapper userMapper;
    public void logout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if("token".equals(cookie.getName())){
                cookie.setValue(null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
    // 通过session获得user对象
    public User getUser(HttpServletRequest request){
      return  (User)request.getSession().getAttribute("user");
    }
}
