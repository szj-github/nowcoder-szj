package com.nowcoder.community.util;

public class RedisKeyUtil {

    private static final String split = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    //用户被点赞 前缀
    private static final String PREFIX_USER_LIEK = "like:user";

    //某个实体的赞 ->:set(sun,zhang,liu)
    public static String getEntityLikeKey(int EntityType, int EntityId){
        return PREFIX_ENTITY_LIKE+split+EntityType+split+EntityId;
    }

    //用户被点赞 key
    public static String getUserLikeKey(int userId){
        return PREFIX_USER_LIEK+split+userId;
    }
}
