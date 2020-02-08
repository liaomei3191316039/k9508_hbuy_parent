package cn.com.java.web.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 *   菜单的消费者控制器
 */
@Controller
@RequestMapping("/webMenuCon")
public class WebMenuConController {

    //依赖引入接收用户页面请求处理响应的模版
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/loadAllMenu")
    public @ResponseBody List<WebMenuEntity> loadAllMenu(){
        return restTemplate.getForObject("http://web-provider/webmenu/loadAllT",List.class);
    }
}
