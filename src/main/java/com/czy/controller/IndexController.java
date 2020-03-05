package com.czy.controller;

import com.czy.dto.PageDTO;
import com.czy.dto.QuestionDTO;
import com.czy.dto.ReceivedDTO;
import com.czy.entity.Question;
import com.czy.dto.TagDTO;
import com.czy.entity.User;
import com.czy.mapper.INotificationMapper;
import com.czy.mapper.IQuesstionMapper;
import com.czy.service.PublishService;
import com.czy.service.ReceivedService;
import com.czy.service.TagService;
import com.czy.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private PublishService publishService;
    @Autowired
    private IQuesstionMapper iQuesstionMapper;
    @Autowired
    TagService tagService;
    @Autowired
    INotificationMapper iNotificationMapper;
    
    @Autowired
    ReceivedService receivedService;
    // 跳转主页面
    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "pageNum",defaultValue = "1") String pageNum,
            @RequestParam(name = "pageSize",defaultValue = "5") String pageSize){
        // 判断当前分页参数是否为空
        if(!StringUtils.isEmpty(pageNum) && !StringUtils.isEmpty(pageSize)){
            List<Question> allQuestion = publishService.findAllQuestion(
                    Integer.valueOf(pageNum),
                    Integer.valueOf(pageSize));

            PageInfo pageInfo = new PageInfo(allQuestion);
            PageDTO<QuestionDTO>userInfo = publishService.findUserInfo(pageInfo);
            model.addAttribute("pages",userInfo.getPages());
            model.addAttribute("pageInfo",userInfo);
        }
        return "index";
    }
    // 跳转提交问题页面
    @GetMapping("/publish")
    public String publish(HttpServletRequest request,Model model){
        List<TagDTO> allTag = tagService.findAllTag();
        model.addAttribute("tagTitle",allTag);
        return "publish";
    }
    // 跳转回复页面
    @GetMapping("/received")
    public String received(){
        return "received";
    }
    // 跳转我的问题页面
    @GetMapping("/profile")
    public String profile(
            Model model,
            @RequestParam(value = "action") String action,
            @RequestParam(value = "pageNum",defaultValue = "1") String pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") String pageSize,
            HttpServletRequest request){
        PageDTO<QuestionDTO> questionByCreator = publishService.findQuestionByCreator(pageNum, pageSize, request);
        model.addAttribute("myquestions",questionByCreator);
        if ("questions".equals(action)) {
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
            return "profile";
        }else{
            User user = userService.getUser(request);
            List<ReceivedDTO> allReceived = receivedService.findAllReceived(user);
            model.addAttribute("section","repies");
            model.addAttribute("sectionName","我的回复");
            model.addAttribute("receiveds",allReceived);
            return "received";
        }
    }   
    // 跳转修改页面
    @GetMapping("/updateQuestion/{questionId}/{pageNum}")
    public String updateQuestions(
            @PathVariable("questionId") String questionId,
            @PathVariable("pageNum") String pageNum,
            Model model){
        Question question = iQuesstionMapper.findQuestionById(questionId);
        question.setPageNum(pageNum);
        System.out.println(question);
        model.addAttribute("question",question);
        return "updatePublish";
    }
    
}
 