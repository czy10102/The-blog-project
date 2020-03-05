package com.czy.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Component
public class GetProperties {
    @Value("${github.client_id}")
    public String CLIENT_ID;
    @Value("${github.client_secret}")
    public String CLIENT_SECRET;
    @Value("${github.redirect_uri}")
    public String REDIRECT_URI;
    @Value("${github.token_uri}")
    public String TOKEN_URI;
    @Value("${github.user_uri}")
    public String USER_URI;
    @Value("${okhttp.mediaType}")
    public String MEDIA_TYPE;
}
