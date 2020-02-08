package cn.com.java.web.seckill.controller;

import cn.com.java.model.WebProductDetailEntity;
import cn.com.java.model.WebSeckillEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author djin
 *   WebSeckill控制器
 * @date 2019-12-09 10:27:01
 */
@Controller
@RequestMapping("/webseckill")
public class WebSeckillController extends BaseController<WebSeckillEntity>{

    //执行秒杀
    @RequestMapping("/doSecKill")
    public @ResponseBody Map<String,Object> doSecKill(Long secId, String token){
        try {
            return webSeckillService.doSecKill(secId,token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*//执行秒杀，测试
    @RequestMapping("/doSecKill")
    public @ResponseBody Map<String,Object> doSecKill(Long secId, Long uid){
        try {
            return webSeckillService.doSecKill(secId,uid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //模拟登陆
    @RequestMapping("/login")
    public @ResponseBody Map<String,Object> login(Long uid){
        try {
            return webSeckillService.login(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    //加载可以秒杀的商品
    @RequestMapping("/loadUPSecKill")
    public @ResponseBody List<Map<String, Object>> loadUPSecKill(){
        try {
            return webSeckillService.findUPSecKill();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
