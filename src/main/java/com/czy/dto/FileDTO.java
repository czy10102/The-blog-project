package com.czy.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Data
@ToString
public class FileDTO {
    private int success;
    private String message;
    private String url;
}
