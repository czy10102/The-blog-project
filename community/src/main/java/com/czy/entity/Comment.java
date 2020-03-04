package com.czy.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Comment extends User{
    private long id;
    private Long parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long LikeCount;
    private String content;
    private Long commentContent;
}
