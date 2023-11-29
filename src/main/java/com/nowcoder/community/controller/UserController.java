package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PublicKey;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    /*用户相关*/

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;


    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "/site/setting";
    }

    /*设置上传头像*/
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        System.out.println("头像");
        System.out.println(headerImage);
        if(headerImage==null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));//获取后缀名

        /*判断后缀是否正确*/
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }

        /*生成随机文件名用于保存*/
        filename = CommunityUtil.generateUUID()+suffix;
        /*确定文件存放路径*/
        System.out.println(uploadPath);
        File file = new File(uploadPath+filename);
        try {
            /*将文件内容写入进入*/
            headerImage.transferTo(file);
        } catch (IOException e) {
            log.info("上传失败");
            throw new RuntimeException(e);
        }
        /*存放成功，更新头像路径 :http://localhost/community/user/header/xxx.png*/
        User user = hostHolder.getUser();

        String headerUrl = domain+contextPath+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }


    /*获取头像*/
    @GetMapping("/header/{filename}")
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        /*服务器存放的路径*/
        filename = uploadPath+"/"+filename;
        /*文件后缀*/
        String suffix = filename.substring(filename.lastIndexOf("."));

        /*响应图片*/
        response.setContentType("image/"+suffix);
        try {
            OutputStream outputStream = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b=fileInputStream.read(buffer))!=-1){/*等于-1代表没读到*/
                outputStream.write(buffer,0,b);
            }
        } catch (IOException e) {
            log.info("读取头像失败");
            throw new RuntimeException(e);
        }
    }

    /*个人主页点赞*/
    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("用户不存在");
        }
        model.addAttribute("user",user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);
        return "/site/profile";
    }
}
