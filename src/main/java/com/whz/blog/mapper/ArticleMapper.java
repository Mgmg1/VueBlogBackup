package com.whz.blog.mapper;

import com.whz.blog.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {

    Integer addArticle(Article article);

    Article queryByArticleId(Integer articleId);

    List<Map<String, Object>> queryCategoryInfos(Integer userId);

    List<Map<String, Object>> queryCountForEachNoteType(Integer userId);

    List<Article> queryInfosByTagName(Integer userId, String tagName);

    List<Article> queryInfosByCategoryName(Integer userId, String categoryName);

    List<Article> queryInfosByNoteType(Integer userId, String noteType);

    List<Article> queryAllInfosByUserId(Integer userId);

    Integer deleteArticleByArticleId(int articleId,int userId);
}
