package com.whz.blog.controller;

import com.whz.blog.entity.Result;
import com.whz.blog.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class ConfigController {

    @Autowired
    ConfigService configService;

    @GetMapping("/defaultuid")
    public Object getDefaultUserId(){

        Result result = new Result();
        result.setCode(200);
        String defaultUserId;
        try {
            defaultUserId = configService.queryDefaultUserId();
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            return result;
        }

        result.setData(defaultUserId);
        result.setMessage("OK");
        return result;
    }

}
