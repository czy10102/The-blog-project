package com.czy.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
   
}
