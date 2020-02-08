package cn.com.java.web.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *   前台登陆模块启动类
 */
@SpringBootApplication(scanBasePackages = "cn.com.java.web.sso.*")
@MapperScan("cn.com.java.web.sso.mapper")
@EnableEurekaClient
@ServletComponentScan(basePackages = "cn.com.java.web.sso.filter")
public class WebSSOStart {

    public static void main(String[] args) {
        SpringApplication.run(WebSSOStart.class);
    }

}
