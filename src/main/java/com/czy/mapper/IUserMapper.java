package com.czy.mapper;

import com.czy.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface IUserMapper {
    @Insert("INSERT INTO user(name,account_id,token,gmt_modified,gmt_create,avatarUrl) " +
            "VALUES(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User uer);
    
    @Results(
            @Result(column = "ACCOUNT_ID",property = "accountId")
    )
    @Select("SELECT * FROM user WHERE token = #{token} ")
    User findUserByToken(@Param("token")String token);
    @Results({
            @Result(column = "ACCOUNT_ID", property = "accountId"),
            @Result(column = "GMT_CREATE", property = "gmtCreate")
    } )
    @Select("SELECT * FROM user WHERE account_id = #{accountId}")
    User findUserById(@Param("accountId") Integer accountId);
   
    @Delete("DELETE FROM user WHERE account_id = #{accountId}")
    void deleteUserByAccountId(@Param("accountId") String accountId);
    
}
