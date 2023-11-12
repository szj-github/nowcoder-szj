package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.swing.text.AbstractDocument;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String,Object> Register(User user){
        Map<String,Object> map = new HashMap<>();
        //空值判断
        if(user==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        //判断账号是否已经被注册
        User user1 = userMapper.selectByName(user.getUsername());
        System.out.println("账号已经被注册"+user1);
        if (user1!=null){
            map.put("usernameMsg","该账号已经存在");
            return map;
        }
        //验证邮箱是否已经被注册
        User user2 = userMapper.selectByEmail(user.getEmail());
        if(user2!=null){
            map.put("emailMsg","邮箱已经注册");
            return map;
        }
        //注册用户,先生成一个5位的salt
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));

        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl("https://img-blog.csdnimg.cn/20190918135101160.png");
        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        Context context = new Context();
        context.setVariable("email",user.getEmail());

        String url = domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);

        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String,Object> login(String username,String password,int expiredSeconds){

        Map<String,Object> map = new HashMap<>();
        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        User user = userMapper.selectByName(username);
        if(user==null){
            map.put("usernameMsg","用户不存在");
            return map;
        }
        if(user.getStatus()==0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        //验证密码
        password = CommunityUtil.md5(password+user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }
        //oK>>生成登录凭证

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);//0代表有效状态
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*100));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);//通过 改变状态
    }

    /*获得登录ticket*/
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    /*更新头像*/
    public int updateHeader(int userId,String headerUrl){
        return userMapper.updateHeader(userId,headerUrl);
    }
}
