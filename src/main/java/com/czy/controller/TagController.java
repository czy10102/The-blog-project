package com.czy.controller;

import com.czy.entity.Tag;
import com.czy.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TagController {
    @Autowired
    TagService tagService;
    // 查询全部标签
    // 可优化 标签不是重要数据 添加到缓存里
    @GetMapping("/tags")
    public String findAllTags(Model model){
        ;
        return "publish";
    }
}
