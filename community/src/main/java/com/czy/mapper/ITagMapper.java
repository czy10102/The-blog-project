package com.czy.mapper;

import com.czy.entity.Tag;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ITagMapper {
    List<Tag> findAllTag();
}
