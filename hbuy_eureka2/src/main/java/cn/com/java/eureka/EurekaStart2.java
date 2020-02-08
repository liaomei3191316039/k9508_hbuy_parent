package cn.com.java.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *   注册中心2
 */
@SpringBootApplication
@EnableEurekaServer  //启动注册中心的服务端
public class EurekaStart2 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaStart2.class);
    }
}
