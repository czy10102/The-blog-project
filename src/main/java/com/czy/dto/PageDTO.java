package com.czy.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;
@ToString
@Data
public class PageDTO <T>{ 
    private int pageNum;
    private int pages;
    private int firstPage;
    private List<T> list;
    private int prePage;
    private int nextPage;
    private int lastPage;
}
