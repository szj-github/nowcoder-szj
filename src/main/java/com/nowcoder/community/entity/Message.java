package com.nowcoder.community.entity;

import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Data
public class Message {

    private int id;
    private int fromId;       //消息的发送方userId
    private int toId;           //消息的接收方userId
    private String conversationId;  //fromId_toId
    private String content;
    private int status; //0:未读，1：系统消息，2：已读
    private Date createTime;

}
