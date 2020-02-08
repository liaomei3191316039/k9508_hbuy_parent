package cn.com.java.web.buycar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *   前台模块启动类
 */
@SpringBootApplication(scanBasePackages = "cn.com.java.web.buycar.*")
@EnableEurekaClient  //开启注册中心的客户端
@EnableCaching  //开启缓存
@ServletComponentScan(basePackages = "cn.com.java.web.buycar.filter")
@MapperScan("cn.com.java.web.buycar.mapper")
public class BuyCarStart {

    public static void main(String[] args) {
        SpringApplication.run(BuyCarStart.class);
    }

}
