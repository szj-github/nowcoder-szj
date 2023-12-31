package com.nowcoder.community.util;

public interface CommunityConstant {
    int ACTIVATION_SUCCESS=0;
    int ACTIVATION_REPEAT=1;
    int ACTIVATION_FAILURE=2;

    /*
    * 默认登录状态超时时间*/
    int DEFAULT_EXPIRED_SECONDS = 3600*12;

    /*
    * 记住状态下的登录凭证超时时间*/

    int REMEMBER_EXPIRED_SECONDS = 3600*24*100;

    /*实体类型:帖子*/
    int ENTITY_TYPE_POST = 1;

    /*实体类型：评论*/
    int ENTITY_TYPE_COMMENT = 2;

}
