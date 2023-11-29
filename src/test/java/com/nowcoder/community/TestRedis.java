package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes=CommunityApplication.class)
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString(){

        /*:在redis里代表的是空格，没有任何意义，相当于下划线的作用*/
        String key = "test:count";
        redisTemplate.opsForValue().set(key,1);
        System.out.println(redisTemplate.opsForValue().get(key));

        redisTemplate.opsForValue().increment(key);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.opsForValue().decrement(key);
        System.out.println(redisTemplate.opsForValue().get(key));
    }


    @Test
    public void testHashes(){
        String key = "test:user";
        redisTemplate.opsForHash().put(key,"name","sun");
        System.out.println(redisTemplate.opsForHash().get(key,"name"));
    }

    @Test
    public void testList(){
        String key = "test:people";
        redisTemplate.opsForList().leftPush(key,"111");
        redisTemplate.opsForList().rightPush(key,"222");
        redisTemplate.opsForList().leftPush(key,"333");
        System.out.println("list长度为"+redisTemplate.opsForList().size(key));

        System.out.println(redisTemplate.opsForList().index(key,1));
        System.out.println(redisTemplate.opsForList().range(key,0,2));
    }


    @Test
    public void testSet(){
        String key = "test:stu";

        redisTemplate.opsForSet().add(key,"sun");

        redisTemplate.opsForSet().size(key);

        //随机弹出一个值
        redisTemplate.opsForSet().pop(key);

        //获得所有元素
        redisTemplate.opsForSet().members(key);
    }

    /*有序集合*/
    @Test
    public void testSortSet(){
        /*有序集合要有value和对应的分数(分数越高越在前面)*/
        String key = "test:teacher";
        redisTemplate.opsForZSet().add(key,"孙悟空",90);

        redisTemplate.opsForZSet().add(key,"八戒",50);

        //获取所有成员
        System.out.println(redisTemplate.opsForZSet().zCard(key));
        //获取某个成员的分数
        System.out.println(redisTemplate.opsForZSet().score(key,"八戒"));
        //获得某个成员的排名
        System.out.println(redisTemplate.opsForZSet().rank(key,"八戒"));
    }

    @Test
    public void testDelete(){
        redisTemplate.delete("key");
        //判断是否存在某个key
        System.out.println(redisTemplate.hasKey("key"));
    }

    /*多次访问同一个key*/
    @Test
    public void testMulti(){
        String key = "test:count";
        BoundValueOperations operations =redisTemplate.boundValueOps(key);
        operations.increment();
        operations.increment();
    }

    /*编程式事务*/
    @Test
    public void testTransaction(){
        Object object = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = "test:count";

                operations.multi();//启用事务

                //多个作业
                operations.opsForValue().set(key,"sun");

                return operations.exec();
            }
        });
        System.out.println("事务"+object);
    }
}
