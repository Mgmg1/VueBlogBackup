package com.whz.blog.service.impl;

import com.whz.blog.entity.Tag;
import com.whz.blog.mapper.TagMapper;
import com.whz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/12 11:02
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    
    @Override
    public List<Map<String, Object>> queryTagInfos(Integer userId) {
        return tagMapper.queryTagInfos(userId);
    }

    @Override
    public Integer addTag(List<Tag> tagList) {
        return tagMapper.addTag(tagList);
    }

    @Override
    public List<Tag> queryTagsByArticleId(Integer articleId) {
        return tagMapper.queryByArticleId( articleId );
    }

    @Override
    public Integer deleteTagsByArticleId(int articleId) {
        return tagMapper.deleteTagsByArticleId( articleId );
    }


}
