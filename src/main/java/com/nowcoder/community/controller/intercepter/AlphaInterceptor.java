package com.nowcoder.community.controller.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class AlphaInterceptor implements HandlerInterceptor {

    /*
    * 在controller前执行*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        log.debug("prehandle: "+handler.toString());
        return true;
    }
    /*
    * 在controller执行之后*/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("postHandle: "+handler.toString());
    }

    /*
    *在templateEngine之后执行 */

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("在templateEngine之后执行"+handler.toString());
    }

}
