package com.czy.controller;

import com.czy.common.GetUcloud;
import com.czy.dto.FileDTO;
import com.czy.exception.CustomizeErrorCode;
import com.czy.exception.CustomizeException;
import com.czy.provider.UcloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    GetUcloud getUcloud;
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String path = getUcloud.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO =  new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setMessage("操作成功");
            fileDTO.setUrl(path);
            return fileDTO;
        }catch (Exception o){
            throw new CustomizeException(CustomizeErrorCode.PHOTO_UPLOADFALE);
        }
        
    }
}
