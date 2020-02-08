package cn.com.java.web.products.controller;

import cn.com.java.web.model.WebSortEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *   测试thymeleaf模板的控制器
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/toShow")
    public String toShow(Model model, HttpServletRequest request){
        model.addAttribute("userName","张三");
        model.addAttribute("pwd","123123");
        model.addAttribute("name","<span style='color:red'>Jerry</span>");
        /***************解析实体对象***************/
        WebSortEntity sort = new WebSortEntity();
        sort.setId(1001l);
        sort.setSortname("衬衣");
        sort.setUpdatetime(new Date());
        model.addAttribute("sort",sort);
        /************解析set和list集合************/
        Set<String> allNames = new HashSet<String>() ;
        List<Integer> allIds = new ArrayList<Integer>() ;
        for (int x = 0 ; x < 5 ; x ++) {
            allNames.add("boot-" + x) ;
            allIds.add(x) ;
        }
        model.addAttribute("names", allNames) ;
        model.addAttribute("ids", allIds) ;
        /******************内置对象*******************/
        request.setAttribute("requestMessage", "springboot-request");
        request.getSession().setAttribute("sessionMessage", "springboot-session");
        request.getServletContext().setAttribute("applicationMessage",
                "springboot-application");
        model.addAttribute("url", "www.baidu.cn");
        request.setAttribute("url2",
                "<span style='color:red'>www.baidu.cn</span>");

        return "show";
    }
}
