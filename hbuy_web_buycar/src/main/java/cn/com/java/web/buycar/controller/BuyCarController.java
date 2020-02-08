package cn.com.java.web.buycar.controller;

import cn.com.java.web.service.BuyCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *   购物车的控制器
 */
@Controller
@RequestMapping("/buyCar")
public class BuyCarController {

    //业务层对象
    @Autowired
    private BuyCarService buyCarService;

    //添加购物车
    @RequestMapping("/addBuyCar")
    public @ResponseBody
    Map<String,Object> addBuyCar(Integer goodId, Integer num, String token, HttpServletRequest request, HttpServletResponse response){
        try {
            return buyCarService.addBuyCar(goodId,num,token,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //登陆后的购物车合并
    @RequestMapping("/loginAndUpdBuyCar/{token}")
    public @ResponseBody Map<String,Object> loginAndUpdBuyCar(@PathVariable("token") String token, HttpServletRequest request, HttpServletResponse response){
        try {
            return buyCarService.loginAndUpdBuyCar(token,request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/loadBuyCar")
    public @ResponseBody List<Map<String,Object>> loadBuyCar(String token,HttpServletRequest request){
        try {
            return buyCarService.loadBuyCar(token,request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/addToMQ")
    public @ResponseBody Map<String,Object> addToMQ(String token, String proIds, Float allPrice){
        try {
            return buyCarService.addToMQ(token,proIds,allPrice);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }



}
