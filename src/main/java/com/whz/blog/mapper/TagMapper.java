package com.whz.blog.mapper;

import com.whz.blog.entity.Tag;

import java.util.List;
import java.util.Map;

public interface TagMapper {

    List<Map<String, Object>> queryTagInfos(Integer userId);

    Integer addTag(List<Tag> tagList);

    List<Tag> queryByArticleId(Integer articleId);
}
