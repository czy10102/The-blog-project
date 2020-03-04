package com.czy.dto;

import com.czy.entity.Question;
import com.czy.entity.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Data
@ToString
public class QuestionDTO extends User {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCont;
    private Integer viewCount;
    private Integer likeCount;
}
