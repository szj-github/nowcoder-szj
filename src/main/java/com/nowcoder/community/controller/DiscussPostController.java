package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Target;
import java.util.*;

@Controller
@RequestMapping("/discuss")
@Slf4j
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJsonString(403,"还未登录");/*403无权限*/
        }
        /*创建一个dao*/
        DiscussPost post = new DiscussPost();
        post.setContent(content);
        post.setTitle(title);
        post.setCreateTime(new Date());
        post.setUserId(user.getId());

        /*传给service*/
        discussPostService.addDiscussPost(post);

        return CommunityUtil.getJsonString(0,"发布成功");

    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        /*传给模板*/
        model.addAttribute("post",post);

        //帖子点赞信息
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
        model.addAttribute("likeCount",likeCount);
        int likeStatus = hostHolder.getUser()==null?0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_POST,post.getId());
        model.addAttribute("likeStatus",likeStatus);

        /*获取作者信息，不能用Threadlocal，因为threadlocal获取的是当前登录的用户*/
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);//将作者信息也传给模板

        //查评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(post.getCommentCount());

        /*获取帖子评论：对帖子的评论*/
        List<Comment> commentList =commentService.findCommentsByEntity(ENTITY_TYPE_POST,post.getId(),page.getOffset(),page.getLimit());//类型：1代表帖子
        log.info("帖子评论");
        log.info(commentList.toString());
        /*获取对应的user信息*/
        List<Map<String , Object>> commentVoList = new ArrayList<>();
        if(commentList!=null){
            for (Comment comment:commentList){
                //map是一个评论，和该评论的作者：ENTITY_TYPE_POST
                Map<String,Object> commentVo = new HashMap<>();
                commentVo.put("comment",comment);
                commentVo.put("user",userService.findUserById(comment.getUserId()));

                //评论点赞信息
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeCount",likeCount);
                likeStatus = hostHolder.getUser()==null?0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeStatus",likeStatus);

                //查该评论下的回复数据
                List<Comment>  replyList= commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,comment.getId(),0,Integer.MAX_VALUE);//通过评论的id获取回复
                log.info("查询"+comment+replyList.toString());
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if(replyList!=null){
                    for (Comment reply:replyList){
                        Map<String,Object> replyVo = new HashMap<>();
                        replyVo.put("reply",reply);
                        replyVo.put("user",userService.findUserById(reply.getUserId()));

                        //评论回复点赞信息
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeCount",likeCount);
                        likeStatus = hostHolder.getUser()==null?0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeStatus",likeStatus);

                        //回复的目标
                        User targetUser =reply.getTargetId()==0?null:userService.findUserById(reply.getTargetId());
                        replyVo.put("target", targetUser);
                        replyVoList.add(replyVo);
                        log.info(reply.toString());
                    }
                }
                /*把每个评论的回复放进去*/
                commentVo.put("replys",replyVoList);
                log.info("每个评论的回复");
                log.info(replyVoList.toString());
                //获取回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("replyCount",replyCount);

                //
                commentVoList.add(commentVo);
            }


        }

        /*回复：给评论的评论*/
        model.addAttribute("comments",commentVoList);

        /*返回模板*/
        return "/site/discuss-detail";
    }

}
