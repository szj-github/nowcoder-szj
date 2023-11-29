package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.security.PublicKey;
import java.util.*;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @GetMapping("/letter/list")
    @LoginRequired
    public String getLetterList(Model model, Page page){
        User user  = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        System.out.println("userid"+user.getId());
        List<Message> conversationList = messageService.findConversation(user.getId(),page.getOffset(),page.getLimit());
        System.out.println("数据列表"+conversationList);
        List<Map<String ,Object>> conversations = new ArrayList<>();
        if (conversationList!=null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount",messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));

                int targetId = user.getId()==message.getFromId()?message.getToId():message.getFromId();

                map.put("target",userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        System.out.println("数据"+conversations);
        model.addAttribute("conversations",conversations);

        //查询总未读消息数量，不带conversationId
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);

        return "/site/letter";
    }

    @PostMapping("/letter/send")
    @ResponseBody //异步作用+解析返回值
    public String sendLetter(String toName,String content){
        User user = hostHolder.getUser();
        User to_user = userService.findUserByName(toName);
        if(to_user==null){
            return CommunityUtil.getJsonString(1,"目标用户不存在");
        }

        Message message = new Message();
        message.setFromId(user.getId());
        message.setToId(to_user.getId());
        if(message.getFromId()<message.getToId()){
            message.setConversationId(message.getFromId()+"_"+message.getToId());
        }else {
            message.setConversationId(message.getToId()+"_"+message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        messageService.readMessage(getLetterIds(message.getConversationId()));
        return CommunityUtil.getJsonString(0);
    }

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId,Page page,Model model){
        page.setRows(messageService.findLetterCount(conversationId));
        page.setLimit(5);
        page.setPath("/letter/detail/"+conversationId);

        List<Message> messageList = messageService.findLetters(conversationId,page.getOffset(),page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();

        if(messageList != null){

            for (Message message:messageList){
                Map<String,Object> map = new HashMap<>();
                map.put("letter",message);
                map.put("fromUser",userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }

        model.addAttribute("letters",letters);
        model.addAttribute("target",getLetterTarget(conversationId));
        System.out.println("获取user"+getLetterTarget(conversationId));
        messageService.readMessage(getLetterIds(conversationId));
        return "/site/letter-detail";
    }

    /*通过conversationId获取用户User*/
    public User getLetterTarget(String conversationId){
        String[] names = conversationId.split("_");
        int id1 = Integer.parseInt(names[0]);
        int id2 = Integer.parseInt(names[1]);
        if (id1==hostHolder.getUser().getId()){
            return userService.findUserById(id2);
        }
        return userService.findUserById(id1);
    }

    /*获取当前会话所有消息的id*/
    public List<Integer> getLetterIds(String conversationId){
        List<Integer> list = new ArrayList<>();
        List<Message> letterList = messageService.findLetters(conversationId,0,Integer.MAX_VALUE);

        if (letterList != null) {
            for (Message letter : letterList) {
                list.add(letter.getId());
            }
        }
        System.out.println("所有conversationDd"+list);
        return list;
    }
}
