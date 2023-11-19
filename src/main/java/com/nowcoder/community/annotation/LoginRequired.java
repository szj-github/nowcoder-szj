package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*自定义注解：拦截器接口注解*/
/*登陆验证*/
@Target(ElementType.METHOD)//作用域：方法上
@Retention(RetentionPolicy.RUNTIME)//有效时间：运行时
public @interface LoginRequired {
}
