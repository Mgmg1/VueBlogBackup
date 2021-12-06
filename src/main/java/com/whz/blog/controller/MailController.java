package com.whz.blog.controller;

import com.whz.blog.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

/**
 * @Description TODO
 * @Author WHZ
 * @Date 2021/2/10 20:11
 */
@RestController
@Validated
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /*
        根据向邮件帐号发送邮件
     */
    @PostMapping("/mail")
    public Object sendMineMail(
            @RequestParam("email") @Email String email,
            HttpSession session
    ) throws MessagingException {
        Result result = new Result();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("滋滋滋~博客注册验证码");

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        String veriCode = stringBuilder.toString();

        helper.setText("   <p><img src='cid:image'/></p><h3>验证码:"+veriCode+"</h3> ",true);
        //setText必须在addInLine前，否则图片无法显示！！！
        // jar包中文件不存在于真实的路径中，通过Resource的方式获取.而不是 File
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource resource = patternResolver.getResource("classpath:/static/1.jpg");
        helper.addInline("image",resource);
        helper.setTo(email);
        helper.setFrom(this.from);
//        helper.addAttachment("附件名",new File(this.getClass().getResource("/static/1.jpg").getPath()));
        mailSender.send(mimeMessage);
        session.setAttribute("veriCode",veriCode);
        session.setAttribute("email",email);
        result.setMessage("发送成功!");
        result.setCode(200);
        result.setData(true);
        return result;
    }
}
