package cn.com.java.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *   后台管理模块启动类
 */
@SpringBootApplication(scanBasePackages = "cn.com.java.admin.*")
@MapperScan("cn.com.java.admin.mapper")
@ServletComponentScan(basePackages = "cn.com.java.admin.filter")
@EnableEurekaClient
public class AdminStart {

    public static void main(String[] args) {
        SpringApplication.run(AdminStart.class);
    }

}
