package com.czy.controller;

import com.czy.dto.PageDTO;
import com.czy.entity.Comment;
import com.czy.entity.ResponseResult;
import com.czy.entity.User;
import com.czy.exception.CommonErrorCode;
import com.czy.mapper.ICommnetMapper;
import com.czy.service.CommentService;
import com.czy.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    ICommnetMapper iCommnetMapper;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @ResponseBody
    @PostMapping("/comment")
    public ResponseResult comment(
            @RequestBody Comment comment,HttpServletRequest request){
        User user = userService.getUser(request);
        commentService.addComment(comment,user);
        return ResponseResult.success();
    }
    // 点赞数添加
    @GetMapping("/like")
    public String addLike(@RequestParam("commentId") String commentId){
        Comment comment = commentService.addLike(commentId);
        return "redirect:/questions/info?questionId="+comment.getParentId();
    }
    // 查询评论
    @ResponseBody
    @GetMapping("/twoComment")
    public List<Comment> getTwoComment(@RequestParam("commentId") String commentId, 
                                       HttpServletRequest request){
        List<Comment> commentById = commentService.findCommentById(commentId, request);
        return  commentById;
    }
    // 插入二级评论
    @ResponseBody
    @PostMapping("/addComment")
    public ResponseResult addComment(@RequestBody Comment comment){
      //  commentService.addComment(comment,);
        return ResponseResult.success();
    }
}
