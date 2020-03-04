package com.czy.service;

import com.czy.dto.QuestionDTO;
import com.czy.dto.ReceivedDTO;
import com.czy.entity.Comment;
import com.czy.entity.Notification;
import com.czy.entity.Question;
import com.czy.entity.User;
import com.czy.enums.CommentTypeEnum;
import com.czy.enums.NotificationEnum;
import com.czy.exception.CustomizeErrorCode;
import com.czy.exception.CustomizeException;
import com.czy.mapper.ICommnetMapper;
import com.czy.mapper.INotificationMapper;
import com.czy.mapper.IQuesstionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Service
public class ReceivedService {
    @Autowired
    INotificationMapper iNotificationMapper;
    @Autowired
    IQuesstionMapper iQuesstionMapper;
    @Autowired
    ICommnetMapper iCommnetMapper;
    // 查询全部未读的通知
    public Integer findAllNotReadReceived(User user){
        if(StringUtils.isEmpty(user)){
            throw new CustomizeException(CustomizeErrorCode.USER_ID_ISNOT);
        }
      return iNotificationMapper.findAllNotReadReceived(user.getAccountId());
    }
    
    public List<ReceivedDTO> findAllReceived(User user){
        if(StringUtils.isEmpty(user)){
            throw new CustomizeException(CustomizeErrorCode.USER_ID_ISNOT);
        }
        List<Notification> allReceived = iNotificationMapper.findAllReceived(user.getAccountId());
        List<ReceivedDTO> receivedDTOS = new ArrayList<>();
        for(Notification notification : allReceived){
            Integer type = notification.getType();
            Long outerId = notification.getOuterId();
            if(type == CommentTypeEnum.QUESSION.getType()){
                Question questionById = iQuesstionMapper.findQuestionById(outerId + "");
                notification.setOuterTitle(questionById.getTitle());
                ReceivedDTO receivedDTO = this.creaaReceived(notification, NotificationEnum.REPLAY_QUESTION.getName());
                receivedDTOS.add(receivedDTO);
            }else{
                Comment commnet = iCommnetMapper.findCommnetById(outerId + "");
                notification.setOuterTitle(commnet.getContent());
                ReceivedDTO receivedDTO = this.creaaReceived(notification, NotificationEnum.REPLAY_COMMENT.getName());
                receivedDTOS.add(receivedDTO);
            }
        }
        return receivedDTOS;
    }
    
    public ReceivedDTO creaaReceived(Notification notification,String title){
        ReceivedDTO receivedDTO = new ReceivedDTO();
        receivedDTO.setId(notification.getId());
        receivedDTO.setGmtCreate(notification.getGmtCreate());
        receivedDTO.setName(notification.getNotifierName());
        receivedDTO.setOuterTitle(notification.getOuterTitle());
        receivedDTO.setType(notification.getType());
        receivedDTO.setTypeTitle(title);
        receivedDTO.setStatus(notification.getStatus());
        return receivedDTO;
    }
}
