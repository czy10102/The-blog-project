package com.czy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.czy.mapper")
public class CommunityApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(CommunityApplication.class);
    }
}

