package com.whz.blog.service;

import com.whz.blog.entity.Tag;

import java.util.List;
import java.util.Map;

public interface TagService {

    List<Map<String, Object>> queryTagInfos(Integer userId);

    Integer addTag(List<Tag> tagList);

    List<Tag> queryTagsByArticleId(Integer articleId);
}
