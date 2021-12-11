package com.whz.blog.service.impl;

import com.whz.blog.entity.Config;
import com.whz.blog.mapper.ConfigMapper;
import com.whz.blog.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    ConfigMapper configMapper;

    @Override
    public String queryDefaultUserId() {
        return configMapper.queryPropsValue(Config.DEFAULT_USER_ID);
    }
}
