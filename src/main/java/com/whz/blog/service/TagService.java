package com.whz.blog.service;

import com.whz.blog.entity.Tag;

import java.util.List;
import java.util.Map;

public interface TagService {

    List<Map<String, Object>> queryTagInfos(Integer userId);

    Integer addTag(String[] tagNames);

    List<Map<String,Object>> queryTagsByArticleId(Integer articleId);

    Integer deleteArticleTagLinkByArticleId(int articleId);

    Integer addArticleTagLink( int articleId,List<Integer> tagIds );

    List<Tag> queryByTagNames( String[] tagNames );
}
