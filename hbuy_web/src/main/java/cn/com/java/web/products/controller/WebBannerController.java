package cn.com.java.web.products.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import cn.com.java.web.model.WebBannerEntity;
 
/**
 * 
 * @author djin
 *   WebBanner控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webbanner")
public class WebBannerController extends BaseController<WebBannerEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webbanner";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webbanner.html";
    }
}
