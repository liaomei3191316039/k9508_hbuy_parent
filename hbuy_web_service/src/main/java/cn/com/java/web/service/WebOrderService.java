package cn.com.java.web.service;

import cn.com.java.model.WebOrderEntity;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author djin
 *    WebOrder业务层接口
 * @date 2019-12-09 10:27:01
 */
public interface WebOrderService extends BaseService<WebOrderEntity>{
	
	//监听mysql订单表
    Map<String,Object> listenerOrder() throws Exception;

    //加载登陆用户的订单数据
    List<WebOrderEntity> findOrdersByUid(String token) throws Exception;
}
