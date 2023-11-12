package com.nowcoder.community.config;

import com.nowcoder.community.controller.intercepter.AlphaInterceptor;
import com.nowcoder.community.controller.intercepter.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /*资源拦截器*/
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    /*登录拦截器*/
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    /*spring调用时，传进来的对象去注册*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/register","/login");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");

    }
}
