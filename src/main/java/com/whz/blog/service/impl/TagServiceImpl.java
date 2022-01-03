package com.whz.blog.service.impl;

import com.whz.blog.entity.Tag;
import com.whz.blog.mapper.TagMapper;
import com.whz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/12 11:02
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    
    @Override
    public List<Map<String, Object>> queryTagInfos(Integer userId) {
        return tagMapper.queryTagInfos(userId);
    }

    @Override
    public Integer addTag(String[] tagNames) {
        int result = 0;
        for (int i = 0; i < tagNames.length; i++) {
            result += tagMapper.addTag(tagNames[i]);
        }
        return result;
    }

    @Override
    public List<Map<String,Object>>queryTagsByArticleId(Integer articleId) {
        return tagMapper.queryByArticleId( articleId );
    }

    @Override
    public Integer deleteArticleTagLinkByArticleId(int articleId) {
        return tagMapper.deleteArticleTagLinkByArticleId( articleId );
    }

    @Override
    public Integer addArticleTagLink(int articleId, List<Integer> tagIds) {
        return tagMapper.addArticleTagLink(articleId, tagIds);
    }

    @Override
    public List<Tag> queryByTagNames(String[] tagNames) {
        return tagMapper.queryByTagNames( tagNames );
    }

}
