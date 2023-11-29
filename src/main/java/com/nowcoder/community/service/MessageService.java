package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Map;

@Service
public class MessageService {


    /*conversation：会话
    * Letter：消息*/
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /*查询某个用户的会话数量*/
    public List<Message> findConversation(int userId,int offset,int limit){
        return messageMapper.selectConversations(userId,offset,limit);
    }

    /*查询某个用户的会话数量*/
    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    /*查询某个会话的消息列表*/
    public List<Message> findLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetters(conversationId,offset,limit);
    }

    /*查询某个会话的消息letter数量*/
    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    /*查询未读消息数量*/
    public int findLetterUnreadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }

    /*添加一条消息*/
    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));

        return messageMapper.insertMessage(message);
    }

    /*把消息列表变为已读*/
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }
}
