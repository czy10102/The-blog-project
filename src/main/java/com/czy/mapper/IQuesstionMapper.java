package com.czy.mapper;

import com.czy.dto.QuestionDTO;
import com.czy.entity.Question;
import lombok.Data;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface IQuesstionMapper {
    @Results(id="question",value = {
            @Result(column = "GMT_CREATE",property = "gmtCreate"),
            @Result(column = "GMT_MODIFIED",property = "gmtModified"),
            @Result(column = "COMMENT_CONT",property = "commentCont"),
            @Result(column = "VIEW_COUNT",property = "viewCount"),
            @Result(column = "LIKE_COUNT",property = "likeCount"),
            @Result(column = "TAG",property = "tag"),
    })
    @Select("SELECT * FROM question ORDER BY GMT_CREATE DESC")
    // 查询全部问题
    List<Question> findAllQuestion();
    
    // 根据用户Id查询全部问题
    @ResultMap("question")
    @Select("SELECT * FROM question WHERE creator = #{creator} ")
    List<Question> findQuestionByCreator(@Param("creator") String creator);
    
    // 根据问题id删除指定问题
    @Delete("DELETE FROM question WHERE id = #{questionId} ")
    void deleteQuestionById(@Param("questionId") String questionId);
    // 根据问题id查询指定问题
    @Results(value = {
            @Result(column = "GMT_CREATE",property = "gmtCreate"),
            @Result(column = "GMT_MODIFIED",property = "gmtModified"),
            @Result(column = "COMMENT_CONT",property = "commentCont"),
            @Result(column = "VIEW_COUNT",property = "viewCount"),
            @Result(column = "LIKE_COUNT",property = "likeCount"),
            @Result(column = "TAG",property = "tag"),
    })
    @Select("SELECT * FROM question WHERE id = #{questionId}")
    Question findQuestionById(@Param("questionId") String questionId);
    // 根据问题id修改指定问题
    @Update("UPDATE question SET title = #{title},description = #{description},tag = #{tag} WHERE id = #{questionId}")
    void updateQuestionById(
            @Param("title") String title,
            @Param("description") String description,
            @Param("tag") String tag,
            @Param("questionId") String questionId
            );

    @Update("UPDATE question SET VIEW_COUNT = VIEW_COUNT + #{question.viewCount} WHERE id = #{question.id}")
    void incViewCount(@Param("question") Question question);

    @Update("UPDATE question SET COMMENT_CONT = COMMENT_CONT + #{question.commentCont} WHERE id = #{question.id}")
    void incCommentCount(@Param("question") Question question);

    // 插入问题
    @Insert("INSERT INTO question (title,description,gmt_create,gmt_modified,creator,comment_cont,view_count,like_count,tag)VALUES(" +
            "#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{commentCont},#{viewCount},#{likeCount},#{tag})")
    void create(Question question);
    
    // 查询相关问题
    //select * from QUESTION where tag regexp 'spring|springboot' and id!=11;
    @Select("SELECT * FROM QUESTION WHERE tag regexp #{question.tag} AND id!= #{question.id} ")
    List<Question> getTag(@Param("question") Question question);
}
