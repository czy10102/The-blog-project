package com.czy.interceptor;

import com.czy.entity.User;
import com.czy.exception.CustomizeErrorCode;
import com.czy.exception.CustomizeException;
import com.czy.mapper.INotificationMapper;
import com.czy.mapper.IUserMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    IUserMapper userMapper;
    @Autowired
    INotificationMapper iNotificationMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies !=null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String tokenValue = cookie.getValue();
                    User user = userMapper.findUserByToken(tokenValue);
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);

                    // 查询全部的未读信息
                    if(user!=null){
                        Integer number = iNotificationMapper.findAllNotReadReceived(user.getAccountId());
                        request.getSession().setAttribute("number",number);
                    }
                }
            }
         }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
