package com.czy.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class Tag {
    private Long id;
    private String tagName;
    private Long gmtCreate;
    private Long parent;
    List<Tag> tags;
} 
