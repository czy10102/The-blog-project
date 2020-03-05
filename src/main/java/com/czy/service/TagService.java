package com.czy.service;

import com.czy.entity.Tag;
import com.czy.dto.TagDTO;
import com.czy.mapper.ITagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TagService {
    @Autowired
    ITagMapper iTagMapper;
    public List<TagDTO> findAllTag(){
        List<TagDTO> list = new ArrayList<>();
        List<Tag> allTag = iTagMapper.findAllTag();
        for (Tag tag:allTag){
            TagDTO tagDTO = new TagDTO();
            tagDTO.setCategoryName(tag.getTagName());
            tagDTO.setTags(tag.getTags());
            list.add(tagDTO);
        }
        return  list;
    }
}
