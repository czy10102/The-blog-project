package com.czy.controller;

import com.czy.dto.PageDTO;
import com.czy.dto.QuestionDTO;
import com.czy.entity.Comment;
import com.czy.entity.Question;
import com.czy.entity.Tag;
import com.czy.service.CommentService;
import com.czy.service.PublishService;
import com.czy.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class PublishController {
   private String url = "redirect:/profile?action=questions&pageNum=";
    @Autowired
    PublishService publishService;
    @Autowired
    CommentService commentService;

    // 发布问题
    @PostMapping("/publish")
    public String doPublish(Question question,
                            HttpServletRequest request,
                            Model model){
        publishService.saveQuestion(question,request);
        return "redirect:/";
    }
    
    // 删除问题
    @GetMapping("/delete/{questionId}/{pageNum}")
    public String deleteQuestion(
            @PathVariable("questionId") String questionId,
            @PathVariable("pageNum")String pageNum){
        publishService.deleteQuestionById(questionId);
        return url + pageNum;
    }
    // 修改问题
    @PostMapping("/update")
    public String updateQuestion(Question question){
        publishService.updateQuestion(question);
        return url + question.getPageNum();
    }
    // 问题详细页面
    @GetMapping("/info")
    public String questionInfo(@RequestParam("questionId") String questionId,
                               @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                               Model model){
        QuestionDTO questionInfo = publishService.getQuestionInfo(questionId);
        PageDTO<Comment> pageInfo = commentService.getCommentInfo(
                questionInfo.getId(),
                pageNum,
                pageSize);
        List<Question> tagQuestion = publishService.getTagQuestion(questionInfo);
        model.addAttribute("info",questionInfo);
        model.addAttribute("commnet",pageInfo);
        model.addAttribute("tagQuestion",tagQuestion);
        return "questionInfo";
    }
}
