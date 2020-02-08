package cn.com.java.web.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *   测试的控制器
 */
@Controller
@RequestMapping("/demoPro")
public class DemoProController {

    //依赖业务层对象
    @Autowired
    private DemoService demoService;

    @RequestMapping("/loadTestRibbon/{userName}")  //restful传参
    public @ResponseBody String loadTestRibbon(@PathVariable("userName") String userName){
        try {
            return demoService.testRibbon(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
