package com.whz.blog.service.impl;

import com.whz.blog.entity.Article;
import com.whz.blog.entity.Tag;
import com.whz.blog.mapper.ArticleMapper;
import com.whz.blog.service.ArticleService;
import com.whz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/10 21:20
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    TagService tagService;

    @Override
    public Boolean addArticle(Article article, String[] tags) {

        Integer result = articleMapper.addArticle(article);
        if(result!=1) {
            return false;
        }

        if(tags.length !=0 ){
            List<Tag> tagList = Arrays.stream(tags).map((tagName) -> {
                Tag tag = new Tag();
                tag.setTagName(tagName);
                tag.setUserId(article.getUserId());
                //articleId 被回写
                tag.setArticleId(article.getArticleId());
                return tag;
            }).collect(Collectors.toList());
            Integer tagNums = tagService.addTag(tagList);
            if(tagNums==0 ){
                return false;
            }
        }

        return true;
    }
    @Override
    public Article queryByArticleId(Integer articleId) {
        return articleMapper.queryByArticleId(articleId);
    }

    @Override
    public List<Map<String, Object>> queryCountForEachNoteType(Integer userId) {
        return articleMapper.queryCountForEachNoteType(userId);
    }

    @Override
    public List<Map<String, Object>> queryCategoryInfos(Integer userId) {
        return articleMapper.queryCategoryInfos(userId);
    }

    @Override
    public List<Article> queryInfosByTagName(Integer userId, String tagName) {

        return articleMapper.queryInfosByTagName(userId,tagName);
    }

    @Override
    public List<Article> queryInfosByCategoryName(Integer userId, String categoryName) {
        return articleMapper.queryInfosByCategoryName(userId,categoryName);
    }

    @Override
    public List<Article> queryInfosByNoteType(Integer userId, String noteType) {
        Article article = new Article();
        article.setNoteType(Integer.parseInt(noteType));
        article.setUserId(userId);
        return articleMapper.queryInfosByNoteType(userId,noteType);
    }

    @Override
    public Map<String, Object> queryBlogInfos(Integer userId) {
        List<Map<String, Object>> noteTypes = this.queryCountForEachNoteType( userId );
        List<Map<String, Object>> categoryInfos = this.queryCategoryInfos( userId );
        List<Map<String, Object>> tagInfos = tagService.queryTagInfos( userId );
        Map<String, Object> blogInfos = new HashMap<>();
        blogInfos.put("noteTypeInfo",noteTypes);
        blogInfos.put("categoryInfo",categoryInfos);
        blogInfos.put("tagInfos",tagInfos);
        return blogInfos;
    }

    @Override
    public List<Article> queryAllInfosByUserId(Integer userId) {
        return articleMapper.queryAllInfosByUserId(userId);
    }

    @Override
    public Integer deleteArticleByArticleId(int articleId,int userId) {

        tagService.deleteTagsByArticleId(articleId ); //存在外键时，必须先删除外键关联的记录，再删除实体
        return articleMapper.deleteArticleByArticleId(articleId,userId);
        //当删除后返回的受影响的行数为0时，说明发生了未知的错误。
    }

}
