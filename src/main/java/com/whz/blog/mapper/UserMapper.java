package com.whz.blog.mapper;

import com.whz.blog.entity.User;


import java.util.List;

public interface UserMapper {

     Integer addUser(User user);

     User queryByNameOrEmail(String nameOrEmail);

    User queryByUserId(Integer userId);

    User queryByEmail(String email);

    User queryByUserName(String userName);

    Integer updateHeadImgUrl(Integer userId,String headImgUrl);

    Integer updateAnnouncement(Integer userId, String announcement);

    Integer updateBiliGithubUrl(Integer userId, String githubUrl, String bilibiliUrl);

}
