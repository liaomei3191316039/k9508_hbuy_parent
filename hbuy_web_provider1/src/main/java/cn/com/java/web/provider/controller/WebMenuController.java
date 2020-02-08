package cn.com.java.web.provider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author djin
 *   WebMenu控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webmenu")
public class WebMenuController extends BaseController<WebMenuEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webmenu";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webmenu.html";
    }
}
