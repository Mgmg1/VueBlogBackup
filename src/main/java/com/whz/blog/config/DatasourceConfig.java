package com.whz.blog.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatasourceConfig {

    public static final String STATVIEW_MAPPING = "/druid/**";

    /*
        @ConfigurationProperties
        整体注入自定义配置
     */
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource druidDatasource( ) { return new DruidDataSource(); }

    /*
        Servlet的监控视图
     */
    @Bean
    public ServletRegistrationBean<?> statViewServlet( ) {
        //注意要放入 viewservlet，否则会报错
        ServletRegistrationBean<StatViewServlet> viewServlet = new ServletRegistrationBean<>(new StatViewServlet());
        viewServlet.setUrlMappings(Collections.singletonList(STATVIEW_MAPPING));
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","123456");
        initParams.put("allow",""); //默认就是允许所有人访问
//        initParams.put("deny",""); ban 登录IP

        //要放在 put 后！！否则配置不起效
        viewServlet.setInitParameters( initParams );
        return viewServlet;
    }


    /*
        配置所要监控的UrlMapping 的 Filter,
        监控和过滤静态地址
     */
    @Bean
    public FilterRegistrationBean<?> webStatFilter() {
        //注意要放入webstatfilter，否则会报错
        FilterRegistrationBean<?> filter  = new FilterRegistrationBean<>(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,/druid/**");
        filter.setUrlPatterns(Collections.singletonList("/*"));

        filter.setInitParameters( initParams );
        return filter;
    }
}
