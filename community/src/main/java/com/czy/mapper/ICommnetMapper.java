package com.czy.mapper;

import com.czy.entity.Comment;
import com.czy.entity.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ICommnetMapper {
    @Results(id = "comment",value = {
            @Result(column = "PARENT_ID",property = "parentId"),
            @Result(column = "TYPE",property = "type"),
            @Result(column = "COMMENTATOR",property = "commentator"),
            @Result(column = "GMT_CREATE",property = "gmtCreate"),
            @Result(column = "GMT_MODIFIED",property = "gmtModified"),
            @Result(column = "LIKE_COUNT",property = "LikeCount"),
            @Result(column = "CONTENT",property = "content"),
            @Result(column = "COMMENT_CONTENT",property = "commentContent")
    })
    @Select("SELECT * FROM COMMENT WHERE id = #{commentId}")
    Comment findCommnetById(String commentId);
    
    @ResultMap("comment")
    @Select("SELECT * FROM  COMMENT WHERE PARENT_ID = #{parentId} ORDER BY GMT_CREATE DESC")
    List<Comment> findCommentByParentId(String parentId);
   
    @Update("UPDATE COMMENT SET LIKE_COUNT = LIKE_COUNT + #{comment.LikeCount} WHERE id = #{comment.id}")
    void incCommentLike(@Param("comment") Comment comment);

    @Insert("INSERT INTO COMMENT (PARENT_ID,TYPE,COMMENTATOR,GMT_CREATE,GMT_MODIFIED,LIKE_COUNT,CONTENT)VALUES" +
            "(#{comment.parentId},#{comment.type},#{comment.commentator},#{comment.gmtCreate},#{comment.gmtModified},#{comment.LikeCount},#{comment.content})")
    void addComment(@Param("comment") Comment comment);
    @ResultMap("comment")
    @Select("SELECT * FROM COMMENT WHERE PARENT_ID = #{commentId} AND TYPE = 2")
    List<Comment>findTwoComment(@Param("commentId") String commentId);

    @Update("UPDATE COMMENT SET COMMENT_CONTENT = COMMENT_CONTENT + #{comment.commentContent} WHERE id = #{comment.id}")
    void incViewCount(@Param("comment") Comment comment);
}
