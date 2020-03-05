package com.czy.service;

import com.czy.dto.PageDTO;
import com.czy.dto.QuestionDTO;
import com.czy.entity.Comment;
import com.czy.entity.Notification;
import com.czy.entity.Question;
import com.czy.entity.User;
import com.czy.enums.CommentTypeEnum;
import com.czy.enums.NotificationEnum;
import com.czy.enums.NotificationStatusEnum;
import com.czy.exception.CustomizeErrorCode;
import com.czy.exception.CustomizeException;
import com.czy.mapper.ICommnetMapper;
import com.czy.mapper.INotificationMapper;
import com.czy.mapper.IQuesstionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    ICommnetMapper iCommnetMapper;
    @Autowired
    IQuesstionMapper quesstionMapper;
    
    @Autowired

    INotificationMapper iNotificationMapper;
    @Transactional
    public void addComment(Comment co,User user){
        Comment comment = this.setComment(co);
        if(StringUtils.isEmpty(comment.getParentId())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_WARING);
        }
        
        // 是回复评论还是回复问题
        Notification notification = new Notification();
        Question question = null;
                String commentId = comment.getParentId() + "";
        if(comment.getType() == CommentTypeEnum.QUESSION.getType()){
            
            // 问题
          
             question = quesstionMapper.findQuestionById(commentId);
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            // 加入MQ优化 先保存到MQ 指定时间一起写入数据库
            question.setCommentCont(1);
            quesstionMapper.incCommentCount(question);
            createNotify(comment,user,question,NotificationEnum.REPLAY_QUESTION);
        }else{
            Comment commnet = iCommnetMapper.findCommnetById(commentId);
            question = quesstionMapper.findQuestionById(commnet.getParentId()+"");
            createNotify(comment,user,question,NotificationEnum.REPLAY_COMMENT);
            // 评论
        }
        iCommnetMapper.addComment(comment);
    }
    private void createNotify(Comment comment,User user,Question question,NotificationEnum notificationEnum){
        Notification notificationOne = new Notification();
        notificationOne.setType(notificationEnum.getType());
        notificationOne.setOuterTitle(question.getTitle());
        notificationOne.setNotifierName(user.getName());
        notificationOne.setNotifier(Long.valueOf(comment.getCommentator()));
        notificationOne.setReceiver(Long.valueOf(question.getCreator()));
        notificationOne.setGmtCreate(System.currentTimeMillis());
        notificationOne.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationOne.setOuterId(comment.getParentId());
        iNotificationMapper.addNotification(notificationOne);
    }
    private Comment setComment(Comment comment) {
        comment.setCommentator(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        return comment;
    }
    // 根据问题id获取评论信息
    public PageDTO<Comment> getCommentInfo(Integer commentId,Integer pageNum,Integer pageSize) {
        if(StringUtils.isEmpty(commentId)){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_ID_NOT_FOUND);
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> list = iCommnetMapper.findCommentByParentId(commentId + "");
        PageInfo<Comment> pageInfo = new PageInfo<>(list);
        PageDTO pageDTO = new PageDTO();
        BeanUtils.copyProperties(pageInfo,pageDTO);
        return pageDTO;
    }
    // 点赞
    public Comment addLike(String commentId) {
        Comment commnet = iCommnetMapper.findCommnetById(commentId);
        commnet.setLikeCount(1L);
        iCommnetMapper.incCommentLike(commnet);
        return commnet;
    }
    @Autowired
    UserService userService;
    // 查询全部二级评论
    public  List<Comment>  findCommentById(String commentId, 
                                          HttpServletRequest request) {
        if(StringUtils.isEmpty(commentId)){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userService.getUser(request);
        List<Comment> twoComment = iCommnetMapper.findTwoComment(commentId);
        for (Comment comment:twoComment) {
            BeanUtils.copyProperties(user,comment);
        }
        return twoComment;
    }
    // 插入2级评论
    public void insertCommnet(Comment comment) {
        Comment newComment = this.setComment(comment);
        iCommnetMapper.addComment(newComment);
        incNumber(comment);
    }
    
    // 增加评论数
    public void incNumber(Comment comment){
        Comment comment1 = new Comment();
        comment1.setId(comment.getParentId());
        comment1.setCommentContent(1L);
        iCommnetMapper.incViewCount(comment);
    }
    
}
