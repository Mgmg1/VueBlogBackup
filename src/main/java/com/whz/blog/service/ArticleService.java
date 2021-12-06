package com.whz.blog.service;

import com.whz.blog.entity.Article;
import com.whz.blog.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    Boolean addArticle(Article article, String[] tags);

    Article queryByArticleId(Integer articleId);

    List<Map<String, Object>> queryCountForEachNoteType(Integer userId);

    List<Map<String, Object>> queryCategoryInfos(Integer userId);

    List<Article> queryInfosByTagName(Integer userId, String tagName);

    List<Article> queryInfosByCategoryName(Integer userId, String categoryName);

    List<Article> queryInfosByNoteType(Integer userId, String noteType);

    Map<String,Object> queryBlogInfos(Integer userId);

    List<Article> queryAllInfosByUserId(Integer userId);

    Integer deleteArticleByArticleId(int articleId, int userId);
}
