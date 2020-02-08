package cn.com.java.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *   前台模块启动类
 */
@SpringBootApplication(scanBasePackages = "cn.com.java.web.*")
@MapperScan("cn.com.java.web.mapper")
@EnableEurekaClient
@ServletComponentScan(basePackages = "cn.com.java.web.filter")
public class WebStart {

    public static void main(String[] args) {
        SpringApplication.run(WebStart.class);
    }

}
