package com.czy.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Question {
    // 问题id
    private Integer id;
    // 标题
    private String title;
    // 说明
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCont;
    private Integer viewCount;
    private Integer likeCount;
    private String pageNum;
}
