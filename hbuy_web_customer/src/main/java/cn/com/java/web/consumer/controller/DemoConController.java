package cn.com.java.web.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 *   消费者测试的控制器
 */
@Controller
@RequestMapping("/demoCon")
public class DemoConController {

    //依赖引入接收用户页面请求处理响应的模版
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/loadTestPro/{userName}")
    public @ResponseBody String loadTestPro(@PathVariable("userName") String userName){
        System.out.println(userName);
        //url：是访问搭建的提供者模块的路径
        return restTemplate.getForObject("http://web-provider/demoPro/loadTestRibbon/"+userName,String.class);
    }


}
