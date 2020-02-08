package cn.com.java.web.mongodb.controller;

import cn.com.java.web.mongodb.service.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.bson.Document;
import java.util.List;

/**
 *   mongodb的控制器
 */
@Controller
@RequestMapping("/discuss")
public class DiscussController {

    @Autowired
    private DiscussService discussService;

    //加载所有的商品评论数据
    @RequestMapping("/loadAllDiscuss")
    public @ResponseBody List<Document> loadAllDiscuss(){
        try {
            return discussService.findAllDiscuss();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
