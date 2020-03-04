package com.czy.entity;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Notification {
    private Long id;
    // 通知人
    private Long notifier;
    // 接收人
    private Long receiver;
    private Long outerId;
    private Integer type;
    private Long gmtCreate;
    private Integer status;
    private String notifierName;
    private String outerTitle;
}
