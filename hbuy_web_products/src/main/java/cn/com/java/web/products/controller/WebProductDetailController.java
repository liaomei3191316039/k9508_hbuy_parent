package cn.com.java.web.products.controller;

import cn.com.java.model.WebProductDetailEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author djin
 *   WebProductDetail控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webproductdetail")
public class WebProductDetailController extends BaseController<WebProductDetailEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webproductdetail";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webproductdetail.html";
    }
}
