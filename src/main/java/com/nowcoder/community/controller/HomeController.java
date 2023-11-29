package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        /*方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入到Model中
        * 所以模板可以直接访问page对象*/
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPost(0,page.getOffset(),page.getLimit());
        List<Map<String,Object>> discussPosts =  new ArrayList<>();
        for (DiscussPost discussPost:list){
            Map<String,Object> map = new HashMap<>();
            map.put("post",discussPost);
            map.put("user",userService.findUserById(discussPost.getUserId()));
            long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId());
            map.put("likeCount",likeCount);

            discussPosts.add(map);
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }


    @GetMapping("/error")
    public String getErrorPage(){
        return "/error/500";
    }
}
