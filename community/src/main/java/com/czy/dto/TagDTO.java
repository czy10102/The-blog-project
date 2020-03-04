package com.czy.dto;

import com.czy.entity.Tag;
import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@ToString
public class TagDTO {
    private String categoryName;
    private List<Tag> tags;
}
