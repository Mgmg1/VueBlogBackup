package com.whz.blog.controller;

import com.whz.blog.entity.Result;
import com.whz.blog.entity.User;

import com.whz.blog.service.UserService;
import com.whz.blog.util.JwtUtil;
import com.whz.blog.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
    public static final String AUTO_LOGIN = "AUTO_LOGIN";
    public static final String USER = "user";

    /*
        登录
        isAutoLogin = 1 时则 记录为自动登录
        否则不是
     */
    @PostMapping("/login")
    public Object login(
            User user,
            @RequestParam(value = "isAutoLogin",required = true)  Boolean isAutoLogin,
            HttpServletRequest request,
            HttpSession session){

        Result result = new Result();
        User dbUser = userService.queryByNameOrEmail(user.getUserName());
        if(dbUser==null || !dbUser.getPassword().equals(Md5Utils.getMD5(user.getPassword(),1))){
            result.setCode(200);
            result.setMessage("帐号或者密码错误");
            result.setData(false);
            return result;
        }
        result.setCode(200);
        result.setMessage("登录成功");
        session.setAttribute(UserController.USER,dbUser);

        Map<String, Object> map = new HashMap<>();
        String[] signs = new String[]{ request.getRemoteAddr() };//jwt由ip地址确认

        String key = JwtUtil.appendKey( signs ) ;
        String token = JwtUtil.create(dbUser.getUserId(),key,isAutoLogin);
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
        关于自动登录：
        1 : 自动登录，
       其它 : 否
     */
    @PostMapping("/autologin")
    public Object autoLogin(
            @RequestHeader(value = "Authorization",required = true) String authToken,
            HttpServletRequest request,
            HttpSession session){
        /*
            先判断session是否存在user，不存在则
            再判断jwt是否有效
         */
        Result result = new Result();
        result.setCode(200);
        Map<String, Object> map = new HashMap<>();

        User user = (User) session.getAttribute(UserController.USER);
        if( user != null ) { //session找到了用户，说明在短期内未点击登出！！
            result.setMessage("OK");
            result.setData( map );
            map.put(UserController.USER,user);
            return result;
        }

        int userId;
        String[] signs = new String[]{request.getRemoteAddr()};
        if ( !JwtUtil.verify(authToken,JwtUtil.appendKey( signs ))) {
            //token检验失败，如果在创建token时加上过期时间，时间过期了这里就是校验失败
            result.setMessage("verification error");
            return result;
        }
        if( !JwtUtil.getIsAutoLogin(authToken) ) {
            return result;
        }

        userId = JwtUtil.getUserId(authToken);

        user = userService.queryByUserId(userId);
        if(user == null){
            result.setMessage("找不到该用户");
            return result;
        }
        session.setAttribute( UserController.USER,user );
        map.put(UserController.USER,user);
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
