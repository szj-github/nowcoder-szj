package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);//分页查询
    /*    一共有多少行
    如果只有一个参数，并且在<if>语句里使用，则必须加别名*/
    int selectDiscussPostRows(@Param("userId") int userId);

    /*新增帖子*/
    int insertDiscussPost(DiscussPost discussPost);

    /*帖子详情*/
    DiscussPost selectDiscussPostById(int id);

    /*更新评论数量*/
    int updateCommentCount(int id,int commentCount);
}
