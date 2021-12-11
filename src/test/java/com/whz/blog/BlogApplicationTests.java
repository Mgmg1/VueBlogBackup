package com.whz.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    ApplicationContext context;

//    @Test
//    void databaseConnection( ) throws SQLException {
//        DataSource bean = context.getBean(DataSource.class);
//        System.out.println( bean.getConnection() );
//    }
//
//    @Test
//    void redisConnection() {
//        StringRedisTemplate bean = context.getBean(StringRedisTemplate.class);
//        System.out.println(  bean.getConnectionFactory().getConnection() );
//    }


}
