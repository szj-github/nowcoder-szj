package com.nowcoder.community;

import com.nowcoder.community.controller.Index;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Autowired
    private Index index;
    @Autowired
    private SimpleDateFormat simpleDateFormat;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    void testApp(){
        System.out.println("测试"+applicationContext);
    }
    @Test
    void testIndex(){
        String result = index.index();
        System.out.println("接口结果"+result);
    }
    @Test
    void testDataBean(){
        SimpleDateFormat simpleDateFormat =  applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));

        System.out.println(""+simpleDateFormat.format(new Date()));
    }
}
