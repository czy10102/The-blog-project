package com.czy.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReceivedDTO {
    private long id;
    private String name;
    private String outerTitle;
    private String typeTitle;
    private long gmtCreate;
    private Integer  type;
    private Integer status;
}
