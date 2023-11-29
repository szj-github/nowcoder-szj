package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    /*当前用户会话列表，针对每个会话，返回最新的一条*/
    List<Message> selectConversations(int userId,int offset,int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询某个会话的私信消息列表
    List<Message> selectLetters(String conversationId, int offset,int limit);

    //查询某个会话所包含的私信消息数量
    int selectLetterCount(String conversationId);

    //查询未读私信数量：针对所有会话，和针对某个用户的会话 userId是指当前用户
    int selectLetterUnreadCount(int userId,String conversationId);


    //发送私信,新增一个消息
    int insertMessage(Message message);

    //修改未读状态方法
    int updateStatus(List<Integer> ids, int status);
}
