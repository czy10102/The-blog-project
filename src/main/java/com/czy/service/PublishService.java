package com.czy.service;

import com.czy.dto.PageDTO;
import com.czy.dto.QuestionDTO;
import com.czy.entity.Question;
import com.czy.entity.User;
import com.czy.exception.CustomizeErrorCode;
import com.czy.exception.CustomizeException;
import com.czy.mapper.IQuesstionMapper;
import com.czy.mapper.IUserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublishService {
    private Logger logger = Logger.getLogger(PublishService.class);
    @Autowired
    IQuesstionMapper iQuesstionMapper;
    @Autowired
    UserService userService;
    @Autowired
    IUserMapper iUserMapper;
    
    // 保存问题信息
    public void saveQuestion(Question question, HttpServletRequest request){
        User user = userService.getUser(request);
        question.setGmtCreate(System.currentTimeMillis());
        question.setCreator(Integer.valueOf(user.getAccountId()));
        question.setGmtModified(question.getGmtCreate());
        question.setCommentCont(0);
        question.setViewCount(0);
        question.setLikeCount(0);
        System.out.println(question);
        iQuesstionMapper.create(question);
    }
    
    // 查询全部问题信息
    public List<Question> findAllQuestion(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Question> allQuestion = iQuesstionMapper.findAllQuestion();
        return allQuestion;
    }
    // 根据问题id查出用户信息
    public PageDTO<QuestionDTO> findUserInfo(PageInfo<Question> pageInfo){
        PageDTO<QuestionDTO> pageDTO = new PageDTO();
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question:pageInfo.getList()){
            QuestionDTO questionDTO = new QuestionDTO();
            Integer creator = question.getCreator();
            User user = iUserMapper.findUserById(creator);
            if(user == null){
              continue;
            }
            BeanUtils.copyProperties(user,questionDTO);
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOS.add(questionDTO);
        }
        BeanUtils.copyProperties(pageInfo,pageDTO);
        pageDTO.setList(questionDTOS);
        return pageDTO;
    }
    
    // 根据指定id查询指定问题
    public PageDTO<QuestionDTO>   findQuestionByCreator(String pageNum,String pageSize,HttpServletRequest servlet){
        List<Question> questionByCreator = null;
       if(!StringUtils.isEmpty(pageNum) && !StringUtils.isEmpty(pageSize)){
           User user = userService.getUser(servlet);
           PageHelper.startPage(Integer.valueOf(pageNum),Integer.valueOf(pageSize));
           questionByCreator = iQuesstionMapper.findQuestionByCreator(user.getAccountId());
       }
       PageInfo<Question> pageInfo = new PageInfo(questionByCreator);
       PageDTO<QuestionDTO> userInfo = this.findUserInfo(pageInfo);
        return userInfo;
    }
    
    // 根据指定Id删除问题
    public void deleteQuestionById(String id){
        if(StringUtils.isEmpty(id)){
           logger.debug("要删除的Id为空"); 
           return;
        }
        System.out.println(id);
        Question questuion = iQuesstionMapper.findQuestionById(id);
        if(StringUtils.isEmpty(questuion)){
            logger.debug("删除对象不存在");
            return;
        }
        iQuesstionMapper.deleteQuestionById(id);
    }
    
    // 修改问题信息
    public void updateQuestion(Question question){
        if(question!=null){
            iQuesstionMapper.updateQuestionById(
                    question.getTitle(),
                    question.getDescription(),
                    question.getTag(),
                    question.getId()+""
                    );
        }
    }
    // 得到问题信息
    public QuestionDTO getQuestionInfo(String questionId) {
        if(StringUtils.isEmpty(questionId)){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_ID_NOT_FOUND);
        }
        Question question = iQuesstionMapper.findQuestionById(questionId);
        if(StringUtils.isEmpty(question)){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        this.addViewCount(question);
        Integer creator = question.getCreator();
        // 查出用户
        User user = iUserMapper.findUserById(creator);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(user,questionDTO);
        BeanUtils.copyProperties(question,questionDTO);
        
        return questionDTO;
    }
    // 增加浏览数      加入MQ优化 先保存到MQ 指定时间一起写入数据库
    private void addViewCount(Question question) {
        Integer viewCount = question.getViewCount();
        question.setViewCount(1);
        iQuesstionMapper.incViewCount(question);
        question.setViewCount(viewCount);
    }
    // 查询相关问题
    public List<Question> getTagQuestion(QuestionDTO questionInfo) {
        String tag = questionInfo.getTag();
        String[] tags = tag.split(",");
        String collect = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionInfo.getId());
        question.setTag(collect);
        return iQuesstionMapper.getTag(question);
    }
}
