package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    /*查询所有评论数据，根据帖子id，帖子类型*/
    List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);

    /*查询数据条目数*/
    int selectCountByEntity(int entityType,int entityId);

    /*增加评论*/
    int insertComment(Comment comment);
}
