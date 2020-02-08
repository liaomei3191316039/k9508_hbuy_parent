package cn.com.java.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *   购物车的业务层接口
 */
public interface BuyCarService {

    //添加购物车
    Map<String,Object> addBuyCar(Integer goodId, Integer num, String token, HttpServletRequest request, HttpServletResponse response) throws Exception;

    //登陆并直接更新购物车
    Map<String, Object> loginAndUpdBuyCar(String token, HttpServletRequest request, HttpServletResponse response) throws Exception;

    //加载购物车
    List<Map<String,Object>> loadBuyCar(String token, HttpServletRequest request) throws Exception;

    //添加订单-->RabbitMQ中
    Map<String,Object> addToMQ(String token, String proIds, Float allPrice) throws Exception;
}
