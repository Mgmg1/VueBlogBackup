package com.whz.blog.service.impl;

import com.whz.blog.entity.User;
import com.whz.blog.mapper.UserMapper;
import com.whz.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/10 18:10
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer register(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public User queryByNameOrEmail(String nameOrEmail) {
        return userMapper.queryByNameOrEmail(nameOrEmail);
    }

    @Override
    public User queryByUserId(Integer userId) {
        return userMapper.queryByUserId(userId);
    }

    @Override
    public User queryByUserName(String userName) {
        return userMapper.queryByUserName(userName);
    }

    @Override
    public User queryByEmail(String email) {
        return userMapper.queryByEmail(email);
    }

    @Override
    public Integer canUserRegister(String userName, String email) {

        if( this.queryByEmail(email) !=null ) {
            return 2;
        }
        if( this.queryByUserName(userName) != null ) {
            return 1;
        }
        return 0;
    }

    @Override
    public Integer updateHeadImgUrl(Integer userId,String headImgUrl) {
        return userMapper.updateHeadImgUrl(userId,headImgUrl);
    }

    @Override
    public Integer updateAnnouncement(Integer userId, String announcement) {
        return userMapper.updateAnnouncement(userId,announcement);
    }

    @Override
    public Integer updateBiliGithubUrl(Integer userId, String githubUrl, String bilibiliUrl) {
        return userMapper.updateBiliGithubUrl(userId,githubUrl,bilibiliUrl);
    }


}
