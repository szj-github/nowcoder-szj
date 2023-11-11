package com.nowcoder.community;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(12);
        System.out.println(user);
    }

    @Test
    public void  testInsertUser(){
        User user = new User();
        user.setUsername("sun");
        user.setPassword("12345");
        user.setEmail("222@qq.com");
        user.setHeaderUrl("1.png");
        user.setCreateTime(new Date());
        int rows = userMapper.insertUser(user);
        System.out.println("id:"+Integer.toString(user.getId())+"\t"+"row:"+Integer.toString(rows));
    }

    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("12345");
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10)); //1000毫秒*60
        loginTicketMapper.insertLoginTicket(loginTicket);
    }
}
