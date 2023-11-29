package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import io.netty.handler.ssl.OpenSsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //实现点赞业务方法，参数：哪个用户点的赞，点赞对象实体类型，实体id
    public void like(int userId,int entityType,int entityId,int entityUserId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
/*        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey,userId); //判断点赞集合中是否包含这个用户
        if(isMember){
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        }else {
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }*/

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                boolean isMember = operations.opsForSet().isMember(entityLikeKey,userId);

                operations.multi();
                if (isMember){
                    operations.opsForSet().remove(entityLikeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);

                    System.out.println(isMember+userLikeKey+"用户点赞"+operations.opsForValue().get(userLikeKey));
                }else {
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
    }

    //查询实体点赞数量
    public long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某个实体的点赞状态
    public int findEntityLikeStatus(int userId , int entityType, int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId)==true?1:0;
    }

    //查询某个用户获得的赞数量
    public int findUserLikeCount(int userId){
        String entityUserLike = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(entityUserLike);
        System.out.println("数量"+count);
        return count==null?0:count.intValue();
    }
}
