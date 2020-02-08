package cn.com.java.web.products.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import cn.com.java.web.model.WebProductImgEntity;
 
/**
 * 
 * @author djin
 *   WebProductImg控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webproductimg")
public class WebProductImgController extends BaseController<WebProductImgEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webproductimg";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webproductimg.html";
    }
}
