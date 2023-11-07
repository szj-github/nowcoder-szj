package com.nowcoder.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@Controller
public class Index {

    @RequestMapping(value = "/index1",method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        return "11";
    }

    @RequestMapping(value = "/index2",method = RequestMethod.GET)
    @ResponseBody
    public void index2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8;");
        Writer writer = response.getWriter();
        writer.write("<h1>测试</h1>");
    }

    //查询参数：index3?cuurent=1&limit=10
    @GetMapping("/index3")
    @ResponseBody
    public String pageData(@RequestParam(name = "current",required = false,defaultValue = "1") int current,
                           @RequestParam(name = "limit",required = false,defaultValue = "10") int limit){

        System.out.println(current+limit);
        return Integer.toString(current+limit);
    }
    //路径参数：index4/text/{text}
    @GetMapping("/index4/text/{text}")
    @ResponseBody
    public String text(@PathVariable("text") String text){
        System.out.println(text);
        return text;
    }

    //post请求
    @PostMapping("/index4")
    @ResponseBody
    public String postText(@RequestParam("name") String name,@RequestParam("age") int age){
        return name+Integer.toString(age);
    }

    //响应HTML模板数据
    //采用主动创建bean的方式
    @GetMapping("/teacher")
    public ModelAndView getTeacher(){
         ModelAndView modelAndView = new ModelAndView();
         modelAndView.addObject("name","孙智健");
         modelAndView.addObject("age","24");
         modelAndView.setViewName("/demo/view");
         return modelAndView;
    }

    //采用dispatchServlet 引用Model bean的方式
    @GetMapping("/school")
    public String getSchool(Model model){
        model.addAttribute("name","首师大");
        model.addAttribute("age","70");
        return "/demo/view";
    }

    //响应json数据
    //responseBody如果返回值是字符串，那么直接将字符串写到客户端；如果是一个对象，会将对象转化为json串，然后写到客户端。
    @RequestMapping("/getEmp")
    @ResponseBody
    public  List<Map<String ,Object>> getEmp(){
        List<Map<String ,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("ID",123);
        list.add(map);
        return list;
    }
}
