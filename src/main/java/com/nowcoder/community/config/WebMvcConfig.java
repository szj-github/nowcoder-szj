package com.nowcoder.community.config;

import com.nowcoder.community.controller.intercepter.AlphaInterceptor;
import com.nowcoder.community.controller.intercepter.LoginRequiredInterceptor;
import com.nowcoder.community.controller.intercepter.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /*
    * alphaInterceptor: 资源拦截器，用于拦截一些静态资源请求，但排除了一些特定的路径（.css, .js, .png, .jpg, .jpeg），并添加了一些路径模式（/register, /login）。

    loginTicketInterceptor: 全局 登录凭证拦截器，用于处理登录凭证的逻辑，或者存储ticket到threadlocal这种操作。同样排除了一些静态资源路径。

    loginRequiredInterceptor: 登录验证拦截器，用于拦截需要登录才能访问的页面和请求，同样排除了一些静态资源路径。*/

    /*资源拦截器*/
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    /*登录拦截器*/
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;


    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    /*spring调用时，传进来的对象去注册*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/register","/login");

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");

    }
}
