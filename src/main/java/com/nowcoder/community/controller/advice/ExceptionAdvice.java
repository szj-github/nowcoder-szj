package com.nowcoder.community.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*扫描带有controller注解的bean*/
@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response){
        log.error("服务器发生异常"+e.getMessage());//
    }
}
