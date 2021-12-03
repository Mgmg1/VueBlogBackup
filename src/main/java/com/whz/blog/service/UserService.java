package com.whz.blog.service;

import com.whz.blog.entity.User;

public interface UserService {

    Integer register(User user);

    User queryByNameOrEmail(String nameOrEmail);

    User queryByUserId(Integer userId);

    User queryByUserName(String userName);

    User queryByEmail(String email);

    Integer canUserRegister(String userName,String email);

    Integer updateHeadImgUrl(Integer userId,String headImgUrl);

    Integer updateAnnouncement(Integer userId, String announcement);

    Integer updateBiliGithubUrl(Integer userId, String githubUrl, String bilibiliUrl);
}
