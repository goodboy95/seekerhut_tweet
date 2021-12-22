package com.seekerhut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.seekerhut.mySqlMapper")
public class TweetApplication {

    public static void main(String[] args) {
        run(TweetApplication.class, args);
    }
}