package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

//    @Test
//    public void  testMail() {
//        mailClient.sendMail("991865291@qq.com","test","welcome");
//    }

    @Test
    public void testHtml(){
        Context context = new Context();
        context.setVariable("username","孙智健");

        String content = templateEngine.process("/mail/demo",context);//构造html，并把动态内容传给它，不需要加html

        mailClient.sendMail("991865291@qq.com","欢迎加入",content);
    }
}
