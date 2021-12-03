package com.whz.blog.controller;

import com.whz.blog.entity.Article;
import com.whz.blog.entity.Result;
import com.whz.blog.service.ArticleService;
import com.whz.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/10 20:34
 */
@Validated
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @PostMapping("/upload")
    public Object uploadMd(
            @Valid Article article,
            @RequestParam(value = "blog",required = true) MultipartFile file,
            @RequestParam(value = "tags",required = true) String[] tags){
        Result result = new Result();
        result.setCode(200);
//        DataSize.ofMegabytes(50)

        if (file.isEmpty() || file.getSize() > 3 * 1024 * 2014) {
            result.setMessage("文件为空或过大");
            return result;
        }
        if(Arrays.stream(tags).distinct().count() < tags.length) {
            result.setMessage("标签不可重复!!");
            return result;
        }

        Boolean isSuccess = false;
        try {
            byte[] bytes = file.getBytes();
            article.setContent(bytes);
            article.setCreateDate(new Date());
            isSuccess  = articleService.addArticle(article, tags);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isSuccess){
            result.setData(true);
            result.setMessage("添加成功");
        }
        return result;
    }

    @GetMapping("/blogcontent")
    public Object getArticleWithId(
            @RequestParam(value = "articleid",required = true) @NotNull Integer articleId
    ){
        Result result = new Result();
        result.setCode(200);
        try{
            Article article = articleService.queryByArticleId(articleId);
            if( article != null ) {
                String charsetName = "utf-8";
                String blogContent = new String((byte[]) article.getContent(),charsetName);
                article.setContent(blogContent);
            }
            result.setData(article);
        } catch (Exception e){
            result.setCode(500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/blogcount")
    public Object getBlogsInfo(
            @RequestParam(value = "uid",required = true) @NotNull Integer userId
    ){
        Result result = new Result();
        result.setCode(200);
        try {
            Map<String, Object> blogInfos = articleService.queryBlogInfos(userId);
            result.setData(blogInfos);
            result.setMessage("OK");
        } catch (Exception e){
            result.setCode(500);
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("/bloginfos")
    public Object getBlogsInfoList(
            @RequestParam(value = "type",required = true) @NotNull String type,
            @RequestParam(value = "uid",required = true) @NotNull Integer userId,
            @RequestParam(value = "name",required = true) @NotNull String typeVal
    ){
        Result result = new Result();
        result.setCode(200);
        try{
            List<Article> articleList = null ;
            if("tag".equals(type)){
                articleList = articleService.queryInfosByTagName(userId,typeVal);
            }
            if("category".equals(type)){
                articleList = articleService.queryInfosByCategoryName(userId,typeVal);
            }
            if("notetype".equals(type)){
                articleList =  articleService.queryInfosByNoteType(userId,typeVal);
            }
            if("all".equals(type) && "all".equals(typeVal)){
                articleList =  articleService.queryAllInfosByUserId(userId);
            }
            result.setData(articleList);
        }catch (Exception e){
            result.setCode(500);
            e.printStackTrace();
        }
        return result;
    }
}
