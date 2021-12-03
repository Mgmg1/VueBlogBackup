package com.whz.blog.controller;

import com.whz.blog.entity.Result;
import com.whz.blog.entity.Tag;
import com.whz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("/tags")
    public Object queryTagsByArticleId(
            @RequestParam(value = "articleId",required = true) Integer articleId
    ) {
        Result result = new Result();
        result.setCode(200);

        try {
            List<Tag> tagList = tagService.queryTagsByArticleId(articleId);
            result.setData(tagList);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
        }

        result.setMessage("OK");
        return result;
    }
}
