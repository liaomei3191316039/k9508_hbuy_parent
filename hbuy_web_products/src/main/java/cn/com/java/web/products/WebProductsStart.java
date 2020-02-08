package cn.com.java.web.products;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *   前台模块启动类
 */
@SpringBootApplication(scanBasePackages = "cn.com.java.web.products.*")
@EnableEurekaClient
@MapperScan("cn.com.java.web.products.mapper")
@EnableScheduling  //开启任务调度
public class WebProductsStart {

    public static void main(String[] args) {
        SpringApplication.run(WebProductsStart.class);
    }

}
