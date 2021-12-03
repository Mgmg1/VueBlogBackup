package com.whz.blog.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/9 21:07
 */
public class User {


    private Integer userId;

    @NotNull
    @Length(min = 3,max = 10,message = "用户名请保持长度为3~10")
    private String userName;

    @NotNull
    @Length(min = 6,max = 20,message = "密码请保持长度为6~20")
    private String password;

//    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
    @NotEmpty
    @Length(max = 30,message = "邮箱长度过长!")
    @Email
    private String email;

    //    @Pattern(regexp = "[a-zA-z]+://[^\\s]*")
//    @URL(message = "请输入正确的Url")
    private String headImgUrl;

    private String announcement;

    private String githubUrl;

    private String bilibiliUrl;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getBilibiliUrl() {
        return bilibiliUrl;
    }

    public void setBilibiliUrl(String bilibiliUrl) {
        this.bilibiliUrl = bilibiliUrl;
    }
}
