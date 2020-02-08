package cn.com.java.web.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *   前台模块启动类
 */
@SpringBootApplication(scanBasePackages = "cn.com.java.web.mongodb.*")
@EnableEurekaClient
@ServletComponentScan(basePackages = "cn.com.java.web.mongodb.filter")
public class MongoDBStart {

    public static void main(String[] args) {
        SpringApplication.run(MongoDBStart.class);
    }

}
