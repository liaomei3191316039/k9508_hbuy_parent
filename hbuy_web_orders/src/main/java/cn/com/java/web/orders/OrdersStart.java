package cn.com.java.web.orders;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *   前台模块启动类
 */
@SpringBootApplication(scanBasePackages = "cn.com.java.web.orders.*")
@EnableEurekaClient  //开启注册中心的客户端
@EnableCaching  //开启缓存
@EnableScheduling  //开启任务调度
@MapperScan("cn.com.java.web.orders.mapper")
public class OrdersStart {

    public static void main(String[] args) {
        SpringApplication.run(OrdersStart.class);
    }

}
