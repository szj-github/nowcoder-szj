package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;  //登录凭证 唯一标识符
    private int status;     //有效？
    private Date expired;//凭证到期日期
}
