package com.czy.mapper;

import com.czy.entity.Notification;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface INotificationMapper {
    @Results(id = "test",value = {
            @Result(column = "ID",property = "id"),
            @Result(column = "NOTIFIER",property = "notifier"),
            @Result(column = "RECEIVER",property = "receiver"),
            @Result(column = "OUTERID",property = "outerId"),
            @Result(column = "TYPE",property = "type"),
            @Result(column = "GMT_CREATE",property = "gmtCreate"),
            @Result(column = "STATUS",property = "status"),
            @Result(column = "NOTIFIER_NAME",property = "notifierName"),
            @Result(column = "OUTER_TITLE",property = "outerTitle"),
    })
    
    @Select("SELECT * FROM NOTIFICATION WHERE RECEIVER = #{userId} ORDER BY GMT_CREATE  DESC")
    List<Notification> findAllReceived(@Param("userId") String userId);

    @Insert("INSERT INTO NOTIFICATION (NOTIFIER,RECEIVER,OUTERID,TYPE,GMT_CREATE,STATUS,NOTIFIER_NAME,OUTER_TITLE)" +
            "VALUES(#{notificationOne.notifier},#{notificationOne.receiver}," +
            "#{notificationOne.outerId},#{notificationOne.type},#{notificationOne.gmtCreate},#{notificationOne.status}," +
            "#{notificationOne.notifierName},#{notificationOne.outerTitle})")
    void addNotification(@Param("notificationOne") Notification notification);


    @Select("SELECT count(*) FROM NOTIFICATION WHERE RECEIVER = #{userId} AND STATUS = 0")
    Integer findAllNotReadReceived(@Param("userId")String userId);
    
    @Update("UPDATE NOTIFICATION SET STATUS = 1 WHERE id = #{id}")
    void updateReceive(@Param("id") String id);
    
    @Select("SELECT * FROM NOTIFICATION WHERE id = #{id}")
    Notification findNotificationById(String id);

    @Update("UPDATE NOTIFICATION SET STATUS = 1 WHERE RECEIVER = #{id}")
    void ReadAll(@Param("id") String id);
}
