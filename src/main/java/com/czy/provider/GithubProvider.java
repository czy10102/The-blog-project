package com.czy.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.czy.common.GetProperties;
import com.czy.dto.AccessTokenDTO;
import com.czy.dto.GithubUser;
import com.czy.entity.GitHubReqeustParam;
import com.czy.entity.User;
import com.czy.mapper.IUserMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Component
public class GithubProvider {
    @Autowired
    GetProperties getProperties;
    @Autowired
    IUserMapper userMapper;
    // 得到Github的Token
    private String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get(getProperties.MEDIA_TYPE);
        // 通过code远程调用获取token
        OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
            Request request = new Request.Builder()
                    .url(getProperties.TOKEN_URI)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                String token = responseBody.split("&")[0].split("=")[1];
                return token;
            }catch (IOException e) {
                e.printStackTrace();
            }
            return  null;
        }
    // 根据token获得用户信息
    private GithubUser getUser(String accessToken)  {
        try{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getProperties.USER_URI+accessToken)
                .build();
         Response response = client.newCall(request).execute();
            String string = response.body().string();
           return JSON.parseObject(string, GithubUser.class);
        }catch (IOException o){
            o.printStackTrace();
        }
        return null;
    }
    
    
    public void getUser(GitHubReqeustParam gitHubReqeustParam) {
        // 得到AccessTokenDTO对象
        AccessTokenDTO acessTokenDTO = this.getAccessTokenDTO(gitHubReqeustParam);
        // 得到AccessToken
        String accessToken = this.getAccessToken(acessTokenDTO);
        // 通过AccessToken 得到用户信息
        GithubUser user = this.getUser(accessToken);
        if(user!=null){
            // 登录成功 插入H2数据库
            this.saveH2DataBase(user,gitHubReqeustParam);
        }
    }

    
    // 将用户信息插入H2缓存
    private void saveH2DataBase(GithubUser githubUser,GitHubReqeustParam gitHubReqeustParam) {
        HttpServletResponse response = gitHubReqeustParam.getResponse();
        User user = new User();
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setName(githubUser.getLogin());
        user.setAccountId(String.valueOf(githubUser.getId()));
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl(githubUser.getAvatar_url());
        User userById = userMapper.findUserById(Integer.valueOf(user.getAccountId()));
        if(userById != null){
            System.out.println(userById.getAccountId() + "id");
            userMapper.deleteUserByAccountId(userById.getAccountId());
        }
        userMapper.insert(user);
        Cookie cookie = new Cookie("token", token);
     //   cookie.setMaxAge(5000);
        response.addCookie(cookie);
    }
    


    // 封装AccessTokenDTO对象
    private AccessTokenDTO getAccessTokenDTO(GitHubReqeustParam gitHubReqeustParam){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(getProperties.CLIENT_ID);
        accessTokenDTO.setClient_secret(getProperties.CLIENT_SECRET);
        accessTokenDTO.setCode(gitHubReqeustParam.getCode());
        accessTokenDTO.setRedirect_uri(getProperties.REDIRECT_URI);
        accessTokenDTO.setState(gitHubReqeustParam.getState());
        return accessTokenDTO;
    }
}
