package cn.com.java.web.products.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import cn.com.java.web.model.WebUsersEntity;
 
/**
 * 
 * @author djin
 *   WebUsers控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webusers")
public class WebUsersController extends BaseController<WebUsersEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webusers";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webusers.html";
    }
}
