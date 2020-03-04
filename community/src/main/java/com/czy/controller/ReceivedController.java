package com.czy.controller;

import com.czy.entity.Comment;
import com.czy.entity.Notification;
import com.czy.entity.Question;
import com.czy.entity.User;
import com.czy.enums.CommentTypeEnum;
import com.czy.mapper.ICommnetMapper;
import com.czy.mapper.INotificationMapper;
import com.czy.mapper.IQuesstionMapper;
import com.czy.service.ReceivedService;
import com.czy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class ReceivedController {
    @Autowired
    ReceivedService receivedService;
    @Autowired
    INotificationMapper iNotificationMapper;
    @Autowired
    IQuesstionMapper iQuesstionMapper;
    @Autowired
    UserService userService;
    @Autowired
    ICommnetMapper iCommnetMapper;
    @GetMapping("/readMessage")
    public String readMessage(@RequestParam("id") String id){
        Notification notificationById = iNotificationMapper.findNotificationById(id);
        String outerId =  notificationById.getOuterId() +"";
        iNotificationMapper.updateReceive(id);
        if(notificationById.getType() == CommentTypeEnum.QUESSION.getType()){
            return "redirect:/questions/info?questionId="+outerId;
        }else{
            Comment commnet = iCommnetMapper.findCommnetById(outerId);
            Long parentId = commnet.getParentId();
            return "redirect:/questions/info?questionId="+parentId;
        }
    }
    
    @GetMapping("/readAll")
    public String readAll(HttpServletRequest request){
        User user = userService.getUser(request);
        if(user!=null){
            String accountId = user.getAccountId();
            iNotificationMapper.ReadAll(accountId);
        }
        return "redirect:/profile?action=repies";
    }
}
