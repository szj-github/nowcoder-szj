package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;


    @GetMapping("/register")
    public String getRegister(){
        return "/site/register";
    }

    @PostMapping("/register")
    public String register(Model model, User user){
        Map<String,Object> map = userService.Register(user);
        if (map==null || map.isEmpty()){
            model.addAttribute("msg","注册成功,我们已经向您的邮箱发送了激活邮件");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }
}
