package cn.com.java.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *   易买网页面跳转的控制器
 */
@Controller
@RequestMapping("/model")
public class ModelController {

    @RequestMapping("/toLayout")
    public String toLayout(){
        return "layout";
    }

    /**
     * 页面adminmenus.html
     */
    @RequestMapping("/adminmenusHtml")
    public String adminmenusHtml(){
        return "redirect:/html/adminmenus.html";
    }

    /**
     * 页面adminuserauthority.html
     */
    @RequestMapping("/adminuserauthorityHtml")
    public String adminuserauthorityHtml(){
        return "redirect:/html/adminuserauthority.html";
    }

    /**
     * 页面adminusers.html
     */
    @RequestMapping("/adminusersHtml")
    public String adminusersHtml(){
        return "redirect:/html/adminusers.html";
    }
}