package com.whz.blog.controller;

import com.whz.blog.entity.Article;
import com.whz.blog.entity.Result;
import com.whz.blog.service.ArticleService;
import com.whz.blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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

    /*
        上传文章
     */
    @PostMapping("/upload")
    public Object uploadMd(
            @Valid Article article,
            @RequestParam(value = "blog",required = true) MultipartFile file,
            @RequestParam(value = "tags",required = true) String[] tags){
        Result result = new Result();
        result.setCode(200);

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

    /*
        根据articleId查找博客
     */
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

    /*
        根据userId查询博客的数量信息
     */
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

    /*
        根据所给的 type 查询文章信息
     */
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

    /*
        根据 articleId提供blog文件
     */
    @GetMapping("/download")
    public Result downloadBlogContentByArticleId(
            @RequestParam(value = "articleid",required = true) Integer articleId,
            HttpServletResponse response
    ) {
        Result result = new Result();
        result.setMessage("ok");
        result.setCode(200);
        ServletOutputStream outputStream = null;
        try {
            //获取页面输出流
            //读取文件
            Article article = articleService.queryByArticleId(articleId);

            if( article == null ) {
                return result;
            }


            String fileName = article.getTitle() + " " + article.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString() + ".md";
            byte[] bytes = (byte[]) article.getContent();
            //向输出流写文件
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            outputStream = response.getOutputStream();
            response.setHeader("Content-Disposition","attachment;filename="+fileName);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            return result;
        } catch (IOException e) {
            result.setMessage("下载发生异常");
            result.setCode(500);
            return result;
        } finally {
            if( outputStream != null ) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
        根据articleId删除文章Id
        只能让拥有该article的用户删除
     */
    @PostMapping("/deletearticle")
    public Result deleteArticleByArticleId(
            @RequestParam(value = "aid",required = true) Integer articleId,
            @RequestParam(value = "uid",required = true) Integer userId
    ) {
        Result result = new Result();
        result.setCode( 500 );
        result.setMessage("发生了未知的错误");

        try {
            int integer = articleService.deleteArticleByArticleId(articleId,userId);
            if( integer == 0 ) {
                return result;
            }
            result.setCode(200);
            result.setMessage("OK");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
