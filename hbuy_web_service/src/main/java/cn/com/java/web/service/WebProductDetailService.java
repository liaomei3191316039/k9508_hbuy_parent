package cn.com.java.web.service;

import cn.com.java.model.WebProductDetailEntity;

/**
 * 
 * @author djin
 *    WebProductDetail业务层接口
 * @date 2019-12-09 10:27:01
 */
public interface WebProductDetailService extends BaseService<WebProductDetailEntity>{
	
	//创建商品详情静态页面
    String makeHtmls();
}
