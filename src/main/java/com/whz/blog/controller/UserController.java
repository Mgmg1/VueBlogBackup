package com.whz.blog.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.whz.blog.BlogApplication;
import com.whz.blog.entity.Result;
import com.whz.blog.entity.User;

import com.whz.blog.service.UserService;
import com.whz.blog.util.JwtUtil;
import com.whz.blog.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/10 14:58
 */
@RestController
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    /*
        登录
     */
    @PostMapping("/login")
    public Object login(
            @RequestHeader(value = "fid",required = true) String fingerPrintId,
            User user,
            HttpSession session){

        Result result = new Result();
        User dbUser = userService.queryByNameOrEmail(user.getUserName());
        if(dbUser==null || !dbUser.getPassword().equals(Md5Utils.getMD5(user.getPassword(),1))){
            result.setCode(200);
            result.setMessage("帐号或者密码错误");
            result.setData(false);
            return result;
        }

        session.setAttribute("user",dbUser);
        result.setCode(200);
        result.setMessage("登录成功");
        Map<String, Object> map = new HashMap<>();
        String token = jwtUtil.create(dbUser.getUserId(),fingerPrintId);
        map.put("token",token);
        map.put("user",dbUser);
        result.setData(map);
        return result;
    }

    /*
        注册
     */
    @PostMapping("/register")
    public Object register(
            @RequestParam(value = "vericode",required = true)String code ,
            @Valid User user,
            HttpSession session){

        Result result = new Result();
        result.setCode(200);

        Integer canUserRegister = userService.canUserRegister(user.getUserName(),user.getEmail() );
        if( canUserRegister == 1 ) {
            result.setMessage("用户名已被使用");
            return result;
        }
        if(canUserRegister == 2) {
            result.setMessage("邮箱已被使用");
            return result;
        }

        String veriCode = (String) session.getAttribute("veriCode");
        if(veriCode==null||!veriCode.equals(code)){
            result.setMessage("验证码错误");
            return result;
        }

        user.setEmail((String) session.getAttribute("email"));
        String md5Password = Md5Utils.getMD5(user.getPassword(), 1);
        user.setPassword(md5Password);

        try{
            Integer nums = userService.register(user);
            if(nums==0){
                result.setMessage("遇到未知的错误");
                result.setCode(500);
                return result;
            }
        } catch (Exception e) {
            result.setCode(500);
            return result;
        }

        //注册成功后移除vericode
        session.removeAttribute("veriCode");
        result.setMessage("注册成功");
        result.setData(true);
        return result;
    }

    /*
        自动登录逻辑
     */
    @PostMapping("/autologin")
    public Object autoLogin(
            @RequestHeader(value = "Authorization",required = true) String authToken,
            @RequestHeader(value = "fid",required = true) String fingerPrintId,
            @RequestParam(value = "isAutoLogin",required = true) Boolean isAutoLogin,
            HttpSession session){

        /*
            先判断session是否存在user，不存在则
            再判断jwt是否有效
         */
        Result result = new Result();
        result.setCode(200);

        Integer userId;
        User user = (User) session.getAttribute("user");

        if(user!=null){
            userId = user.getUserId();
        }else if ( !isAutoLogin || !jwtUtil.verify(authToken,fingerPrintId)) {
            //token检验失败，如果在创建token时加上过期时间，时间过期了这里就是校验失败
            return result;
        }else {
            userId = jwtUtil.getUserId(authToken);
        }

        User dbUser = userService.queryByUserId(userId);
        if(dbUser == null){
            result.setMessage("找不到该用户");
            return result;
        }

        session.setAttribute("user",dbUser);

        Map<String, Object> map = new HashMap<>();
        String token = jwtUtil.create(dbUser.getUserId(),fingerPrintId);
        map.put("token",token);
        map.put("user",dbUser);
        result.setData(map);
        return result;
    }


    /*
        登出,销毁session
        前端会同时取消自动登录
     */
    @RequestMapping("/logout")
    public Object logout(HttpSession session){

        Result result = new Result();
        result.setCode(200);

        session.invalidate();

        result.setMessage("已登出");
        result.setData(true);

        return result;
    }

    /*
        根据uid查询用户信息
     */
    @GetMapping("/userInfo")
    public Object queryUserInfoByUserId(
            @RequestParam(value = "uid",required = true) Integer userId
    ){
        Result result = new Result();
        result.setCode(200);
        User user;
        try {
            user = userService.queryByUserId(userId);
        }catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            return result;
        }

        result.setData(user);
        result.setMessage("OK");
        return result;
    }

    /*
        设置博客封面图片的url
     */
    @PostMapping("/setheadimgurl")
    public Object updateHeadImgUrl(
            @RequestParam(value = "uid",required = true) Integer userId,
            @RequestParam(value = "url",required = true) String headImgUrl
    ) {

        Result result = new Result();
        result.setCode(200);

        try {
            Integer res  = userService.updateHeadImgUrl(userId, headImgUrl );
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("遇到未知的错误");
        }

        result.setMessage("OK");
        result.setData(true);
        return result;
    }

    /*
        设置公共
     */
    @PostMapping("/setannon")
    public Object updateAnnouncement(
            @RequestParam(value = "uid",required = true) Integer userId,
            @RequestParam(value = "annon",required = true) String announcement
    ) {

        Result result = new Result();
        result.setCode(200);

        try {
            Integer res = userService.updateAnnouncement(userId,announcement);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("遇到未知的错误");
        }

        result.setMessage("OK");
        result.setData(true);
        return result;
    }

    /*
        设置github的url
     */
    @PostMapping("/setgburl")
    public Object updateBiliGithubUrl(
            @RequestParam(value = "uid",required = true) Integer userId,
            @RequestParam(value = "githubUrl",required = true) String githubUrl,
            @RequestParam(value = "bilibiliUrl",required = true) String bilibiliUrl
    ) {
        Result result = new Result();
        result.setCode(200);

        try {
           Integer nums =  userService.updateBiliGithubUrl(userId, githubUrl,bilibiliUrl );
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
        }
        result.setMessage("OK");
        result.setData(true);
        return result;
    }
}
