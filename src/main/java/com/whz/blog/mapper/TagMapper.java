package com.whz.blog.mapper;

import com.whz.blog.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TagMapper {

    List<Map<String, Object>> queryTagInfos(Integer userId);

    Integer addTag(String tagName);

    List<Map<String,Object>> queryByArticleId(Integer articleId);

    Integer deleteArticleTagLinkByArticleId(int articleId);

//    Integer addArticleTagLink(int articleId, List<Integer> tagIds ); 这种情况只能使用map包装了

    /**
     *
     * @param  包含 articleId 和  tagIds
     * @return
     */
    Integer addArticleTagLink(int articleId,List<Integer> tagIds );

    List<Tag> queryByTagNames( String[] tagNames );
}
