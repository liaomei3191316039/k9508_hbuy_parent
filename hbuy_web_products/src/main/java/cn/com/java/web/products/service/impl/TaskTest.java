package cn.com.java.web.products.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *   任务调度的测试类
 */
@Component
public class TaskTest {

   // @Scheduled(cron = "0/3 * * * * ? ") // 间隔3秒执行
    public void test01(){
        System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"执行了test01方法。。");
    }

   // @Scheduled(cron = "0/6 * * * * ? ") // 间隔6秒执行
    public void test02(){
        System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())+"执行了test02方法!!");
    }
}
