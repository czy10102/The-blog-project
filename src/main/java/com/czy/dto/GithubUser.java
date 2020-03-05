package com.czy.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GithubUser {
    private String login;
    private Long id;
    private String avatar_url;
    
}
